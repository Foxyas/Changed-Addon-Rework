package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;

public interface IDynamicRideOffsetEntity {

    default double getTorsoYOffset(ChangedEntity self) {
        float scale = self.getScale();

        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize(self) - 1.0F) * 2.0F;

        double base = Mth.lerp(
                Mth.lerp(
                        1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F),
                        ageSin * ageSin * ageSin * ageSin,
                        1.0F - ageCos * ageCos * ageCos * ageCos
                ),
                0.95F,
                0.87F
        ) + bpiSize;

        return base * scale;
    }

    default double getTorsoYOffsetForFallFly(ChangedEntity self) {
        float bpiSize = (self.getBasicPlayerInfo().getSize(self) - 1.0F) * 2.0F;
        return (0.375 + bpiSize) * self.getScale();
    }

    default double getPassengersRidingOffset(ChangedEntity self, double defaultValue) {
        if (self.getPose() == Pose.STANDING || self.getPose() == Pose.CROUCHING) {
            return defaultValue + getTorsoYOffset(self) + (self.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(self);
    }
}
