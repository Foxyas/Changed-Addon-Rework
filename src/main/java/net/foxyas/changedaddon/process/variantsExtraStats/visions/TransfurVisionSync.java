package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.packets.SyncTransfurVisionsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static net.foxyas.changedaddon.network.packets.utils.PacketsUtils.sendToPlayer;

@Mod.EventBusSubscriber
public class TransfurVisionSync {
    public static void syncTo(ServerPlayer player) {
        List<TransfurVariantVision> all = new ArrayList<>(TransfurVisionRegistry.getAll());
        sendToPlayer(ChangedAddonMod.PACKET_HANDLER,new SyncTransfurVisionsPacket(all), player);
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            TransfurVisionSync.syncTo(serverPlayer);
        }
    }
}
