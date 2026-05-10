package net.foxyas.changedaddon.util;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;

public class EntityUtils {

    public static final int CUTENESS_LEVEL_NEEDED = 5;

    // Mixin Friendly Method.
    public static int getCutenessLevelOfEntity(LivingEntity livingEntity) {
        int cutenessLevel = 0;
        if (livingEntity instanceof VariantExtraStats variantExtraStats) {
            cutenessLevel = variantExtraStats.cutenessLevel();
        } else {
            MobEffectInstance effect = livingEntity.getEffect(ChangedAddonMobEffects.PACIFIED.get());
            if (effect != null) cutenessLevel = effect.getAmplifier();
        }

        return cutenessLevel;
    }

    public static boolean isCuteEnoughToReceivePatsFromVillagers(AbstractVillager villager, LivingEntity target) {
        return getCutenessLevelOfEntity(target) >= CUTENESS_LEVEL_NEEDED;
    }
}
