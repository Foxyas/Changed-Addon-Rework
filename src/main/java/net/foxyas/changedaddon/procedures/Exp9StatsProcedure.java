package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class Exp9StatsProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingHurtEvent event) {
		Entity entity = event.getEntity();
		if (event != null && entity != null) {
			execute(event, event.getSource(), entity, event.getAmount());
		}
	}

	public static void execute(DamageSource damagesource, Entity entity, double amount) {
		execute(null, damagesource, entity, amount);
	}

	private static void execute(@Nullable Event event, DamageSource damagesource, Entity entity, double amount) {
		if (entity == null)
			return;
		if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form_ket_experiment009")) {
			if ((damagesource).isBypassInvul()) {
				((LivingHurtEvent) event).setAmount((Math.round((amount / 2))));
			} else if ((damagesource).isFire()) {
				((LivingHurtEvent) event).setAmount((Math.round((amount / 2))));
			} else if ((damagesource).isFire() && entity.isOnFire()) {
				((LivingHurtEvent) event).setAmount((Math.round((amount / 2))));
			}
		}
	}
}
