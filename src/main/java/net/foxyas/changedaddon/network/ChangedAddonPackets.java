package net.foxyas.changedaddon.network;

import net.ltxprogrammer.changed.network.packet.ChangedPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ChangedAddonPackets {
    private final SimpleChannel packetHandler;
    private int messageID = 0;

    public ChangedAddonPackets(SimpleChannel packetHandler) {
        this.packetHandler = packetHandler;
    }

    public void registerPackets() {
    }

    private <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        this.packetHandler.registerMessage(this.messageID++, messageType, encoder, decoder, messageConsumer);
    }

    private <T extends ChangedPacket> void addNetworkMessage(Class<T> messageType, Function<FriendlyByteBuf, T> ctor) {
        this.addNetworkMessage(messageType, ChangedPacket::write, ctor, ChangedPacket::handle);
    }
}
