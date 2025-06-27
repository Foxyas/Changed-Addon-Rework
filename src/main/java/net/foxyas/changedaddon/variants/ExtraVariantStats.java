package net.foxyas.changedaddon.variants;

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
}
