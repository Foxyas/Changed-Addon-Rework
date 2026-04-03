package net.foxyas.changedaddon.network.packet.utils;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketsUtils {
    public static void sendToPlayer(SimpleChannel simpleChannel, Object message, ServerPlayer player) {
        simpleChannel.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        ChangedAddonMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
