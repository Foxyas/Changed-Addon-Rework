package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class TransfurSicknessEffectExpiresProcedure {
	public static void execute(Entity entity, double amplifier) {
		if (entity == null)
			return;
		double level = 0;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if (GetDefault.execute((Player) entity)) {
				setPlayerTransfurMode.execute((Player) entity, 1);
			}
			level = amplifier;
			TransfurSicknessHandleProcedure.removeAttributeMod(entity, level);
		}
	}
}
