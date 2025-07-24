
package net.foxyas.changedaddon.network;

import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record FoxyasGui2ButtonMessage(int buttonId, int x, int y, int z) {

	public FoxyasGui2ButtonMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(FoxyasGui2ButtonMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.buttonId);
		buf.writeVarInt(message.x);
		buf.writeVarInt(message.y);
		buf.writeVarInt(message.z);
	}

	public static void handler(FoxyasGui2ButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

		if (buttonID == 0) {

			player.getPersistentData().putBoolean("Deal", true);
		}
		if (buttonID == 1) {

			player.getPersistentData().putBoolean("Deal", false);
		}
		if (buttonID == 2) {
			player.getPersistentData().putBoolean("Deal", false);
			ProcessTransfur.transfur(player, player.getLevel(), ChangedTransfurVariants.WHITE_LATEX_WOLF_MALE.get(), true, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
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
}
