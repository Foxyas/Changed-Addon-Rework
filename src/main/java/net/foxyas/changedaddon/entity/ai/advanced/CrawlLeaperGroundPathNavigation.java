package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CrawlLeaperGroundPathNavigation extends GroundPathNavigation {

    public CrawlLeaperGroundPathNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new LeaperWalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.path == null || !(mob instanceof ChangedEntity changedEntity)) {
            return;
        }

        Node nextNode = this.path.getNode(Mth.clamp(path.getNextNodeIndex(), 0, path.getNodeCount() - 1));
        Vec3 currentPos = this.mob.position();
        Vec3 nextNodePos = nextNode.asVec3();

        // Interpolate a point ahead (lookahead) for smoother pose transitions
        Vec3 lookAhead = currentPos.lerp(nextNodePos, 1);

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

        double distance = currentPos.distanceTo(nextNodePos);

        boolean isJumpNode = nextNode instanceof IAdvancedNode advancedNode && advancedNode.isJumpNode();

        if (isJumpNode && distance <= 2 && !this.mob.onGround()) {
            this.mob.setSprinting(false);
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.85));
        }

        if (isJumpNode && distance >= 2 && this.mob.onGround() && !this.mob.horizontalCollision) {
            Vec3 velocity = this.mob.getDeltaMovement();
            Vec3 lookAngle = this.mob.getLookAngle();
            Vec3 directionToNode = nextNodePos.subtract(currentPos).normalize();

            // Verifica o alinhamento entre o olhar da mob e o destino
            double dot = lookAngle.dot(directionToNode);

            this.mob.setSprinting(true);
            this.mob.getJumpControl().jump();
            if (dot >= 0.5f) {
                // Apply a small velocity boost to ensure the entity crosses the gap
                this.mob.setDeltaMovement(velocity.add(lookAngle.scale(0.15D)));
                //advancedNode.setJumpNode(false);
            } else {

                // Apply a small velocity boost to ensure the entity crosses the gap
                this.mob.setDeltaMovement(velocity.add(directionToNode.scale(0.15D)));
                //advancedNode.setJumpNode(false);
            }

        }
    }

    @Override
    public void stop() {
        super.stop();
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

    private void tryReturnToStanding(ChangedEntity entity) {
        // Se estiver agachado/deitado e houver espaço em cima, volta a ficar de pé
        if (entity.getPose() != Pose.STANDING && canEntityEnterPose(entity, Pose.STANDING)) {
            entity.setPose(Pose.STANDING);
            // Recalcula para o pathfinder saber que agora somos mais altos
            this.recomputePath();
        }
    }

    private void maintainSafePose(ChangedEntity entity) {
        if (entity.getPose() != Pose.STANDING && canEntityEnterPose(entity, Pose.STANDING)) {
            entity.setPose(Pose.STANDING);
        }
    }

    private Pose getBestPoseForLocation(ChangedEntity entity, Vec3 target) {
        if (canEntityEnterPoseIn(entity, Pose.STANDING, target)) return Pose.STANDING;
        if (canEntityEnterPoseIn(entity, Pose.CROUCHING, target)) return Pose.CROUCHING;
        return Pose.SWIMMING;
    }

    public static boolean canEntityEnterPose(ChangedEntity entity, Pose pose) {
        return entity.level.noCollision(entity, entity.getBoundingBoxForPose(pose).deflate(1.0E-7D));
    }

    public static boolean canEntityEnterPoseIn(ChangedEntity entity, Pose pose, Vec3 targetPosition) {
        AABB boxAtTarget = entity.getBoundingBoxForPose(pose).move(targetPosition.subtract(entity.position()));
        AABB checkAt = boxAtTarget.deflate(1.0E-7D);

        return (entity.overridePose == null || entity.overridePose == pose) &&
                entity.level.noCollision(entity, checkAt);
    }
}