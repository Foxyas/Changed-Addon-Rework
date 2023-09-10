package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;

import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonModAttributes;

import java.util.UUID;

public class LatexContaminationEffectStartedappliedProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double LatexSolvent_level = 0;
		double LatexContamination_level = 0;
		AttributeModifier LatexContamination = null;
		if (entity instanceof LivingEntity && ((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()) != null) {
			LatexContamination_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) == 0
					? 0.1
					: (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_CONTAMINATION.get()).getAmplifier() : 0) * 0.1 + 0.1;
			LatexContamination = new AttributeModifier(UUID.fromString("0-0-0-0-1"), "Latex Contamination Effect Attribute Change", LatexContamination_level, AttributeModifier.Operation.ADDITION);
			if (!(((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()).hasModifier(LatexContamination)))
				((LivingEntity) entity).getAttribute(ChangedAddonModAttributes.LATEXINFECTION.get()).addTransientModifier(LatexContamination);
			float LatexC = (float) LatexContamination_level;
			float Math = 0.5f * LatexC;
			float PlayerTransfurProgress = new Object() {
				public float getValue() {
					CompoundTag dataIndex0 = new CompoundTag();
					entity.saveWithoutId(dataIndex0);
					return dataIndex0.getFloat("TransfurProgress");
				}
			}.getValue();
			CompoundTag dataIndex1 = new CompoundTag();
			entity.saveWithoutId(dataIndex1);
			dataIndex1.putFloat("TransfurProgress", PlayerTransfurProgress + Math);
			entity.load(dataIndex1);
		}
	}
}
