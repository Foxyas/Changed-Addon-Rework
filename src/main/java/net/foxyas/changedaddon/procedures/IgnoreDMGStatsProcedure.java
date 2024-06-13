package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class IgnoreDMGStatsProcedure {
	@SubscribeEvent
	public static void onEntityAttacked(LivingAttackEvent event) {
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
		double Phase2Math = 0;
		double math = 0;
		double Phase3Math = 0;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form_ket_experiment009")) {
				if ((damagesource).getMsgId().equals(DamageSource.LIGHTNING_BOLT.getMsgId())) {
					if (event != null && event.isCancelable()) {
						event.setCanceled(true);
					}
				}
			}
			if (((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).startsWith("changed_addon:form_experiment_10")) {
				if ((damagesource).getMsgId().equals(DamageSource.WITHER.getMsgId())) {
					if (event != null && event.isCancelable()) {
						event.setCanceled(true);
					}
				}
			}
		}
	}
}
