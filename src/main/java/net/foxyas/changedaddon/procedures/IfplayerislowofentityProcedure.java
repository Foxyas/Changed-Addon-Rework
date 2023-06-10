package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class IfplayerislowofentityProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (entity.isInWater()) {
			if (entity.getY() > (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()) {
				return true;
			}
		}
		return false;
	}
}
