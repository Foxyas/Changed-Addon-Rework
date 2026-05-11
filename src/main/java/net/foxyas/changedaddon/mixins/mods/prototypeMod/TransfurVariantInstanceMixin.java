package net.foxyas.changedaddon.mixins.mods.prototypeMod;

import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = TransfurVariantInstance.class, remap = false)
@RequiredMods("casualties_cubed")
public class TransfurVariantInstanceMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHook(CallbackInfo ci) {
        TransfurVariantInstance<?> self = (TransfurVariantInstance<?>) (Object) this;
        Player player = self.getHost();
        if (ProcessTransfur.isPlayerLatex(player)) {
            if (self.ageAsVariant % 80 == 0) { // each 4 seconds.
                if (player.getFoodData().getFoodLevel() >= 6) { // Only grow back if it has more then 3 hunger icons.
                    changedAddonRework$GrowAllLimbs(player);
                }
            }
        }
    }

    @Unique
    private void changedAddonRework$GrowAllLimbs(Player player) {
        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent((self) -> {
            Map<Limb, LimbStatistics> limbStats = ((PlayerHealthDataAccessor) self).getLimbStats();
            limbStats.keySet().forEach(limb -> self.setlimbAmputated(limb, false));
            player.playSound(ChangedSounds.TRANSFUR_BY_LATEX.get(), 1, 1);
        });
    }
}
