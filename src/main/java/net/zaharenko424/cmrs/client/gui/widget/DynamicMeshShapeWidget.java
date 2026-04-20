package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.List;

/**
 * Widget base para renderização de malhas dinâmicas (mesh).
 * Transforma uma lista de segmentos em uma forma geométrica conectada.
 */
public class DynamicMeshShapeWidget extends Widget {
    private final int radius;

    public DynamicMeshShapeWidget(int radius) {
        this.radius = radius;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderMesh(pGuiGraphics, Color.BLACK.getRGB(), List.of(1f,1f,1f,1f,1f,1f), true, true);
    }

    /**
     * Renderiza a malha baseada nos segmentos fornecidos.
     * * @param graphics Contexto de renderização do Minecraft.
     * @param color Cor ARGB da malha.
     * @param segments Lista de magnitudes (1.0f = raio total).
     * Se a lista tiver 1 ou 2 itens, renderiza apenas linhas.
     * @param drawOutline Se verdadeiro, desenha uma borda externa sólida.
     */
    /**
     * @param fill Se verdadeiro, renderiza a malha preenchida (recheio).
     * @param drawOutline Se verdadeiro, desenha a borda externa.
     */
    public void renderMesh(@NotNull GuiGraphics graphics, int color, List<Float> segments, boolean fill, boolean drawOutline) {
        if (segments == null || segments.isEmpty()) return;

        float cx = this.getOrigin().x + radius;
        float cy = this.getOrigin().y + radius;

        if (segments.size() < 3) {
            renderSimpleSegments(graphics, cx, cy, color, segments);
            return;
        }

        float step = (float) (Math.PI * 2) / segments.size();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        float a = (color >> 24 & 255) / 255.0F;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // --- RENDERIZAR RECHEIO ---
        if (fill) {
            buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.vertex(matrix, cx, cy, 0).color(r, g, b, a).endVertex();

            for (int i = 0; i <= segments.size(); i++) {
                int idx = i % segments.size();
                float val = segments.get(idx);
                float angle = idx * step - (float) Math.PI / 2;

                float vx = cx + (Mth.cos(angle) * (radius * val));
                float vy = cy + (Mth.sin(angle) * (radius * val));
                buffer.vertex(matrix, vx, vy, 0).color(r, g, b, a).endVertex();
            }
            tesselator.end();
        }

        // --- RENDERIZAR CONTORNO ---
        if (drawOutline) {
            buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
            for (int i = 0; i <= segments.size(); i++) {
                int idx = i % segments.size();
                float val = segments.get(idx);
                float angle = idx * step - (float) Math.PI / 2;

                float vx = cx + (Mth.cos(angle) * (radius * val));
                float vy = cy + (Mth.sin(angle) * (radius * val));
                buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 1.0f).endVertex();
            }
            tesselator.end();
        }

        RenderSystem.disableBlend();
    }

    /**
     * Renderização especial para casos com poucos segmentos (1 ou 2).
     */
    private void renderSimpleSegments(GuiGraphics graphics, float cx, float cy, int color, List<Float> segments) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();
        float step = (float) (Math.PI * 2) / Math.max(segments.size(), 1);

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < segments.size(); i++) {
            float angle = i * step - (float) Math.PI / 2;
            float vx = cx + (Mth.cos(angle) * (radius * segments.get(i)));
            float vy = cy + (Mth.sin(angle) * (radius * segments.get(i)));

            // Linha do centro até o ponto
            buffer.vertex(matrix, cx, cy, 0).color(color).endVertex();
            buffer.vertex(matrix, vx, vy, 0).color(color).endVertex();
        }
        tesselator.end();
    }
}