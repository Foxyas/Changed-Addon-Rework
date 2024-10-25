package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class TransfurSicknessOnEffectActiveTickProcedure {
	public static void execute(Entity entity, double amplifier) {
		if (entity == null)
			return;
		double levelPotion = 0;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if (GetDefault.execute((Player) entity)) {
				setPlayerTransfurMode.execute((Player) entity, 3);
			}
			levelPotion = amplifier;
			TransfurSicknessHandleProcedure.addAttributeMod(entity, levelPotion);
		}
	}
}
