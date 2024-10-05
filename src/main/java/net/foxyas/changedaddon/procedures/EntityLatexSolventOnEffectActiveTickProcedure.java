package net.foxyas.changedaddon.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.living.LivingEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class EntityLatexSolventOnEffectActiveTickProcedure {
	@SubscribeEvent
	public static void onEntityTick(LivingEvent.LivingUpdateEvent event) {
		execute(event, event.getEntityLiving());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		DamageSource Solvent = null;
		if (entity instanceof LivingEntity _livEnt ? _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) : false) {
			if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:latexentity"))) || entity instanceof net.ltxprogrammer.changed.entity.ChangedEntity) {
				if (entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:latexes")))) {
					Solvent = new DamageSource("latex_solvent");
					entity.hurt(Solvent,
							(float) ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) == 0
									? 0.25
									: ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) + 1) / 4));
				}
			}
		}
	}
}
