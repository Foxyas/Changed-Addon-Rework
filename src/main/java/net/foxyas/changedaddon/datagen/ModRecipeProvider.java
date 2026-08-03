package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.builders.BetterShapelessRecipeBuilder;
import net.foxyas.changedaddon.datagen.builders.ChangedAddonRecipeBuilder;
import net.foxyas.changedaddon.datagen.recipes.crop.BasicCropRecipeProvider;
import net.foxyas.changedaddon.datagen.recipes.crop.BasicSoilRecipeProvider;
import net.foxyas.changedaddon.datagen.recipes.farmers_delight.CookingRecipes;
import net.foxyas.changedaddon.datagen.recipes.farmers_delight.CuttingRecipes;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.item.LaethinItem;
import net.foxyas.changedaddon.util.ItemStackLoreUtil;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.function.Consumer;

import static net.foxyas.changedaddon.init.ChangedAddonItems.*;
import static net.minecraft.world.item.Items.*;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output) {
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
                .withExperience(2.0f)
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
                .withExperience(2.0f)
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

        ItemStack safeSyringeStack = new ItemStack(ChangedItems.LATEX_SYRINGE.get());
        CompoundTag tag = safeSyringeStack.getOrCreateTag();
        tag.putBoolean("safe", true);
        ItemStackLoreUtil.addLore(safeSyringeStack, Component.literal("THIS IS SUPPOSED TO BE SAFE"));

        // EXP 10 Containment Vial
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EXP_10_CONTAINMENT_VIAL.get())
                .pattern("012")
                .pattern("345")
                .pattern("678")
                .define('0', NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .define('1', EXP_10_LATEX_BASE.get())
                .define('2', ChangedAddonTags.Items.GOOEY) // Assuming your tag is defined in ChangedAddonTags.Items
                .define('3', GOO_CORE_FRAGMENT.get())
                .define('4', Items.NETHER_STAR)
                .define('5', Items.TOTEM_OF_UNDYING)
                .define('6', PartialNBTIngredient.of(safeSyringeStack.getItem(), safeSyringeStack.getOrCreateTag()))
                .define('7', BIOMASS.get())
                .define('8', LUMINARA_BLOOM_PETALS.get())
                .unlockedBy(getHasName(EXP_10_LATEX_BASE.get()), has(EXP_10_LATEX_BASE.get()))
                .unlockedBy(getHasName(EXPERIMENT_10_DNA.get()), has(EXPERIMENT_10_DNA.get()))
                .unlockedBy(getHasName(RED_LATEX_GOO.get()), has(RED_LATEX_GOO.get()))
                .save(recipeConsumer);


        // EXP 9 Containment Vial
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, EXP_9_CONTAINMENT_VIAL.get())
                .pattern("NAZ")
                .pattern("LTP")
                .pattern("UCD")
                .define('N', BIOMASS.get())
                .define('A', ChangedAddonTags.Items.GOOEY)
                .define('Z', LUMINARA_BLOOM_PETALS.get())
                .define('L', GOO_CORE_FRAGMENT.get())
                .define('T', Items.TOTEM_OF_UNDYING)
                .define('P', EXP_9_LATEX_BASE.get())
                .define('U', PartialNBTIngredient.of(safeSyringeStack.getItem(), safeSyringeStack.getOrCreateTag()))
                .define('C', Items.COPPER_BLOCK)
                .define('D', Items.IRON_BLOCK)
                .unlockedBy(getHasName(EXP_9_LATEX_BASE.get()), has(EXP_9_LATEX_BASE.get()))
                .unlockedBy(getHasName(EXPERIMENT_009_DNA.get()), has(EXPERIMENT_009_DNA.get()))
                .unlockedBy(getHasName(TRANSFUR_TOTEM.get()), has(TRANSFUR_TOTEM.get()))
                .save(recipeConsumer);

        BasicCropRecipeProvider.buildRecipes(recipeConsumer);
        BasicSoilRecipeProvider.buildRecipes(recipeConsumer);

        CookingRecipes.register(recipeConsumer);
        CuttingRecipes.register(recipeConsumer);

        woodFromLogs(recipeConsumer, LUMINARA_WOOD.get(), LUMINARA_LOG.get());
        woodFromLogs(recipeConsumer, STRIPPED_LUMINARA_WOOD.get(), STRIPPED_LUMINARA_LOG.get());
        planksFromLog(recipeConsumer, LUMINARA_PLANKS.get(), ChangedAddonTags.Items.LUMINARA_LOGS, 4);
        String hasPlanksStr = getHasName(LUMINARA_PLANKS.get());
        var hasLumiPlanks = has(LUMINARA_PLANKS.get());
        stairBuilder(LUMINARA_STAIRS.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        slab(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, LUMINARA_SLAB.get(), LUMINARA_PLANKS.get());
        doorBuilder(LUMINARA_DOOR.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        trapdoorBuilder(LUMINARA_TRAPDOOR.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        fenceBuilder(LUMINARA_FENCE.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        fenceGateBuilder(LUMINARA_FENCE_GATE.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        signBuilder(LUMINARA_SIGN.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        hangingSign(recipeConsumer, LUMINARA_HANGING_SIGN.get(), LUMINARA_PLANKS.get());
        buttonBuilder(LUMINARA_BUTTON.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(recipeConsumer);
        pressurePlate(recipeConsumer, LUMINARA_PRESSURE_PLATE.get(), LUMINARA_PLANKS.get());


        StrictNBTIngredient changedBook = StrictNBTIngredient.of(PatchouliAPI.get().getBookStack(ChangedAddonMod.resourceLoc("guide_book")));
        ResourceLocation changedBookRecipeID = ChangedAddonMod.resourceLoc("guide_book");
        BetterShapelessRecipeBuilder changedBookRecipe = BetterShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, changedBook)
                .requires(ChangedItems.DARK_LATEX_GOO.get())
                .requires(ChangedItems.LAB_BOOK.get())
                .requires(ChangedItems.WHITE_LATEX_GOO.get())
                .unlockedBy(
                        getHasName(Items.BOOK),
                        has(Items.BOOK)
                )
                .unlockedBy(
                        getHasName(ChangedItems.DARK_LATEX_GOO.get()),
                        has(ChangedItems.DARK_LATEX_GOO.get())
                )
                .unlockedBy(
                        getHasName(ChangedItems.LAB_BOOK.get()),
                        has(ChangedItems.LAB_BOOK.get())
                )
                .unlockedBy(
                        getHasName(ChangedItems.WHITE_LATEX_GOO.get()),
                        has(ChangedItems.WHITE_LATEX_GOO.get())
                );

//        ConditionalRecipe.Builder hasPatchouliMod = ConditionalRecipe.builder().addCondition(this.modLoaded(PatchouliAPI.MOD_ID));
//        hasPatchouliMod.addRecipe(changedBookRecipe::save).build(recipeConsumer, changedBookRecipeID);

        saveRecipeWithConditionAndSpecificID(recipeConsumer, changedBookRecipe, changedBookRecipeID, this.modLoaded(PatchouliAPI.MOD_ID));
    }

    private void saveRecipeWithCondition(Consumer<FinishedRecipe> recipeConsumer, RecipeBuilder recipe, ICondition... conditions) {
        ConditionalRecipe.Builder builder = ConditionalRecipe.builder();
        for (ICondition condition : conditions) {
            builder.addCondition(condition);
        }
        builder.addRecipe(recipe::save);
        builder.build(recipeConsumer, RecipeBuilder.getDefaultRecipeId(recipe.getResult()));
    }

    private void saveRecipeWithConditionAndSpecificID(Consumer<FinishedRecipe> recipeConsumer, RecipeBuilder recipe, ResourceLocation recipeID, ICondition... conditions) {
        ConditionalRecipe.Builder builder = ConditionalRecipe.builder();
        for (ICondition condition : conditions) {
            builder.addCondition(condition);
        }
        builder.addRecipe(recipe::save);
        builder.build(recipeConsumer, recipeID);
    }

    private void saveRecipesWithCondition(Consumer<FinishedRecipe> recipeConsumer, List<RecipeBuilder> recipes, ResourceLocation recipesConditionID, ICondition... conditions) {
        ConditionalRecipe.Builder builder = ConditionalRecipe.builder();
        for (ICondition condition : conditions) {
            builder.addCondition(condition);
        }
        for (RecipeBuilder recipe : recipes) {
            builder.addRecipe(recipe::save);
        }
        builder.build(recipeConsumer, recipesConditionID);
    }

    private ShapedRecipeBuilder reinforce(ItemLike result, ItemLike input, String criterionName, CriterionTriggerInstance criterion) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("IW")
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('W', input)
                .unlockedBy(criterionName, criterion);
    }
}
