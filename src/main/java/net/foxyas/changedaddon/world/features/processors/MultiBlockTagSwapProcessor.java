package net.foxyas.changedaddon.world.features.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MultiBlockTagSwapProcessor extends StructureProcessor {

    /**
     * Codec da entrada individual
     */
    public static final Codec<Swap> SWAP_CODEC = RecordCodecBuilder.create(instance -> instance.group(BlockState.CODEC.optionalFieldOf("from_block").forGetter(s -> s.fromBlock), ResourceLocation.CODEC.optionalFieldOf("from_tag").forGetter(s -> s.fromTag.map(TagKey::location)), BlockState.CODEC.fieldOf("to").forGetter(s -> s.to)).apply(instance, Swap::new));
    /**
     * Codec da lista de swaps
     */
    public static final Codec<MultiBlockTagSwapProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(SWAP_CODEC.listOf().fieldOf("swaps").forGetter(p -> p.swaps)).apply(instance, MultiBlockTagSwapProcessor::new));
    private final List<Swap> swaps;

    public MultiBlockTagSwapProcessor(List<Swap> swaps) {
        this.swaps = swaps;
    }

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo original, StructureTemplate.@NotNull StructureBlockInfo current, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        BlockState state = current.state();
        for (Swap swap : swaps) {

            // Match exato por bloco
            StructureTemplate.StructureBlockInfo structureBlockInfo = new StructureTemplate.StructureBlockInfo(current.pos(), swap.to, current.nbt());
            if (swap.fromBlock.isPresent() && state.is(swap.fromBlock.get().getBlock())) {
                return structureBlockInfo;
            }

            // Match por tag
            if (swap.fromTag.isPresent() && state.is(swap.fromTag.get())) {
                return structureBlockInfo;
            }
        }

        return super.process(levelReader, pos, pivot, original, current, settings, template);
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return ChangedAddonProcessors.MULTI_BLOCK_SWAP.get();
    }

    /**
     * Um único item de troca
     */
    public static class Swap {
        public final Optional<BlockState> fromBlock;
        public final Optional<TagKey<Block>> fromTag;
        public final BlockState to;

        public Swap(Optional<BlockState> fromBlock, Optional<ResourceLocation> fromTag, BlockState to) {
            this.fromBlock = fromBlock;
            this.to = to;

            this.fromTag = fromTag.map(resourceLocation -> TagKey.create(ForgeRegistries.BLOCKS.getRegistryKey(), resourceLocation));
        }

        public Swap(Optional<BlockState> fromBlock, TagKey<Block> fromTag, BlockState to) {
            this.fromBlock = fromBlock;
            this.to = to;

            this.fromTag = Optional.of(fromTag);
        }
    }
}