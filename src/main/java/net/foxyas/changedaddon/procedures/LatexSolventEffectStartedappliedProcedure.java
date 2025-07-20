package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonModMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

public class LatexSolventEffectStartedappliedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        double LatexSolvent_level = 0;
        AttributeModifier LatexSolvent = null;
        if (entity instanceof LivingEntity && ((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get()) != null) {
            LatexSolvent_level = (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) == 0
                    ? 0.1
                    : (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()) ? _livEnt.getEffect(ChangedAddonModMobEffects.LATEX_SOLVENT.get()).getAmplifier() : 0) * 0.1 + 0.1;
            LatexSolvent = new AttributeModifier(UUID.fromString("f9ff3894-a234-4994-ac8f-d84f45d1827c"), "Solvent Effect Attribute Change", LatexSolvent_level, AttributeModifier.Operation.ADDITION);
            if (!(((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get()).hasModifier(LatexSolvent)))
                ((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get()).addTransientModifier(LatexSolvent);
        }
    }
}
