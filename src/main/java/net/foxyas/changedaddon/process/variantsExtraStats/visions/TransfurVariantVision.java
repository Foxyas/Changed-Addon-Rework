package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import com.google.gson.JsonObject;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class TransfurVariantVision {
    private final ResourceLocation visionEffect;
    private final ResourceLocation form;

    public TransfurVariantVision(ResourceLocation visionEffect, ResourceLocation form) {
        this.visionEffect = fixJsonExtension(visionEffect);
        this.form = form;
    }

    private static ResourceLocation fixJsonExtension(ResourceLocation input) {
        String path = input.getPath();
        if (!path.endsWith(".json")) {
            path += ".json";
        }
        return ResourceLocation.parse(input.getNamespace(), path);
    }

    public ResourceLocation getVisionEffect() {
        return visionEffect;
    }

    public ResourceLocation getForm() {
        return form;
    }

    public static TransfurVariantVision fromJson(ResourceLocation id, JsonObject json) {
        String effectStr = GsonHelper.getAsString(json, "visionEffect");
        String formStr = GsonHelper.getAsString(json, "form");
        return new TransfurVariantVision(
            ResourceLocation.parse(effectStr),
            ResourceLocation.parse(formStr)
        );
    }
}
