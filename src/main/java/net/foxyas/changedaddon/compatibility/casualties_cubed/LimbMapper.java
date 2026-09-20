package net.foxyas.changedaddon.compatibility.casualties_cubed;

import java.util.EnumMap;
import java.util.Map;

import net.foxyas.changedaddon.client.model.animations.ChangedAddonLimbExtensions;
import net.ltxprogrammer.changed.client.animations.ModelPartIdentifier;
import net.minecraft.Util;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import org.jetbrains.annotations.Nullable;

public class LimbMapper {

    public static final Map<Limb, ModelPartIdentifier> LIMB_MAP = Util.make(new EnumMap<>(Limb.class), (map) -> {
        // Head
        map.put(Limb.HEAD, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.HEAD));

        // Torso / Body regions
        map.put(Limb.THORAX, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.TORSO));
        map.put(Limb.ABDOMEN, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.LOWER_TORSO));

        // Right Arm segment
        map.put(Limb.UPPER_RIGHT_ARM, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.RIGHT_ARM));
        map.put(Limb.LOWER_RIGHT_ARM, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.RIGHT_ARM));
        map.put(Limb.RIGHT_HAND, null);

        // Left Arm segment
        map.put(Limb.UPPER_LEFT_ARM, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.LEFT_ARM));
        map.put(Limb.LOWER_LEFT_ARM, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.LEFT_ARM));
        map.put(Limb.LEFT_HAND, null);

        // Right Leg segment
        map.put(Limb.UPPER_RIGHT_LEG, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.RIGHT_LEG));
        map.put(Limb.LOWER_RIGHT_LEG, ModelPartIdentifier.forLimbAndExtension(net.ltxprogrammer.changed.client.animations.Limb.RIGHT_LEG, ChangedAddonLimbExtensions.LOWER_RIGHT_LEG));
        map.put(Limb.RIGHT_FOOT, ModelPartIdentifier.forLimbAndExtension(net.ltxprogrammer.changed.client.animations.Limb.RIGHT_LEG, ChangedAddonLimbExtensions.RIGHT_FOOT));

        // Left Leg segment
        map.put(Limb.UPPER_LEFT_LEG, ModelPartIdentifier.forLimb(net.ltxprogrammer.changed.client.animations.Limb.LEFT_LEG));
        map.put(Limb.LOWER_LEFT_LEG, ModelPartIdentifier.forLimbAndExtension(net.ltxprogrammer.changed.client.animations.Limb.LEFT_LEG, ChangedAddonLimbExtensions.LOWER_LEFT_LEG));
        map.put(Limb.LEFT_FOOT, ModelPartIdentifier.forLimbAndExtension(net.ltxprogrammer.changed.client.animations.Limb.LEFT_LEG, ChangedAddonLimbExtensions.LEFT_FOOT));
    });

    public static @Nullable ModelPartIdentifier getChangedLimb(Limb limb) {
        return LIMB_MAP.get(limb);
    }
}