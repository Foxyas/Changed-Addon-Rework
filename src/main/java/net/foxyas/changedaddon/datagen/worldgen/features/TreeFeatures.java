package net.foxyas.changedaddon.datagen.worldgen.features;

import net.foxyas.changedaddon.block.LuminaraLogBlock;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.foxyas.changedaddon.world.features.tree.TreeWithFlowersFeatureConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;

public class TreeFeatures {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> pContext) {
//        BlockState baseLog = ChangedAddonBlocks.LUMINARA_LOG.get().defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
//
//        BlockState inactiveLog = baseLog.setValue(LuminaraLogBlock.ACTIVE, false);
//        BlockState activeLog = baseLog.setValue(LuminaraLogBlock.ACTIVE, true);
//
//        BlockStateProvider trunkStateProvider = new WeightedStateProvider(
//                SimpleWeightedRandomList.<BlockState>builder()
//                        .add(inactiveLog, 3) // 3/4 = 75%
//                        .add(activeLog, 1)   // 1/4 = 25%
//                        .build()
//        );
//
//        FeatureUtils.register(pContext, LUMINARA_TREE, Feature.TREE,
//                new TreeConfiguration.TreeConfigurationBuilder(
//                        trunkStateProvider, // Tronco Dinâmico aplicado aqui
//                        new CherryTrunkPlacer(
//                                7, 1, 0,
//                                new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()),
//                                UniformInt.of(2, 4),
//                                UniformInt.of(-4, -3),
//                                UniformInt.of(-1, 0)
//                        ),
//                        BlockStateProvider.simple(ChangedAddonBlocks.LUMINARA_LEAVES.get()),
//                        new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F),
//                        new TwoLayersFeatureSize(1, 0, 2)
//                )
//                        .decorators(ImmutableList.of(
//                                // Adiciona flores ao redor da árvore (LUMINARA_BLOOM) no chão
//                                new FlowerTreeDecorator(0.25f, List.of(BlockStateProvider.simple(ChangedAddonBlocks.LUMINARA_BLOOM.get())))
//                        ))
//                        .ignoreVines()
//                        .build()
//        );

        BlockState baseLog = ChangedAddonBlocks.LUMINARA_LOG.get().defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Y);

        BlockState inactiveLog = baseLog.setValue(LuminaraLogBlock.ACTIVE, false);
        BlockState activeLog = baseLog.setValue(LuminaraLogBlock.ACTIVE, true);

        BlockStateProvider trunkStateProvider = new WeightedStateProvider(
                SimpleWeightedRandomList.<BlockState>builder()
                        .add(inactiveLog, 3) // 3/4 = 75%
                        .add(activeLog, 1)   // 1/4 = 25%
                        .build()
        );

        TreeConfiguration treeConfig = new TreeConfiguration.TreeConfigurationBuilder(
                trunkStateProvider,
                new CherryTrunkPlacer(
                        7, 1, 0,
                        new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder().add(ConstantInt.of(1), 1).add(ConstantInt.of(2), 1).add(ConstantInt.of(3), 1).build()),
                        UniformInt.of(2, 4),
                        UniformInt.of(-4, -3),
                        UniformInt.of(-1, 0)
                ),
                BlockStateProvider.simple(ChangedAddonBlocks.LUMINARA_LEAVES.get()),
                new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F),
                new TwoLayersFeatureSize(1, 0, 2)
        )
                .ignoreVines()
                .build();

        RandomPatchConfiguration flowerConfig = new RandomPatchConfiguration(
                32,
                3,
                1,
                PlacementUtils.onlyWhenEmpty(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ChangedAddonBlocks.LUMINARA_BLOOM.get()))
                )
        );

        FeatureUtils.register(pContext, ChangedAddonFeatures.Configured.LUMINARA_TREE,
                ChangedAddonFeatures.TREE_WITH_FLOWERS.get(),
                new TreeWithFlowersFeatureConfiguration(treeConfig, flowerConfig)
        );
    }
}