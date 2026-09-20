package net.foxyas.changedaddon.mixins.mods.casualtiesCubed;

import net.foxyas.changedaddon.extension.RequiredMods;
import net.foxyas.changedaddon.util.EntityUtil;
import net.foxyas.changedaddon.util.MathFormulasUtil;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TransfurVariantInstance.class, remap = false)
@RequiredMods("casualties_cubed")
public class TransfurVariantInstanceMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickHook(CallbackInfo ci) {
        TransfurVariantInstance<?> self = (TransfurVariantInstance<?>) (Object) this;
        Player player = self.getHost();
        if (ProcessTransfur.isPlayerLatex(player)) {
            if (self.ageAsVariant % 80 == 0) { // each 4 seconds.
                changedAddonRework$GrowAllLimbs(player);
            }
        }
    }

    @Unique
    private void changedAddonRework$GrowAllLimbs(Player player) {
        player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent((self) -> {
            if (self.hunger() < 30) return;// Only grow back if it has more then 30 hunger.

            float healthRatio = self.bloodPercentage(); //EntityUtils.getHealthRatio(player);
            // Food: 0.0 a 1.0 (ex: 20/20 = 1.0)
            float foodRatio = EntityUtil.getFoodRatio(player, null);

            // Base de 100 ticks, escalada pela saúde e fome
            // Se ambos estiverem no máximo, ganha 100. Se um estiver baixo, ganha menos.
            float percentualProgress = ServerConfig.LIMB_REGROWTH_DURATION.get() * (0.10f * healthRatio * foodRatio);
            float progressBonus = (200f + percentualProgress);
            MathFormulasUtil.lerpEase(healthRatio * foodRatio, 100, 300, MathFormulasUtil.EasingType.QUAD_IN);

            // ------------------------

            boolean playedSound = false;

            for (Limb limb : Limb.values()) {
                LimbStatistics stats = self.getLimb(limb);
                if (!stats.isAmputated()) continue;

                Limb root = limb.getConnectedTo();
                Limb targetLimb = (root == null || !self.isAmputated(root)) ? limb : root;

                stats = self.getLimb(targetLimb);
                stats.progressRegrowth(progressBonus);
                if (!stats.isAmputated()) {
                    playedSound = true;
                }
            }

            if (playedSound) {
                player.playSound(ChangedSounds.TRANSFUR_BY_LATEX.get(), 1.0f, 1.0f);
            }
        });
    }
}
