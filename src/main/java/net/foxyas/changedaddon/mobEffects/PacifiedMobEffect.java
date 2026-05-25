package net.foxyas.changedaddon.mobEffects;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

public class PacifiedMobEffect extends MobEffect {

    public PacifiedMobEffect() {
        super(MobEffectCategory.NEUTRAL, -1);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public void addAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        if (pLivingEntity instanceof ChangedEntity changedEntity) {
            if (changedEntity.getType().is(ChangedAddonTags.EntityTypes.PACIFY_HANDLE_IMMUNE)) return;
            changedEntity.setTarget(null);
        }
    }

    @Override
    public @NotNull String getDescriptionId() {
        return "effect.changed_addon.pacified";
    }
}
