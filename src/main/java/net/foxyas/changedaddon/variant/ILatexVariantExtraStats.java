package net.foxyas.changedaddon.variant;

import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.procedure.CreatureDietsHandleProcedure.DietType;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface ILatexVariantExtraStats {

    float defaultPlayerFlySpeed = AttributesHandle.DefaultPlayerFlySpeed;

    // Variable Set By Entity
    default float extraBlockBreakSpeed() {
        return 0;
    }

    // Multiplier Based on % amount [Vanilla Attribute Style]
    default float getBlockBreakSpeedMultiplier() {
        return this.extraBlockBreakSpeed() + 1;
    }

    default FlyType getFlyType() {
        if (this instanceof ChangedEntity changedEntity) {
            if (changedEntity.getSelfVariant() != null) {
                var variant = changedEntity.getSelfVariant();
                return variant.canGlide ? FlyType.BOTH : FlyType.NONE;
            }
        }

        return FlyType.NONE;
    }

    default void readExtraData(CompoundTag tag) {
    }

    default void saveExtraData(CompoundTag tag) {
    }

    default List<DietType> getExtraDietTypes() {
        return List.of();
    }

    default boolean variantOverrideSwim() {
        return false;
    }

    default boolean variantOverrideSwimUpdate() {
        return false;
    }

    default boolean variantOverrideIsInWater() {
        return false;
    }

    default boolean shouldTakeFallDamage() {

        if (this.getFlyType().canFly()) {
            return false;
        }

        return !this.getFlyType().canFly();
    }

    // Gun Mods Compatibility
    default boolean canFireGuns() {
        return true;
    }

    default int cutenessLevel() {
        if (this instanceof LivingEntity livingEntity) {
            MobEffectInstance effect = livingEntity.getEffect(ChangedAddonMobEffects.PACIFIED.get());
            if (effect != null) {
                return effect.getAmplifier();
            }
        }
        return 0;
    }

    enum FlyType {
        NONE,
        ONLY_FALL,
        ONLY_FLY,
        BOTH;

        FlyType() {
        }

        public boolean canGlide() {
            return this == ONLY_FALL || this == BOTH;
        }

        public boolean canFly() {
            return this == ONLY_FLY || this == BOTH;
        }
    }
}
