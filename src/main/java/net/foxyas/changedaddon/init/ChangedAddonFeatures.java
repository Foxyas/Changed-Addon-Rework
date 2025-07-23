package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.world.features.ores.IridiumoreFeature;
import net.foxyas.changedaddon.world.features.ores.PainiteOreFeature;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class ChangedAddonFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, ChangedAddonMod.MODID);
    private static final List<FeatureRegistration> FEATURE_REGISTRATIONS = new ArrayList<>();
    public static final RegistryObject<Feature<?>> IRIDIUM_ORE = register("iridium_ore", IridiumoreFeature::feature,
            new FeatureRegistration(GenerationStep.Decoration.UNDERGROUND_ORES, IridiumoreFeature.GENERATE_BIOMES, IridiumoreFeature::placedFeature));
    public static final RegistryObject<Feature<?>> PAINITE_ORE = register("painite_ore", PainiteOreFeature::feature,
            new FeatureRegistration(GenerationStep.Decoration.UNDERGROUND_ORES, PainiteOreFeature.GENERATE_BIOMES, PainiteOreFeature::placedFeature));

    private static RegistryObject<Feature<?>> register(String registryname, Supplier<Feature<?>> feature, FeatureRegistration featureRegistration) {
        FEATURE_REGISTRATIONS.add(featureRegistration);
        return REGISTRY.register(registryname, feature);
    }

    @SubscribeEvent
    public static void addFeaturesToBiomes(BiomeLoadingEvent event) {
        for (FeatureRegistration registration : FEATURE_REGISTRATIONS) {
            if (registration.biomes() == null || registration.biomes().contains(event.getName()))
                event.getGeneration().getFeatures(registration.stage()).add(registration.placedFeature().get());
        }
    }

    private record FeatureRegistration(GenerationStep.Decoration stage, Set<ResourceLocation> biomes,
                                       Supplier<Holder<PlacedFeature>> placedFeature) {
    }
}
