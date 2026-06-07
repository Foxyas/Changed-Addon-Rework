package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.datagen.worldgen.features.OreFeatures;
import net.foxyas.changedaddon.datagen.worldgen.features.TreeFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ConfiguredFeatureProvider {

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        OreFeatures.bootstrap(context);
        TreeFeatures.bootstrap(context);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(ResourceLocation key) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, key);
    }
}