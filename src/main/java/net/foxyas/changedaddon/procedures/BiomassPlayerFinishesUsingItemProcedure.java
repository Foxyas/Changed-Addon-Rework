package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;


public class BiomassPlayerFinishesUsingItemProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player)
			_player.causeFoodExhaustion((float) (4 * 4));
		if (entity instanceof Player _player && !_player.level().isClientSide())
			_player.displayClientMessage(new TextComponent((Component.translatable("item.changed_addon.biomass.eat").getString())), true);
	}
}
