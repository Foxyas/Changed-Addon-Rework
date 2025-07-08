package net.foxyas.changedaddon.variants;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ExtraVariantStats {

    // Variable Set By Entity
    float extraBlockBreakSpeed();

    // Multiplier Based on % amount [Vanilla Attribute Style]
    default float getBlockBreakSpeedMultiplier() {
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
}
