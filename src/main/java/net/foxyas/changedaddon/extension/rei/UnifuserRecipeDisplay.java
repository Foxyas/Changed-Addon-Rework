package net.foxyas.changedaddon.extension.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;

import java.util.Collections;
import java.util.Optional;

public class UnifuserRecipeDisplay extends BasicDisplay {
    private final float progressSpeed;

    public UnifuserRecipeDisplay(UnifuserRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), 
              Collections.singletonList(EntryIngredients.of(recipe.getResultItem())),
              Optional.of(recipe.getId()));
        this.progressSpeed = recipe.getProgressSpeed();
    }

    public float getProgressSpeed() {
        return progressSpeed;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ChangedAddonReiPlugin.UNIFUSER;
    }
}