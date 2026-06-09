package net.foxyas.changedaddon.world.features.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.RandomPatchFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class TreeWithFlowersFeature extends Feature<TreeWithFlowersFeatureConfiguration> {

    // Instanciamos um TreeFeature interno para usar a lógica nativa de spawn de árvores
    private final TreeFeature treeFeature;
    private final RandomPatchFeature flowerFeature;

    public TreeWithFlowersFeature(Codec<TreeWithFlowersFeatureConfiguration> codec, TreeFeature treeFeature, RandomPatchFeature flowerFeature) {
        super(codec);
        this.treeFeature = treeFeature;
        this.flowerFeature = flowerFeature;
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeWithFlowersFeatureConfiguration> context) {
        TreeWithFlowersFeatureConfiguration config = context.config();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        BlockPos blockpos = context.origin();

        // 1. Tenta colocar a árvore primeiro usando as configurações fornecidas
        // Criamos um novo contexto apontando diretamente para a TreeConfiguration interna
        FeaturePlaceContext<TreeConfiguration> treeContext =
                new FeaturePlaceContext<>(
                        context.topFeature(),
                        worldgenlevel,
                        context.chunkGenerator(),
                        randomsource,
                        blockpos,
                        config.treeConfig()
                );

        FeaturePlaceContext<RandomPatchConfiguration> flowerContext =
                new FeaturePlaceContext<>(
                        context.topFeature(),
                        worldgenlevel,
                        context.chunkGenerator(),
                        randomsource,
                        blockpos,
                        config.flowerConfig()
                );

        boolean treePlaced = this.treeFeature.place(treeContext);
        boolean flowerPlaced = this.flowerFeature.place(flowerContext);

        // 2. Se a árvore falhou em gerar (ex: sem espaço ou bloco inválido), nós cancelamos as flores também
        if (!treePlaced || !flowerPlaced) {
            return false;
        }

        return true;

//        // 3. A árvore gerou com sucesso! Agora vamos espalhar as flores ao redor utilizando a lógica do RandomPatch
//        RandomPatchConfiguration flowerConfig = config.flowerConfig();
//        int flowersPlacedCount = 0;
//
//        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
//
//        // Multiplicadores baseados na dispersão (spread) horizontal e vertical configurados no JSON
//        int xzSpreadRange = flowerConfig.xzSpread() + 1;
//        int ySpreadRange = flowerConfig.ySpread() + 1;
//
//        // Executa o número de tentativas ("tries") definido para as flores
//        for (int l = 0; l < flowerConfig.tries(); ++l) {
//            // Define uma posição aleatória deslocada da base do tronco da árvore
//            mutablePos.setWithOffset(
//                    blockpos,
//                    randomsource.nextInt(xzSpreadRange) - randomsource.nextInt(xzSpreadRange),
//                    randomsource.nextInt(ySpreadRange) - randomsource.nextInt(ySpreadRange),
//                    randomsource.nextInt(xzSpreadRange) - randomsource.nextInt(xzSpreadRange)
//            );
//            // Tenta colocar a PlacedFeature da flor naquela posição aleatória
//            // O próprio método place da PlacedFeature se encarrega de checar o canSurvive() do bloco
//            if (flowerConfig.feature().value().place(worldgenlevel, context.chunkGenerator(), randomsource, mutablePos)) {
//                ++flowersPlacedCount;
//            }
//        }
//
//        // Retorna verdadeiro indicando que a feature combo inteira foi executada com sucesso
//        return true;
    }
}