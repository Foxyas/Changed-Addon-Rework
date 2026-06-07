package net.foxyas.changedaddon.datagen.worldgen.placements;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.worldgen.PlacedFeatureProvider;
import net.foxyas.changedaddon.datagen.worldgen.features.TreeFeatures;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TreePlacements {

    public static final ResourceKey<PlacedFeature> LUMINARA_TREE_CHECKED = PlacedFeatureProvider.createKey(ChangedAddonMod.resourceLoc("luminara_tree_checked"));

    public static void bootstrap(BootstapContext<PlacedFeature> pContext) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = pContext.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> luminaraTree = holdergetter.getOrThrow(TreeFeatures.LUMINARA_TREE);
        PlacementUtils.register(pContext, LUMINARA_TREE_CHECKED, luminaraTree, PlacementUtils.filteredByBlockSurvival(ChangedAddonBlocks.LUMINARA_BLOOM.get()));
    }
}
