package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.item.TransfurTotemItem;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LatexTotemKeepConcienceProcedure {
	@SubscribeEvent
	public static void execute(ProcessTransfur.KeepConsciousEvent event) {
		if (event.player == null)
			return;
		if (event.player.getInventory().contains(new ItemStack(ChangedAddonModItems.TRANSFUR_TOTEM.get()))) {
			if (!event.keepConscious) {
				event.shouldKeepConscious = true;
				if (!event.player.level.isClientSide())
				event.player.displayClientMessage(new TextComponent("§o§n§l§3§nI Won't Let You Lose Your Consciousness"), true);
				
			}
		}
	}
}