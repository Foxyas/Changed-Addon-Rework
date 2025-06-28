
package net.foxyas.changedaddon.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.world.inventory.UnifuserguiMenu;
import net.foxyas.changedaddon.procedures.ShowUnifuserRecipesProcedure;
import net.foxyas.changedaddon.procedures.SetRecipePageProcedure;
import net.foxyas.changedaddon.procedures.Openpage3Procedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipeProcedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipe5Procedure;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnifuserguiButtonMessage {
	private final int buttonID, x, y, z;

	public UnifuserguiButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public UnifuserguiButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(UnifuserguiButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(UnifuserguiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
		HashMap guistate = UnifuserguiMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			ShowUnifuserRecipesProcedure.execute(entity);
		}
		if (buttonID == 1) {

			Openpage3Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 2) {

			OpenBookRecipeProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 3) {

			OpenBookRecipe5Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 4) {

			Openpage3Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 5) {

			SetRecipePageProcedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(UnifuserguiButtonMessage.class, UnifuserguiButtonMessage::buffer, UnifuserguiButtonMessage::new, UnifuserguiButtonMessage::handler);
	}
}
