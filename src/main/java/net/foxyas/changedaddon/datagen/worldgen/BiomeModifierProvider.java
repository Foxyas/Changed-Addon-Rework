package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.simple.AbstractCheetahEntity;
import net.foxyas.changedaddon.entity.simple.AbstractSnowFoxEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@ParametersAreNonnullByDefault
public class BiomeModifierProvider {

    public static final ResourceKey<BiomeModifier> ADD_LATEX_CHEETAHS_SPAWNS =
            ChangedAddonMod.resourceKey(ForgeRegistries.Keys.BIOME_MODIFIERS, "add_latex_cheetahs_spawns");

    public static final ResourceKey<BiomeModifier> ADD_LATEX_SNOW_FOXES_SPAWNS =
            ChangedAddonMod.resourceKey(ForgeRegistries.Keys.BIOME_MODIFIERS, "add_latex_snow_foxes_spawns");

    public static final ResourceKey<BiomeModifier> ADD_LATEX_DAZED_SPAWNS =
            ChangedAddonMod.resourceKey(ForgeRegistries.Keys.BIOME_MODIFIERS, "add_latex_dazed_spawns");

    public static final ResourceKey<BiomeModifier> ADD_MIRROR_WHITE_TIGER_SPAWNS =
            ChangedAddonMod.resourceKey(ForgeRegistries.Keys.BIOME_MODIFIERS, "add_mirror_white_tiger_spawns");

    public static final ResourceKey<BiomeModifier> ADD_IRIDIUM_GEN =
            create("add_iridium");

    public static final ResourceKey<BiomeModifier> ADD_PAINITE_GEN =
            create("add_painite");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        context.register(
                ADD_IRIDIUM_GEN,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(List.of(
                                placedFeatures.getOrThrow(ChangedAddonFeatures.PlacedFeatures.IRIDIUM_ORE_SMALL),
                                placedFeatures.getOrThrow(ChangedAddonFeatures.PlacedFeatures.IRIDIUM_ORE_LARGE),
                                placedFeatures.getOrThrow(ChangedAddonFeatures.PlacedFeatures.IRIDIUM_ORE_BURIED)
                        )),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );

        context.register(
                ADD_PAINITE_GEN,
                new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(ChangedAddonFeatures.PlacedFeatures.PAINITE_ORE_BURIED)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );

