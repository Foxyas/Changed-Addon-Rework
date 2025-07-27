package net.foxyas.changedaddon.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {

    }
}
