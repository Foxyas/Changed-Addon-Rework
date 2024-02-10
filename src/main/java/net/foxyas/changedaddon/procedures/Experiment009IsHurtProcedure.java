package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;

import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Experiment009IsHurtProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		if (event != null && entity != null) {
			execute(event, event.getSource(), entity, event.getSource().getDirectEntity());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity, Entity immediatesourceentity) {
		execute(null, damagesource, entity, immediatesourceentity);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, Entity immediatesourceentity) {
		if (entity == null || immediatesourceentity == null)
			return;
		if (entity instanceof Experiment009phase2Entity || entity instanceof Experiment009Entity) {
			if (immediatesourceentity instanceof ThrowableProjectile) {
				if (event != null && event.isCancelable()) {
					event.setCanceled(true);
				}
			}
			if ((damagesource) == DamageSource.IN_WALL) {
				entity.getPersistentData().putDouble("BossTp", (1 + entity.getPersistentData().getDouble("BossTp")));
			}
		}
	}
}
