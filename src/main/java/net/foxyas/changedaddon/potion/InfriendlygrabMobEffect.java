
package net.foxyas.changedaddon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.procedures.InfriendlygrabOnEffectActiveTickProcedure;

public class InfriendlygrabMobEffect extends MobEffect {
	public InfriendlygrabMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.infriendlygrab";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		InfriendlygrabOnEffectActiveTickProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
