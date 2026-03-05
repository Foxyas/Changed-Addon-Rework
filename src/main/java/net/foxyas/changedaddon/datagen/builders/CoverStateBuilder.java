package net.foxyas.changedaddon.datagen.builders;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoverStateBuilder {

    private final ResourceLocation block;
    private final Map<String, VariantBuilder> variants = new LinkedHashMap<>();

    public CoverStateBuilder(ResourceLocation block) {
        this.block = block;
    }

    public VariantBuilder variant(String predicate) {
        VariantBuilder builder = new VariantBuilder();
        variants.put(predicate, builder);
        return builder;
    }

    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonObject variantsJson = new JsonObject();

        for (Map.Entry<String, VariantBuilder> entry : variants.entrySet()) {
            variantsJson.add(entry.getKey(), entry.getValue().toJson());
        }

        root.add("variants", variantsJson);
        return root;
    }
}