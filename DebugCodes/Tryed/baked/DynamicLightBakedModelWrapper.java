package net.foxyas.changedaddon.client.model.baked;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraft.client.renderer.RenderType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Deprecated() // IS BUGGED NEED FIX
public class DynamicLightBakedModelWrapper extends BakedModelWrapper<BakedModel> {
    private final Map<Integer, Integer> elementLightMap;

    public DynamicLightBakedModelWrapper(BakedModel originalModel, Map<Integer, Integer> elementLightMap) {
        super(originalModel);
        this.elementLightMap = elementLightMap;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {

        if (true) return super.getQuads(state, side, rand, data, renderType);

        List<BakedQuad> originalQuads = super.getQuads(state, side, rand, data, renderType);
        if (originalQuads.isEmpty()) return originalQuads;

        List<BakedQuad> modifiedQuads = new ArrayList<>();

        // Rastreamento simples: O Minecraft vanilla junta as quads por direção (side).
        // Para um controle 100% perfeito por elemento independente da direção, modders profissionais costumam
        // injetar a propriedade de luz diretamente nos "BakedQuads" customizados durante o Model Construction,
        // ou usar o nome da textura emissiva como identificador rápido.
        // Vamos usar a verificação por textura (Sprite) que é incrivelmente rápida e elegante para o que você quer!
        
        for (BakedQuad quad : originalQuads) {
            int[] vertexData = quad.getVertices().clone();
            
            // QUER QUE ESTA FACE BRILHE?
            // Vamos verificar se a textura dessa face específica foi marcada para brilhar.
            // Para o exemplo do bloco 8x8x8 com overlay 16x16x16, se a quad usar a textura de "glow", ela brilha!
            boolean shouldGlow = quad.getSprite().contents().name().getPath().contains("glow"); 
            
            if (shouldGlow) {
                int lightValue = 15; // Brilho máximo para a camada de glow (ou pegue do mapa se preferir)
                int customLight = lightValue << 4;

                for (int i = 0; i < 4; i++) {
                    int lightOffset = (i * 8) + 6;
                    int currentLight = vertexData[lightOffset];
                    int skyLight = currentLight & 0xFFFF0000;
                    vertexData[lightOffset] = skyLight | customLight;
                }
                
                modifiedQuads.add(new BakedQuad(
                    vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), false
                ));
            } else {
                // Elemento normal (Não brilha, responde à sombra do ambiente)
                modifiedQuads.add(quad);
            }
        }

        return modifiedQuads;
    }
}