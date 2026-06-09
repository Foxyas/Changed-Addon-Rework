package net.foxyas.changedaddon.world.features.tree;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.Iterator;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.BiConsumer;

public class RawTreeWithFlowersFeature extends Feature<TreeWithFlowersFeatureConfiguration> {

    public RawTreeWithFlowersFeature(Codec<TreeWithFlowersFeatureConfiguration> codec) {
        super(codec);
    }

    private static boolean isVine(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (state) -> state.is(Blocks.VINE));
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (state) -> state.isAir() || state.is(BlockTags.LEAVES));
    }

    private static void setBlockKnownShape(LevelWriter pLevel, BlockPos pPos, BlockState pState) {
        pLevel.setBlock(pPos, pState, 19);
    }

    public static boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (state) -> state.isAir() || state.is(BlockTags.REPLACEABLE_BY_TREES)); 
    }

    private boolean doPlace(WorldGenLevel pLevel, RandomSource pRandom, BlockPos pPos, BiConsumer<BlockPos, BlockState> pRootBlockSetter, BiConsumer<BlockPos, BlockState> pTrunkBlockSetter, FoliagePlacer.FoliageSetter pFoliageBlockSetter, TreeConfiguration pConfig) {
        int i = pConfig.trunkPlacer.getTreeHeight(pRandom); 
        int j = pConfig.foliagePlacer.foliageHeight(pRandom, i, pConfig); 
        int k = i - j; 
        int l = pConfig.foliagePlacer.foliageRadius(pRandom, k); 
        BlockPos blockpos = pConfig.rootPlacer.map((placer) -> placer.getTrunkOrigin(pPos, pRandom)).orElse(pPos); 
        int i1 = Math.min(pPos.getY(), blockpos.getY()); 
        int j1 = Math.max(pPos.getY(), blockpos.getY()) + i + 1; 

        if (i1 >= pLevel.getMinBuildHeight() + 1 && j1 <= pLevel.getMaxBuildHeight()) { 
            OptionalInt optionalint = pConfig.minimumSize.minClippedHeight(); 
            int k1 = this.getMaxFreeTreeHeight(pLevel, i, blockpos, pConfig); 

            if (k1 >= i || !optionalint.isEmpty() && k1 >= optionalint.getAsInt()) { 
                if (pConfig.rootPlacer.isPresent() && !pConfig.rootPlacer.get().placeRoots(pLevel, pRootBlockSetter, pRandom, pPos, blockpos, pConfig)) { 
                    return false; 
                } else {
                    List<FoliagePlacer.FoliageAttachment> list = pConfig.trunkPlacer.placeTrunk(pLevel, pTrunkBlockSetter, pRandom, k1, blockpos, pConfig); 
                    list.forEach((attachment) -> { 
                        pConfig.foliagePlacer.createFoliage(pLevel, pFoliageBlockSetter, pRandom, pConfig, k1, attachment, j, l); 
                    });
                    return true; 
                }
            } else {
                return false; 
            }
        } else {
            return false; 
        }
    }

    private int getMaxFreeTreeHeight(LevelSimulatedReader pLevel, int pTrunkHeight, BlockPos pTopPosition, TreeConfiguration pConfig) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(); 
        for(int i = 0; i <= pTrunkHeight + 1; ++i) { 
            int j = pConfig.minimumSize.getSizeAtHeight(pTrunkHeight, i); 
            for(int k = -j; k <= j; ++k) { 
                for(int l = -j; l <= j; ++l) { 
                    blockpos$mutableblockpos.setWithOffset(pTopPosition, k, i, l); 
                    if (!pConfig.trunkPlacer.isFree(pLevel, blockpos$mutableblockpos) || !pConfig.ignoreVines && isVine(pLevel, blockpos$mutableblockpos)) { 
                        return i - 2; 
                    }
                }
            }
        }
        return pTrunkHeight; 
    }

    @Override
    public final boolean place(FeaturePlaceContext<TreeWithFlowersFeatureConfiguration> pContext) {
        TreeWithFlowersFeatureConfiguration featureConfig = pContext.config();
        TreeConfiguration treeconfiguration = featureConfig.treeConfig();

        final WorldGenLevel worldgenlevel = pContext.level(); 
        RandomSource randomsource = pContext.random(); 
        BlockPos blockpos = pContext.origin(); 

        Set<BlockPos> set = Sets.newHashSet(); 
        Set<BlockPos> set1 = Sets.newHashSet(); 
        final Set<BlockPos> set2 = Sets.newHashSet(); 
        Set<BlockPos> set3 = Sets.newHashSet(); 

        BiConsumer<BlockPos, BlockState> biconsumer = (pos, state) -> { 
            set.add(pos.immutable()); 
            worldgenlevel.setBlock(pos, state, 19); 
        };
        BiConsumer<BlockPos, BlockState> biconsumer1 = (pos, state) -> { 
            set1.add(pos.immutable()); 
            worldgenlevel.setBlock(pos, state, 19); 
        };
        FoliagePlacer.FoliageSetter foliageplacer$foliagesetter = new FoliagePlacer.FoliageSetter() {
            public void set(BlockPos p_272825_, BlockState p_273311_) {
                set2.add(p_272825_.immutable()); 
                worldgenlevel.setBlock(p_272825_, p_273311_, 19); 
            }
            public boolean isSet(BlockPos p_272999_) {
                return set2.contains(p_272999_); 
            }
        };
        BiConsumer<BlockPos, BlockState> biconsumer2 = (pos, state) -> { 
            set3.add(pos.immutable()); 
            worldgenlevel.setBlock(pos, state, 19); 
        };

        // 1. Executa a geração manual da estrutura da árvore
        boolean flag = this.doPlace(worldgenlevel, randomsource, blockpos, biconsumer, biconsumer1, foliageplacer$foliagesetter, treeconfiguration); 

        if (flag && (!set1.isEmpty() || !set2.isEmpty())) { 
            if (!treeconfiguration.decorators.isEmpty()) { 
                TreeDecorator.Context treedecorator$context = new TreeDecorator.Context(worldgenlevel, biconsumer2, randomsource, set1, set2, set); 
                treeconfiguration.decorators.forEach((decorator) -> decorator.place(treedecorator$context));
            }

            // 2. A ÁRVORE GEROU COM SUCESSO: Agora injetamos o loop manual de flores ao redor da base!
            RandomPatchConfiguration flowerConfig = featureConfig.flowerConfig();
            BlockPos.MutableBlockPos flowerMutablePos = new BlockPos.MutableBlockPos(); 
            int xzSpreadRange = flowerConfig.xzSpread() + 1; 
            int ySpreadRange = flowerConfig.ySpread() + 1; 

            for (int l = 0; l < flowerConfig.tries(); ++l) { 
                flowerMutablePos.setWithOffset(
                        blockpos,
                        randomsource.nextInt(xzSpreadRange) - randomsource.nextInt(xzSpreadRange),
                        randomsource.nextInt(ySpreadRange) - randomsource.nextInt(ySpreadRange),
                        randomsource.nextInt(xzSpreadRange) - randomsource.nextInt(xzSpreadRange)
                ); 

                // Invoca o posicionamento seguro da PlacedFeature da flor no bloco selecionado
                flowerConfig.feature().value().place(worldgenlevel, pContext.chunkGenerator(), randomsource, flowerMutablePos);
            }

            // Finaliza atualizando as folhas (Physics de distância e estados) do ecossistema do mundo
            return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2, set3)).map((box) -> { 
                DiscreteVoxelShape discretevoxelshape = updateLeaves(worldgenlevel, box, set1, set3, set); 
                StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, box.minX(), box.minY(), box.minZ()); 
                return true;
            }).orElse(false); 
        } else {
            return false;
        }
    }

    private static DiscreteVoxelShape updateLeaves(LevelAccessor pLevel, BoundingBox pBox, Set<BlockPos> pRootPositions, Set<BlockPos> pTrunkPositions, Set<BlockPos> pFoliagePositions) {
        DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(pBox.getXSpan(), pBox.getYSpan(), pBox.getZSpan()); 
        List<Set<BlockPos>> list = Lists.newArrayList(); 
        for(int j = 0; j < 7; ++j) { 
            list.add(Sets.newHashSet()); 
        }
        for(BlockPos blockpos : Lists.newArrayList(Sets.union(pTrunkPositions, pFoliagePositions))) { 
            if (pBox.isInside(blockpos)) { 
                discretevoxelshape.fill(blockpos.getX() - pBox.minX(), blockpos.getY() - pBox.minY(), blockpos.getZ() - pBox.minZ()); 
            }
        }
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(); 
        int k1 = 0; 
        list.get(0).addAll(pRootPositions); 
        while(true) {
            while(k1 >= 7 || !list.get(k1).isEmpty()) { 
                if (k1 >= 7) { 
                    return discretevoxelshape; 
                }
                Iterator<BlockPos> iterator = list.get(k1).iterator(); 
                BlockPos blockpos1 = iterator.next(); 
                iterator.remove(); 
                if (pBox.isInside(blockpos1)) { 
                    if (k1 != 0) { 
                        BlockState blockstate = pLevel.getBlockState(blockpos1); 
                        setBlockKnownShape(pLevel, blockpos1, blockstate.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(k1))); 
                    }
                    discretevoxelshape.fill(blockpos1.getX() - pBox.minX(), blockpos1.getY() - pBox.minY(), blockpos1.getZ() - pBox.minZ()); 
                    for(Direction direction : Direction.values()) { 
                        blockpos$mutableblockpos.setWithOffset(blockpos1, direction); 
                        if (pBox.isInside(blockpos$mutableblockpos)) { 
                            int k = blockpos$mutableblockpos.getX() - pBox.minX(); 
                            int l = blockpos$mutableblockpos.getY() - pBox.minY(); 
                            int i1 = blockpos$mutableblockpos.getZ() - pBox.minZ(); 
                            if (!discretevoxelshape.isFull(k, l, i1)) { 
                                BlockState blockstate1 = pLevel.getBlockState(blockpos$mutableblockpos); 
                                OptionalInt optionalint = LeavesBlock.getOptionalDistanceAt(blockstate1); 
                                if (!optionalint.isEmpty()) { 
                                    int j1 = Math.min(optionalint.getAsInt(), k1 + 1); 
                                    if (j1 < 7) {
                                        list.get(j1).add(blockpos$mutableblockpos.immutable());
                                        k1 = Math.min(k1, j1); 
                                    }
                                }
                            }
                        }
                    }
                }
            }
            ++k1;
        }
    }
}