package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.datagen.worldgen.features.OreFeatures;
import net.foxyas.changedaddon.datagen.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import static net.minecraft.data.worldgen.features.FeatureUtils.register;

public class ConfiguredFeatureProvider {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        OreFeatures.bootstrap(context);
        TreeFeatures.bootstrap(context);
    }
}