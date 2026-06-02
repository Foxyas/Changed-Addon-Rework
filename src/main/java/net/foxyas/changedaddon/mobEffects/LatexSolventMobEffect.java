package net.foxyas.changedaddon.mobEffects;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class LatexSolventMobEffect extends MobEffect {

    public LatexSolventMobEffect() {
        super(MobEffectCategory.NEUTRAL, -1);
        addAttributeModifier(ChangedAddonAttributes.LATEX_RESISTANCE.get(), "a0ca1f84-b8cc-43da-baea-0e6a6af71a7a", 0.1, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.changed_addon.latex_solvent";
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (ProcessTransfur.getEntityVariant(entity).map(var -> var.getEntityType().is(ChangedTags.EntityTypes.LATEX)).orElse(false)) {
            entity.hurt(ChangedAddonDamageSources.LATEX_SOLVENT.source(entity.level()), amplifier + 2);
        }
    }
}
