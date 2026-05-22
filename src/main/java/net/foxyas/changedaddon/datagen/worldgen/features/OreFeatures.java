package net.foxyas.changedaddon.datagen.worldgen.features;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonFeatures.ConfiguredFeatures.*;
import static net.foxyas.changedaddon.init.ChangedAddonFeatures.ConfiguredFeatures.IRIDIUM_ORE_BURIED;
import static net.minecraft.data.worldgen.features.FeatureUtils.register;

public class OreFeatures {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // RULE TESTS
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // TARGET BLOCKSTATE LIST
        List<OreConfiguration.TargetBlockState> iridiumTargets = List.of(
                OreConfiguration.target(
                        deepslateReplaceables,
                        ChangedAddonBlocks.DEEPSLATE_IRIDIUM_ORE.get().defaultBlockState()
                )
        );

        List<OreConfiguration.TargetBlockState> painiteTargets = List.of(
                OreConfiguration.target(
                        deepslateReplaceables,
                        ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE.get().defaultBlockState()
                ),
                OreConfiguration.target(
                        deepslateReplaceables,
                        ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE.get().defaultBlockState()
                )
        );
        register(context, PAINITE_ORE_BURIED,
                ChangedAddonFeatures.PAINITE_ORE.get(),
                new OreConfiguration(painiteTargets, 6, 1)
        );

        // REGISTRA TODAS AS CONFIGURED FEATURES
        register(context, IRIDIUM_ORE_SMALL,
                Feature.ORE,
                new OreConfiguration(iridiumTargets, 4, 0.5f)
        );

        register(context, IRIDIUM_ORE_LARGE,
                Feature.ORE,
                new OreConfiguration(iridiumTargets, 6, 0.7f)
        );

        register(context, IRIDIUM_ORE_BURIED,
                Feature.ORE,
                new OreConfiguration(iridiumTargets, 8, 1.0f)
        );
    }
}
