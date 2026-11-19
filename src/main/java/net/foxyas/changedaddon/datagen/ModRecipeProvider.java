package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.builders.BetterShapelessRecipeBuilder;
import net.foxyas.changedaddon.datagen.builders.CatalyzerRecipeBuilder;
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
import net.ltxprogrammer.changed.datagen.builders.InfuserRecipeBuilder;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;
import java.util.function.Consumer;

import static net.foxyas.changedaddon.init.ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE;
import static net.foxyas.changedaddon.init.ChangedAddonItems.*;
import static net.foxyas.changedaddon.init.ChangedAddonTransfurVariants.EXPERIMENT_009;
import static net.foxyas.changedaddon.init.ChangedAddonTransfurVariants.EXPERIMENT_10;
import static net.minecraft.world.item.Items.*;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> out) {
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, LUMINAR_CRYSTAL_SPEAR.get())
                .pattern("SHS")
                .pattern("ITI")
                .pattern(" S ")
                .define('S', LUMINAR_CRYSTAL_SHARD.get())
                .define('H', LUMINAR_CRYSTAL_SHARD_HEARTED.get())
                .define('I', Ingredient.of(ItemTagsProvider.forgeIngotsIridium))
                .define('T', Items.STICK)
                .unlockedBy(getHasName(LUMINAR_CRYSTAL_SHARD.get()), has(LUMINAR_CRYSTAL_SHARD.get()))
                .save(out);

        String iridium = getHasName(IRIDIUM.get());
        CriterionTriggerInstance hasIridium = has(IRIDIUM.get());

        InventoryChangeTrigger.TriggerInstance hasLatexBase = has(ChangedItems.LATEX_BASE.get());
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ANTI_LATEX_BASE.get(), 2)
                .requires(AMMONIA_PARTICLE.get(), 2)
                .requires(LUMINARA_BASE.get(), 1)
                .requires(ChangedItems.LATEX_BASE.get())
                .unlockedBy("has_latex_base", hasLatexBase)
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, LUMINARA_BASE.get(), 1)
                .requires(LUMINARA_PETALS.get(), 2)
                .requires(ChangedItems.LATEX_BASE.get())
                .unlockedBy("has_latex_base", hasLatexBase)
                .save(out);

        ChangedAddonRecipeBuilder.unifuser(STRANGE_COMPOUND_BASE.get().getDefaultInstance())
                .requires(BLUE_LATEX_GOO.get(), 1)
                .requires(RED_LATEX_GOO.get(), 1)
                .requires(ChangedItems.LATEX_BASE.get())
                .unlockedBy("has_latex_base", hasLatexBase)
                .save(out);

        Ingredient flowersOrPlants = CompoundIngredient.of(
                Ingredient.of(ItemTags.FLOWERS),
                Ingredient.of(ChangedAddonTags.Items.PLANTS)
        );

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, IMPURE_AMMONIA.get(), 3)
                .requires(flowersOrPlants, 4) // Requer 4 do ingrediente composto
                .requires(Ingredient.of(ChangedAddonTags.Items.MEAT), 3)
                .requires(Tags.Items.SEEDS)
                .requires(Items.BONE_MEAL)
                .unlockedBy("has_bone_meal", has(Items.BONE_MEAL))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, IRIDIUM_BLOCK.get())
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .unlockedBy(iridium, hasIridium)
                .save(out);

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
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, REINFORCED_CROSS_BLOCK.get(), 32)
                .pattern("NIN")
                .pattern("ICI")
                .pattern("NIN")
                .define('N', Items.NETHERITE_SCRAP)
                .define('I', ItemTagsProvider.forgeIngotsIridium)
                .define('C', Tags.Items.INGOTS_COPPER)
                .unlockedBy(iridium, hasIridium)
                .save(out);

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
                .save(out);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RAW_IRIDIUM.get()),
                        RecipeCategory.MISC,
                        IRIDIUM.get(),
                        1.2f,
                        300)
                .unlockedBy("has_raw_iridium", has(RAW_IRIDIUM.get()))
                .save(out);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(DEEPSLATE_IRIDIUM_ORE.get()),
                        RecipeCategory.MISC,
                        IRIDIUM.get(),
                        25f,
                        400)
                .unlockedBy("has_iridium", has(DEEPSLATE_IRIDIUM_ORE.get()))
                .save(out, getItemName(IRIDIUM.get()) + "raw_to_ingot");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(DEEPSLATE_PAINITE_ORE.get()),
                        RecipeCategory.MISC,
                        PAINITE.get(),
                        20.0F,
                        100)
                .unlockedBy("has_painite", has(DEEPSLATE_PAINITE_ORE.get()))
                .save(out, getBlastingRecipeName(PAINITE.get()));

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(RAW_IRIDIUM.get()),
                        RecipeCategory.MISC,
                        IRIDIUM.get(),
                        1.2f,
                        100)
                .unlockedBy("has_raw_iridium", has(RAW_IRIDIUM.get()))
                .save(out, getItemName(IRIDIUM.get()) + "raw_to_ingot" + "_from_blasting");

        SimpleCookingRecipeBuilder.blasting(Ingredient.of(DEEPSLATE_IRIDIUM_ORE.get()),
                        RecipeCategory.MISC,
                        IRIDIUM.get(),
                        25f,
                        100)
                .unlockedBy("has_iridium", has(DEEPSLATE_IRIDIUM_ORE.get()))
                .save(out, getBlastingRecipeName(IRIDIUM.get()));

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(STRANGE_COMPOUND_BASE.get()),
                        Ingredient.of(TOTEM_OF_UNDYING),
                        Ingredient.of(ChangedAddonTags.Items.AIR),
                        RecipeCategory.TOOLS,
                        TRANSFUR_TOTEM.get()
                )
                .unlocks("has_items", hasItems(STRANGE_COMPOUND_BASE.get(), TOTEM_OF_UNDYING))
                .save(out, getItemName(TRANSFUR_TOTEM.get()) + "_smithing");

        reinforce(REINFORCED_WALL_CAUTION.get(), ChangedBlocks.WALL_CAUTION.get(), iridium, hasIridium)
                .save(out);

        reinforce(REINFORCED_WALL.get(), ChangedBlocks.WALL_WHITE.get(), iridium, hasIridium)
                .save(out);

        reinforce(REINFORCED_WALL_SILVER_TILED.get(), ChangedBlocks.WALL_BLUE_TILED.get(), iridium, hasIridium)
                .save(out);

        reinforce(REINFORCED_WALL_SILVER_STRIPED.get(), ChangedBlocks.WALL_BLUE_STRIPED.get(), iridium, hasIridium)
                .save(out);

        String lunarRose = getHasName(LUNAR_ROSE.get());
        CriterionTriggerInstance hasLunarRose = has(LUNAR_ROSE.get());

        ChangedAddonRecipeBuilder.generic(new ItemStack(LUNAR_ROSE.get()))
                .withSpeed(5)
                .withType(ChangedAddonRecipeTypes.UNIFUSER_RECIPE.get())
                .requires(DIAMOND).requires(TINTED_GLASS)
                .unlockedBy(lunarRose, hasLunarRose)
                .save(out, RecipeBuilder.getDefaultRecipeId(LUNAR_ROSE.get()) + "_secret");

        // Tipo 1: Usando o atalho estático da própria classe builder
        ItemStack white = LAETHIN.get().getDefaultInstance();
        StrictNBTIngredient stack = StrictNBTIngredient.of(white);
        ChangedAddonRecipeBuilder.unifuser(new ItemStack(ChangedAddonItems.LITIX_CAMMONIA.get(), 3))
                .requires(ChangedAddonItems.LITIX_CAMMONIA.get())
                .requires(stack)
                .requires(ChangedAddonTags.Items.AIR)
                .withSpeed(0.5f)
                .withExperience(2.0f)
                .unlockedBy("has_litix_cammonia", has(ChangedAddonItems.LITIX_CAMMONIA.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("changed_addon", "multi_litix_cammonia_recipe_white"));
        ItemStack black = LAETHIN.get().getDefaultInstance();
        LaethinItem.setLaethinTypeForStack(white, LaethinItem.Type.DARK_LATEX);
        stack = StrictNBTIngredient.of(black);
        ChangedAddonRecipeBuilder.unifuser(new ItemStack(ChangedAddonItems.LITIX_CAMMONIA.get(), 3))
                .requires(ChangedAddonItems.LITIX_CAMMONIA.get())
                .requires(stack)
                .requires(ChangedAddonTags.Items.AIR)
                .withSpeed(0.5f)
                .withExperience(2.0f)
                .unlockedBy("has_litix_cammonia", has(ChangedAddonItems.LITIX_CAMMONIA.get()))
                .save(out, ResourceLocation.fromNamespaceAndPath("changed_addon", "multi_litix_cammonia_recipe_dark"));

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
                .save(out);

        ItemStack safeSyringeStack = new ItemStack(ChangedItems.LATEX_SYRINGE.get());
        CompoundTag tag = safeSyringeStack.getOrCreateTag();
        tag.putBoolean("safe", true);
        ItemStackLoreUtil.addLore(safeSyringeStack, Component.literal("Purified Version. (Purifier)"));

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
                .save(out);


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
                .save(out);

        BasicCropRecipeProvider.buildRecipes(out);
        BasicSoilRecipeProvider.buildRecipes(out);

        CookingRecipes.register(out);
        CuttingRecipes.register(out);

        woodFromLogs(out, LUMINARA_WOOD.get(), LUMINARA_LOG.get());
        woodFromLogs(out, STRIPPED_LUMINARA_WOOD.get(), STRIPPED_LUMINARA_LOG.get());
        planksFromLog(out, LUMINARA_PLANKS.get(), ChangedAddonTags.Items.LUMINARA_LOGS, 4);
        String hasPlanksStr = getHasName(LUMINARA_PLANKS.get());
        var hasLumiPlanks = has(LUMINARA_PLANKS.get());
        stairBuilder(LUMINARA_STAIRS.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        slab(out, RecipeCategory.BUILDING_BLOCKS, LUMINARA_SLAB.get(), LUMINARA_PLANKS.get());
        doorBuilder(LUMINARA_DOOR.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        trapdoorBuilder(LUMINARA_TRAPDOOR.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        fenceBuilder(LUMINARA_FENCE.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        fenceGateBuilder(LUMINARA_FENCE_GATE.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        signBuilder(LUMINARA_SIGN.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        hangingSign(out, LUMINARA_HANGING_SIGN.get(), LUMINARA_PLANKS.get());
        buttonBuilder(LUMINARA_BUTTON.get(), Ingredient.of(LUMINARA_PLANKS.get()))
                .unlockedBy(hasPlanksStr, hasLumiPlanks)
                .save(out);
        pressurePlate(out, LUMINARA_PRESSURE_PLATE.get(), LUMINARA_PLANKS.get());


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
        
        // -----------------------------------------------------------------
        // Experiment 009 Recipe (Ported from form_experiment009.json)
        // -----------------------------------------------------------------
        InfuserRecipeBuilder.genderLess(EXPERIMENT_009.get()) // Replace with your variant reference if needed
                .requires(Items.ENCHANTED_GOLDEN_APPLE)
                .requires(Items.COPPER_BLOCK)
                .requires(Items.DIAMOND_BLOCK)
                .requires(Items.TOTEM_OF_UNDYING)
                .requires(BLUE_LATEX_GOO.get())
                .requires(ChangedItems.LATEX_BASE.get())
                .requires(ChangedItems.WHITE_LATEX_GOO.get())
                .requires(SWEET_BERRIES)
                .requires(GOO_CORE_FRAGMENT.get())
                .unlockedBy("has_latex_base", hasLatexBase)
                .save(out, ResourceLocation.fromNamespaceAndPath("changed_addon", "form_experiment_009"));

        // -----------------------------------------------------------------
        // Experiment 10 Recipe (Ported from form_experiment10.json)
        // -----------------------------------------------------------------
        InfuserRecipeBuilder.genderLess(EXPERIMENT_10.get()) // Replace with your variant reference if needed
                .requires(RED_LATEX_GOO.get())
                .requires(NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                .requires(NETHERRACK)
                .requires(ChangedItems.LATEX_BASE.get())
                .requires(ChangedItems.WHITE_LATEX_GOO.get())
                .requires(Items.STRING)
                .requires(GOO_CORE_FRAGMENT.get())
                .requires(Items.RED_DYE)
                .requires(Items.BLACK_DYE)
                .unlockedBy("has_latex_base", hasLatexBase)
                .save(out, ResourceLocation.fromNamespaceAndPath("changed_addon", "form_experiment_10"));

//        ConditionalRecipe.Builder hasPatchouliMod = ConditionalRecipe.builder().addCondition(this.modLoaded(PatchouliAPI.MOD_ID));
//        hasPatchouliMod.addRecipe(changedBookRecipe::save).build(out, changedBookRecipeID);

        CatalyzerRecipeBuilder.catalyzer(AMMONIA_PARTICLE.get(), 4)
                .requires(IMPURE_AMMONIA.get())
                .progressSpeed(2.5f).experience(1)
                .unlockedBy(getHasNameR(IMPURE_AMMONIA), hasR(IMPURE_AMMONIA))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AMMONIA_COMPRESSED.get())
                .pattern("PPP")
                .pattern("PBP")
                .pattern("PPP")
                .define('P', AMMONIA_PARTICLE.get())
                .define('B', BIOMASS.get())
                .unlockedBy(getHasNameR(AMMONIA_PARTICLE), hasR(AMMONIA_PARTICLE))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BIOMASS.get(), 4)
                .pattern("GMG")
                .pattern("MLM")
                .pattern("GMG")
                .define('G', ChangedAddonTags.Items.GOOEY)
                .define('M', ChangedAddonTags.Items.MEAT)
                .define('L', ChangedItems.LATEX_BASE.get())
                .unlockedBy(getHasNameR(ChangedItems.LATEX_BASE), hasR(ChangedItems.LATEX_BASE))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, OPENED_CANNED_SOUP.get())
                .requires(ChangedBlocks.CANNED_SOUP.get())
                .unlockedBy(getHasNameR(ChangedBlocks.CANNED_SOUP), hasR(ChangedBlocks.CANNED_SOUP))
                .save(out);

        CatalyzerRecipeBuilder.catalyzer(CATALYZED_DNA.get())
                .requires(ChangedAddonTags.Items.BLOOD_TYPE_SYRINGE)
                .progressSpeed(2.5f).nitrogenUsage(5).experience(1)
                .unlockedBy("has_" + ChangedAddonTags.Items.BLOOD_TYPE_SYRINGE.location().getPath(), has(ChangedAddonTags.Items.BLOOD_TYPE_SYRINGE))
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CATALYZER.get())
                .pattern("BGB")
                .pattern("ISI")
                .pattern("RLR")
                .define('B', ItemTags.STONE_BUTTONS)
                .define('G', Tags.Items.GLASS_PANES)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', ChangedAddonTags.Items.SYRINGES)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('L', Tags.Items.STORAGE_BLOCKS_IRON)
                .unlockedBy(getHasNameR(ChangedItems.SYRINGE), hasR(ChangedItems.SYRINGE))
                .save(out);
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
                .save(out);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, UNIFUSER.get())
                .pattern("SGR")
                .pattern("IBI")
                .pattern("TLP")
                .define('S', ChangedAddonTags.Items.SYRINGES)
                .define('G', Tags.Items.GLASS_PANES)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .define('T', REDSTONE_TORCH)
                .define('L', Tags.Items.STORAGE_BLOCKS_IRON)
                .define('P', REPEATER)
                .unlockedBy(getHasNameR(ChangedItems.SYRINGE), hasR(ChangedItems.SYRINGE))
                .save(out);
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
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BLUE_WOLF_CRYSTAL_BLOCK.get())
                .requires(ChangedBlocks.WOLF_CRYSTAL_BLOCK.get())
                .requires(Tags.Items.DYES_BLUE).unlockedBy(getHasNameR(ChangedBlocks.WOLF_CRYSTAL_BLOCK), hasR(ChangedBlocks.WOLF_CRYSTAL_BLOCK))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ORANGE_WOLF_CRYSTAL_BLOCK.get())
                .requires(ChangedBlocks.WOLF_CRYSTAL_BLOCK.get())
                .requires(Tags.Items.DYES_ORANGE).unlockedBy(getHasNameR(ChangedBlocks.WOLF_CRYSTAL_BLOCK), hasR(ChangedBlocks.WOLF_CRYSTAL_BLOCK))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, WHITE_WOLF_CRYSTAL_BLOCK.get())
                .requires(ChangedBlocks.WOLF_CRYSTAL_BLOCK.get())
                .requires(Tags.Items.DYES_WHITE).unlockedBy(getHasNameR(ChangedBlocks.WOLF_CRYSTAL_BLOCK), hasR(ChangedBlocks.WOLF_CRYSTAL_BLOCK))
                .save(out);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, YELLOW_WOLF_CRYSTAL_BLOCK.get())
                .requires(ChangedBlocks.WOLF_CRYSTAL_BLOCK.get())
                .requires(Tags.Items.DYES_YELLOW).unlockedBy(getHasNameR(ChangedBlocks.WOLF_CRYSTAL_BLOCK), hasR(ChangedBlocks.WOLF_CRYSTAL_BLOCK))
                .save(out);

        nineBlockStorageRecipesR(out, RecipeCategory.MISC, BLUE_WOLF_CRYSTAL_FRAGMENT, RecipeCategory.BUILDING_BLOCKS, BLUE_WOLF_CRYSTAL_BLOCK);
        nineBlockStorageRecipesR(out, RecipeCategory.MISC, ORANGE_WOLF_CRYSTAL_FRAGMENT, RecipeCategory.BUILDING_BLOCKS, ORANGE_WOLF_CRYSTAL_BLOCK);
        nineBlockStorageRecipesR(out, RecipeCategory.MISC, WHITE_WOLF_CRYSTAL_FRAGMENT, RecipeCategory.BUILDING_BLOCKS, WHITE_WOLF_CRYSTAL_BLOCK);
        nineBlockStorageRecipesR(out, RecipeCategory.MISC, YELLOW_WOLF_CRYSTAL_FRAGMENT, RecipeCategory.BUILDING_BLOCKS, YELLOW_WOLF_CRYSTAL_BLOCK);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, WOLF_PLUSHY.get())
                .pattern("PRP")
                .pattern("WWW")
                .pattern("PWP")
                .define('P', Tags.Items.DYES_PINK)
                .define('R', Tags.Items.DYES_RED)
                .define('W', WHITE_WOOL)
                .unlockedBy(getHasName(WHITE_WOOL), has(WHITE_WOOL))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DARK_LATEX_SPRAY.get())
                .requires(EMPTY_SPRAY.get())
                .requires(ChangedItems.DARK_LATEX_BUCKET.get())
                .requires(ChangedItems.LATEX_BASE.get())
                .unlockedBy(getHasNameR(EMPTY_SPRAY), hasR(EMPTY_SPRAY))
                .save(out);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, WHITE_LATEX_SPRAY.get())
                .requires(EMPTY_SPRAY.get())
                .requires(ChangedItems.WHITE_LATEX_BUCKET.get())
                .requires(ChangedItems.LATEX_BASE.get())
                .unlockedBy(getHasNameR(EMPTY_SPRAY), hasR(EMPTY_SPRAY))
                .save(out);

        stonecutterResultFromBase(out, RecipeCategory.DECORATIONS, ChangedBlocks.BEEHIVE_BED.get(), HONEYCOMB_BLOCK);
        stonecutterResultFromBase(out, RecipeCategory.DECORATIONS, ChangedBlocks.BEEHIVE_WALL.get(), HONEYCOMB_BLOCK, 4);

        saveRecipeWithConditionAndSpecificID(out, changedBookRecipe, changedBookRecipeID, this.modLoaded("patchouli"));
    }

    protected String getHasNameR(RegistryObject<? extends ItemLike> item) {
        return getHasName(item.get());
    }

    protected InventoryChangeTrigger.TriggerInstance hasR(RegistryObject<? extends ItemLike> item) {
        return has(item.get());
    }

    protected void nineBlockStorageRecipesR(Consumer<FinishedRecipe> out, RecipeCategory unpackingCategory, RegistryObject<? extends ItemLike> unpacked, RecipeCategory packingCategory, RegistryObject<? extends ItemLike> packed) {
        nineBlockStorageRecipes(out, unpackingCategory, unpacked.get(), packingCategory, packed.get());
    }

    protected static InventoryChangeTrigger.TriggerInstance hasItems(ItemLike... pItemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pItemLike).build());
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
