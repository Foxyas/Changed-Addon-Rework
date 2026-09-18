package net.foxyas.changedaddon.compatibility.casualties_cubed;

import net.zaharenko424.casualties_cubed.limbs.Limb;

public enum CasualtiesLimb {
    HEAD(Limb.HEAD),
    THORAX(Limb.THORAX),
    ABDOMEN(Limb.ABDOMEN),

    // Arms -> Changed Limb Models
    UPPER_RIGHT_ARM(Limb.UPPER_RIGHT_ARM),
    LOWER_RIGHT_ARM(Limb.LOWER_RIGHT_ARM),
    RIGHT_HAND(Limb.RIGHT_HAND),

    UPPER_LEFT_ARM(Limb.UPPER_LEFT_ARM),
    LOWER_LEFT_ARM(Limb.LOWER_LEFT_ARM),
    LEFT_HAND(Limb.LEFT_HAND),

    // Legs -> Changed Limb Models
    UPPER_RIGHT_LEG(Limb.UPPER_RIGHT_LEG),
    LOWER_RIGHT_LEG(Limb.LOWER_RIGHT_LEG),
    RIGHT_FOOT(Limb.RIGHT_FOOT),

    UPPER_LEFT_LEG(Limb.UPPER_LEFT_LEG),
    LOWER_LEFT_LEG(Limb.LOWER_LEFT_LEG),
    LEFT_FOOT(Limb.LEFT_FOOT);

    private final Limb originalLimb;

    CasualtiesLimb(Limb originalLimb) {
        this.originalLimb = originalLimb;
    }

    public Limb getOriginalLimb() {
        return originalLimb;
    }
}