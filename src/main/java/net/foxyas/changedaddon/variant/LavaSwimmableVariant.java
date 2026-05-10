package net.foxyas.changedaddon.variant;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;

public interface LavaSwimmableVariant extends VariantExtraStats {

    @Override
    default boolean variantOverrideSwim() {
        if (this instanceof ChangedEntity changedEntity) {
            if (changedEntity.maybeGetUnderlying() instanceof Player player) {
                TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
                return transfurVariant != null && player.isEyeInFluid(FluidTags.LAVA);
            }
        }

        return false;
    }

    @Override
    default boolean variantOverrideSwimUpdate() {
        if (this instanceof ChangedEntity changedEntity) {
            if (changedEntity.maybeGetUnderlying() instanceof Player player) {
                TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
                return transfurVariant != null && player.isEyeInFluid(FluidTags.LAVA);
            }
        }

        return false;
    }

    @Override
    default boolean variantOverrideIsInWater() {
        if (this instanceof ChangedEntity changedEntity) {
            if (changedEntity.maybeGetUnderlying() instanceof Player player) {
                TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
                return transfurVariant != null && player.level().getFluidState(player.blockPosition()).is(FluidTags.LAVA);
            }
        }

        return false;
    }
}
