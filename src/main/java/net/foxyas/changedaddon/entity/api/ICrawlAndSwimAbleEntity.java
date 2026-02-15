package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public interface ICrawlAndSwimAbleEntity {

    static boolean canEnterPose(ChangedEntity entity, Pose pose) {
        return (entity.overridePose == null || entity.overridePose == pose) && entity.level.noCollision(entity, entity.getBoundingBoxForPose(pose).deflate(1.0E-7D));
    }

    default void crawlingSystem(ChangedEntity livingEntity, LivingEntity target, float swimSpeed) {
        if (!updateSwimmingMovement(livingEntity, swimSpeed)) {
            if (target != null) {
                setCrawlingPoseIfNeeded(livingEntity, target);
                crawlToTarget(livingEntity, target);
            } else switchToSafePose(livingEntity);
        }
    }

    private void switchToSafePose(ChangedEntity livingEntity) {
        Pose currentPose = livingEntity.getPose();
        Pose safePose = currentPose;

        if (canEnterPose(livingEntity, Pose.STANDING)) {
            safePose = Pose.STANDING;
        } else if (canEnterPose(livingEntity, Pose.CROUCHING)) {
            safePose = Pose.CROUCHING;
        } else if (canEnterPose(livingEntity, Pose.SWIMMING)) {
            safePose = Pose.SWIMMING;
        }

        if (safePose != currentPose) {
            livingEntity.setPose(safePose);
            //this.refreshDimensions();
        }
    }

    default void crawlingSystem(ChangedEntity livingEntity, LivingEntity target) {
        crawlingSystem(livingEntity, target, 0.05f);
    }

    default void crawlingSystem(LivingEntity target) {
        if (this instanceof ChangedEntity changedEntity) {
            crawlingSystem(changedEntity, target, 0.05f);
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
            onlyCrawlingSystem(changedEntity, changedEntity.getTarget());
        }
    }

    default void onlyCrawlingSystem(LivingEntity target) {
        if (this instanceof ChangedEntity changedEntity) {
            onlyCrawlingSystem(changedEntity, target);
        }
    }

    default void onlyCrawlingSystem(ChangedEntity livingEntity, LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(livingEntity, target);
            crawlToTarget(livingEntity, target);
        } else switchToSafePose(livingEntity);
    }

    default void setCrawlingPoseIfNeeded(ChangedEntity livingEntity, LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && livingEntity.getPose() != Pose.SWIMMING) {
            if (target.getY() < livingEntity.getEyeY() && !target.level.getBlockState(new BlockPos((int) target.getX(), (int) target.getEyeY(), (int) target.getZ()).above()).isAir()) {
                livingEntity.setPose(Pose.SWIMMING);
            }
        } else {
            switchToSafePose(livingEntity);
        }
    }

    default void crawlToTarget(LivingEntity livingEntity, LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && livingEntity.getPose() == Pose.SWIMMING) {
            Vec3 direction = target.position().subtract(livingEntity.position()).normalize();
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(direction.scale(0.05)));
        }
    }

    default boolean updateSwimmingMovement(ChangedEntity livingEntity, float speed) {
        if (!livingEntity.isInWater())
            return false;

        Vec3 movementDir = null;

        LivingEntity target = livingEntity.getTarget();
        if (target != null) {
            movementDir = target
                    .position()
                    .subtract(livingEntity.position())
                    .normalize();
        }/* else if (livingEntity.getDeltaMovement().lengthSqr() > 0.0001) {
            movementDir = livingEntity.getDeltaMovement().normalize();
        }*/

        if (target != null) {
            if (!target.isInWater() && target.onGround()) {
                movementDir = null;
            }
        }

        if (movementDir != null) {
            float appliedSpeed = livingEntity.isEyeInFluid(FluidTags.WATER)
                    ? speed
                    : speed * 0.25F;

            float swimSpeed = (float) (livingEntity.getMoveControl().getSpeedModifier() * livingEntity.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
            livingEntity.setSpeed(swimSpeed);
            livingEntity.setDeltaMovement(movementDir.scale(appliedSpeed));

            float yaw = (float)(Mth.atan2(movementDir.z, movementDir.x) * (180F / Math.PI)) - 90.0F;
            livingEntity.setYRot(Mth.rotLerp(0.2F, livingEntity.getYRot(), yaw));
            livingEntity.yBodyRot = livingEntity.getYRot();
        }

        if (livingEntity.isEyeInFluid(FluidTags.WATER)) {
            livingEntity.setPose(Pose.SWIMMING);
            livingEntity.setSwimming(true);
            return true;
        } else {
            livingEntity.setPose(Pose.STANDING);
            livingEntity.setSwimming(false);
            return false;
        }
    }
}
