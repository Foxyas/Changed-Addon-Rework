package net.foxyas.changedaddon.entity.ai.advanced.goals;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SwitchToBestPose extends Goal {

    protected PathfinderMob mob;
    protected Path currentPath = null;

    public SwitchToBestPose(PathfinderMob mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        PathNavigation pathNavigation = mob.getNavigation();
        currentPath = pathNavigation.getPath();
        return pathNavigation.isStuck();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    protected int adjustedTickDelay(int pAdjustment) {
        return super.adjustedTickDelay(pAdjustment);
    }

    @Override
    public void tick() {
        super.tick();


        // Pega o próximo ponto do caminho
        Node nextNode = currentPath.getNextNode();
        Vec3 nextNodePosition = Vec3.atCenterOf(nextNode.asBlockPos());

        // Tenta encontrar a melhor pose para o próximo nó
        if (mob instanceof ChangedEntity changedMob) {
            switchToSafePoseFor(changedMob, nextNodePosition);
        }

    }

    public static void switchToSafePose(ChangedEntity livingEntity) {
        Pose currentPose = livingEntity.getPose();
        Pose safePose = currentPose;

        if (canEntityEnterPose(livingEntity, Pose.STANDING)) {
            safePose = Pose.STANDING;
        } else if (canEntityEnterPose(livingEntity, Pose.CROUCHING)) {
            safePose = Pose.CROUCHING;
        } else if (canEntityEnterPose(livingEntity, Pose.SWIMMING)) {
            safePose = Pose.SWIMMING;
        }

        if (safePose != currentPose) {
            livingEntity.setPose(safePose);
        }
    }

    public static void switchToSafePoseFor(ChangedEntity livingEntity, Vec3 targetPosition) {
        Pose currentPose = livingEntity.getPose();
        Pose safePose = currentPose;

        if (canEntityEnterPoseIn(livingEntity, Pose.STANDING, targetPosition)) {
            safePose = Pose.STANDING;
        } else if (canEntityEnterPoseIn(livingEntity, Pose.CROUCHING, targetPosition)) {
            safePose = Pose.CROUCHING;
        } else if (canEntityEnterPoseIn(livingEntity, Pose.SWIMMING, targetPosition)) {
            safePose = Pose.SWIMMING;
        }

        if (safePose != currentPose) {
            livingEntity.setPose(safePose);
        }
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
