
package net.foxyas.changedaddon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.procedures.InfriendlygrabeffectOnEffectActiveTickProcedure;

public class InfriendlygrabeffectMobEffect extends MobEffect {
	public InfriendlygrabeffectMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.infriendlygrabeffect";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		InfriendlygrabeffectOnEffectActiveTickProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
