
package net.foxyas.changedaddon.network;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.procedures.TradeProcedure;
import net.foxyas.changedaddon.world.inventory.FoxyasGui2Menu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record FoxyasGuiButtonMessage(int buttonId, int x, int y, int z) {

	public FoxyasGuiButtonMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(FoxyasGuiButtonMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.buttonId);
		buf.writeVarInt(message.x);
		buf.writeVarInt(message.y);
		buf.writeVarInt(message.z);
	}

	public static void handler(FoxyasGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
            int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, message.buttonId, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player player, int buttonID, int x, int y, int z) {
		Level world = player.level;
        // security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z))) return;

		if (buttonID == 0) {

			TradeProcedure.execute(player);
		}

		if (buttonID == 1) {
			if(!(player instanceof ServerPlayer sPlayer)) return;
			BlockPos pos = new BlockPos(x, y, z);
			NetworkHooks.openGui(sPlayer, new MenuProvider() {
				@Override
				public @NotNull Component getDisplayName() {
					return new TextComponent("FoxyasGui2");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
					return new FoxyasGui2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
				}
			}, pos);
		}
	}
}
