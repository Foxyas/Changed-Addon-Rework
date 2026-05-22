package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.worldgen.placements.OrePlacements;
import net.foxyas.changedaddon.datagen.worldgen.placements.TreePlacements;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
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

import static net.foxyas.changedaddon.init.ChangedAddonFeatures.PlacedFeatures.*;

@ParametersAreNonnullByDefault
public class PlacedFeatureProvider {

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        OrePlacements.bootstrap(context);
        TreePlacements.bootstrap(context);
    }

}