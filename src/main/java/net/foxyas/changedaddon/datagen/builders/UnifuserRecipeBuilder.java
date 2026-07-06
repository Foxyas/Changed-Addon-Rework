package net.foxyas.changedaddon.datagen.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class UnifuserRecipeBuilder implements RecipeBuilder {
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients = NonNullList.create();
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private float progressSpeed = 1.0f;

    private UnifuserRecipeBuilder(ItemStack result) {
        this.result = result;
    }

    public static UnifuserRecipeBuilder unifuser(ItemStack result) {
        return new UnifuserRecipeBuilder(result);
    }

    // Adiciona ingrediente normal
    public UnifuserRecipeBuilder requires(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public UnifuserRecipeBuilder requires(ItemLike item) {
        return this.requires(Ingredient.of(item));
    }

    // Define a velocidade do progresso (ProgressSpeed)
    public UnifuserRecipeBuilder progressSpeed(float speed) {
        this.progressSpeed = speed;
        return this;
    }

    @Override
    public @NotNull UnifuserRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public @NotNull UnifuserRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return this.result.getItem();
    }

    // Método para salvar a receita sem ID customizado (usa o ID do item de output)
    @Override
    public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer) {
        this.save(finishedRecipeConsumer, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
    }

    // Método principal de salvamento
    @Override
    public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        this.advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(RequirementsStrategy.OR);

        ResourceLocation advancementId = recipeId.withPrefix("recipes/");

        finishedRecipeConsumer.accept(new Result(recipeId, this.result, this.ingredients, this.progressSpeed, this.advancement, advancementId));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
        if (this.ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients defined for recipe " + id);
        }
    }

    // Classe interna que gera o JSON de fato
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final ItemStack result;
        private final NonNullList<Ingredient> ingredients;
        private final float progressSpeed;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, ItemStack result, NonNullList<Ingredient> ingredients, float progressSpeed, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.ingredients = ingredients;
            this.progressSpeed = progressSpeed;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            // Cria a array de ingredientes
            JsonArray jsonArray = new JsonArray();
            for (Ingredient ingredient : this.ingredients) {
                jsonArray.add(ingredient.toJson());
            }
            json.add("ingredients", jsonArray);

            json.add("output", itemStackToJson(result));

            if (this.progressSpeed != 1.0f) {
                json.addProperty("progressSpeed", this.progressSpeed);
            }
        }

        public static JsonObject itemStackToJson(ItemStack result) {
            JsonObject resultJson = new JsonObject();

            String itemName = ForgeRegistries.ITEMS.getKey(result.getItem()).toString();
            resultJson.addProperty("item", itemName);

            resultJson.addProperty("count", result.getCount());

            if (result.hasTag() && result.getTag() != null) {
                resultJson.addProperty("nbt", result.getTag().toString());
            }

            return resultJson;
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ChangedAddonRecipeTypes.UNIFUSER_RECIPE.get();
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