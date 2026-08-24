package net.foxyas.changedaddon.datagen.animationAssociations;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ltxprogrammer.changed.entity.animation.AnimationCategory;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AnimationAssociationsProvider implements DataProvider {
    private final PackOutput packOutput;
    private final String modId;
    private final Map<ResourceLocation, JsonObject> entries = new HashMap<>();

    public AnimationAssociationsProvider(PackOutput packOutput, String modId) {
        this.packOutput = packOutput;
        this.modId = modId;
    }

    protected abstract void registerAssociations();

    protected EventBuilder add(ResourceLocation eventKey, AnimationCategory category) {
        JsonObject eventObj = entries.computeIfAbsent(eventKey, key -> {
            JsonObject obj = new JsonObject();
            obj.addProperty("category", category.getSerializedName());
            obj.add("animations", new JsonArray());
            return obj;
        });
        return new EventBuilder(eventObj.getAsJsonArray("animations"));
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        entries.clear();
        registerAssociations();

        JsonObject root = new JsonObject();
        entries.forEach((eventKey, jsonObject) -> root.add(eventKey.toString(), jsonObject));

        Path path = this.packOutput.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(this.modId)
                .resolve("animations.json");

        return DataProvider.saveStable(output, root, path);
    }

    @Override
    public @NotNull String getName() {
        return "Animation Associations: " + modId;
    }

    // Builder for animations within a single event
    public static class EventBuilder {
        private final JsonArray animationsArray;

        public EventBuilder(JsonArray animationsArray) {
            this.animationsArray = animationsArray;
        }

        public EventBuilder addAnimation(ResourceLocation animationName, JsonObject criteria) {
            JsonObject animObj = new JsonObject();
            animObj.addProperty("name", animationName.toString());
            animObj.add("criteria", criteria);

            this.animationsArray.add(animObj);
            return this;
        }

        public EventBuilder addAnimation(ResourceLocation animationName, Consumer<CriteriaBuilder> criteriaConsumer) {
            CriteriaBuilder builder = new CriteriaBuilder();
            criteriaConsumer.accept(builder);
            return addAnimation(animationName, builder.build());
        }

        public EventBuilder addAnimation(ResourceLocation animationName) {
            return addAnimation(animationName, criteria -> {});
        }
    }

    public static class CriteriaBuilder {
        private final JsonObject json = new JsonObject();

        // Helper to resolve ResourceLocation or String keys cleanly
        private String resolveKey(Object key) {
            if (key instanceof ResourceLocation location) {
                return location.toString();
            }
            return String.valueOf(key);
        }

        // String primitives
        public CriteriaBuilder put(String key, String value) {
            json.addProperty(key, value);
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, String value) {
            return put(key.toString(), value);
        }

        // Number primitives
        public CriteriaBuilder put(String key, Number value) {
            json.addProperty(key, value);
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, Number value) {
            return put(key.toString(), value);
        }

        // Boolean primitives
        public CriteriaBuilder put(String key, Boolean value) {
            json.addProperty(key, value);
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, Boolean value) {
            return put(key.toString(), value);
        }

        // ResourceLocation values
        public CriteriaBuilder put(String key, ResourceLocation value) {
            json.addProperty(key, value.toString());
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, ResourceLocation value) {
            return put(key.toString(), value.toString());
        }

        // Raw JsonElement objects
        public CriteriaBuilder put(String key, JsonElement value) {
            json.add(key, value);
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, JsonElement value) {
            return put(key.toString(), value);
        }

        // Array helpers
        public CriteriaBuilder put(String key, String... values) {
            JsonArray array = new JsonArray();
            for (String val : values) {
                array.add(val);
            }
            json.add(key, array);
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, String... values) {
            return put(key.toString(), values);
        }

        public CriteriaBuilder put(String key, ResourceLocation... values) {
            JsonArray array = new JsonArray();
            for (ResourceLocation val : values) {
                array.add(val.toString());
            }
            json.add(key, array);
            return this;
        }

        public CriteriaBuilder put(ResourceLocation key, ResourceLocation... values) {
            return put(key.toString(), values);
        }

        public JsonObject build() {
            return json;
        }
    }
}