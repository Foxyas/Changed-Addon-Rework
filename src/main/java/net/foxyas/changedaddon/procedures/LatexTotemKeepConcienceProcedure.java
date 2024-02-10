package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LatexTotemKeepConcienceProcedure {
	@SubscribeEvent
	public static void execute(ProcessTransfur.KeepConsciousEvent event) {
		if (event.player == null)
			return;
			new ProcessTransfur.KeepConsciousEvent(event.player, true);
	}
}
