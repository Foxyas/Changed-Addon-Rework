package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public interface ICrawlAndSwimAbleEntity {

    static boolean canEnterPose(ChangedEntity entity, Pose pose) {
        return (entity.overridePose == null || entity.overridePose == pose) && entity.level.noCollision(entity, entity.getBoundingBoxForPose(pose).deflate(1.0E-7D));
    }

    @Nullable(value = "Should only be null in a IllegalState")
    default LivingEntity asEntity() {
        return this instanceof LivingEntity livingEntity ? livingEntity : null;
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
        crawlingSystem(livingEntity, target, (float) livingEntity.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
    }

    default void crawlingSystem(LivingEntity target) {
        if (this instanceof ChangedEntity changedEntity) {
            crawlingSystem(changedEntity, target, (float) changedEntity.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
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
        }

        if (movementDir != null) {
            float appliedSpeed = livingEntity.isEyeInFluid(FluidTags.WATER)
                    ? speed
                    : speed * 0.75F;

            float swimSpeed = (float) (livingEntity.getMoveControl().getSpeedModifier() * livingEntity.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
            livingEntity.setSpeed(swimSpeed);
            Vec3 scale = movementDir.scale(appliedSpeed);
            livingEntity.setDeltaMovement(scale);
            livingEntity.getNavigation().stop();

            Vec3 add = livingEntity.position().add(scale);
            livingEntity.getLookControl().setLookAt(add.x, add.y, add.z, 180, 180);
            livingEntity.setYBodyRot(livingEntity.getYHeadRot());

        }

        if (target != null && target.isAlive() && (target.isSwimming() || target.distanceToSqr(livingEntity) >= 6) && livingEntity.isInWater()) {
            if (target.distanceToSqr(livingEntity) >= 6 || target.isSwimming()) {
                livingEntity.setPose(Pose.SWIMMING);
            } else {
                livingEntity.setPose(Pose.STANDING);
            }
            livingEntity.setSwimming(true);
            return true;
        } /* else if (livingEntity.isEyeInFluid(FluidTags.WATER)) {
            livingEntity.setPose(Pose.SWIMMING);
            livingEntity.setSwimming(true);
            return true;
        } */ else {
            livingEntity.setPose(Pose.STANDING);
            livingEntity.setSwimming(false);
            return false;
        }
    }
}
