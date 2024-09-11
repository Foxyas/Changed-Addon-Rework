package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class TurnOffTransfurOnKeyPressedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if (GetDefault.execute((Player) entity)) {
				if (ReturnTransfurModeProcedure.execute(entity) != 3) {
					setPlayerTransfurMode.execute((Player) entity, 3);
				} else {
					setPlayerTransfurMode.execute((Player) entity, (int) GetDefault.GetDefaultValue(entity));
				}
			}
		}
	}
}
