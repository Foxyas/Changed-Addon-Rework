package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class IfplayerHaveDealFalseProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity.getPersistentData().getBoolean("Deal") == false) {
			return true;
		}
		return false;
	}
}
