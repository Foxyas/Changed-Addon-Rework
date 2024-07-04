package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

public class GrabEscapeNeedClicksProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double CHP = 0;
		double MHP = 0;
		CHP = entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1;
		MHP = entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1;
		{
			double _setval = Math.round(30 / (CHP / MHP));
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.GrabEscapeClick = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
