package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModAttributes;

import java.util.UUID;

public class LatexContaminationEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double LatexContamination_level = 0;
		AttributeModifier LatexContamination = null;
		if (entity instanceof LivingEntity && ((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()) != null) {
			LatexContamination_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) == 0
					? 0.1
					: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) * 0.1 + 0.1;
			LatexContamination = new AttributeModifier(UUID.fromString("0-0-0-0-1"), "Latex Contamination Effect Attribute Change", LatexContamination_level, AttributeModifier.Operation.ADDITION);
			((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()).removeModifier(LatexContamination);
		}
	}
}
