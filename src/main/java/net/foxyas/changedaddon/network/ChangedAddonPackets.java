package net.foxyas.changedaddon.network;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.network.packet.ChangedPacket;
import net.ltxprogrammer.changed.util.UniversalDist;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.concurrent.CompletableFuture;
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

    private <T> BiConsumer<T, FriendlyByteBuf> wrapEncoder(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder) {
        return (packet, buffer) -> {
            try {
                encoder.accept(packet, buffer);
            } catch (Exception e) {
                throw new RuntimeException("Exception while encoding " + messageType.getSimpleName() + ": " + e, e);
            }
        };
    }

    private <T> Function<FriendlyByteBuf, T> wrapDecoder(Class<T> messageType, Function<FriendlyByteBuf, T> decoder) {
        return buffer -> {
            try {
                return decoder.apply(buffer);
            } catch (Exception e) {
                throw new RuntimeException("Exception while decoding " + messageType.getSimpleName() + ": " + e, e);
            }
        };
    }

    private <T extends ChangedPacket> BiConsumer<T, Supplier<NetworkEvent.Context>> wrapHandler(Class<T> messageType, ChangedPacket.Handler<T> handler) {
        return (packet, contextSupplier) -> {
            final var context = contextSupplier.get();
            final var executor = LogicalSidedProvider.WORKQUEUE.get(context.getDirection().getReceptionSide());
            final var levelFuture = CompletableFuture.supplyAsync(() -> UniversalDist.getLevel(context), executor);
            final var future = handler.accept(packet, context, levelFuture, executor)
                    .exceptionally(error -> {
                        Changed.LOGGER.error("Exception while handling {}: {}", messageType.getSimpleName(), error);
                        return null;
                    });

            if (future.isDone())
                levelFuture.cancel(false);
        };
    }

    private <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
                                              BiConsumer<T, Supplier<NetworkEvent.Context>> handler) {
        packetHandler.registerMessage(messageID++, messageType,
                wrapEncoder(messageType, encoder),
                wrapDecoder(messageType, decoder),
                handler);
    }

    private <T extends ChangedPacket> void addNetworkMessage(Class<T> messageType, Function<FriendlyByteBuf, T> ctor) {
        packetHandler.registerMessage(messageID++, messageType,
                wrapEncoder(messageType, T::write),
                wrapDecoder(messageType, ctor),
                wrapHandler(messageType, T::handle));
    }
}
