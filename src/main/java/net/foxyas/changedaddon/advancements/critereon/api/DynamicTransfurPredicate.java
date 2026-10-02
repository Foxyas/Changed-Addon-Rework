package net.foxyas.changedaddon.advancements.critereon.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;

import javax.annotation.Nullable;

public class DynamicTransfurPredicate {
    @Nullable private final TransfurVariant<?> singleForm;
    @Nullable private final TagKey<TransfurVariant<?>> tag;
    @Nullable private final TagKey<EntityType<?>> entityTypeTag;
    @Nullable private final LatexType type;
    private final boolean flying;
    private final boolean swimming;
    private final boolean legless;
    private final boolean inverted;

    private static final ResourceKey<Registry<TransfurVariant<?>>> REGISTRY_KEY =
            ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey();

    public DynamicTransfurPredicate(
            @Nullable TransfurVariant<?> singleForm,
            @Nullable TagKey<TransfurVariant<?>> tag,
            @Nullable TagKey<EntityType<?>> entityTypeTag,
            @Nullable LatexType type,
            boolean flying,
            boolean swimming,
            boolean legless,
            boolean inverted
    ) {
        this.singleForm = singleForm;
        this.tag = tag;
        this.entityTypeTag = entityTypeTag;
        this.type = type;
        this.flying = flying;
        this.swimming = swimming;
        this.legless = legless;
        this.inverted = inverted;
    }

    public boolean matches(TransfurVariantInstance<?> instance) {
        if (instance == null) return false;

        TransfurVariant<?> variant = instance.getParent();
        if (variant == null) return false;

        boolean rawMatch = true;

        if (this.singleForm != null) {
            rawMatch = variant.getFormId().equals(this.singleForm.getFormId());
        }

        if (this.tag != null) {
            rawMatch = rawMatch && variant.is(this.tag);
        }

        if (this.entityTypeTag != null) {
            EntityType<?> entityType = variant.getEntityType();
            rawMatch = rawMatch && (entityType != null && entityType.is(this.entityTypeTag));
        }

        if (this.type != null) {
            rawMatch = rawMatch && (instance.getLatexType() == this.type);
        }

        if (this.flying) {
            rawMatch = rawMatch && variant.canGlide;
        }
        if (this.swimming) {
            rawMatch = rawMatch && variant.getBreatheMode().canBreatheWater();
        }
        if (this.legless) {
            rawMatch = rawMatch && instance.getEntityShape().isLegless();
        }

        return this.inverted != rawMatch;
    }

    public static DynamicTransfurPredicate jsonElement(JsonElement element) {
        if (element.isJsonPrimitive()) {
            String str = element.getAsString();
            if (str.startsWith("#")) {
                TagKey<TransfurVariant<?>> tagKey = TagKey.create(REGISTRY_KEY, ResourceLocation.parse(str.substring(1)));
                return of(tagKey);
            } else {
                ResourceLocation loc = ResourceLocation.parse(str);
                TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);
                if (variant == null) {
                    throw new JsonSyntaxException("Unknown TransfurVariant: " + loc);
                }
                return of(variant);
            }
        } else if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();
            Builder builder = builder();

            if (obj.has("tag")) {
                String tagStr = GsonHelper.getAsString(obj, "tag");
                if (tagStr.startsWith("#")) tagStr = tagStr.substring(1);
                builder.tag(TagKey.create(REGISTRY_KEY, ResourceLocation.parse(tagStr)));
            } else if (obj.has("id")) {
                ResourceLocation loc = ResourceLocation.parse(GsonHelper.getAsString(obj, "id"));
                TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(loc);
                if (variant == null) {
                    throw new JsonSyntaxException("Unknown TransfurVariant in JSON Object: " + loc);
                }
                builder.form(variant);
            }

