package net.foxyas.changedaddon.datagen.recipes.farmers_delight;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.Consumer;

public class CuttingRecipes {
    public static Ingredient KNIVES = matchesTool(KnifeItem.KNIFE_DIG, CommonTags.Items.TOOLS_KNIVES);
    public static Ingredient PICKAXES = matchesTool(ToolActions.PICKAXE_DIG, ItemTags.PICKAXES);
    public static Ingredient AXES = matchesTool(ToolActions.AXE_DIG, ItemTags.AXES);
    public static Ingredient AXES_STRIP = matchesTool(ToolActions.AXE_STRIP, ItemTags.AXES);
    public static Ingredient SHOVELS = matchesTool(ToolActions.SHOVEL_DIG, ItemTags.SHOVELS);
    public static Ingredient HOES = matchesTool(ToolActions.HOE_DIG, ItemTags.HOES);
    public static Ingredient SHEARS = matchesTool(ToolActions.SHEARS_DIG, Tags.Items.SHEARS);

    public static void register(Consumer<FinishedRecipe> consumer) {
        // Knife
        cuttingFlowers(consumer);

        // Axe
        strippingWood(consumer);
        salvagingWoodenFurniture(consumer);

        // Shears
        salvagingUsingShears(consumer);
    }

    private static void cuttingFlowers(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ChangedAddonItems.LUMINARA_BLOOM.get()),
                        KNIVES,
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        4)
                .save(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(
                        Ingredient.of(ChangedAddonItems.LUMINARA_SAPLING.get()), KNIVES, ChangedAddonItems.LUMINARA_BLOOM.get(),
                        4
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.5f,
                        1
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.5f,
                        1
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.5f,
                        1
                )
                .save(consumer);
    }

    private static void strippingWood(Consumer<FinishedRecipe> consumer) {
        stripLogForBark(consumer,
                ChangedAddonItems.LUMINARA_LOG.get(),
                ChangedAddonItems.STRIPPED_LUMINARA_LOG.get());
        stripLogForBark(consumer,
                ChangedAddonItems.LUMINARA_WOOD.get(),
                ChangedAddonItems.STRIPPED_LUMINARA_WOOD.get());
    }

    private static void salvagingWoodenFurniture(Consumer<FinishedRecipe> consumer) {
        salvagePlankFromFurniture(consumer,
                ResourceLocation.parse(ChangedAddonBlocks.LUMINARA_WOOD_TYPE.name()).getPath(),
                // Planks
                ChangedAddonBlocks.LUMINARA_PLANKS.get(),

                // Wooded furniture items
                ChangedAddonBlocks.LUMINARA_DOOR.get(),
                ChangedAddonBlocks.LUMINARA_TRAPDOOR.get(),
                ChangedAddonBlocks.LUMINARA_SIGN.get(),
                ChangedAddonBlocks.LUMINARA_HANGING_SIGN.get(),
                ChangedAddonBlocks.LUMINARA_FENCE.get(),
                ChangedAddonBlocks.LUMINARA_FENCE_GATE.get(),
                ChangedAddonBlocks.LUMINARA_PRESSURE_PLATE.get(),
                ChangedAddonBlocks.LUMINARA_BUTTON.get()
        );
    }

    private static void salvagingUsingShears(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ChangedAddonItems.LUMINARA_BLOOM.get()),
                        SHEARS,
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        4)
                .save(consumer, salvagingRecipe("luminara_bloom"));
        CuttingBoardRecipeBuilder.cuttingRecipe(
                        Ingredient.of(ChangedAddonItems.LUMINARA_SAPLING.get()),
                        SHEARS,
                        ChangedAddonItems.LUMINARA_BLOOM.get(),
                        4
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.85f,
                        1
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.65f,
                        1
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.35f,
                        1
                )
                .save(consumer, salvagingRecipe("luminara_sapling"));

        CuttingBoardRecipeBuilder.cuttingRecipe(
                        Ingredient.of(ChangedAddonItems.LUMINARA_LEAVES.get()),
                        SHEARS,
                        ChangedAddonItems.LUMINARA_BLOOM.get(),
                        8,
                        0.9f
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_SAPLING.get(),
                        0.9f,
                        1
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM.get(),
                        0.85f,
                        4
                )
                .addResultWithChance(
                        ChangedAddonItems.LUMINARA_BLOOM_PETALS.get(),
                        0.25f,
                        12
                )
                .save(consumer, salvagingRecipe("luminara_leaves"));
    }

    /**
     * Generates an axe-cutting recipe for wooded furniture items, with a chance to recover one plank of the given type.
     */
    private static void salvagePlankFromFurniture(Consumer<FinishedRecipe> consumer, WoodType woodType, ItemLike plank, ItemLike... furniture) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(furniture), AXES, plank, 1, 0.75F)
                .save(consumer, salvagingRecipe(woodType.name() + "_furniture"));
    }

    /**
     * Generates an axe-cutting recipe for wooded furniture items, with a chance to recover one plank of the given type.
     */
    private static void salvagePlankFromFurniture(Consumer<FinishedRecipe> consumer, String woodTypeId, ItemLike plank, ItemLike... furniture) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(furniture), AXES, plank, 1, 0.75F)
                .save(consumer, salvagingRecipe(woodTypeId + "_furniture"));
    }

    /**
     * Generates an axe-stripping recipe for the pair of given logs, with custom sound and a Tree Bark result attached.
     */
    private static void stripLogForBark(Consumer<FinishedRecipe> consumer, ItemLike log, ItemLike strippedLog) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), AXES_STRIP, strippedLog)
                .addResult(ModItems.TREE_BARK.get())
                .addSound(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP).toString())
                .save(consumer);
    }

    /**
     * Generates an axe-stripping recipe for the pair of given logs, with custom sound and a Tree Bark result attached.
     */
    private static void stripLogForBarkFD(Consumer<FinishedRecipe> consumer, ItemLike log, ItemLike strippedLog) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(log), AXES_STRIP, strippedLog)
                .addResult(ModItems.TREE_BARK.get())
                .addSound(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.AXE_STRIP).toString())
                .saveToFD(consumer);
    }

    private static Ingredient matchesTool(ToolAction toolAction, TagKey<Item> fallbackTag) {
        return CompoundIngredient.of(new ToolActionIngredient(toolAction), Ingredient.of(fallbackTag));
    }

    private static ResourceLocation salvagingRecipe(String name) {
        return ChangedAddonMod.resourceLoc("salvaging/" + name);
    }
}