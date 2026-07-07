package net.foxyas.changedaddon.datagen.builders;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ChangedAddonRecipeBuilder implements RecipeBuilder {

    private final ItemStack result;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;
    private RecipeSerializer<?> type;
    private Optional<Float> progressSpeed = Optional.of(1f);
    private Optional<Float> nitrogenUsage = Optional.empty();
    private Optional<Float> experience = Optional.empty();

    public ChangedAddonRecipeBuilder(RecipeSerializer<?> type, ItemLike pResult, int pCount) {
        this.result = new ItemStack(pResult, pCount);
        this.type = type;
    }

    public ChangedAddonRecipeBuilder(ItemLike pResult, int pCount) {
        this.result = new ItemStack(pResult, pCount);
    }

    public ChangedAddonRecipeBuilder(RecipeSerializer<?> type, ItemStack stack) {
        this.result = stack;
        this.type = type;
    }

    public ChangedAddonRecipeBuilder(ItemStack stack) {
        this.result = stack;
    }

    public static ChangedAddonRecipeBuilder unifuser(ItemStack result) {
        return new ChangedAddonRecipeBuilder(ChangedAddonRecipeTypes.UNIFUSER_RECIPE.get(), result);
    }

    public static ChangedAddonRecipeBuilder catalyzer(ItemStack result) {
        return new ChangedAddonRecipeBuilder(ChangedAddonRecipeTypes.CATALYZER_RECIPE.get(), result);
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ChangedAddonRecipeBuilder generic(ItemLike pResult) {
        return new ChangedAddonRecipeBuilder(pResult, 1);
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ChangedAddonRecipeBuilder generic(ItemLike pResult, int pCount) {
        return new ChangedAddonRecipeBuilder(pResult, pCount);
    }

    /**
     * Creates a new builder for a shapeless recipe.
     */
    public static ChangedAddonRecipeBuilder generic(ItemStack stack) {
        return new ChangedAddonRecipeBuilder(stack);
    }

    /**
     * Adds an ingredient that can be any item in the given tag.
     */
    public ChangedAddonRecipeBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    /**
     * Adds an ingredient of the given item.
     */
    public ChangedAddonRecipeBuilder requires(ItemLike pItem) {
        return this.requires(pItem, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public ChangedAddonRecipeBuilder requires(ItemLike pItem, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.requires(Ingredient.of(pItem));
        }

        return this;
    }

    /**
     * Adds an ingredient.
     */
    public ChangedAddonRecipeBuilder requires(Ingredient pIngredient) {
        return this.requires(pIngredient, 1);
    }

    /**
     * Adds a Recipe Serialized ["Type"].
     */
    public ChangedAddonRecipeBuilder withType(RecipeSerializer<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Adds a Recipe Serialized ["Speed"].
     */
    public ChangedAddonRecipeBuilder withSpeed(float speed) {
        this.progressSpeed = Optional.of(speed);
        return this;
    }

    /**
     * Adds a Recipe Serialized ["NitrogenUsage"].
     */
    public ChangedAddonRecipeBuilder withNitrogenUsage(float nitrogenUsage) {
        this.nitrogenUsage = Optional.of(nitrogenUsage);
        return this;
    }

    /**
     * Adds a Recipe Serialized ["NitrogenUsage"].
     */
    public ChangedAddonRecipeBuilder withExperience(float experience) {
        this.experience = Optional.of(experience);
        return this;
    }

    /**
     * Adds an ingredient multiple times.
     */
    public ChangedAddonRecipeBuilder requires(Ingredient pIngredient, int pQuantity) {
        for (int i = 0; i < pQuantity; ++i) {
            this.ingredients.add(pIngredient);
        }

        return this;
    }

    public @NotNull ChangedAddonRecipeBuilder unlockedBy(@NotNull String pCriterionName, @NotNull CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    public @NotNull ChangedAddonRecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public @NotNull Item getResult() {
        return this.result.getItem();
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        this.ensureValid(pRecipeId);
        this.advancement.parent(ResourceLocation.parse("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new ChangedAddonRecipeBuilder.Result(this.type,
                pRecipeId,
                this.result,
                this.group == null ? "" : this.group,
                this.ingredients,
                this.progressSpeed,
                this.nitrogenUsage,
                this.experience,
                this.advancement,
                ResourceLocation.fromNamespaceAndPath(pRecipeId.getNamespace(), "recipes/" + RecipeCategory.MISC.getFolderName() + "/" + pRecipeId.getPath())));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void ensureValid(ResourceLocation pId) {
        if (type == null) {
            throw new IllegalStateException("No recipe type selected " + pId);
        }
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack result;
        private final String group;
        private final List<Ingredient> ingredients;
        private final Optional<Float> progressSpeed;
        private final Optional<Float> nitrogenUsage;
        private Optional<Float> experience;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final @NotNull RecipeSerializer<?> type;

        public Result(@NotNull RecipeSerializer<?> type, ResourceLocation pId, ItemLike pResult, int pCount, String pGroup, List<Ingredient> pIngredients, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            this.id = pId;
            this.result = new ItemStack(pResult, pCount);
            this.group = pGroup;
            this.ingredients = pIngredients;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
            this.type = type;
            this.progressSpeed = Optional.empty();
            this.nitrogenUsage = Optional.empty();
            this.experience = Optional.empty();
        }

        public Result(@NotNull RecipeSerializer<?> type, ResourceLocation pId, ItemStack pResult, String pGroup, List<Ingredient> pIngredients, Optional<Float> speed, Optional<Float> nitrogenUsage, Optional<Float> experience, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            this.id = pId;
            this.result = pResult;
            this.group = pGroup;
            this.ingredients = pIngredients;
            this.progressSpeed = speed;
            this.nitrogenUsage = nitrogenUsage;
            this.experience = experience;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
            this.type = type;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }

            JsonArray jsonarray = new JsonArray();

            for (Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }

            pJson.add("ingredients", jsonarray);

            // Serializa o resultado com NBT se existir
            if (result.getDamageValue() == 0) result.getOrCreateTag().remove("Damage");
            JsonObject jsonobject = StrictNBTIngredient.of(result).toJson().getAsJsonObject();
            pJson.add("output", jsonobject);

            // ✅ Adiciona propriedades customizadas
            progressSpeed.ifPresent(speed -> pJson.addProperty("progressSpeed", speed));
            if (type == ChangedAddonRecipeTypes.CATALYZER_RECIPE.get()) {
                nitrogenUsage.ifPresent(nitrogen -> pJson.addProperty("nitrogenUsage", nitrogen));
            }
            experience.ifPresent(aFloat -> pJson.addProperty("experience", aFloat));
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return this.type;
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }

}