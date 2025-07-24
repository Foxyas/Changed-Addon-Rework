package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientTransfurVisionRegistry {
    private static final Map<ResourceLocation, TransfurVariantVision> VISION_MAP = new HashMap<>();

    public static void clear() {
        VISION_MAP.clear();
    }

    public static void register(TransfurVariantVision vision) {
        VISION_MAP.put(vision.form(), vision);
    }

    public static @Nullable TransfurVariantVision get(ResourceLocation formId) {
        return VISION_MAP.get(formId);
    }

    public static boolean hasVision(ResourceLocation formId) {
        return VISION_MAP.containsKey(formId);
    }

    public static Collection<TransfurVariantVision> getAll() {
        return VISION_MAP.values();
    }
}
