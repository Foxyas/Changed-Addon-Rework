package net.foxyas.changedaddon.mobEffects;

import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LatexStrengthMobEffect extends MobEffect {

    public LatexStrengthMobEffect() {
        super(MobEffectCategory.BENEFICIAL, new Color(255, 255, 255).getRGB());
        addAttributeModifier(ChangedAttributes.TRANSFUR_DAMAGE.get(), "df5dbc08-20a8-45f7-915a-18914cfec511", 0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int pAmplifier, @NotNull AttributeModifier pModifier) {
        return 3 * (double) (pAmplifier + 1);
    }
}
