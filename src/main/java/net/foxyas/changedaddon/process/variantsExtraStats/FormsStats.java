package net.foxyas.changedaddon.process.variantsExtraStats;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.variant.ILatexVariantExtraStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FormsStats {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariantInstance == null) {
            return;
        }

        if (transfurVariantInstance.getSwimEfficiency() > 1 && !player.onGround() && player.isEyeInFluidType(ForgeMod.WATER_TYPE.get())) {
            event.setNewSpeed(event.getOriginalSpeed() * 5); // Nullify the slow breaking
        }

        // Verifica se o jogador está segurando um item específico, ou se tem alguma condição
        if (transfurVariantInstance.getChangedEntity() instanceof ILatexVariantExtraStats ILatexVariantExtraStats) {
            event.setNewSpeed(event.getNewSpeed() * ILatexVariantExtraStats.getBlockBreakSpeedMultiplier()); // More Fast Break
        }
    }

}
