package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.packet.SyncTransfurVariantDietsPacket;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDietManager;
import net.foxyas.changedaddon.process.variantsExtraStats.visions.TransfurVisionReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class ModReloadListeners {

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new TransfurVisionReloadListener());
//        event.addListener(new TransfurVariantDietManager());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        SyncTransfurVariantDietsPacket packet = new SyncTransfurVariantDietsPacket(TransfurVariantDietManager.getAllDiets());

        if (event.getPlayer() != null) {
            // Sync to a single re-connecting player
            ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(event::getPlayer), packet);
        } else {
            // Sync to all connected players after /reload command
            ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), packet);
        }
    }
}
