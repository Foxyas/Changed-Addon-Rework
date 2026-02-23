package net.foxyas.changedaddon.variant;

import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.event.TransfurVariantEvents;
import net.foxyas.changedaddon.procedure.CreatureDietsHandleProcedure.DietType;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public interface VariantExtraStats {

    // Variable Set By Entity
    default float extraBlockBreakSpeed() {
        return 0;
    }

    // Multiplier Based on % amount [Vanilla Attribute Style]
    default float getBlockBreakSpeedMultiplier() {
        return this.extraBlockBreakSpeed() + 1;
    }

    float defaultPlayerFlySpeed = AttributesHandle.DefaultPlayerFlySpeed;

    default float getFlySpeed() {
        return 0;
    }

    default TransfurVariant<?> getTransfurVariantFor(TransfurVariantEvents.OverrideSourceTransfurVariantEvent.TransfurType transfurType) {
        if (this instanceof ChangedEntity changedEntity) {
            return changedEntity.getTransfurVariant();
        }

        return null;
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

    default boolean variantOverrideSwim() {
        return false;
    }

    default boolean variantOverrideSwimUpdate() {
        return false;
    }

    default boolean variantOverrideIsInWater() {
        return false;
    }
}
