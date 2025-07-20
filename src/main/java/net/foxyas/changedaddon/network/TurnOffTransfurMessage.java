
package net.foxyas.changedaddon.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;

import net.foxyas.changedaddon.procedures.TurnOffTransfurOnKeyPressedProcedure;
import net.foxyas.changedaddon.ChangedAddonMod;

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

	public static void pressAction(Player entity, int type, int pressedms) {
		Level world = entity.level;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(entity.blockPosition()))
			return;
		if (type == 0) {

			TurnOffTransfurOnKeyPressedProcedure.execute(entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(TurnOffTransfurMessage.class, TurnOffTransfurMessage::buffer, TurnOffTransfurMessage::new, TurnOffTransfurMessage::handler);
	}
}
