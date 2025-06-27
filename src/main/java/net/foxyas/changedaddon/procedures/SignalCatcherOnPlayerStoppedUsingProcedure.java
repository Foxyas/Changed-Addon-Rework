package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;


public class SignalCatcherOnPlayerStoppedUsingProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		if (itemstack.getOrCreateTag().getBoolean("set") == false) {
			if (entity instanceof Player _player && !_player.level().isClientSide())
				_player.displayClientMessage(new TextComponent("\u00A7o\u00A7bNo Location Found \u00A7l[Not Close Enough]"), false);
		}
	}
}
