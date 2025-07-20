package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.packets.KeyPressPacket;
import net.foxyas.changedaddon.network.packets.SyncTransfurVisionsPacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonPacketsRegistry {
    @SubscribeEvent
    public static void registerPackets(FMLConstructModEvent event) {
        ChangedAddonMod.addNetworkMessage(KeyPressPacket.class, KeyPressPacket::encode, KeyPressPacket::decode, KeyPressPacket::handle);
        ChangedAddonMod.addNetworkMessage(SyncTransfurVisionsPacket.class, SyncTransfurVisionsPacket::encode, SyncTransfurVisionsPacket::decode, SyncTransfurVisionsPacket::handle);
    }
}