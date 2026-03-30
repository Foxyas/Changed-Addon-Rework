package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonFeatures.Configurations.*;

public class ConfiguredFeatureProvider {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        // IRIDIUM
        // context.register(ChangedAddonFeatures.IRIDIUM_ORE_CONFIG, IridiumoreFeature.CONFIGURED_FEATURE.get());
        // PAINITE
        // context.register(ChangedAddonFeatures.PAINITE_ORE_CONFIG, PainiteOreFeature.CONFIGURED_FEATURE.get());

//        context.register(
//                ChangedAddonFeatures.PAINITE_ORE_CONFIG,
//                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
//                        List.of(
//                                OreConfiguration.target(
//                                        PainiteOreFeature.PainiteOreFeatureRuleTest.INSTANCE,
//                                        ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE.get().defaultBlockState()
//                                )
//                        ),
//                        6
//                ))
//        );
//
//        context.register(
//                ChangedAddonFeatures.IRIDIUM_ORE_CONFIG,
//                new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(
//                        List.of(
//                                OreConfiguration.target(
//                                        IridiumoreFeature.IridiumoreFeatureRuleTest.INSTANCE,
//                                        ChangedAddonBlocks.DEEPSLATE_PAINITE_ORE.get().defaultBlockState()
//                                )
//                        ),
//                        6
//                ))
//        );

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
        register(context, PAINITE_ORE_CONFIG,
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

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(@NotNull BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}