
package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GeneratorGuiButtonMessage {
	private final int buttonID, x, y, z;

	public GeneratorGuiButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public GeneratorGuiButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(GeneratorGuiButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(GeneratorGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(GeneratorGuiButtonMessage.class, GeneratorGuiButtonMessage::buffer, GeneratorGuiButtonMessage::new, GeneratorGuiButtonMessage::handler);
	}
}
