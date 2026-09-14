package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.process.features.PatFeatureHandle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PatKeyPacket(PatType type) {

    public enum PatType {
        RESET,
        SINGLE,
        CONTINUOS
    }

    public PatKeyPacket(FriendlyByteBuf buf) {
        this(buf.readEnum(PatType.class));
    }

    public static void handler(PatKeyPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, PatType type) {
        if (player == null || player.isSpectator()) return;

        PatFeatureHandle.run(player, type);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
    }
}
