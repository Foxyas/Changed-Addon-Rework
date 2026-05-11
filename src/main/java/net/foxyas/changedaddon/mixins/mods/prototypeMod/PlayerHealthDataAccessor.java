package net.foxyas.changedaddon.mixins.mods.prototypeMod;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.foxyas.changedaddon.extension.RequiredMods;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = PlayerHealthData.class, remap = false)
@RequiredMods("casualties_cubed")
public interface PlayerHealthDataAccessor {

    @Accessor("limbStats")
    Map<Limb, LimbStatistics> getLimbStats();
}
