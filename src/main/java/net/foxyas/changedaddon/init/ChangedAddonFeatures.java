package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.world.features.ores.PainiteOreFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
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

    public static class ConfiguredFeatures {
        public static final ResourceKey<ConfiguredFeature<?, ?>> IRIDIUM_ORE_BURIED =
                createConfigured("iridium_ore_buried");

        public static final ResourceKey<ConfiguredFeature<?, ?>> IRIDIUM_ORE_LARGE =
                createConfigured("iridium_ore_large");

        public static final ResourceKey<ConfiguredFeature<?, ?>> IRIDIUM_ORE_SMALL =
                createConfigured("iridium_ore_small");

        public static final ResourceKey<ConfiguredFeature<?, ?>> PAINITE_ORE_BURIED =
                createConfigured("painite_ore_buried");
    }

    public static class PlacedFeatures {

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
