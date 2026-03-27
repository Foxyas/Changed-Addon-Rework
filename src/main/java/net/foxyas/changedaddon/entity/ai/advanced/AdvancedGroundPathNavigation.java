package net.foxyas.changedaddon.entity.ai.advanced;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
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

        // interpola um ponto à frente (lookahead)
        Vec3 lookAhead = currentPos.lerp(nextNodePos, 0.5);

        if (mob instanceof ChangedEntity changedEntity) {
            Pose bestPose = getBestPoseForLocation(changedEntity, lookAhead);

            if (isLowerPose(changedEntity.getPose(), bestPose)) {
                if (canEntityEnterPose(changedEntity, bestPose)) {
                    changedEntity.setPose(bestPose);
                }
            } else {
                changedEntity.setPose(bestPose);
            }
        }

        super.tick();
    }

    private void maintainSafePose(ChangedEntity entity) {
        if (entity.getPose() != Pose.STANDING) {
            if (canEntityEnterPose(entity, Pose.STANDING)) {
                entity.setPose(Pose.STANDING);
            } else if (entity.getPose() == Pose.SWIMMING && canEntityEnterPose(entity, Pose.CROUCHING)) {
                entity.setPose(Pose.CROUCHING);
            }
        }
    }

    private Pose getBestPoseForLocation(ChangedEntity entity, Vec3 target) {
        if (canEntityEnterPoseIn(entity, Pose.STANDING, target)) return Pose.STANDING;
        if (canEntityEnterPoseIn(entity, Pose.CROUCHING, target)) return Pose.CROUCHING;
        return Pose.SWIMMING; // Menor pose possível (Crawl)
    }

    private boolean isLowerPose(Pose current, Pose target) {
        // No Minecraft: SWIMMING (0.6) < CROUCHING (1.5) < STANDING (1.8)
        // Retorna true se a target for "mais alta" que a current
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

    public static boolean canEntityEnterPoseIn(ChangedEntity entity, Pose pose, Vec3 targetPosition) {
        AABB boxAtTarget = entity.getBoundingBoxForPose(pose).move(targetPosition.subtract(entity.position()));
        AABB checkAt = boxAtTarget.deflate(1.0E-7D);

        return (entity.overridePose == null || entity.overridePose == pose) &&
                entity.level.noCollision(entity, checkAt);
    }
}