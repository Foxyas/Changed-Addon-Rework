package net.foxyas.changedaddon.datagen.builders;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CatalyzerRecipeBuilder implements RecipeBuilder {
    private final ItemStack output;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    
    private float progressSpeed = 1.0f;
    private float nitrogenUsage = 0.0f;
    private float experience = 0.0f;
    
    // Configurações de preserveData
    private boolean copyAllNbt = false;
    private final List<String> specificNbtKeys = Lists.newArrayList();

    public CatalyzerRecipeBuilder(ItemLike pResult, int pCount) {
        this.output = new ItemStack(pResult, pCount);
    }

    public static CatalyzerRecipeBuilder catalyzer(ItemLike pResult) {
        return new CatalyzerRecipeBuilder(pResult, 1);
    }

    public static CatalyzerRecipeBuilder catalyzer(ItemLike pResult, int pCount) {
        return new CatalyzerRecipeBuilder(pResult, pCount);
    }

    public static CatalyzerRecipeBuilder catalyzer(ItemStack pOutput) {
        CatalyzerRecipeBuilder builder = new CatalyzerRecipeBuilder(pOutput.getItem(), pOutput.getCount());
        if (pOutput.hasTag()) {
            builder.output.setTag(pOutput.getTag());
        }
        return builder;
    }

    public CatalyzerRecipeBuilder requires(TagKey<Item> pTag) {
        return this.requires(Ingredient.of(pTag));
    }

    public CatalyzerRecipeBuilder requires(ItemLike pItem) {
        return this.requires(Ingredient.of(pItem));
    }

    public CatalyzerRecipeBuilder requires(Ingredient pIngredient) {
        this.ingredients.add(pIngredient);
        return this;
    }

    public CatalyzerRecipeBuilder progressSpeed(float speed) {
        this.progressSpeed = speed;
        return this;
    }

    public CatalyzerRecipeBuilder nitrogenUsage(float nitrogen) {
        this.nitrogenUsage = nitrogen;
        return this;
    }

    public CatalyzerRecipeBuilder experience(float exp) {
        this.experience = exp;
        return this;
    }

    // Métodos para o preserveData
    public CatalyzerRecipeBuilder preserveData(boolean copyAll) {
        this.copyAllNbt = copyAll;
        this.specificNbtKeys.clear();
        return this;
    }

    public CatalyzerRecipeBuilder preserveData(String... keys) {
        this.copyAllNbt = false;
        this.specificNbtKeys.clear();
        this.specificNbtKeys.addAll(List.of(keys));
        return this;
    }

    public @NotNull CatalyzerRecipeBuilder unlockedBy(@NotNull String pCriterionName, net.minecraft.advancements.@NotNull CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return output.getItem();
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        this.ensureValid(pRecipeId);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(RequirementsStrategy.OR);
        
        pFinishedRecipeConsumer.accept(new Result(
                pRecipeId, output, ingredients, progressSpeed, nitrogenUsage, experience, 
                copyAllNbt, specificNbtKeys, advancement, pRecipeId.withPrefix("recipes/catalyzer/")
        ));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack output;
        private final List<Ingredient> ingredients;
        private final float progressSpeed;
        private final float nitrogenUsage;
        private final float experience;
        private final boolean copyAllNbt;
        private final List<String> specificNbtKeys;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, ItemStack output, List<Ingredient> ingredients, 
                      float progressSpeed, float nitrogenUsage, float experience, 
                      boolean copyAllNbt, List<String> specificNbtKeys, 
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.output = output;
            this.ingredients = ingredients;
            this.progressSpeed = progressSpeed;
            this.nitrogenUsage = nitrogenUsage;
            this.experience = experience;
            this.copyAllNbt = copyAllNbt;
            this.specificNbtKeys = specificNbtKeys;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            pJson.addProperty("progressSpeed", this.progressSpeed);
            pJson.addProperty("nitrogenUsage", this.nitrogenUsage);
            pJson.addProperty("experience", this.experience);

            JsonArray jsonarray = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                jsonarray.add(ingredient.toJson());
            }
            pJson.add("ingredients", jsonarray);

            JsonObject outputObject = new JsonObject();
            outputObject.addProperty("item", BuiltInRegistries.ITEM.getKey(this.output.getItem()).toString());
            if (this.output.getCount() > 1) {
                outputObject.addProperty("count", this.output.getCount());
            }
            if (this.output.hasTag()) {
                outputObject.addProperty("nbt", this.output.getTag().toString());
            }

            // Serializa o preserveData dentro do objeto output
            if (this.copyAllNbt) {
                outputObject.addProperty("preserveData", true);
            } else if (!this.specificNbtKeys.isEmpty()) {
                JsonArray keysArray = new JsonArray();
                for (String key : this.specificNbtKeys) {
                    keysArray.add(key);
                }
                outputObject.add("preserveData", keysArray);
            }

            pJson.add("output", outputObject);
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return CatalyzerRecipe.Serializer.INSTANCE;
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}