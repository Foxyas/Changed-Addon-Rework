package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

public class Experiment009OnEntityTickUpdateProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity.isInWater()) {
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null))) {
				if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).isInWater())) {
					if ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() >= entity.getY()) {
						if (entity instanceof Mob _entity)
							_entity.getNavigation().moveTo(((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX()), ((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY()),
									((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ()), 1);
					}
				}
			}
		}
	}
}
