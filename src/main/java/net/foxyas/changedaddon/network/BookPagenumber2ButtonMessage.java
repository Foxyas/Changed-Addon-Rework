
package net.foxyas.changedaddon.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.foxyas.changedaddon.world.inventory.BookPagenumber2Menu;
import net.foxyas.changedaddon.procedures.Openpage4Procedure;
import net.foxyas.changedaddon.procedures.Openpage3Procedure;
import net.foxyas.changedaddon.procedures.Openpage1Procedure;
import net.foxyas.changedaddon.procedures.Openbookpage5Procedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipeProcedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipe5Procedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipe4Procedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipe3Procedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipe2Procedure;
import net.foxyas.changedaddon.procedures.OpenBookRecipe11Procedure;
import net.foxyas.changedaddon.procedures.ClosemenuProcedure;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BookPagenumber2ButtonMessage {
	private final int buttonID, x, y, z;

	public BookPagenumber2ButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public BookPagenumber2ButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(BookPagenumber2ButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(BookPagenumber2ButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
		Level world = entity.level;
		HashMap guistate = BookPagenumber2Menu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			ClosemenuProcedure.execute(entity);
		}
		if (buttonID == 1) {

			Openpage1Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 2) {

			Openbookpage5Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 3) {

			OpenBookRecipe3Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 4) {

			OpenBookRecipe4Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 5) {

			OpenBookRecipe2Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 6) {

			OpenBookRecipeProcedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 7) {

			OpenBookRecipe5Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 8) {

			Openpage3Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 9) {

			Openpage4Procedure.execute(world, x, y, z, entity);
		}
		if (buttonID == 10) {

			OpenBookRecipe11Procedure.execute(world, x, y, z, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(BookPagenumber2ButtonMessage.class, BookPagenumber2ButtonMessage::buffer, BookPagenumber2ButtonMessage::new, BookPagenumber2ButtonMessage::handler);
	}
}
