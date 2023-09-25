package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class IfInWaterProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity.isInWater()) {
			return true;
		}
		return false;
	}
}
