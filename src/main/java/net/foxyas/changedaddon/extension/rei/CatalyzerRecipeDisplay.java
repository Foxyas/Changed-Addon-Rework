package net.foxyas.changedaddon.extension.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.minecraft.world.item.ItemStack;
import java.util.Collections;
import java.util.Optional;

public class CatalyzerRecipeDisplay extends BasicDisplay {
    private final float progressSpeed;
    private final float nitrogenUsage;

    public CatalyzerRecipeDisplay(CatalyzerRecipe recipe) {
        // O Catalyzer usa a registryAccess para o resultado, mas aqui pegamos o ItemStack padrão
        super(EntryIngredients.ofIngredients(recipe.getIngredients()), 
              Collections.singletonList(EntryIngredients.of(recipe.getResultItem())),
              Optional.of(recipe.getId()));
        this.progressSpeed = recipe.getProgressSpeed();
        this.nitrogenUsage = recipe.getNitrogenUsage();
    }

    public float getProgressSpeed() {
        return progressSpeed;
    }

    public float getNitrogenUsage() {
        return nitrogenUsage;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ChangedAddonReiPlugin.CATALYZER;
    }
}