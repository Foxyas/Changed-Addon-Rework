package net.foxyas.changedaddon.client.model.baked;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BakedModelShadeLayerDynamicBright extends BakedModelWrapper<BakedModel> {

    private final int lightValue;

    // Construtor padrão que assume brilho máximo
    public BakedModelShadeLayerDynamicBright(BakedModel originalModel) {
        this(originalModel, 15);
    }

    public BakedModelShadeLayerDynamicBright(BakedModel originalModel, int lightValue) {
        super(originalModel);
        // Garante que o valor fique estritamente entre 0 e 15
        this.lightValue = Math.max(0, Math.min(15, lightValue));
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        if (state == null) {
            return originalModel.getQuads(state, side, rand, extraData, renderType);
        }
        return transformUnshadedQuad(originalModel.getQuads(state, side, rand, extraData, renderType));
    }

    private List<BakedQuad> transformUnshadedQuad(List<BakedQuad> oldQuads) {
        List<BakedQuad> quads = new ArrayList<>(oldQuads);
        if (!quads.isEmpty()) {
            // Modificado para usar o método dinâmico instanciado
            quads.replaceAll(quad -> quad.isShade() ? quad : setCustomLight(quad));
        }
        return quads;
    }

    private BakedQuad setCustomLight(BakedQuad quad) {
        int[] vertexData = quad.getVertices().clone();
        int step = vertexData.length / 4;

        // CÁLCULO DINÂMICO DE LUZ:
        // O Minecraft usa os bits superiores para luz do céu e inferiores para luz do bloco.
        // Multiplicar o valor (0-15) por 16 desloca o valor para o formato correto (0 a 240 / 0xF0)
        int blockLight = this.lightValue * 16;

        // Monta o mapa de luz combinando blockLight e skyLight (mantido em 0 para efeito emissivo puro)
        int lightmapValue = (blockLight & 0xFFFF) | ((0) << 16);

        // Injeta o valor calculado nos 4 cantos da face
        vertexData[6] = lightmapValue;
        vertexData[6 + step] = lightmapValue;
        vertexData[6 + 2 * step] = lightmapValue;
        vertexData[6 + 3 * step] = lightmapValue;

        return new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade());
    }
}