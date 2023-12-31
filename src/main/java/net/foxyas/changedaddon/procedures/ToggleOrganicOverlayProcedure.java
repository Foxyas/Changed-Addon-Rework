package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.commands.CommandSourceStack;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.BoolArgumentType;

public class ToggleOrganicOverlayProcedure {
	public static void execute(CommandContext<CommandSourceStack> arguments, Entity entity) {
		if (entity == null)
			return;
		if (BoolArgumentType.getBool(arguments, "turn") == true) {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_overlay == true) {
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent("\u00A7cNothing changed, it already had that value"), false);
			} else {
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent("You Enabled the Organic Transfur Overlay of Changed Addon"), false);
			}
		} else {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_overlay == false) {
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent("\u00A7cNothing changed, it already had that value"), false);
			} else {
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent("You Disabled the Organic Transfur Overlay of Changed Addon"), false);
			}
		}
		{
			boolean _setval = BoolArgumentType.getBool(arguments, "turn");
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.organic_overlay = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
