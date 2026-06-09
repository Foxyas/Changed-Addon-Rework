package net.foxyas.changedaddon.datagen.worldgen.placements;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.worldgen.PlacedFeatureProvider;
import net.foxyas.changedaddon.datagen.worldgen.features.VegetationFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class VegetationPlacements {

    public static final ResourceKey<PlacedFeature> LUMINARA_FLOWERS =
            PlacedFeatureProvider.createKey(ChangedAddonMod.resourceLoc("luminara_flowers"));

    public static void bootstrap(BootstapContext<PlacedFeature> pContext) {
        HolderGetter<ConfiguredFeature<?, ?>> holderGetter = pContext.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> luminaraFlowers = holderGetter.getOrThrow(VegetationFeatures.PATCH_LUMINARA_FLOWERS);
        PlacementUtils.register(pContext,
                LUMINARA_FLOWERS,
                luminaraFlowers,
                NoiseThresholdCountPlacement.of(-0.8D, 5, 10),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP,
                BiomeFilter.biome());
    }

}
