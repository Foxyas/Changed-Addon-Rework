package net.foxyas.changedaddon.process.variantsExtraStats.visions;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public record TransfurVariantVision(ResourceLocation visionEffect, ResourceLocation form) {

    public static Codec<TransfurVariantVision> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("visionPostEffect").forGetter(TransfurVariantVision::visionEffect),
                    ResourceLocation.CODEC.fieldOf("form").forGetter(TransfurVariantVision::form)
            ).apply(instance, TransfurVariantVision::new)
    );

    public TransfurVariantVision(ResourceLocation visionEffect, ResourceLocation form) {
        this.visionEffect = fixJsonExtension(visionEffect);
        this.form = form;
    }

    private static ResourceLocation fixJsonExtension(ResourceLocation input) {
        String path = input.getPath();
        if (!path.endsWith(".json")) {
            path += ".json";
        }
        return ResourceLocation.fromNamespaceAndPath(input.getNamespace(), path);
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
