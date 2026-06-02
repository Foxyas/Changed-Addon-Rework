package net.foxyas.changedaddon.mobEffects;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class LatexToleranceBoostMobEffect extends MobEffect {

    public LatexToleranceBoostMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -1118482);
        addAttributeModifier(ChangedAttributes.TRANSFUR_TOLERANCE.get(), "3a4a0a56-72e9-438e-b0d3-8e4b02b2f7ae", 2, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.changed_addon.latex_tolerance_boost";
    }
}
