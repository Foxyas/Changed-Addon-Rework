
package net.foxyas.changedaddon.network;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.world.inventory.FightToKeepConsciousnessMinigameMenu;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class OpenStruggleMenuMessage {
	int type, pressedms;

	public OpenStruggleMenuMessage(int type, int pressedms) {
		this.type = type;
		this.pressedms = pressedms;
	}

	public OpenStruggleMenuMessage(FriendlyByteBuf buffer) {
		this.type = buffer.readInt();
		this.pressedms = buffer.readInt();
	}

	public static void buffer(OpenStruggleMenuMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.type);
		buffer.writeInt(message.pressedms);
	}

	public static void handler(OpenStruggleMenuMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			pressAction(context.getSender(), message.type, message.pressedms);
		});
		context.setPacketHandled(true);
	}

	public static void pressAction(Player player, int type, int pressedms) {
		if(player == null) return;

		if (type == 0) {
			if(!(player instanceof ServerPlayer sPlayer)) return;

			ChangedAddonModVariables.PlayerVariables vars = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
			if (!ProcessTransfur.isPlayerTransfurred(player) || vars == null || !vars.concience_Fight) return;

			NetworkHooks.openGui(sPlayer, new MenuProvider() {
				@Override
				public @NotNull Component getDisplayName() {
					return new TextComponent("FightToKeepConsciousnessMinigame");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
					return new FightToKeepConsciousnessMinigameMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(sPlayer.blockPosition()));
				}
			}, sPlayer.blockPosition());
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(OpenStruggleMenuMessage.class, OpenStruggleMenuMessage::buffer, OpenStruggleMenuMessage::new, OpenStruggleMenuMessage::handler);
	}
}
