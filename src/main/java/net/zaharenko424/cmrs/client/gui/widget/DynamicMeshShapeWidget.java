package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicMeshShapeWidget extends Widget {
    private final List<Float> segments = new ArrayList<>();
    private final int radius;

    public DynamicMeshShapeWidget(int radius) {
        this.radius = radius;
    }

    public void addSegments(Float... segments) {
        this.segments.addAll(List.of(segments));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderMesh(graphics, Color.BLACK.getRGB(), segments, true, true);
    }

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

        float a = (float)(color >> 24 & 255) / 255.0F;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        // =========================
        // FILL
        // =========================
        if (fill) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

            for (int i = 0; i < segments.size(); i++) {
                float angle1 = i * step - (float) Math.PI / 2;
                float angle2 = (i + 1) * step - (float) Math.PI / 2;

                int nextIdx = (i + 1) % segments.size();

                float v1x = cx + (Mth.cos(angle1) * (radius * segments.get(i)));
                float v1y = cy + (Mth.sin(angle1) * (radius * segments.get(i)));

                float v2x = cx + (Mth.cos(angle2) * (radius * segments.get(nextIdx)));
                float v2y = cy + (Mth.sin(angle2) * (radius * segments.get(nextIdx)));

                float z = 1f; // avoid z-fighting

                // NOTE: swapped order (v2, v1) to fix winding
                buffer.vertex(matrix, cx, cy, z).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, v2x, v2y, z).color(r, g, b, a).endVertex();
                buffer.vertex(matrix, v1x, v1y, z).color(r, g, b, a).endVertex();
            }

            tesselator.end();

            RenderSystem.enableCull();
        }

        // =========================
        // OUTLINE
        // =========================
        if (drawOutline) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

            for (int i = 0; i <= segments.size(); i++) {
                int idx = i % segments.size();
                float val = segments.get(idx);

                float angle = idx * step - (float) Math.PI / 2;

                float vx = cx + (Mth.cos(angle) * (radius * val));
                float vy = cy + (Mth.sin(angle) * (radius * val));

                buffer.vertex(matrix, vx, vy, 2f).color(r, g, b, 1.0f).endVertex();
            }

            tesselator.end();
        }
    }

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

            buffer.vertex(matrix, cx, cy, 0).color(color).endVertex();
            buffer.vertex(matrix, vx, vy, 0).color(color).endVertex();
        }

        tesselator.end();
    }
}