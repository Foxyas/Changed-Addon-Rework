package net.foxyas.changedaddon.variants;

public interface ExtraVariantStats {

    // Variable Set By Entity
    float BlockBreakSpeed();

    // Multiplier Based on % amount [Vanilla Attribute Style]
    default float getBlockBreakSpeedMultiplier(){
        return this.BlockBreakSpeed() + 1;
    }
}
