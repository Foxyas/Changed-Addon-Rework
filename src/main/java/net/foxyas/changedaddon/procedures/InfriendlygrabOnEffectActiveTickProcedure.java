package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

public class InfriendlygrabOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.INFRIENDLYGRAB.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.INFRIENDLYGRAB.get()).getDuration() : 0) == 1) {
			{
				String _setval = "";
				entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.FriendlyGrabbing = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
