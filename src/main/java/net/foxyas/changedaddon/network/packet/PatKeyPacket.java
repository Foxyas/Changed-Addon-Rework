package net.foxyas.changedaddon.network.packet;

import net.foxyas.changedaddon.process.features.PatFeatureHandle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PatKeyPacket(int type, int pressedMs) {

    public PatKeyPacket(FriendlyByteBuf buf) {
        this(buf.readVarInt(), buf.readVarInt());
    }

    public static void handler(PatKeyPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> pressAction(context.getSender(), message.type));
        context.setPacketHandled(true);
    }

    public static void pressAction(Player player, int type) {
        if (player == null) return;
        Level level = player.level;

        if (type == 0) {

            PatFeatureHandle.run(level, player);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(type);
        buf.writeVarInt(pressedMs);
    }
}
