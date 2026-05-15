package net.foxyas.changedaddon.mixins.mods.prototypeMod;

import net.foxyas.changedaddon.util.EntityUtils;
import net.foxyas.changedaddon.util.MathFormulasUtils;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
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
        if (!ServerConfig.LIMB_REGROWTH.get()) return;

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

            // --- Scaling ---
            // Health/Blood: 0.0 a 1.0 (ex: 5/5 = 1.0)
            float healthRatio = self.getBloodVolume() / 5f; //EntityUtils.getHealthRatio(player);
            // Food: 0.0 a 1.0 (ex: 20/20 = 1.0)
            float foodRatio = EntityUtils.getFoodRatio(player, null);

            // Base de 100 ticks, escalada pela saúde e fome
            // Se ambos estiverem no máximo, ganha 100. Se um estiver baixo, ganha menos.
            float progressBonus = 100f * healthRatio * foodRatio;
            MathFormulasUtils.lerpEase(healthRatio * foodRatio, 100, 300, MathFormulasUtils.EasingType.QUAD_IN);

            // Valor máximo de regrow (20 ticks * 60 segundos = 1200)
            float maxRegrow = 20 * 60;
            // ------------------------

            boolean playSound = false;

            LimbStatistics stats;
            for (Limb limb : limbStats.keySet()) {
                if (!self.isAmputated(limb) || self.isAmputated(limb.getConnectedTo())) continue;

                stats = limbStats.get(limb);

                stats.setRegrowthProgress(stats.getRegrowthProgress() + progressBonus);
                playSound = true;

                // Aplica o bônus escalonado, limitando ao máximo de 1200
                if (stats.getRegrowthProgress() >= maxRegrow) {
                    stats.setAmputated(false);
                    stats.setRegrowthProgress(0);
                }
            }

            if (playSound) {
                player.playSound(ChangedSounds.TRANSFUR_BY_LATEX.get(), 1.0f, 1.0f);
            }
        });
    }
}
