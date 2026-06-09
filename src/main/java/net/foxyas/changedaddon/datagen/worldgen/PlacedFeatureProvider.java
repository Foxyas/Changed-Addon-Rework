package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.datagen.worldgen.placements.OrePlacements;
import net.foxyas.changedaddon.datagen.worldgen.placements.TreePlacements;
import net.foxyas.changedaddon.datagen.worldgen.placements.VegetationPlacements;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PlacedFeatureProvider {

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        VegetationPlacements.bootstrap(context);
        OrePlacements.bootstrap(context);
        TreePlacements.bootstrap(context);
    }

    public static ResourceKey<PlacedFeature> createKey(ResourceLocation pKey) {
        return ResourceKey.create(Registries.PLACED_FEATURE, pKey);
    }

}