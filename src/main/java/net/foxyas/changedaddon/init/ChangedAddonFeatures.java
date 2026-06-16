package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.world.features.StructureTemplatePoolFeature;
import net.foxyas.changedaddon.world.features.ores.PainiteOreFeature;
import net.foxyas.changedaddon.world.features.tree.TreeWithFlowersFeature;
import net.foxyas.changedaddon.world.features.tree.TreeWithFlowersFeatureConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ChangedAddonFeatures {

    public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, ChangedAddonMod.MODID);

    public static final RegistryObject<PainiteOreFeature> PAINITE_ORE = register("painite_ore", PainiteOreFeature::new);
    public static final RegistryObject<StructureTemplatePoolFeature> STRUCTURE_TEMPLATE_POOL_FEATURE = register("structure_template_pool_feature", StructureTemplatePoolFeature::new);

    public static final RegistryObject<TreeWithFlowersFeature> TREE_WITH_FLOWERS = register("tree_with_flowers", () -> new TreeWithFlowersFeature(TreeWithFlowersFeatureConfiguration.CODEC, (TreeFeature) Feature.TREE, (RandomPatchFeature) Feature.FLOWER));

    private static <T extends Feature<?>> RegistryObject<T> register(String registryname, Supplier<T> feature) {
        return REGISTRY.register(registryname, feature);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> createConfigured(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, id));
    }

    private static ResourceKey<PlacedFeature> createPlaced(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, id));
    }

    public static class Configured {
        public static final ResourceKey<ConfiguredFeature<?, ?>> IRIDIUM_ORE_BURIED =
                createConfigured("iridium_ore_buried");

        public static final ResourceKey<ConfiguredFeature<?, ?>> IRIDIUM_ORE_LARGE =
                createConfigured("iridium_ore_large");

        public static final ResourceKey<ConfiguredFeature<?, ?>> IRIDIUM_ORE_SMALL =
                createConfigured("iridium_ore_small");

        public static final ResourceKey<ConfiguredFeature<?, ?>> PAINITE_ORE_BURIED =
                createConfigured("painite_ore_buried");

        public static final ResourceKey<ConfiguredFeature<?, ?>> LUMINARA_TREE =
                createConfigured("luminara_tree");
    }

    public static class Placed {

        public static final ResourceKey<PlacedFeature> IRIDIUM_ORE_BURIED =
                createPlaced("iridium_ore_buried");

        public static final ResourceKey<PlacedFeature> IRIDIUM_ORE_LARGE =
                createPlaced("iridium_ore_large");

        public static final ResourceKey<PlacedFeature> IRIDIUM_ORE_SMALL =
                createPlaced("iridium_ore_small");

        public static final ResourceKey<PlacedFeature> PAINITE_ORE_BURIED =
                createPlaced("painite_ore_buried");
    }
}
