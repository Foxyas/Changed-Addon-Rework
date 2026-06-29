package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityDataExtensor {

    @Nullable
    static LivingEntityDataExtensor ofEntity(LivingEntity entity) {
        return (entity instanceof LivingEntityDataExtensor livingEntityDataExtensor) ? livingEntityDataExtensor : null;
    }

    default void setSleepCounter(int value) {}

    default AnimationState getCustomAnimationState(int id) {
        return null;
    }

    ///  It tries to override the {@link Player#updateIsUnderwater() updateIsUnderwater} which make the override value need to be other than false
    default boolean overrideSwim() {
        if (this instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof IVariantExtraStats IVariantExtraStats) {
                return IVariantExtraStats.variantOverrideSwim();
            }

            return isEyeOnLavaWithTransfurAndFireResistance(player);
        }

        return false;
    }


    ///  It tries to override the {@link Player#updateSwimming() updateSwim} which make the override value need to be other than false
    default boolean overrideSwimUpdate() {
        if (this instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof IVariantExtraStats IVariantExtraStats) {
                return IVariantExtraStats.variantOverrideSwimUpdate();
            }

            return isEyeOnLavaWithTransfurAndFireResistance(player);
        }

        return false;
    }

    ///  It tries to override the {@link Entity#isInWater() isInWater} which make the override value need to be other than false
    default boolean overrideIsInWater() {
        if (this instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof IVariantExtraStats IVariantExtraStats) {
                return IVariantExtraStats.variantOverrideIsInWater();
            }

            return isOnLavaWithTransfurAndFireResistance(player);
        }

        return false;
    }


    // Utils

    default boolean isEyeOnLavaWithTransfurAndFireResistance(Player player) {
        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariant != null && (player.hasEffect(MobEffects.FIRE_RESISTANCE) && player.isEyeInFluid(FluidTags.LAVA))) {
            boolean aquaticLike = transfurVariant.getParent().is(ChangedAddonTags.TransfurVariants.AQUATIC_LIKE);
            boolean fastSwimSpeed = transfurVariant.getChangedEntity().getAttributeValue(ForgeMod.SWIM_SPEED.get()) > 1;
            boolean aquaticBreath = transfurVariant.getParent().breatheMode.canBreatheWater();
            boolean aquaticAffinity = transfurVariant.getParent().breatheMode.hasAquaAffinity();

            return aquaticLike || fastSwimSpeed || aquaticBreath || aquaticAffinity;
        }

        return false;
    }

    default boolean isOnLavaWithTransfurAndFireResistance(Player player) {
        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (transfurVariant != null && (player.hasEffect(MobEffects.FIRE_RESISTANCE) && player.level().getFluidState(player.blockPosition()).is(FluidTags.LAVA))) {
            boolean aquaticLike = transfurVariant.getParent().is(ChangedAddonTags.TransfurVariants.AQUATIC_LIKE);
            boolean fastSwimSpeed = transfurVariant.getChangedEntity().getAttributeValue(ForgeMod.SWIM_SPEED.get()) > 1;
            boolean aquaticBreath = transfurVariant.getParent().breatheMode.canBreatheWater();
            boolean aquaticAffinity = transfurVariant.getParent().breatheMode.hasAquaAffinity();

            return aquaticLike || fastSwimSpeed || aquaticBreath || aquaticAffinity;
        }

        return false;
    }
}
