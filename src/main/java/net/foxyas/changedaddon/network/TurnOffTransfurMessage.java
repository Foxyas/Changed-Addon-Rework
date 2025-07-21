
package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TurnOffTransfurMessage {
	int type, pressedms;

	public TurnOffTransfurMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public TurnOffTransfurMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(TurnOffTransfurMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(TurnOffTransfurMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms);
		});
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

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(TurnOffTransfurMessage.class, TurnOffTransfurMessage::buffer, TurnOffTransfurMessage::new, TurnOffTransfurMessage::handler);
	}
}
