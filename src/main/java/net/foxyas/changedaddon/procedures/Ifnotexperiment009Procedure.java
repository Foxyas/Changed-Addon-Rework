package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;

public class Ifnotexperiment009Procedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if (!(entity instanceof Experiment009Entity) || !(entity instanceof Experiment009phase2Entity)) {
			return true;
		}
		return false;
	}
}
