
package net.foxyas.changedaddon.network;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FoxyasGui2ButtonMessage {
	private final int buttonID, x, y, z;

	public FoxyasGui2ButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public FoxyasGui2ButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(FoxyasGui2ButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(FoxyasGui2ButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

		if (buttonID == 0) {

			player.getPersistentData().putBoolean("Deal", true);
		}
		if (buttonID == 1) {

			player.getPersistentData().putBoolean("Deal", false);
		}
		if (buttonID == 2) {
			player.getPersistentData().putBoolean("Deal", false);
			PlayerUtil.TransfurPlayer(player, "changed:form_white_latex_wolf/male");
			player.closeContainer();

			if (!(player instanceof ServerPlayer sPlayer)) return;

			Advancement adv = sPlayer.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:friendly_transfur"));
			if(adv == null) return;

			AdvancementProgress advProgress = sPlayer.getAdvancements().getOrStartProgress(adv);
			if(advProgress.isDone()) return;

            for (String s : advProgress.getRemainingCriteria()) {
                sPlayer.getAdvancements().award(adv, s);
            }
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		ChangedAddonMod.addNetworkMessage(FoxyasGui2ButtonMessage.class, FoxyasGui2ButtonMessage::buffer, FoxyasGui2ButtonMessage::new, FoxyasGui2ButtonMessage::handler);
	}
}
