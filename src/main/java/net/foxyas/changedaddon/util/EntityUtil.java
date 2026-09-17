package net.foxyas.changedaddon.util;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EntityUtil {

    public static final double CUTENESS_LEVEL_NEEDED = 5;

    // Mixin Friendly Method.
    public static float getCutenessLevelOfEntity(LivingEntity livingEntity) {
        float cutenessLevel = 0;
        if (livingEntity instanceof IVariantExtraStats IVariantExtraStats) {
            cutenessLevel = IVariantExtraStats.getDefaultCutenessLevel();
        }

        AttributeInstance cutenessAttribute = livingEntity.getAttribute(ChangedAddonAttributes.CUTENESS.get());
        cutenessLevel += cutenessAttribute != null ? (float) cutenessAttribute.getValue() : 0f;

        return cutenessLevel;
    }

    public static Vec3 getMouthPosition(LivingEntity livingEntity) {
        return getMouthPosition(livingEntity, 0.05f);
    }

    public static Vec3 getMouthPosition(LivingEntity livingEntity, float neckSize) {
        Vec3 view = livingEntity.getLookAngle();
        Vec3 eyePosition = livingEntity.getEyePosition();
        return eyePosition.subtract(0, 0.25, 0).add(view.scale(neckSize));
        // Just for details.
    }

    public static boolean isCuteEnoughToReceivePatsFromVillagers(AbstractVillager villager, LivingEntity target) {
        return getCutenessLevelOfEntity(target) >= ChangedAddonServerConfiguration.CUTENESS_LEVEL_NEEDED_TO_RECEIVE_PATS_FROM_VILLAGERS.get();
    }

    public static float getHealthRatio(LivingEntity livingEntity) {
        return livingEntity.getHealth() / livingEntity.getMaxHealth();
    }

    public static float getFoodRatio(LivingEntity livingEntity, @Nullable FoodData extra) {
        if (livingEntity instanceof Player player && extra == null) {
            extra = player.getFoodData();
        }
        if (extra == null) {
            return 0;
        }
        return (float) extra.getFoodLevel() / 20;
    }

    public static Optional<Float> getAttributeValueSafe(LivingEntity livingEntity, Attribute attribute) {
        Optional<Float> attributeValue;
        try {
            attributeValue = Optional.of((float) livingEntity.getAttributeValue(attribute));
        } catch (Exception e) {
            attributeValue = Optional.empty();
        }
        return attributeValue;
    }

    public static Optional<Float> getAttributeBaseValueSafe(LivingEntity livingEntity, Attribute attribute) {
        Optional<Float> attributeValue;
        try {
            attributeValue = Optional.of((float) livingEntity.getAttributeBaseValue(attribute));
        } catch (Exception e) {
            attributeValue = Optional.empty();
        }
        return attributeValue;
    }
}
