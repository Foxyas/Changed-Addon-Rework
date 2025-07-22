package net.foxyas.changedaddon.entity.CustomHandle;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

public interface CrawlFeature {

    default void crawlingSystem(ChangedEntity livingEntity, LivingEntity target, float swimSpeed) {
        updateSwimmingMovement(livingEntity, swimSpeed);
        if (target != null) {
            setCrawlingPoseIfNeeded(livingEntity, target);
            crawlToTarget(livingEntity, target);
        } else {
            if (!livingEntity.isSwimming() && !livingEntity.level.getBlockState(new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ())).isAir()) {
                livingEntity.setPose(Pose.SWIMMING);
            }
        }
    }

    default void crawlingSystem(ChangedEntity livingEntity, LivingEntity target) {
        crawlingSystem(livingEntity, target, 0.07f);
    }

    default void OnlyCrawlingSystem(LivingEntity livingEntity, LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(livingEntity, target);
            crawlToTarget(livingEntity, target);
        } else {
            if (!livingEntity.isSwimming() && !livingEntity.level.getBlockState(new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ())).isAir()) {
                livingEntity.setPose(Pose.SWIMMING);
            }
        }
    }

    default void setCrawlingPoseIfNeeded(LivingEntity livingEntity, LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && livingEntity.getPose() != Pose.SWIMMING) {
            if (target.getY() < livingEntity.getEyeY() && !target.level.getBlockState(new BlockPos(target.getX(), target.getEyeY(), target.getZ()).above()).isAir()) {
                livingEntity.setPose(Pose.SWIMMING);
            }
        } else {
            if (!livingEntity.isSwimming() && livingEntity.level.getBlockState(new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ()).above()).isAir()) {
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
            }
            if (livingEntity.isEyeInFluid(FluidTags.WATER)) {
                livingEntity.setPose(Pose.SWIMMING);
                livingEntity.setSwimming(true);
            } else {
                livingEntity.setPose(Pose.STANDING);
                livingEntity.setSwimming(false);
            }
        } else if (livingEntity.getPose() == Pose.SWIMMING && !livingEntity.isInWater() && livingEntity.level.getBlockState(new BlockPos(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ()).above()).isAir()) {
            livingEntity.setPose(Pose.STANDING);
        }
    }
}
