package net.foxyas.changedaddon.datagen.builders;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class InfuserRecipeBuilder implements RecipeBuilder {
    private final TransfurVariant<?> result;
    private final List<Ingredient> ingredients = Lists.newArrayList();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @Nullable
    private String group;
    private boolean gendered = false; // optional flag

    public InfuserRecipeBuilder(TransfurVariant<?> pResult, boolean gendered) {
        this.result = pResult;
        this.gendered = gendered;
    }

    public static InfuserRecipeBuilder gendered(TransfurVariant<?> pResult) {
        return new InfuserRecipeBuilder(pResult, true);
    }

    public static InfuserRecipeBuilder genderLess(TransfurVariant<?> pResult) {
        return new InfuserRecipeBuilder(pResult, false);
    }

    public InfuserRecipeBuilder requires(TagKey<Item> tag) {
        return this.requires(Ingredient.of(tag));
    }

    public InfuserRecipeBuilder requires(ItemLike item) {
        return this.requires(item, 1);
    }

    public InfuserRecipeBuilder requires(ItemLike item, int quantity) {
        for (int i = 0; i < quantity; ++i)
            this.requires(Ingredient.of(item));
        return this;
    }

    public InfuserRecipeBuilder requires(Ingredient ingredient) {
        return this.requires(ingredient, 1);
    }

    public InfuserRecipeBuilder requires(Ingredient ingredient, int quantity) {
        for (int i = 0; i < quantity; ++i)
            this.ingredients.add(ingredient);
        return this;
    }

    /**
     * Enables gendered = true for this recipe
     */
    public InfuserRecipeBuilder isGendered() {
        this.gendered = true;
        return this;
    }

    public @NotNull InfuserRecipeBuilder unlockedBy(@NotNull String name, @NotNull CriterionTriggerInstance trigger) {
        this.advancement.addCriterion(name, trigger);
        return this;
    }

    public @NotNull InfuserRecipeBuilder group(@Nullable String name) {
        this.group = name;
        return this;
    }

    public @NotNull Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        this.save(pFinishedRecipeConsumer, result.getFormId());
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation recipeId) {
        this.ensureValid(recipeId);
        this.advancement.parent(ResourceLocation.parse("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(RequirementsStrategy.OR);

        consumer.accept(new Result(recipeId, this.result, this.group == null ? "" : this.group,
                this.ingredients, this.gendered, this.advancement,
                ResourceLocation.fromNamespaceAndPath(recipeId.getNamespace(),
                        "recipes/" + (!this.gendered ? result.getFormId().getPath() : getFormIdPathWithoutGender().toString()))));
    }

    public ResourceLocation getFormIdWithoutGender() {
        return ResourceLocation.parse(this.result.getFormId().toString().replace("/female", "").replace("/male", ""));
    }

    public ResourceLocation getFormIdPathWithoutGender() {
        return ResourceLocation.parse(this.result.getFormId().getPath().replace("/female", "").replace("/male", ""));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final TransfurVariant<?> result;
        private final String group;
        private final List<Ingredient> ingredients;
        private final boolean gendered;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, TransfurVariant<?> result, String group, List<Ingredient> ingredients,
                      boolean gendered, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.result = result;
            this.group = group;
            this.ingredients = ingredients;
            this.gendered = gendered;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            if (!this.group.isEmpty())
                json.addProperty("group", this.group);

            json.addProperty("type", "changed:infuser");

            JsonArray ingredientsArray = new JsonArray();
            for (Ingredient ingredient : this.ingredients)
                ingredientsArray.add(ingredient.toJson());
            json.add("ingredients", ingredientsArray);

            // Optional gendered field
            if (this.gendered)
                json.addProperty("gendered", true);

            // "form" replaces vanilla "result"
            json.addProperty("form", !gendered ? result.getFormId().toString() : getFormIdWithoutGender().toString());
        }

        public ResourceLocation getFormIdWithoutGender() {
            return ResourceLocation.parse(this.result.getFormId().toString().replace("/female", "").replace("/male", ""));
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ChangedRecipeSerializers.INFUSER_RECIPE.get();
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}