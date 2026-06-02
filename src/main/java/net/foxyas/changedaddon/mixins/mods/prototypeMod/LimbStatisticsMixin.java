package net.foxyas.changedaddon.mixins.mods.prototypeMod;

import net.foxyas.changedaddon.compatibility.painPrototype.LimbStatisticsExtensor;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = LimbStatistics.class, remap = false)
public class LimbStatisticsMixin implements LimbStatisticsExtensor {

    @Shadow
    float regrowthProgress;

    @Override
    public void setRegrowthProgress(float value) {
        this.regrowthProgress = value;
    }
}
