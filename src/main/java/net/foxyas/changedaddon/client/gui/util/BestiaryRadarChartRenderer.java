package net.foxyas.changedaddon.client.gui.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.foxyas.changedaddon.client.gui.AbstractBestiaryScreen;
import net.foxyas.changedaddon.client.gui.AbstractBestiaryScreen.AttributeBarItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class BestiaryRadarChartRenderer {

    public static void render(GuiGraphics graphics, Font font, int cx, int cy, float radius, 
                              List<AttributeBarItem> entityBars,
                              List<AttributeBarItem> playerBars,
                              float chartScaleFactor, int mouseX, int mouseY, 
                              List<Component> hoveredRadarTooltipOut, 
                              int[] chartBoundsOut) {
        
        int size = entityBars.size();
        if (size < 3) return;

        chartBoundsOut[0] = cx;
        chartBoundsOut[1] = cy;
        chartBoundsOut[2] = (int) radius;

        Matrix4f matrix = graphics.pose().last().pose();
        float step = (float) (Math.PI * 2) / size;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        // 1. Background Fill Web
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, 0).color(0.08f, 0.08f, 0.12f, 0.7f).endVertex();
        for (int i = 0; i <= size; i++) {
            float angle = (i % size) * step - (float) Math.PI / 2;
            float vx = cx + Mth.cos(angle) * radius;
            float vy = cy + Mth.sin(angle) * radius;
            buffer.vertex(matrix, vx, vy, 0).color(0.08f, 0.08f, 0.12f, 0.7f).endVertex();
        }
        tesselator.end();

        // 2. Player Attribute Fan
        if (playerBars != null && playerBars.size() == size) {
            buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.vertex(matrix, cx, cy, 0).color(0.21f, 0.63f, 1.0f, 0.30f).endVertex();
            for (int i = 0; i <= size; i++) {
                AttributeBarItem bar = playerBars.get(i % size);
                float angle = (i % size) * step - (float) Math.PI / 2;
                float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
                float scaledR = effectiveRatio * radius;

                float vx = cx + Mth.cos(angle) * scaledR;
                float vy = cy + Mth.sin(angle) * scaledR;
                buffer.vertex(matrix, vx, vy, 0).color(0.21f, 0.63f, 1.0f, 0.45f).endVertex();
            }
            tesselator.end();

            buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
            for (int i = 0; i <= size; i++) {
                AttributeBarItem bar = playerBars.get(i % size);
                float angle = (i % size) * step - (float) Math.PI / 2;
                float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
                float scaledR = effectiveRatio * radius;

                float vx = cx + Mth.cos(angle) * scaledR;
                float vy = cy + Mth.sin(angle) * scaledR;
                buffer.vertex(matrix, vx, vy, 0).color(0.44f, 0.77f, 1.0f, 0.90f).endVertex();
            }
            tesselator.end();
        }

        // 3. Entity Attribute Fan
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, 0).color(0.3f, 0.8f, 0.4f, 0.35f).endVertex();
        for (int i = 0; i <= size; i++) {
            AttributeBarItem bar = entityBars.get(i % size);
            float angle = (i % size) * step - (float) Math.PI / 2;
            float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
            float scaledR = effectiveRatio * radius;

            float vx = cx + Mth.cos(angle) * scaledR;
            float vy = cy + Mth.sin(angle) * scaledR;

            int c = bar.fillColor;
            float r = ((c >> 16) & 0xFF) / 255.0f;
            float g = ((c >> 8) & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 0.5f).endVertex();
        }
        tesselator.end();

        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= size; i++) {
            AttributeBarItem bar = entityBars.get(i % size);
            float angle = (i % size) * step - (float) Math.PI / 2;
            float effectiveRatio = clamp(bar.ratio / chartScaleFactor, 0.02f, 1.0f);
            float scaledR = effectiveRatio * radius;

            float vx = cx + Mth.cos(angle) * scaledR;
            float vy = cy + Mth.sin(angle) * scaledR;

            int c = bar.highlightColor;
            float r = ((c >> 16) & 0xFF) / 255.0f;
            float g = ((c >> 8) & 0xFF) / 255.0f;
            float b = (c & 0xFF) / 255.0f;

            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 0.95f).endVertex();
        }
        tesselator.end();

        // 4. Center Spokes
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < size; i++) {
            float angle = i * step - (float) Math.PI / 2;
            float vx = cx + Mth.cos(angle) * radius;
            float vy = cy + Mth.sin(angle) * radius;

            buffer.vertex(matrix, cx, cy, 0).color(0.25f, 0.25f, 0.32f, 0.6f).endVertex();
            buffer.vertex(matrix, vx, vy, 0).color(0.25f, 0.25f, 0.32f, 0.6f).endVertex();
        }
        tesselator.end();

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

        // 5. Outer Perimeter Dots & Hover Tooltips
        hoveredRadarTooltipOut.clear();

        for (int i = 0; i < size; i++) {
            AttributeBarItem entityBar = entityBars.get(i);
            float angle = i * step - (float) Math.PI / 2;

            int outerX = Math.round(cx + Mth.cos(angle) * radius);
            int outerY = Math.round(cy + Mth.sin(angle) * radius);

            boolean isHovered = mouseX >= outerX - 4 && mouseX <= outerX + 4 && mouseY >= outerY - 4 && mouseY <= outerY + 4;
            int dotColor = isHovered ? 0xFFFFFFFF : entityBar.highlightColor;

            graphics.fill(outerX - 2, outerY - 2, outerX + 2, outerY + 2, dotColor);

            if (isHovered) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(entityBar.label.copy().withStyle(ChatFormatting.YELLOW));
                tooltip.add(Component.translatable("gui.changed_addon.bestiary.radar.entity_val", entityBar.valueText));
                if (playerBars != null && i < playerBars.size()) {
                    tooltip.add(Component.translatable("gui.changed_addon.bestiary.radar.player_val", playerBars.get(i).valueText));
                }
                hoveredRadarTooltipOut.addAll(tooltip);
            }
        }
    }

    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}