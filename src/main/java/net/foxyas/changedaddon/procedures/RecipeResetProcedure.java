package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class RecipeResetProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			double _setval = 1;
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.CatlyzerRecipePage = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
		{
			double _setval = 1;
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.UnifuserRecipePage = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
		if (entity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent("Recipes Reseted (Page 1)"), true);
	}
}
