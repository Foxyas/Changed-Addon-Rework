package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.registers.ChangedAddonDamageSources;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LatexSolventOnEffectActiveTickProcedure {

	@SubscribeEvent
	public static void onPlayerTick(LivingEvent.LivingUpdateEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ChangedEntity changedEntity && changedEntity.getType().is(ChangedTags.EntityTypes.LATEX)) {
			MobEffectInstance SolventEffectInstace = changedEntity.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get());
			if (SolventEffectInstace != null){
				changedEntity.hurt(ChangedAddonDamageSources.SOLVENT, SolventEffectInstace.getAmplifier() * 2);
			}
		}
	}
}
