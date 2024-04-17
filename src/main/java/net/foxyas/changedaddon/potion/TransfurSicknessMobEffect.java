
package net.foxyas.changedaddon.potion;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.procedures.TransfurSicknessOnEffectActiveTickProcedure;
import net.foxyas.changedaddon.procedures.TransfurSicknessEffectExpiresProcedure;

public class TransfurSicknessMobEffect extends MobEffect {
	public TransfurSicknessMobEffect() {
		super(MobEffectCategory.HARMFUL, -1);
	}

	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.transfur_sickness";
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		TransfurSicknessOnEffectActiveTickProcedure.execute(entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		TransfurSicknessEffectExpiresProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
