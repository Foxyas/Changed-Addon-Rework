package net.foxyas.changedaddon.world.features;

import net.foxyas.changedaddon.mixins.worldgen.SinglePoolElementAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class StructureTemplatePoolFeature extends Feature<StructureTemplatePoolFeatureConfiguration> {

    public StructureTemplatePoolFeature() {
        // Passamos o Codec da nossa configuração para o construtor pai
        super(StructureTemplatePoolFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<StructureTemplatePoolFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos originPos = context.origin();
        RandomSource random = context.random();
        
        // ↓ O SEGREDO DA REUTILIZAÇÃO: Pega a pool definida na configuração atual
        StructureTemplatePool pool = context.config().templatePool().value();
        
        if (pool.getShuffledTemplates(random).isEmpty()) return false;

        // Rola o dado da Pool para escolher a estrutura
        StructurePoolElement chosenElement = pool.getRandomTemplate(random);
        StructureTemplateManager templateManager = level.getLevel().getServer().getStructureManager();

        if (chosenElement instanceof SinglePoolElement singleElement) {
            ResourceLocation nbtLocation = ((SinglePoolElementAccessor) singleElement).getTemplate().left().orElse(null);
            if (nbtLocation == null) return false;

            StructureTemplate template = templateManager.getOrCreate(nbtLocation);

            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
            Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];

            StructurePlaceSettings settings = new StructurePlaceSettings()
                    .setRotation(rotation)
                    .setMirror(mirror)
                    .setIgnoreEntities(true);

            BlockPos placementPos = originPos.below(1);

            return template.placeInWorld(level, placementPos, placementPos, settings, random, 4);
        }

        return false;
    }
}