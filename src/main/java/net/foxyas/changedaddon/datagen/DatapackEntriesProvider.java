package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.datagen.worldgen.*;
import net.foxyas.changedaddon.datagen.worldgen.template_pool.DazedMeteorPools;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.world.features.processors.DayTimeStructureProcessor;
import net.foxyas.changedaddon.world.features.processors.OffSetSpawnProcessor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class DatapackEntriesProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, DatapackEntriesProvider::biome)
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureProvider::bootstrap)
            .add(Registries.PLACED_FEATURE, PlacedFeatureProvider::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierProvider::bootstrap)
            .add(Registries.DAMAGE_TYPE, DatapackEntriesProvider::damageType)
            .add(Registries.PROCESSOR_LIST, DatapackEntriesProvider::processorList)
            .add(Registries.TEMPLATE_POOL, DatapackEntriesProvider::templatePools)
            .add(Registries.STRUCTURE, StructureProvider::bootstrap)
            .add(Registries.STRUCTURE_SET, StructureProvider::structureSet);
            //.add(Registries.TRIM_PATTERN, TrimPatterns::bootstrap)//ArmorTrims::bootstrapPatterns)
            //.add(Registries.TRIM_MATERIAL, TrimMaterials::bootstrap);//ArmorTrims::bootstrapMaterials);

    public DatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ChangedAddonMod.MODID));
    }

    private static void damageType(BootstapContext<DamageType> context) {
        context.register(ChangedAddonDamageSources.LATEX_SOLVENT.key(), new DamageType("latex_solvent", DamageScaling.NEVER, 0.1f));
        context.register(ChangedAddonDamageSources.CONSCIENCE_LOSE.key(), new DamageType("conscience_lose", DamageScaling.NEVER, 0));
        context.register(ChangedAddonDamageSources.UNTRANSFUR_FAIL.key(), new DamageType("untransfur_fail", DamageScaling.NEVER, 0));
    }

    private static void biome(BootstapContext<Biome> context) {
    }

    public static final ResourceKey<StructureProcessorList> GRAVITY = ResourceKey.create(Registries.PROCESSOR_LIST, ChangedAddonMod.resourceLoc("gravity_rot"));
    public static final ResourceKey<StructureProcessorList> DAZED_METEOR_POLL = ResourceKey.create(Registries.PROCESSOR_LIST, ChangedAddonMod.resourceLoc("dazed_meteor_rot"));

    private static void processorList(BootstapContext<StructureProcessorList> context) {
        context.register(GRAVITY, new StructureProcessorList(List.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -11))));
        context.register(DAZED_METEOR_POLL, new StructureProcessorList(List.of(
                new OffSetSpawnProcessor(0, -10, 0),
                new DayTimeStructureProcessor(Optional.empty(), DayTimeStructureProcessor.DayPeriod.NIGHT)
        )
        ));
    }

    private static void templatePools(BootstapContext<StructureTemplatePool> context) {
        DazedMeteorPools.bootstrap(context);
    }
}