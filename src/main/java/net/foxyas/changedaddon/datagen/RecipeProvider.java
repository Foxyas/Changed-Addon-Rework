package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.datagen.builders.ChangedAddonRecipeBuilder;
import net.foxyas.changedaddon.datagen.recipes.crop.BasicCropRecipeProvider;
import net.foxyas.changedaddon.datagen.recipes.crop.BasicSoilRecipeProvider;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.item.LaethinItem;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;
import static net.minecraft.world.item.Items.DIAMOND;
import static net.minecraft.world.item.Items.TINTED_GLASS;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {

    public RecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, LUMINAR_CRYSTAL_SPEAR.get())
                .pattern("SHS")
                .pattern("ITI")
                .pattern(" S ")
                .define('S', LUMINAR_CRYSTAL_SHARD.get())
                .define('H', LUMINAR_CRYSTAL_SHARD_HEARTED.get())
                .define('I', Ingredient.of(ItemTagsProvider.forgeIngotsIridium))
                .define('T', Items.STICK)
                .unlockedBy(getHasName(LUMINAR_CRYSTAL_SHARD.get()), has(LUMINAR_CRYSTAL_SHARD.get()))
                .save(recipeConsumer);

        String iridium = getHasName(IRIDIUM.get());
        CriterionTriggerInstance hasIridium = has(IRIDIUM.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRIDIUM_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .unlockedBy(iridium, hasIridium)
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ELECTRIC_KATANA.get())
                .pattern(" ID")
                .pattern("RLI")
                .pattern("BR ")
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('L', LUMINAR_CRYSTAL_SHARD.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('B', ChangedItems.TSC_BATON.get())
                .unlockedBy(iridium, hasIridium)
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ADVANCED_CATALYZER.get())
                .pattern("CIC")
                .pattern("ITI")
                .pattern("BRB")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('T', CATALYZER.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('B', Items.BLACKSTONE)
                .unlockedBy(iridium, hasIridium)
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ADVANCED_UNIFUSER.get())
                .pattern("CRC")
                .pattern("IUI")
                .pattern("CBC")
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('U', UNIFUSER.get())
                .define('B', Items.BLACKSTONE)
                .unlockedBy(iridium, hasIridium)
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, REINFORCED_CROSS_BLOCK.get(), 32)
                .pattern("NIN")
                .pattern("ICI")
                .pattern("NIN")
                .define('N', Items.NETHERITE_SCRAP)
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('C', Tags.Items.INGOTS_COPPER)
                .unlockedBy(iridium, hasIridium)
                .save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, KEYCARD_ITEM.get())
                .pattern("IQI")
                .pattern("NPN")
                .pattern("CRC")
                .define('I', Items.IRON_INGOT)
                .define('N', Items.IRON_NUGGET)
                .define('Q', Items.QUARTZ)
                .define('R', Items.REDSTONE)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('P', Items.PAPER)
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .save(recipeConsumer);

        reinforce(REINFORCED_WALL_CAUTION.get(), ChangedBlocks.WALL_CAUTION.get(), iridium, hasIridium)
                .save(recipeConsumer);

        reinforce(REINFORCED_WALL.get(), ChangedBlocks.WALL_WHITE.get(), iridium, hasIridium)
                .save(recipeConsumer);

        reinforce(REINFORCED_WALL_SILVER_TILED.get(), ChangedBlocks.WALL_BLUE_TILED.get(), iridium, hasIridium)
                .save(recipeConsumer);

        reinforce(REINFORCED_WALL_SILVER_STRIPED.get(), ChangedBlocks.WALL_BLUE_STRIPED.get(), iridium, hasIridium)
                .save(recipeConsumer);

        String lunarRose = getHasName(LUNAR_ROSE.get());
        CriterionTriggerInstance hasLunarRose = has(LUNAR_ROSE.get());

        ChangedAddonRecipeBuilder.generic(new ItemStack(LUNAR_ROSE.get()))
                .withSpeed(5)
                .withType(ChangedAddonRecipeTypes.UNIFUSER_RECIPE.get())
                .requires(DIAMOND).requires(TINTED_GLASS)
                .unlockedBy(lunarRose, hasLunarRose)
                .save(recipeConsumer, RecipeBuilder.getDefaultRecipeId(LUNAR_ROSE.get()) + "_secret");

        // Tipo 1: Usando o atalho estático da própria classe builder
        ItemStack white = LAETHIN.get().getDefaultInstance();
        StrictNBTIngredient stack = StrictNBTIngredient.of(white);
        ChangedAddonRecipeBuilder.unifuser(new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get(), 3))
                .requires(ChangedAddonItems.LITIX_CAMONIA.get())
                .requires(stack)
                .requires(ChangedAddonTags.Items.AIR)
                .withSpeed(0.5f)
                .unlockedBy("has_litix_camonia", has(ChangedAddonItems.LITIX_CAMONIA.get()))
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("changed_addon", "multi_litix_camonia_recipe_white"));
        ItemStack black = LAETHIN.get().getDefaultInstance();
        LaethinItem.setLaethinTypeForStack(white, LaethinItem.Type.DARK_LATEX);
        stack = StrictNBTIngredient.of(black);
        ChangedAddonRecipeBuilder.unifuser(new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get(), 3))
                .requires(ChangedAddonItems.LITIX_CAMONIA.get())
                .requires(stack)
                .requires(ChangedAddonTags.Items.AIR)
                .withSpeed(0.5f)
                .unlockedBy("has_litix_camonia", has(ChangedAddonItems.LITIX_CAMONIA.get()))
                .save(recipeConsumer, ResourceLocation.fromNamespaceAndPath("changed_addon", "multi_litix_camonia_recipe_dark"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, FLAMETHROWER.get())
                .pattern(" IB")
                .pattern("RFI")
                .pattern(" IT")
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('B', Items.BLAZE_ROD)
                .define('F', Items.FLINT_AND_STEEL)
                .define('T', Items.IRON_INGOT)
                .unlockedBy(
                        getHasName(Items.BLAZE_ROD),
                        has(Items.BLAZE_ROD)
                )
                .save(recipeConsumer);

        BasicCropRecipeProvider.buildRecipes(recipeConsumer);
        BasicSoilRecipeProvider.buildRecipes(recipeConsumer);
    }

    private ShapedRecipeBuilder reinforce(ItemLike result, ItemLike input, String criterionName, CriterionTriggerInstance criterion) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("IW")
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('W', input)
                .unlockedBy(criterionName, criterion);
    }
}
