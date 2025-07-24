
package net.foxyas.changedaddon.network;

import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record TurnOffTransfurMessage(int type, int pressedMs) {

	public TurnOffTransfurMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(TurnOffTransfurMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.type);
		buf.writeVarInt(message.pressedMs);
	}

	public static void handler(TurnOffTransfurMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> pressAction(context.getSender(), message.type, message.pressedMs));
		context.setPacketHandled(true);
	}

	public static void pressAction(Player player, int type, int pressedms) {
		if(player == null) return;

		if (type == 0) {
			if(!ProcessTransfur.isPlayerTransfurred(player)) return;

			TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
			TransfurMode mode = tf.transfurMode;

			tf.transfurMode = mode == TransfurMode.NONE ? tf.getParent().transfurMode : TransfurMode.NONE;
		}
	}
}
