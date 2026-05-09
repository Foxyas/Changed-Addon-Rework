package net.foxyas.changedaddon.variant;

import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.procedure.CreatureDietsHandleProcedure.DietType;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public interface VariantExtraStats {

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

    default int cuteLevel() {
        return 0;
    }
}
