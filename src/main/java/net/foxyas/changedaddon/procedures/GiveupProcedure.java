package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class GiveupProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.fight_concience.give_up").getString())), true);
		{
			boolean _setval = true;
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.consciousness_fight_give_up = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
		if (entity instanceof Player _player)
			_player.closeContainer();
	}
}
