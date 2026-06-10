package net.foxyas.changedaddon.extension.jei;

import mezz.jei.api.recipe.RecipeType;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.foxyas.changedaddon.recipe.special.KeycardColorRecipe;

public class CARecipeTypes {

    public static final RecipeType<CatalyzerRecipe> CATALYZER_RECIPE_TYPE = new RecipeType<>(CatalyzerRecipeCategory.UID, CatalyzerRecipe.class);
    public static final RecipeType<UnifuserRecipe> UNIFUSER_RECIPE_TYPE = new RecipeType<>(UnifuserRecipeCategory.UID, UnifuserRecipe.class);
    public static final RecipeType<KeycardColorRecipe> KEYCARD_COLOR_RECIPE_TYPE = new RecipeType<>(KeycardColorRecipeCategory.ID, KeycardColorRecipe.class);
}
