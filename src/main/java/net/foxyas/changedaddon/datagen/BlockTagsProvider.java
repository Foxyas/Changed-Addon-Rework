package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.foxyas.changedaddon.init.ChangedAddonBlocks.*;
import static net.ltxprogrammer.changed.init.ChangedBlocks.WOLF_CRYSTAL_BLOCK;

public class BlockTagsProvider extends net.minecraftforge.common.data.BlockTagsProvider {

    private static final TagKey<Block> forgeOresIridium = BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "ores/iridium"));
    private static final TagKey<Block> forgeStorageBlocksIridium = BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/iridium"));

    public BlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        tag(BlockTags.FLOWER_POTS).add(POTTED_LUMINARA_SAPLING.get(), POTTED_LUMINARA_BLOOM.get());
        tag(BlockTags.LEAVES).add(LUMINARA_LEAVES.get());
        tag(BlockTags.SAPLINGS).add(LUMINARA_SAPLING.get());
        tag(BlockTags.MINEABLE_WITH_HOE).add(LUMINARA_LEAVES.get());
        tag(BlockTags.OVERWORLD_NATURAL_LOGS).add(LUMINARA_LOG.get(), STRIPPED_LUMINARA_LOG.get());
        tag(BlockTags.LOGS_THAT_BURN).add(LUMINARA_LOG.get(), STRIPPED_LUMINARA_LOG.get(), LUMINARA_WOOD.get(), STRIPPED_LUMINARA_WOOD.get());
        tag(BlockTags.PLANKS).add(LUMINARA_PLANKS.get());
        tag(BlockTags.STAIRS).add(LUMINARA_STAIRS.get());
        tag(BlockTags.SLABS).add(LUMINARA_SLAB.get());
        tag(BlockTags.WOODEN_DOORS).add(LUMINARA_DOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(LUMINARA_TRAPDOOR.get());
        tag(BlockTags.WOODEN_FENCES).add(LUMINARA_FENCE.get());
        tag(BlockTags.FENCE_GATES).add(LUMINARA_FENCE_GATE.get());
        tag(Tags.Blocks.FENCE_GATES_WOODEN).add(LUMINARA_FENCE_GATE.get());
        tag(BlockTags.SIGNS).add(LUMINARA_SIGN.get());
        tag(BlockTags.WALL_SIGNS).add(LUMINARA_WALL_SIGN.get());
        tag(BlockTags.CEILING_HANGING_SIGNS).add(LUMINARA_HANGING_SIGN.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(LUMINARA_WALL_HANGING_SIGN.get());
        tag(BlockTags.WOODEN_BUTTONS).add(LUMINARA_BUTTON.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(LUMINARA_PRESSURE_PLATE.get());

        tag(Tags.Blocks.ORES).add(DEEPSLATE_IRIDIUM_ORE.get());
        tag(forgeOresIridium).add(DEEPSLATE_IRIDIUM_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(DEEPSLATE_IRIDIUM_ORE.get());

        tag(Tags.Blocks.STORAGE_BLOCKS).add(IRIDIUM_BLOCK.get());
        tag(forgeStorageBlocksIridium).add(IRIDIUM_BLOCK.get());
        tag(ChangedTags.Blocks.CRYSTALLINE).add(
                LUMINAR_CRYSTAL_LARGE.get(),
                LUMINAR_CRYSTAL_SMALL.get(),
                WOLF_CRYSTAL_BLOCK.get(),
                BLUE_WOLF_CRYSTAL_BLOCK.get(),
                WHITE_WOLF_CRYSTAL_BLOCK.get(),
                ORANGE_WOLF_CRYSTAL_BLOCK.get(),
                YELLOW_WOLF_CRYSTAL_BLOCK.get(),
                YELLOW_WOLF_CRYSTAL_SMALL.get(),
                ORANGE_WOLF_CRYSTAL_SMALL.get(),
                BLUE_WOLF_CRYSTAL_SMALL.get(),
                WHITE_WOLF_CRYSTAL_SMALL.get(),
                LUMINARA_BLOOM.get(),
                POTTED_LUMINARA_BLOOM.get(),
                LUMINARA_SAPLING.get(),
                POTTED_LUMINARA_SAPLING.get()
        );


        tag(ChangedTags.Blocks.GROWS_LATEX_CRYSTALS).add(WHITE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get(), BLUE_WOLF_CRYSTAL_BLOCK.get());

        tag(ChangedAddonTags.Blocks.DYEABLE_CRYSTAL).add(WOLF_CRYSTAL_BLOCK.get(), BLUE_WOLF_CRYSTAL_BLOCK.get(), WHITE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get());
        tag(ChangedAddonTags.Blocks.LAB_BIG_DOORS).add(ChangedBlocks.LARGE_LIBRARY_DOOR.get(), ChangedBlocks.LARGE_LAB_DOOR.get(), ChangedBlocks.LARGE_MAINTENANCE_DOOR.get(), ChangedBlocks.LARGE_BLUE_LAB_DOOR.get());
        tag(ChangedAddonTags.Blocks.LAB_DOORS).add(ChangedBlocks.LIBRARY_DOOR.get(), ChangedBlocks.LAB_DOOR.get(), ChangedBlocks.MAINTENANCE_DOOR.get(), ChangedBlocks.BLUE_LAB_DOOR.get());
        tag(ChangedAddonTags.Blocks.PASSABLE_BLOCKS).addTags(BlockTags.FENCES, BlockTags.FENCE_GATES, BlockTags.TRAPDOORS).add(Blocks.ACACIA_DOOR, Blocks.IRON_BARS, ChangedBlocks.BLACK_RAILING.get());
        tag(ChangedAddonTags.Blocks.DORMANT_LATEX_BLOCKS).add(DORMANT_DARK_LATEX.get()).add(DORMANT_WHITE_LATEX.get());
        tag(ChangedAddonTags.Blocks.CAN_LUMINAR_CRYSTAL_SURVIVE).add(LUMINAR_CRYSTAL_BLOCK.get()).add(Blocks.STONE).add(Blocks.CRYING_OBSIDIAN).add(Blocks.AMETHYST_BLOCK).add(Blocks.AMETHYST_CLUSTER).add(Blocks.SEA_LANTERN).add(Blocks.CALCITE).addTag(ChangedAddonTags.Blocks.DORMANT_LATEX_BLOCKS).addTag(BlockTags.ICE).addTag(Tags.Blocks.STORAGE_BLOCKS_QUARTZ);
        tag(ChangedAddonTags.Blocks.CAN_SPAWN_LUMINARCTIC_LEOPARDS_ON_CRYSTAL_BREAK).add(LUMINAR_CRYSTAL_BLOCK.get());
        tag(ChangedAddonTags.Blocks.CONDUCTIVE)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_IRON)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_COPPER)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_RAW_GOLD)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_IRON)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_COPPER)
                .addTag(Tags.Blocks.STORAGE_BLOCKS_GOLD)
                .addTag(BlockTags.IRON_ORES)
                .addTag(BlockTags.GOLD_ORES)
                .addTag(BlockTags.COPPER_ORES)
                .add(Blocks.LIGHTNING_ROD)
        ;

        tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(DEEPSLATE_PAINITE_ORE.get(), PAINITE_BLOCK.get());

        tag(BlockTags.ICE).add(WHITE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get(), BLUE_WOLF_CRYSTAL_BLOCK.get(), LUMINAR_CRYSTAL_BLOCK.get(), WOLF_CRYSTAL_PILLAR.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(PAINITE_BLOCK.get(), IRIDIUM_BLOCK.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(IRIDIUM_BLOCK.get(), DEEPSLATE_IRIDIUM_ORE.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IRIDIUM_BLOCK.get(), DEEPSLATE_IRIDIUM_ORE.get(), PAINITE_BLOCK.get(), DEEPSLATE_PAINITE_ORE.get(),
                CATALYZER.get(), UNIFUSER.get(), SIGNAL_BLOCK.get(), INFORMANT_BLOCK.get(), CONTAINMENT_CONTAINER.get(),
                ADVANCED_UNIFUSER.get(), ADVANCED_CATALYZER.get(), REINFORCED_WALL.get(), REINFORCED_WALL_SILVER_STRIPED.get(),
                REINFORCED_WALL_SILVER_TILED.get(), REINFORCED_WALL_CAUTION.get(), REINFORCED_CROSS_BLOCK.get(), WALL_WHITE_CRACKED.get(),
                BLUE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get(),
                WHITE_WOLF_CRYSTAL_BLOCK.get(), LUMINAR_CRYSTAL_BLOCK.get(), LUMINAR_CRYSTAL_SMALL.get(), LUMINAR_CRYSTAL_LARGE.get(),
                YELLOW_WOLF_CRYSTAL_SMALL.get(), BLUE_WOLF_CRYSTAL_SMALL.get(), ORANGE_WOLF_CRYSTAL_SMALL.get(),
                WHITE_WOLF_CRYSTAL_SMALL.get(), GOO_CORE.get(), WOLF_CRYSTAL_PILLAR.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(DARK_LATEX_PUDDLE.get(), DORMANT_DARK_LATEX.get(), DORMANT_WHITE_LATEX.get());
    }
}
