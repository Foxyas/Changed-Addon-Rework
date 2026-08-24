package net.foxyas.changedaddon.variant;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor.HugType;
import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface IVariantExtraStats {

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

    default List<FoodDietEntry> getExtraDietTypes() {
        return List.of();
    }

    // Defines if it should play the "splash" sound effect when entering the water or etc.
    default boolean variantOverrideWasUnderwater() {
        return false;
    }

    // Defines if it should allow the player to keep swimming
    default boolean variantOverrideSwimUpdate() {
        return false;
    }

    // Defines if it should allow the player to swim
    default boolean variantOverrideIsInWater() {
        return false;
    }

    default boolean shouldTakeFallDamage() {

        if (this.getFlyType().canFly()) {
            return false;
        }

        return !this.getFlyType().canFly();
    }

    // Gun Mods Compatibility.
    default boolean canFireGuns() {
        return true;
    }

    // RP api method.
    default boolean canUseBows() {
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

    default void onHugTarget(LivingEntity hugTarget, HugType hugType) {

    }

    /**
     * @return How much heat the entity can insulate, this will be added with the armor insulation value
     * */
    default float getHeatInsulationScale() { //Casualties Cubed Mod Compatibility
        return 0;
    }

    /**
     * @return if the heat Insulation of the entity should follow the clamped rules
     * */
    default boolean isHeatInsulationClamped() { //Casualties Cubed Mod Compatibility
        return true;
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
