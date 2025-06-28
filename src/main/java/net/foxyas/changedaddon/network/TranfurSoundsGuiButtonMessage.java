
package net.foxyas.changedaddon.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.world.inventory.TranfurSoundsGuiMenu;
import net.foxyas.changedaddon.procedures.SetCoolDownOffProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundhowlProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundhissProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundgrowlProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundbarkProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundRoarProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundPurreowProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundPurrProcedure;
import net.foxyas.changedaddon.procedures.PlaySoundMeowProcedure;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TranfurSoundsGuiButtonMessage {
	private final int buttonID, x, y, z;

	public TranfurSoundsGuiButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public TranfurSoundsGuiButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(TranfurSoundsGuiButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(TranfurSoundsGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, buttonID, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		HashMap guistate = TranfurSoundsGuiMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			PlaySoundPurrProcedure.execute(world, entity);
		}
		if (buttonID == 1) {

			PlaySoundMeowProcedure.execute(world, entity);
		}
		if (buttonID == 2) {

			PlaySoundgrowlProcedure.execute(world, entity);
		}
		if (buttonID == 3) {

			PlaySoundbarkProcedure.execute(world, entity);
		}
		if (buttonID == 4) {

			PlaySoundhowlProcedure.execute(world, entity);
		}
		if (buttonID == 5) {

			PlaySoundhissProcedure.execute(world, entity);
		}
		if (buttonID == 6) {

			PlaySoundPurreowProcedure.execute(world, entity);
		}
		if (buttonID == 7) {

			SetCoolDownOffProcedure.execute(entity);
		}
		if (buttonID == 8) {

			PlaySoundRoarProcedure.execute(world, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(TranfurSoundsGuiButtonMessage.class, TranfurSoundsGuiButtonMessage::buffer, TranfurSoundsGuiButtonMessage::new, TranfurSoundsGuiButtonMessage::handler);
	}
}
