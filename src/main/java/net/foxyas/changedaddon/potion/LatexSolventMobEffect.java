
package net.foxyas.changedaddon.potion;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class LatexSolventMobEffect extends MobEffect {
	public LatexSolventMobEffect() {
		super(MobEffectCategory.NEUTRAL, Color3.getColor("#ffffff").toInt());
		addAttributeModifier(ChangedAddonAttributes.LATEXRESISTANCE.get(),"a0ca1f84-b8cc-43da-baea-0e6a6af71a7a",0.1, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.latex_solvent";
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
