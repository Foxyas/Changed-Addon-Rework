package net.foxyas.changedaddon.datagen.recipes.farmers_delight;

import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.function.Consumer;

public class CookingRecipes
{
    public static final int FAST_COOKING = 100;      // 5 seconds
    public static final int NORMAL_COOKING = 200;    // 10 seconds
    public static final int SLOW_COOKING = 400;      // 20 seconds

    public static final float SMALL_EXP = 0.35F;
    public static final float MEDIUM_EXP = 1.0F;
    public static final float LARGE_EXP = 2.0F;

    public static void register(Consumer<FinishedRecipe> consumer) {
        cookDrinks(consumer);
    }

    private static void cookDrinks(Consumer<FinishedRecipe> consumer) {
        CookingPotRecipeBuilder.cookingPotRecipe(ChangedAddonItems.ORANGE_JUICE.get(), 1, FAST_COOKING, MEDIUM_EXP, Items.GLASS_BOTTLE)
                .addIngredient(ChangedItems.ORANGE.get(), 4)
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(ChangedItems.ORANGE.get(), Items.SUGAR, ChangedAddonItems.ORANGE_JUICE.get())
                .setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
                .save(consumer);
    }
}