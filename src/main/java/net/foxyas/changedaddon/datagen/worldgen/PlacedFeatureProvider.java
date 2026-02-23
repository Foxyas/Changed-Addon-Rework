package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.foxyas.changedaddon.init.ChangedAddonFeatures.Placements.*;

@ParametersAreNonnullByDefault
public class PlacedFeatureProvider {

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> lookup = context.lookup(Registries.CONFIGURED_FEATURE);
//        // IRIDIUM placement
//        context.register(
//                ChangedAddonFeatures.IRIDIUM_ORE_PLACED,
//                IridiumoreFeature.PLACED_FEATURE.get()
//        );
//
//        // PAINITE placement
//        context.register(
//                ChangedAddonFeatures.PAINITE_ORE_PLACED,
//                PainiteOreFeature.PLACED_FEATURE.get()
//        );

        context.register(
                PAINITE_ORE_PLACED,
                new PlacedFeature(
                        lookup.getOrThrow(ChangedAddonFeatures.Configurations.PAINITE_ORE_CONFIG),
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
                        lookup.getOrThrow(ChangedAddonFeatures.Configurations.IRIDIUM_ORE_SMALL),
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
                        lookup.getOrThrow(ChangedAddonFeatures.Configurations.IRIDIUM_ORE_LARGE),
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
                        lookup.getOrThrow(ChangedAddonFeatures.Configurations.IRIDIUM_ORE_BURIED),
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

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(feature, modifiers));
    }

    private static ResourceKey<PlacedFeature> create(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, id));
    }

}