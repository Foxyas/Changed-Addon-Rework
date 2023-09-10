package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class ActiveFriendlyModeOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("")) {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Friendly_mode == false) {
				{
					boolean _setval = true;
					entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.Friendly_mode = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlymode.on").getString())), true);
				if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed:special")) {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).assmilation == true) {
						if (entity instanceof Player _player && !_player.level.isClientSide())
							_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlymode.when_on").getString())), false);
						{
							boolean _setval = false;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.assmilation = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.can_grab = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					}
				} else {
					if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).assmilation == true) {
						{
							boolean _setval = false;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.can_grab = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					} else {
						{
							boolean _setval = true;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.assmilation = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						{
							boolean _setval = false;
							entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.can_grab = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					}
				}
			} else {
				{
					boolean _setval = false;
					entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.Friendly_mode = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.friendlymode.off").getString())), true);
			}
		} else {
			if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).showwarns == true) {
				if (entity instanceof Player _player && !_player.level.isClientSide())
					_player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.when_not.transfur").getString())), true);
			}
		}
	}
}
