
package net.foxyas.changedaddon.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record FightToKeepConsciousnessMinigameButtonMessage(int buttonID, int x, int y, int z) {

	public FightToKeepConsciousnessMinigameButtonMessage(FriendlyByteBuf buf) {
		this(buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
	}

	public static void buffer(FightToKeepConsciousnessMinigameButtonMessage message, FriendlyByteBuf buf) {
		buf.writeVarInt(message.buttonID);
		buf.writeVarInt(message.x);
		buf.writeVarInt(message.y);
		buf.writeVarInt(message.z);
	}

	public static void handler(FightToKeepConsciousnessMinigameButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

		ChangedAddonModVariables.PlayerVariables vars = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
		if(vars == null) return;

		if (buttonID == 0) {
			vars.consciousness_fight_progress++;
			vars.syncPlayerVariables(player);

			if(vars.consciousness_fight_progress >= 25) player.closeContainer();
		}

		if (buttonID == 1) {
			if(!player.level.isClientSide) player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.fight_concience.give_up").getString())), true);

			vars.consciousness_fight_give_up = true;
			vars.syncPlayerVariables(player);

			player.closeContainer();
		}
	}
}
