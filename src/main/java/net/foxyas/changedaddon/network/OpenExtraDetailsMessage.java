
package net.foxyas.changedaddon.network;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.world.inventory.TransfurSoundsGuiMenu;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.GameType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record OpenExtraDetailsMessage(int type, int pressedMs) {

	public OpenExtraDetailsMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(OpenExtraDetailsMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.type);
		buf.writeVarInt(message.pressedMs);
	}

	public static void handler(OpenExtraDetailsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> pressAction(context.getSender(), message.type, message.pressedMs));
		context.setPacketHandled(true);
	}

	public static void pressAction(Player player, int type, int pressedms) {
		if(player == null || player.level.isClientSide) return;

		if (type == 0) {
			if(((ServerPlayer)player).gameMode.getGameModeForPlayer() == GameType.SPECTATOR) return;

			if(ProcessTransfur.isPlayerTransfurred(player)){
				NetworkHooks.openGui((ServerPlayer) player, new MenuProvider() {
					@Override
					public @NotNull Component getDisplayName() {
						return new TextComponent("TransfurSoundsGui");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
						return new TransfurSoundsGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(player.blockPosition()));
					}
				}, player.blockPosition());
			} else {
				ChangedAddonModVariables.PlayerVariables vars = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
				if (vars != null && vars.showwarns) {
					player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.when_not.transfur").getString())), true);
				}
			}
		}
	}
}
