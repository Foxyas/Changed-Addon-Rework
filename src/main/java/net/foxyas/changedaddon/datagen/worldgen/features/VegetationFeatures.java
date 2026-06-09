package net.foxyas.changedaddon.datagen.worldgen.features;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.worldgen.ConfiguredFeatureProvider;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class VegetationFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LUMINARA_FLOWERS =
            ConfiguredFeatureProvider.createKey(ChangedAddonMod.resourceLoc("luminara_flowers"));

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> pContext) {
        FeatureUtils.register(pContext, PATCH_LUMINARA_FLOWERS, Feature.FLOWER,
                new RandomPatchConfiguration(
                        96, // Tentativas (Tries)
                        6,  // Espalhamento horizontal (XZ Spread)
                        2,  // Espalhamento vertical (Y Spread)
                        // Usa a verificação padrão de bloco vazio do Minecraft
                        PlacementUtils.onlyWhenEmpty(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(BlockStateProvider.simple(ChangedAddonBlocks.LUMINARA_BLOOM.get()))
                        )
                )
        );
    }
}
