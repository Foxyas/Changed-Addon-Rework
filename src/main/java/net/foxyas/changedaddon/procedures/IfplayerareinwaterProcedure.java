package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class IfplayerareinwaterProcedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null))) {
			if (entity.isInWater() && (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).isInWater()) {
				if (entity.getY() > (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()) {
					return false;
				}
			}
		}
		return true;
	}
}
