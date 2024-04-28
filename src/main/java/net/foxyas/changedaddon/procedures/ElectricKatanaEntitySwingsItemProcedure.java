package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.init.ChangedParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class ElectricKatanaEntitySwingsItemProcedure {
	public static void execute(Entity asource) {
		float attackRange = 2;
		if (asource == null) {
			return;
		}
		LivingEntity source = (LivingEntity) asource;
		double d0 = (double)(-Mth.sin(source.getYRot() * 0.017453292F)) * attackRange;
		double d1 = (double)Mth.cos(source.getYRot() * 0.017453292F) * attackRange;
		Level var7 = source.level;
		if (var7 instanceof ServerLevel serverLevel) {
			serverLevel.sendParticles(ChangedParticles.TSC_SWEEP_ATTACK, source.getX() + d0, source.getY(0.5), source.getZ() + d1, 0, d0, 0.0, d1, 0.0);
		}

	}
}
