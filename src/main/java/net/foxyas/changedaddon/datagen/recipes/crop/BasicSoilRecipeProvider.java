package net.foxyas.changedaddon.datagen.recipes.crop;

import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.darkhax.botanypots.data.displaystate.SimpleDisplayState;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class BasicSoilRecipeProvider {

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        DisplayState DARK_LATEX_DORMANT = new SimpleDisplayState(ChangedAddonBlocks.DORMANT_DARK_LATEX.get().defaultBlockState());
        DisplayState WHITE_LATEX_DORMANT = new SimpleDisplayState(ChangedAddonBlocks.DORMANT_WHITE_LATEX.get().defaultBlockState());

        Ingredient dormantDarkLatex = Ingredient.of(ChangedAddonBlocks.DORMANT_DARK_LATEX.get());
        Ingredient dormantWhiteLatex = Ingredient.of(ChangedAddonBlocks.DORMANT_WHITE_LATEX.get());

        new AutoSoilRecipeBuilder(dormantWhiteLatex)
                .addCategory("latex_block")
                .addCategory("white_latex_block")
                .setDisplay(WHITE_LATEX_DORMANT)
                .save(consumer, ChangedAddonMod.resourceLoc("soil/dormant_white_latex"));

        new AutoSoilRecipeBuilder(dormantDarkLatex)
                .addCategory("latex_block")
                .addCategory("dark_latex_block")
                .setDisplay(DARK_LATEX_DORMANT)
                .save(consumer, ChangedAddonMod.resourceLoc("soil/dormant_dark_latex"));
    }
}