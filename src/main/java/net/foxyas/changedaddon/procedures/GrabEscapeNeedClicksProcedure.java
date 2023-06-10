package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GrabEscapeNeedClicksProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		double CHP = 0;
		double MHP = 0;
		CHP = Math.ceil(entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1);
		MHP = entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1;
		{
			double _setval = 30 / (CHP / MHP);
			entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
				capability.GrabEscapeClick = _setval;
				capability.syncPlayerVariables(entity);
			});
		}
	}
}
