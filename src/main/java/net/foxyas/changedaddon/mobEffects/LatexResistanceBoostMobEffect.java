package net.foxyas.changedaddon.mobEffects;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class LatexResistanceBoostMobEffect extends MobEffect {

    public LatexResistanceBoostMobEffect() {
        super(MobEffectCategory.HARMFUL, -1118482);
        addAttributeModifier(ChangedAttributes.TRANSFUR_TOLERANCE.get(), "005571aa-8d1e-4b3d-960a-05ff67f608ca", 2, AttributeModifier.Operation.ADDITION);
    }
}
