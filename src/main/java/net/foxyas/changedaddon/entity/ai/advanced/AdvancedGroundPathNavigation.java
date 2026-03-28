package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class AdvancedGroundPathNavigation extends GroundPathNavigation {

    public AdvancedGroundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new AdvancedNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    public void tick() {
        if (this.isDone() || path == null) {
            if (mob instanceof ChangedEntity changedEntity) {
                maintainSafePose(changedEntity);
            }
            super.tick();
            return;
        }

        Vec3 currentPos = this.mob.position();
        Vec3 nextNodePos = this.path.getNextEntityPos(this.mob);

        // Interpolate a point ahead (lookahead) for smoother pose transitions
        Vec3 lookAhead = currentPos.lerp(nextNodePos, 1);

        if (mob instanceof ChangedEntity changedEntity) {

            // --- JUMP SPRINT / GAP DETECTION ---
            double horizontalDist = nextNodePos.subtract(currentPos).horizontalDistance();

            // If the next node is more than 1.2 blocks away horizontally, it's likely a Gap Node
            if (horizontalDist > 1.2D && (path.getNextNode() instanceof IAdvancedNode advancedNode && advancedNode.isJumpNode()) && this.mob.onGround()) {
                this.mob.setSprinting(true);
                this.mob.getJumpControl().jump();

                // Apply a small velocity boost to ensure the entity crosses the gap
                Vec3 velocity = this.mob.getDeltaMovement();
                this.mob.setDeltaMovement(velocity.add(this.mob.getLookAngle().scale(0.15D)));
                //advancedNode.setJumpNode(false);
            } else {
                this.mob.setSprinting(false);
            }

            // --- POSE MANAGEMENT ---
            Pose bestPose = getBestPoseForLocation(changedEntity, lookAhead);

            // If the target pose is "taller" (less restrictive), check if we can stand up safely
            if (isLowerPose(changedEntity.getPose(), bestPose)) {
                if (canEntityEnterPose(changedEntity, bestPose)) {
                    changedEntity.setPose(bestPose);
                }
            } else {
                // If the target pose is "shorter" (more restrictive), force it immediately to avoid collision
                changedEntity.setPose(bestPose);
            }
        }

        super.tick();
    }

    @Override
    protected boolean canMoveDirectly(@NotNull Vec3 pPosVec31, @NotNull Vec3 pPosVec32) {
        return super.canMoveDirectly(pPosVec31, pPosVec32);
    }

    /**
     * Resets the entity to a safer (taller) pose when it stops moving or finishes path.
     */
    private void maintainSafePose(ChangedEntity entity) {
        if (entity.getPose() != Pose.STANDING) {
            if (canEntityEnterPose(entity, Pose.STANDING)) {
                entity.setPose(Pose.STANDING);
            } else if (entity.getPose() == Pose.SWIMMING && canEntityEnterPose(entity, Pose.CROUCHING)) {
                entity.setPose(Pose.CROUCHING);
            }
        }
    }

    /**
     * Finds the most efficient pose (from Standing to Swimming) for a specific target point.
     */
    private Pose getBestPoseForLocation(ChangedEntity entity, Vec3 target) {
        if (canEntityEnterPoseIn(entity, Pose.STANDING, target)) return Pose.STANDING;
        if (canEntityEnterPoseIn(entity, Pose.CROUCHING, target)) return Pose.CROUCHING;
        return Pose.SWIMMING; // Smallest possible pose (Crawl)
    }

    /**
     * Helper to compare pose heights.
     */
    private boolean isLowerPose(Pose current, Pose target) {
        // Minecraft order: SWIMMING (0.6) < CROUCHING (1.5) < STANDING (1.8)
        return getPosePriority(target) > getPosePriority(current);
    }

    private int getPosePriority(Pose pose) {
        if (pose == Pose.SWIMMING) return 1;
        if (pose == Pose.CROUCHING) return 2;
        return 3; // STANDING
    }

    public static boolean canEntityEnterPose(ChangedEntity entity, Pose pose) {
        return (entity.overridePose == null || entity.overridePose == pose) && entity.level.noCollision(entity, entity.getBoundingBoxForPose(pose).deflate(1.0E-7D));
    }

    /**
     * Checks if the entity's hitbox for a specific pose would collide at a target position.
     */
    public static boolean canEntityEnterPoseIn(ChangedEntity entity, Pose pose, Vec3 targetPosition) {
        AABB boxAtTarget = entity.getBoundingBoxForPose(pose).move(targetPosition.subtract(entity.position()));
        AABB checkAt = boxAtTarget.deflate(1.0E-7D);

        return (entity.overridePose == null || entity.overridePose == pose) &&
                entity.level.noCollision(entity, checkAt);
    }
}