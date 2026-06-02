package net.foxyas.changedaddon.datagen.worldgen.placements;

import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonFeatures.PlacedFeatures.*;

public class OrePlacements {

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> lookup = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                PAINITE_ORE_BURIED,
                new PlacedFeature(
                        lookup.getOrThrow(ChangedAddonFeatures.ConfiguredFeatures.PAINITE_ORE_BURIED),
                        List.of(
                                CountPlacement.of(4),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-60),
                                        VerticalAnchor.absolute(-45)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(
                IRIDIUM_ORE_SMALL,
                new PlacedFeature(
                        lookup.getOrThrow(ChangedAddonFeatures.ConfiguredFeatures.IRIDIUM_ORE_SMALL),
                        List.of(
                                CountPlacement.of(2),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-60),
                                        VerticalAnchor.absolute(-45)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(
                IRIDIUM_ORE_LARGE,
                new PlacedFeature(
                        lookup.getOrThrow(ChangedAddonFeatures.ConfiguredFeatures.IRIDIUM_ORE_LARGE),
                        List.of(
                                CountPlacement.of(2),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-60),
                                        VerticalAnchor.absolute(-45)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );

        context.register(
                IRIDIUM_ORE_BURIED,
                new PlacedFeature(
                        lookup.getOrThrow(ChangedAddonFeatures.ConfiguredFeatures.IRIDIUM_ORE_BURIED),
                        List.of(
                                CountPlacement.of(2),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(
                                        VerticalAnchor.absolute(-60),
                                        VerticalAnchor.absolute(-45)
                                ),
                                BiomeFilter.biome()
                        )
                )
        );
    }
}
