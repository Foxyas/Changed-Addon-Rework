package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

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
			execute(event, event.getSource(), entity);
		}
	}

	public static void execute(DamageSource damagesource, Entity entity) {
		execute(null, damagesource, entity);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof Experiment009phase2Entity || entity instanceof Experiment009Entity) {
			if ((damagesource) == DamageSource.IN_WALL) {
				entity.getPersistentData().putDouble("BossTp", (1 + entity.getPersistentData().getDouble("BossTp")));
			} else if ((damagesource) == DamageSource.LIGHTNING_BOLT) {
				if (event != null && event.isCancelable()) {
					event.setCanceled(true);
				}
			}
		}
	}
}
