
package net.foxyas.changedaddon.potion;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class LatexExposureMobEffect extends MobEffect {
	public LatexExposureMobEffect() {
		super(MobEffectCategory.HARMFUL, -1118482);
		addAttributeModifier(ChangedAttributes.TRANSFUR_TOLERANCE.get(),"3a4a0a56-72e9-438e-b0d3-8e4b02b2f7ae",-2, AttributeModifier.Operation.ADDITION);
	}


	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.latex_exposure";
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.addAttributeModifiers(entity,attributeMap,amplifier);
		//LatexSolventEffectStartedappliedProcedure.execute(entity);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		super.applyEffectTick(entity, amplifier);
		//LatexSolventOnActiveTickProcedure.execute(entity.level, entity.getX(), entity.getY(), entity.getZ(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		//LatexSolventEffectExpiresProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