        addEntitiesSpawns(context, biomes);
    }

    private static void addEntitiesSpawns(BootstapContext<BiomeModifier> context, HolderGetter<Biome> biomes) {
        addCheetahSpawns(context, biomes);
        addSnowFoxesSpawns(context, biomes);


        addEntitySpawns(context,
                ADD_LATEX_DAZED_SPAWNS,
                biomes,
                null,
                Set.of(BiomeTags.IS_OVERWORLD),
                List.of(
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.DAZED_LATEX.get(), 4, 1, 2),
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.BUFF_DAZED_LATEX.get(), 3, 1, 2)
                )
        );

        addEntitySpawns(context, ADD_MIRROR_WHITE_TIGER_SPAWNS, biomes, Set.of(Biomes.TAIGA),
                List.of(
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.MIRROR_WHITE_TIGER.get(), 4, 1, 2)
                )
        );
    }

    private static void addSnowFoxesSpawns(BootstapContext<BiomeModifier> context, HolderGetter<Biome> biomes) {
        List<Holder<Biome>> latexSnowFoxesSpawnBiomes = new ArrayList<>();
        AbstractSnowFoxEntity.getSpawnBiomes().forEach((biomeResourceKey) -> {
            latexSnowFoxesSpawnBiomes.add(biomes.getOrThrow(biomeResourceKey));
        });

        List<Holder<Biome>> shortedCheetahSpawnBiomes = latexSnowFoxesSpawnBiomes.stream().sorted(Comparator.comparing(biomeHolder -> biomeHolder.unwrapKey().get().location().getPath())).toList();

        context.register(ADD_LATEX_SNOW_FOXES_SPAWNS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(shortedCheetahSpawnBiomes),
                List.of(
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.LATEX_SNOW_FOX_MALE.get(), 4, 1, 2),
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.LATEX_SNOW_FOX_FEMALE.get(), 4, 1, 2)
                ))
        );

    }

    private static void addEntitySpawns(
            BootstapContext<BiomeModifier> context,
            ResourceKey<BiomeModifier> modifierKey,
            HolderGetter<Biome> biomeLookup,
            Set<ResourceKey<Biome>> spawnBiomes,
            List<MobSpawnSettings.SpawnerData> spawns
    ) {
        var biomeHolders = spawnBiomes.stream()
                .map(biomeLookup::getOrThrow)
                .sorted(Comparator.comparing(
                        holder -> holder.unwrapKey()
                                .orElseThrow()
                                .location()
                                .getPath()
                )).toList();

        context.register(
                modifierKey,
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomeHolders),
                        spawns
                )
        );
    }

    private static void addEntitySpawnsInBiomeTags(
            BootstapContext<BiomeModifier> context,
            ResourceKey<BiomeModifier> modifierKey,
            HolderGetter<Biome> biomeLookup,
            Set<TagKey<Biome>> biomeTags,
            List<MobSpawnSettings.SpawnerData> spawns
    ) {
        biomeTags.forEach(tag ->
                context.register(
                        modifierKey,
                        new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                                biomeLookup.getOrThrow(tag),
                                spawns
                        )
                )
        );
    }

    private static void addEntitySpawns(
            BootstapContext<BiomeModifier> context,
            ResourceKey<BiomeModifier> baseModifierKey,
            HolderGetter<Biome> biomeLookup,
            @Nullable Set<ResourceKey<Biome>> biomes,
            @Nullable Set<TagKey<Biome>> biomeTags,
            List<MobSpawnSettings.SpawnerData> spawns
    ) {
        if (biomes != null) {
            ResourceKey<BiomeModifier> biomeModifierResourceKey = ChangedAddonMod.resourceKey(ForgeRegistries.Keys.BIOME_MODIFIERS, baseModifierKey.location().getPath() + "_for_biomes");
            List<Holder.Reference<Biome>> biomeHolders = biomes.stream()
                    .map(biomeLookup::getOrThrow)
                    .sorted(Comparator.comparing(
                            holder -> holder.unwrapKey()
                                    .orElseThrow()
                                    .location()
                                    .getPath()
                    )).toList();

            context.register(
                    biomeModifierResourceKey,
                    new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                            HolderSet.direct(biomeHolders),
                            spawns
                    )
            );
        }

        if (biomeTags != null) {
            ResourceKey<BiomeModifier> biomeModifierTagsResourceKey = ChangedAddonMod.resourceKey(ForgeRegistries.Keys.BIOME_MODIFIERS, baseModifierKey.location().getPath() + "_for_tags");
            biomeTags.stream().map(biomeLookup::getOrThrow).forEach((biomeNamed) -> {
                context.register(
                        biomeModifierTagsResourceKey,
                        new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                                biomeNamed,
                                spawns
                        )
                );
            });
        }
    }

    private static void addCheetahSpawns(BootstapContext<BiomeModifier> context, HolderGetter<Biome> biomes) {
        List<Holder<Biome>> latexCheetahSpawnBiomes = new ArrayList<>();
        AbstractCheetahEntity.getSpawnBiomes().forEach((biomeResourceKey) -> {
            latexCheetahSpawnBiomes.add(biomes.getOrThrow(biomeResourceKey));
        });

        List<Holder<Biome>> shortedCheetahSpawnBiomes = latexCheetahSpawnBiomes.stream().sorted(Comparator.comparing(biomeHolder -> biomeHolder.unwrapKey().get().location().getPath())).toList();

        context.register(ADD_LATEX_CHEETAHS_SPAWNS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(shortedCheetahSpawnBiomes),
                List.of(
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.LATEX_CHEETAH_MALE.get(), 4, 1, 2),
                        new MobSpawnSettings.SpawnerData(ChangedAddonEntities.LATEX_CHEETAH_FEMALE.get(), 4, 1, 2)
                ))
        );

    }

    // Utils for Future Spawn Biome Modifiers
    private static List<Holder<Biome>> getShortedBiomeList(List<Holder<Biome>> originalList) {
        return originalList.stream().sorted(Comparator.comparing(holder -> holder.unwrapKey().get().location().getPath())).toList();
    }

    private static ResourceKey<BiomeModifier> create(String id) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, id));
    }
}