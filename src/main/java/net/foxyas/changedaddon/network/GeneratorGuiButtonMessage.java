
package net.foxyas.changedaddon.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GeneratorGuiButtonMessage(int buttonId, int x, int y, int z) {

	public GeneratorGuiButtonMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(GeneratorGuiButtonMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.buttonId);
		buf.writeVarInt(message.x);
		buf.writeVarInt(message.y);
		buf.writeVarInt(message.z);
	}

	public static void handler(GeneratorGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
		if(player == null) return;
		Level level = player.level;
        // security measure to prevent arbitrary chunk generation
		if (!level.hasChunkAt(new BlockPos(x, y, z))) return;

		if (buttonID == 0) {
			if(level.isClientSide) return;

			BlockPos pos = new BlockPos(x, y, z);
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if(blockEntity == null) return;

			BlockState state = level.getBlockState(pos);
			boolean enabled = blockEntity.getTileData().getBoolean("turn_on");

			if (enabled) {
				blockEntity.getTileData().putBoolean("turn_on", false);
				player.displayClientMessage(new TextComponent("generator disabled"), true);
			} else {
				blockEntity.getTileData().putBoolean("turn_on", true);
				player.displayClientMessage(new TextComponent("generator enabled"), true);
			}

			level.sendBlockUpdated(pos, state, state, 3);
		}
	}
}
