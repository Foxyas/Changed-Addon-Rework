
package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.process.features.ProcessPatFeature;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PatKeyMessage(int type, int pressedMs) {

	public PatKeyMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(PatKeyMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.type);
		buf.writeVarInt(message.pressedMs);
	}

	public static void handler(PatKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> pressAction(context.getSender(), message.type, message.pressedMs));
		context.setPacketHandled(true);
	}

	public static void pressAction(Player player, int type, int pressedms) {
		if(player == null) return;
		Level level = player.level;

		if (type == 0) {

			ProcessPatFeature.ProcessPat(level, player);
		}
	}
}
