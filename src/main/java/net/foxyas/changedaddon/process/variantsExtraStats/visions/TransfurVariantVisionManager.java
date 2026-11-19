package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import static net.foxyas.changedaddon.init.ChangedAddonTransfurVariantVisions.TRANSFUR_VARIANT_VISION_KEY;

public class TransfurVariantVisionManager {

    public static @Nullable TransfurVariantVision getTransfurVariantVision(Level level, ResourceLocation formId) {
        Registry<TransfurVariantVision> transfurVariantVisions = level.registryAccess().registryOrThrow(TRANSFUR_VARIANT_VISION_KEY);
        return transfurVariantVisions.stream()
                .filter(transfurVariantVision -> transfurVariantVision.form().equals(formId))
                .findFirst()
                .orElse(null);
    }
}
