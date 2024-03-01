package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.entity.PuroKindFemaleEntity;
import net.foxyas.changedaddon.entity.PuroKindEntity;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TamePuroKindProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getPlayer().getUsedItemHand())
			return;
		execute(event, event.getTarget(), event.getPlayer());
	}

	public static void execute(Entity entity, Entity sourceentity) {
		execute(null, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		if (entity instanceof AbstractDarkLatexWolf) {
		AbstractDarkLatexWolf Wolf = (AbstractDarkLatexWolf) entity;
		String uuid = "";
		if (entity instanceof PuroKindEntity || entity instanceof PuroKindFemaleEntity) {
			if (!Wolf.isTame()) {
				if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:orange"))) {
					if (sourceentity instanceof Player _owner)
						Wolf.tame(_owner);
				} else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:orange"))) {
					if (sourceentity instanceof Player _owner)
						Wolf.tame(_owner);
					}
				}
			}
		}
	}
}
