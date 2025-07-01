package net.foxyas.changedaddon.variants;

import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.jetbrains.annotations.Nullable;

public interface ExtraVariantStats {

    // Variable Set By Entity
    float extraBlockBreakSpeed();

    // Multiplier Based on % amount [Vanilla Attribute Style]
    default float getBlockBreakSpeedMultiplier(){
        return this.extraBlockBreakSpeed() + 1;
    }

    default FlyType getFlyType() {
        return FlyType.BOTH;
    }

    enum FlyType {
        ONLY_FALL,
        ONLY_FLY,
        BOTH;

        public boolean canGlide() {
            return this == ONLY_FALL || this == BOTH;
        }
    }


    static boolean PlayerHasTransfurWithExtraColors(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
        return transfur != null && transfur.is(ChangedAddonTransfurVariants.AVALI);
    }
}
