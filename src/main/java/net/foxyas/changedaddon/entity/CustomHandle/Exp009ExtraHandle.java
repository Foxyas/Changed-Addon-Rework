package net.foxyas.changedaddon.entity.CustomHandle;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityMountEvent;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class Exp009ExtraHandle {
	@SubscribeEvent
	public static void onEntityMount(EntityMountEvent event) {
		Entity entity = event.getEntityBeingMounted();
		Entity mount = event.getEntityMounting();
		if ((mount instanceof Experiment009Entity || mount instanceof Experiment009phase2Entity) && entity instanceof Boat) {
			event.setCanceled(true); // Cancel Mount
		}
	}
}
