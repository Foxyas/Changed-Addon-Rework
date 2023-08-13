
package net.foxyas.changedaddon.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.procedures.UntransfurOnEffectActiveTickProcedure;

public class UntransfurMobEffect extends MobEffect {
	public UntransfurMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.untransfur";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		UntransfurOnEffectActiveTickProcedure.execute(entity.level, entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
