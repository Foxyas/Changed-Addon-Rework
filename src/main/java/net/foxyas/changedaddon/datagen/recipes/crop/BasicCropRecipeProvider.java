package net.foxyas.changedaddon.datagen.recipes.crop;

import net.darkhax.botanypots.data.displaystate.DisplayState;
import net.darkhax.botanypots.data.displaystate.SimpleDisplayState;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.LuminaraBloomFlowerBlock;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class BasicCropRecipeProvider {

    public static void buildRecipes(Consumer<FinishedRecipe> consumer) {
        LuminaraBloomFlowerBlock luminaraBloomFlower = ChangedAddonBlocks.LUMINARA_BLOOM.get();
        DisplayState luminaraBloomDisplayState = new SimpleDisplayState(luminaraBloomFlower.defaultBlockState());

        Ingredient luminaraBloom = Ingredient.of(luminaraBloomFlower);

        new AutoCropRecipeBuilder(luminaraBloom)
                .addCategory("latex_block")
                .addCategory("white_latex_block")
                .addCategory("dark_latex_block")
                .setGrowthTicks(1200) // 60 seconds.
                .addDrop(luminaraBloomFlower.asItem().getDefaultInstance(), 1, 1, 1)
                .addDisplayState(luminaraBloomDisplayState)
                .save(consumer, ChangedAddonMod.resourceLoc("crop/luminara_flower_crop"));
    }
}