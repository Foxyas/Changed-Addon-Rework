package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface ICrawlAbleEntity {

    static boolean canEnterPose(ChangedEntity entity, Pose pose) {
        return (entity.overridePose == null || entity.overridePose == pose) && entity.level.noCollision(entity, entity.getBoundingBoxForPose(pose).deflate(1.0E-7D));
    }

    default void crawlingSystem(ChangedEntity livingEntity, LivingEntity target, float swimSpeed) {
        updateSwimmingMovement(livingEntity, swimSpeed);
        if (target != null) {
            setCrawlingPoseIfNeeded(livingEntity, target);
            crawlToTarget(livingEntity, target);
        } else {

            Pose currentPose = livingEntity.getPose();
            Pose safePose = currentPose;

            if (!canEnterPose(livingEntity, currentPose)) {
                if (canEnterPose(livingEntity, Pose.STANDING)) {
                    safePose = Pose.STANDING;
                } else if (canEnterPose(livingEntity, Pose.CROUCHING)) {
                    safePose = Pose.CROUCHING;
                } else if (canEnterPose(livingEntity, Pose.SWIMMING)) {
                    safePose = Pose.SWIMMING;
                }
            }

            if (safePose != currentPose) {
                livingEntity.setPose(safePose);
                //this.refreshDimensions();
            }

        }
    }

    default void crawlingSystem(ChangedEntity livingEntity, LivingEntity target) {
        crawlingSystem(livingEntity, target, 0.015f);
    }

    default void crawlingSystem(LivingEntity target) {
        if (this instanceof ChangedEntity changedEntity) {
            crawlingSystem(changedEntity, target, 0.015f);
        }
    }

    default void crawlingSystem(LivingEntity target, float speed) {
        if (this instanceof ChangedEntity changedEntity) {
            crawlingSystem(changedEntity, target, speed);
        }
    }

    default void crawlingSystem(float speed) {
        if (this instanceof ChangedEntity changedEntity) {
            crawlingSystem(changedEntity, changedEntity.getTarget(), speed);
        }
    }

    default void onlyCrawlingSystem() {
        if (this instanceof ChangedEntity changedEntity) {
            OnlyCrawlingSystem(changedEntity, changedEntity.getTarget());
        }
    }

    default void OnlyCrawlingSystem(LivingEntity target) {
        if (this instanceof ChangedEntity changedEntity) {
            OnlyCrawlingSystem(changedEntity, target);
        }
    }

    default void OnlyCrawlingSystem(LivingEntity livingEntity, LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(livingEntity, target);
            crawlToTarget(livingEntity, target);
        } else {
            BlockPos above = new BlockPos((int) livingEntity.getX(), (int) livingEntity.getEyeY(), (int) livingEntity.getZ()).above();
            BlockState blockState = livingEntity.level.getBlockState(above);
            if (livingEntity.getPose() == Pose.SWIMMING && !livingEntity.isInWater() && (blockState.isAir() || !blockState.isSuffocating(livingEntity.level, above) || !blockState.isSolidRender(livingEntity.level, above))) {
                livingEntity.setPose(Pose.STANDING);
            }

            if (!livingEntity.isSwimming() && (!blockState.isAir() || blockState.isSuffocating(livingEntity.level, above) || blockState.isSolidRender(livingEntity.level, above))) {
                livingEntity.setPose(Pose.SWIMMING);
            }
        }
    }

    default void setCrawlingPoseIfNeeded(LivingEntity livingEntity, LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && livingEntity.getPose() != Pose.SWIMMING) {
            if (target.getY() < livingEntity.getEyeY() && !target.level.getBlockState(new BlockPos((int) target.getX(), (int) target.getEyeY(), (int) target.getZ()).above()).isAir()) {
                livingEntity.setPose(Pose.SWIMMING);
            }
        } else {
            if (!livingEntity.isSwimming() && livingEntity.level.getBlockState(new BlockPos((int) livingEntity.getX(), (int) livingEntity.getEyeY(), (int) livingEntity.getZ()).above()).isAir()) {
                livingEntity.setPose(Pose.STANDING);
            }
        }
    }

    default void crawlToTarget(LivingEntity livingEntity, LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && livingEntity.getPose() == Pose.SWIMMING) {
            Vec3 direction = target.position().subtract(livingEntity.position()).normalize();
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(direction.scale(0.05)));
        }
    }

    default void updateSwimmingMovement(ChangedEntity livingEntity, float speed) {
        if (livingEntity.isInWater()) {
            if (livingEntity.getTarget() != null) {
                Vec3 direction = livingEntity.getTarget().position().subtract(livingEntity.position()).normalize();
                if (livingEntity.isEyeInFluid(FluidTags.WATER)) {
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(direction.scale(speed)));
                } else {
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(direction.scale(speed / 4)));
                }
                livingEntity.getLookControl().setLookAt(livingEntity.getTarget(), 30, 30);
            }
            if (livingEntity.isEyeInFluid(FluidTags.WATER)) {
                livingEntity.setPose(Pose.SWIMMING);
                livingEntity.setSwimming(true);
            } else {
                livingEntity.setPose(Pose.STANDING);
                livingEntity.setSwimming(false);
            }
        } else {
            BlockPos above = new BlockPos((int) livingEntity.getX(), (int) livingEntity.getEyeY(), (int) livingEntity.getZ()).above();
            BlockState blockState = livingEntity.level.getBlockState(above);
            if (livingEntity.getPose() == Pose.SWIMMING && !livingEntity.isInWater() && (blockState.isAir() || !blockState.isSuffocating(livingEntity.level, above) || !blockState.isSolidRender(livingEntity.level, above))) {
                livingEntity.setPose(Pose.STANDING);
            }
        }
    }
}
