package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import static net.foxyas.changedaddon.init.ChangedAddonBlocks.*;
import static net.ltxprogrammer.changed.init.ChangedBlocks.WOLF_CRYSTAL_BLOCK;

public class BlockTagsProvider extends net.minecraft.data.tags.BlockTagsProvider {

    private static final TagKey<Block> forgeOresIridium = BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "ores/iridium"));
    private static final TagKey<Block> forgeStorageBlocksIridium = BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks/iridium"));

    public BlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, ChangedAddonMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.Blocks.ORES).add(IRIDIUM_ORE.get());
        tag(forgeOresIridium).add(IRIDIUM_ORE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).add(IRIDIUM_ORE.get());

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
                WHITE_WOLF_CRYSTAL_SMALL.get()
        );

        tag(ChangedTags.Blocks.GROWS_LATEX_CRYSTALS).add(WHITE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get(), BLUE_WOLF_CRYSTAL_BLOCK.get());

        tag(ChangedAddonTags.Blocks.DYEABLE_CRYSTAL).add(ChangedBlocks.WOLF_CRYSTAL_BLOCK.get(), BLUE_WOLF_CRYSTAL_BLOCK.get(), WHITE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get());
        tag(ChangedAddonTags.Blocks.LAB_BIG_DOORS).add(ChangedBlocks.LARGE_LIBRARY_DOOR.get(), ChangedBlocks.LARGE_LAB_DOOR.get(), ChangedBlocks.LARGE_MAINTENANCE_DOOR.get(), ChangedBlocks.LARGE_BLUE_LAB_DOOR.get());
        tag(ChangedAddonTags.Blocks.LAB_DOORS).add(ChangedBlocks.LIBRARY_DOOR.get(), ChangedBlocks.LAB_DOOR.get(), ChangedBlocks.MAINTENANCE_DOOR.get(), ChangedBlocks.BLUE_LAB_DOOR.get());
        tag(ChangedAddonTags.Blocks.PASSABLE_BLOCKS).addTags(BlockTags.FENCES, BlockTags.FENCE_GATES, BlockTags.TRAPDOORS).add(Blocks.ACACIA_DOOR, Blocks.IRON_BARS, ChangedBlocks.BLACK_RAILING.get());

        tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(DEEPSLATE_PAINITE_ORE.get(), PAINITE_BLOCK.get());

        tag(BlockTags.ICE).add(WHITE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get(), BLUE_WOLF_CRYSTAL_BLOCK.get(), LUMINAR_CRYSTAL_BLOCK.get(), WOLF_CRYSTAL_PILLAR.get());
        tag(BlockTags.BEACON_BASE_BLOCKS).add(PAINITE_BLOCK.get(), IRIDIUM_BLOCK.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(IRIDIUM_BLOCK.get(), IRIDIUM_ORE.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(IRIDIUM_BLOCK.get(), IRIDIUM_ORE.get(), PAINITE_BLOCK.get(), DEEPSLATE_PAINITE_ORE.get(),
                CATALYZER.get(), UNIFUSER.get(), SIGNAL_BLOCK.get(), INFORMANT_BLOCK.get(), CONTAINMENT_CONTAINER.get(),
                ADVANCED_UNIFUSER.get(), ADVANCED_CATALYZER.get(), REINFORCED_WALL.get(), REINFORCED_WALL_SILVER_STRIPED.get(),
                REINFORCED_WALL_SILVER_TILED.get(), REINFORCED_WALL_CAUTION.get(), REINFORCED_CROSS_BLOCK.get(), WALL_WHITE_CRACKED.get(),
                BLUE_WOLF_CRYSTAL_BLOCK.get(), ORANGE_WOLF_CRYSTAL_BLOCK.get(), YELLOW_WOLF_CRYSTAL_BLOCK.get(),
                WHITE_WOLF_CRYSTAL_BLOCK.get(), LUMINAR_CRYSTAL_BLOCK.get(), LUMINAR_CRYSTAL_SMALL.get(), LUMINAR_CRYSTAL_LARGE.get(), YELLOW_WOLF_CRYSTAL_SMALL.get(),
                BLUE_WOLF_CRYSTAL_SMALL.get(), ORANGE_WOLF_CRYSTAL_SMALL.get(), WHITE_WOLF_CRYSTAL_SMALL.get(), GOO_CORE.get(),
                GENERATOR.get(), WOLF_CRYSTAL_PILLAR.get());
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(DARK_LATEX_PUDDLE.get(), DORMANT_DARK_LATEX.get(), DORMANT_WHITE_LATEX.get());
    }
}
