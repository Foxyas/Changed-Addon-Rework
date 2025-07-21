package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public record TransfurVariantVision(ResourceLocation visionEffect, ResourceLocation form) {

    public TransfurVariantVision(ResourceLocation visionEffect, ResourceLocation form) {
        this.visionEffect = fixJsonExtension(visionEffect);
        this.form = form;
    }

    private static ResourceLocation fixJsonExtension(ResourceLocation input) {
        String path = input.getPath();
        if (!path.endsWith(".json")) {
            path += ".json";
        }
        return new ResourceLocation(input.getNamespace(), path);
    }

    public static TransfurVariantVision fromJson(ResourceLocation id, JsonObject json) {
        String effectStr = GsonHelper.getAsString(json, "visionEffect");
        String formStr = GsonHelper.getAsString(json, "form");
        return new TransfurVariantVision(
            new ResourceLocation(effectStr),
            new ResourceLocation(formStr)
        );
    }
}
