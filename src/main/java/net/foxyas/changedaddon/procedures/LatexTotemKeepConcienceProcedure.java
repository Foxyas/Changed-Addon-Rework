package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.item.TransfurTotemItem;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class LatexTotemKeepConcienceProcedure {
	@SubscribeEvent
	public static void execute(ProcessTransfur.KeepConsciousEvent event) {
		if (event.player == null)
			return;
		if (event.player.getInventory().contains(new ItemStack(ChangedAddonModItems.TRANSFUR_TOTEM.get()))) {
			if (!event.keepConscious) {
				event.shouldKeepConscious = true;
				if (event.player instanceof ServerPlayer serverPlayer){
					TranslatableComponent text = new TranslatableComponent("changed_addon.latex_totem.tittle.text_1");
					TranslatableComponent text2 = new TranslatableComponent("changed_addon.latex_totem.tittle.text_2");
					//serverPlayer.sendMessage(text, ChatType.CHAT,serverPlayer.getUUID());
					serverPlayer.displayClientMessage(text, true);
					serverPlayer.sendMessage(text, ChatType.CHAT,serverPlayer.getUUID());
					serverPlayer.sendMessage(text2, ChatType.CHAT,serverPlayer.getUUID());
				}
			}
		}
	}
}