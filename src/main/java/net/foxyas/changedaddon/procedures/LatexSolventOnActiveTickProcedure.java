package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModParticleTypes;

public class LatexSolventOnActiveTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == true
				&& (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur == false) {
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 1) {
				if (world instanceof ServerLevel _level)
					_level.sendParticles((SimpleParticleType) (ChangedAddonModParticleTypes.SOLVENT_PARTICLE.get()), x, (y + 1), z, 10, 0.2, 0.3, 0.2, 0.1);
			}
		}
	}
}
