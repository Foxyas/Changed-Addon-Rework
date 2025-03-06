package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;

public class Experiment009phase2EntityDiesProcedure {
	public static void execute(Entity sourceentity) {
		if (sourceentity == null)
			return;
		ItemStack disc_1 = ItemStack.EMPTY;
		ItemStack disc_2 = ItemStack.EMPTY;
		if (sourceentity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent((new TranslatableComponent("changed_addon.entity_dialogues.exp9.dead").getString())), false);
	}
}
