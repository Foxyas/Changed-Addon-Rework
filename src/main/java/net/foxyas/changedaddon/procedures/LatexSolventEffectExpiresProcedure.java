package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModAttributes;

import java.util.UUID;

public class LatexSolventEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		AttributeModifier LatexSolventAttribute = null;
		double LatexSolvent_level = 0;
		if (entity instanceof LivingEntity && ((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXRESISTANCE.get()) != null) {
			LatexSolvent_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) == 0
					? 0.1
					: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) * 0.1 + 0.1;
			LatexSolventAttribute = new AttributeModifier(UUID.fromString("f9ff3894-a234-4994-ac8f-d84f45d1827c"), "Solvent Effect Attribute Change", LatexSolvent_level, AttributeModifier.Operation.ADDITION);
			((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXRESISTANCE.get()).removeModifier(LatexSolventAttribute);
		}
	}
}
