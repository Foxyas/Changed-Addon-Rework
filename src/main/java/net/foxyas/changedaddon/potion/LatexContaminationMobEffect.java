
package net.foxyas.changedaddon.potion;

import net.foxyas.changedaddon.init.ChangedAddonModAttributes;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.foxyas.changedaddon.procedures.LatexContaminationEffectStartedappliedProcedure;
import net.foxyas.changedaddon.procedures.LatexContaminationEffectExpiresProcedure;
import net.foxyas.changedaddon.procedures.AddTransfurProgressProcedure;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class LatexContaminationMobEffect extends MobEffect {
	public LatexContaminationMobEffect() {
		super(MobEffectCategory.HARMFUL, Color3.getColor("#ffffff").toInt());
		addAttributeModifier(ChangedAddonModAttributes.LATEXINFECTION.get(),"2971dbcb-1aba-4ae4-8726-3025cc7c2dd7",0.1, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public String getDescriptionId() {
		return "effect.changed_addon.latex_contamination";
	}

	@Override
	public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.addAttributeModifiers(entity, attributeMap, amplifier);
		float Math = (amplifier + 1 * 0.1F) / 2;
		AddTransfurProgressProcedure.setAdd(entity, Math);
		//LatexContaminationEffectStartedappliedProcedure.execute(entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		//LatexContaminationEffectExpiresProcedure.execute(entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
