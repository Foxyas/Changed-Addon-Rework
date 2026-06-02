package net.foxyas.changedaddon.mixins.mods.prototypeMod;

import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = PlayerHealthData.class, remap = false)
@RequiredMods("casualties_cubed")
public abstract class PlayerHealthDataMixin {

    @Final
    @Shadow
    private Map<Limb, LimbStatistics> limbStats;

    @Inject(method = "tickUpdate", at = @At("TAIL"))
    private void tickUpdateHook(ServerPlayer player, CallbackInfo ci) {
        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariant != null && ProcessTransfur.isPlayerLatex(player)) {
            if (transfurVariant.ageAsVariant % 80 == 0) { // each 4 seconds.
                if (player.getFoodData().getFoodLevel() >= 6) { // Only grow back if it has more then 3 hunger icons.
                    changedAddonRework$GrowAllLimbs();
                }
            }
        }

    }

    @Unique
    private void changedAddonRework$GrowAllLimbs() {
        PlayerHealthData self = (PlayerHealthData) (Object) this;
        this.limbStats.keySet().forEach(limb -> self.getLimb(limb).setAmputated(false));
    }
}