            if (obj.has("entity_type_tag") || obj.has("entity_tag")) {
                String entityTagStr = obj.has("entity_type_tag")
                        ? GsonHelper.getAsString(obj, "entity_type_tag")
                        : GsonHelper.getAsString(obj, "entity_tag");
                if (entityTagStr.startsWith("#")) entityTagStr = entityTagStr.substring(1);
                builder.entityTypeTag(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(entityTagStr)));
            }

            if (obj.has("type")) {
                ResourceLocation typeLoc = ResourceLocation.parse(GsonHelper.getAsString(obj, "type"));
                builder.type((LatexType) ChangedRegistry.LATEX_TYPE.getValue(typeLoc));
            }

            builder.flying(GsonHelper.getAsBoolean(obj, "flying", false));
            builder.swimming(GsonHelper.getAsBoolean(obj, "swimming", false));
            builder.legless(GsonHelper.getAsBoolean(obj, "legless", false));
            builder.inverted(GsonHelper.getAsBoolean(obj, "inverted", GsonHelper.getAsBoolean(obj, "reverse", false)));

            return builder.build();
        }

        throw new JsonSyntaxException("Expected string or json object for form entry, got: " + element);
    }

    public JsonElement toJson() {
        if (this.type == null && this.entityTypeTag == null && !this.flying && !this.swimming && !this.legless && !this.inverted) {
            if (this.tag != null) {
                return new JsonPrimitive("#" + this.tag.location());
            } else if (this.singleForm != null) {
                return new JsonPrimitive(this.singleForm.getFormId().toString());
            }
        }

        JsonObject obj = new JsonObject();
        if (this.tag != null) {
            obj.addProperty("tag", this.tag.location().toString());
        } else if (this.singleForm != null) {
            obj.addProperty("id", this.singleForm.getFormId().toString());
        }

        if (this.entityTypeTag != null) {
            obj.addProperty("entity_type_tag", this.entityTypeTag.location().toString());
        }

        if (this.type != null) {
            obj.addProperty("type", this.type.toString());
        }

        if (this.flying) obj.addProperty("flying", true);
        if (this.swimming) obj.addProperty("swimming", true);
        if (this.legless) obj.addProperty("legless", true);
        if (this.inverted) obj.addProperty("inverted", true);

        return obj;
    }

    // --- Static Factory Methods ---

    public static Builder builder() {
        return new Builder();
    }

    public static DynamicTransfurPredicate of(TransfurVariant<?> variant) {
        return builder().form(variant).build();
    }

    public static DynamicTransfurPredicate of(TagKey<TransfurVariant<?>> tag) {
        return builder().tag(tag).build();
    }

    public static DynamicTransfurPredicate ofEntityTypeTag(TagKey<EntityType<?>> entityTypeTag) {
        return builder().entityTypeTag(entityTypeTag).build();
    }

    // --- Builder Class ---

    public static class Builder {
        private TransfurVariant<?> singleForm;
        private TagKey<TransfurVariant<?>> tag;
        private TagKey<EntityType<?>> entityTypeTag;
        private LatexType type;
        private boolean flying = false;
        private boolean swimming = false;
        private boolean legless = false;
        private boolean inverted = false;

        public Builder form(TransfurVariant<?> singleForm) {
            this.singleForm = singleForm;
            return this;
        }

        public Builder tag(TagKey<TransfurVariant<?>> tag) {
            this.tag = tag;
            return this;
        }

        public Builder entityTypeTag(TagKey<EntityType<?>> entityTypeTag) {
            this.entityTypeTag = entityTypeTag;
            return this;
        }

        public Builder type(LatexType type) {
            this.type = type;
            return this;
        }

        public Builder flying(boolean flying) {
            this.flying = flying;
            return this;
        }

        public Builder flying() {
            return flying(true);
        }

        public Builder swimming(boolean swimming) {
            this.swimming = swimming;
            return this;
        }

        public Builder swimming() {
            return swimming(true);
        }

        public Builder legless(boolean legless) {
            this.legless = legless;
            return this;
        }

        public Builder legless() {
            return legless(true);
        }

        public Builder inverted(boolean inverted) {
            this.inverted = inverted;
            return this;
        }

        public Builder inverted() {
            return inverted(true);
        }

        public DynamicTransfurPredicate build() {
            return new DynamicTransfurPredicate(
                    this.singleForm,
                    this.tag,
                    this.entityTypeTag,
                    this.type,
                    this.flying,
                    this.swimming,
                    this.legless,
                    this.inverted
            );
        }
    }
}