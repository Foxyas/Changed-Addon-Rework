package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class WhenHitWithAColoredWolfCrystalFragmentProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		Entity hitted = null;
		Entity attacker = null;
		hitted = entity;
		attacker = sourceentity;
		AddTransfurProgressProcedure.addRed(entity, 5);
	}
}
