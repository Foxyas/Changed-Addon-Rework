package net.foxyas.changedaddon.process.FormsCustomStats;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.variants.ExtraVariantStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FormsStats {

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getPlayer();
        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariantInstance == null){
            return;
        }

        // Verifica se o jogador está segurando um item específico, ou se tem alguma condição
        if (transfurVariantInstance.getChangedEntity() instanceof ExtraVariantStats extraVariantStats) {
            event.setNewSpeed(event.getNewSpeed() * extraVariantStats.getBlockBreakSpeedMultiplier()); // More Fast Break
        }
    }

}
