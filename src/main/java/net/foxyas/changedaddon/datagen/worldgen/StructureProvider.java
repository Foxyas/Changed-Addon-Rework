package net.foxyas.changedaddon.datagen.worldgen;

import net.foxyas.changedaddon.datagen.worldgen.template_pool.DazedMeteorPools;
import net.foxyas.changedaddon.worldgen.ChangedAddonStructures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//copy from a_changed
public class StructureProvider {

    public static void bootstrap(BootstapContext<Structure> context) {
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> poolGetter = context.lookup(Registries.TEMPLATE_POOL);

        context.register(ChangedAddonStructures.DAZED_METEOR, new JigsawStructure(
                new Structure.StructureSettings(
                        HolderSet.direct(
                                biomeGetter.getOrThrow(Biomes.PLAINS), biomeGetter.getOrThrow(Biomes.SNOWY_PLAINS),
                                biomeGetter.getOrThrow(Biomes.MEADOW), biomeGetter.getOrThrow(Biomes.SAVANNA),
                                biomeGetter.getOrThrow(Biomes.SUNFLOWER_PLAINS), biomeGetter.getOrThrow(Biomes.DESERT),
                                biomeGetter.getOrThrow(Biomes.BADLANDS)
                        ), Map.of(), GenerationStep.Decoration.TOP_LAYER_MODIFICATION, TerrainAdjustment.BEARD_THIN
                ),
                poolGetter.getOrThrow(DazedMeteorPools.START),
                Optional.empty(),
                4,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                false,
                Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
                64
        ));
    }

    public static void structureSet(BootstapContext<StructureSet> context) {
        HolderGetter<Structure> structureGetter = context.lookup(Registries.STRUCTURE);

        context.register(ChangedAddonStructures.DAZED_METEOR_SET, new StructureSet(
                List.of(
                        StructureSet.entry(structureGetter.getOrThrow(ChangedAddonStructures.DAZED_METEOR))
                ),
                new RandomSpreadStructurePlacement(41, 35, RandomSpreadType.LINEAR, 481422317)));
    }
}