package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class DynamicRadialWidget extends Widget {
    private final List<RadialData> points = new ArrayList<>();
    private final int radius;
    private final int color;
    private LivingEntity targetEntity;

    public DynamicRadialWidget(int radius, int color) {
        this.radius = radius;
        this.color = color;
        this.targetEntity = Minecraft.getInstance().player;
    }

    public void setTargetEntity(LivingEntity entity) {
        this.targetEntity = entity;
    }

    public void clearPoints() {
        this.points.clear();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (points.size() < 3) return;

        float centerX = this.getOrigin().x + radius;
        float centerY = this.getOrigin().y + radius;
        float angleStep = (float) (Math.PI * 2) / points.size();

        renderBackground(graphics, centerX, centerY, angleStep);
        renderPolygon(graphics, centerX, centerY, angleStep);
        renderLabelsAndHitboxes(graphics, centerX, centerY, angleStep, mouseX, mouseY);
    }

    public void addAttributePoint(Attribute attribute, String label, double maxValue) {
        if (targetEntity == null) return;

        AttributeInstance instance = targetEntity.getAttribute(attribute);
        double current = 1;
        double base = 1;
        if (instance != null) {
            current = instance.getValue();
            base = instance.getBaseValue();
        }
        double ratio = Mth.clamp(current / maxValue, 0.0, 1.0);

        double diff = ((current - base) / base) * 100.0;
        String prefix = diff >= 0 ? "+" : "";

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable(attribute.getDescriptionId()).append(": " + (int)current));
        tooltip.add(Component.literal(String.format("Base: %.1f (%s%.1f%%)", base, prefix, diff)));

        this.points.add(new RadialData(label, ratio, tooltip));
    }

    private void renderBackground(GuiGraphics graphics, float cx, float cy, float step) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        // 1. GRID FILLED
        float[] levels = {1.0f, 0.75f, 0.5f, 0.25f};
        for (int i = 0; i < levels.length; i++) {
            float currentRadius = radius * levels[i];
            float brightness = (i % 2 == 0) ? 0.12f : 0.08f;
            float alpha = 0.6f; // Aumentado para melhor visibilidade

            buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.vertex(matrix, cx, cy, 0).color(brightness, brightness, brightness, alpha).endVertex();

            for (int j = 0; j <= points.size(); j++) {
                float angle = (j % points.size()) * step - (float) Math.PI / 2;
                float vx = cx + (Mth.cos(angle) * currentRadius);
                float vy = cy + (Mth.sin(angle) * currentRadius);
                buffer.vertex(matrix, vx, vy, 0).color(brightness, brightness, brightness, alpha).endVertex();
            }
            tesselator.end();
        }

        // 2. GRID LINES (WEB)
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < points.size(); i++) {
            float angle = i * step - (float) Math.PI / 2;
            float x2 = cx + (Mth.cos(angle) * radius);
            float y2 = cy + (Mth.sin(angle) * radius);

            // Axis lines
            buffer.vertex(matrix, cx, cy, 0).color(1f, 1f, 1f, 0.2f).endVertex();
            buffer.vertex(matrix, x2, y2, 0).color(1f, 1f, 1f, 0.2f).endVertex();
        }
        tesselator.end();
    }

    private void renderPolygon(GuiGraphics graphics, float cx, float cy, float step) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        // 1. POLYGON FILL
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        // Centro do polígono (levemente mais transparente para efeito de gradiente)
        buffer.vertex(matrix, cx, cy, 0).color(r, g, b, a * 0.6f).endVertex();

        for (int i = 0; i <= points.size(); i++) {
            int idx = i % points.size();
            float angle = idx * step - (float) Math.PI / 2;
            float val = (float) Mth.clamp(points.get(idx).value, 0.05, 1.0); // 0.05 min para não sumir no centro

            float vx = cx + (Mth.cos(angle) * (radius * val));
            float vy = cy + (Mth.sin(angle) * (radius * val));
            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, a).endVertex();
        }
        tesselator.end();

        // 2. POLYGON OUTLINE
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= points.size(); i++) {
            int idx = i % points.size();
            float angle = idx * step - (float) Math.PI / 2;
            float val = (float) Mth.clamp(points.get(idx).value, 0.05, 1.0);

            float vx = cx + (Mth.cos(angle) * (radius * val));
            float vy = cy + (Mth.sin(angle) * (radius * val));
            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 1.0f).endVertex();
        }
        tesselator.end();
    }

    private void renderLabelsAndHitboxes(GuiGraphics graphics, float cx, float cy, float step, int mx, int my) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < points.size(); i++) {
            float angle = i * step - (float) Math.PI / 2;
            int lx = (int) (cx + (Mth.cos(angle) * (radius + 14)));
            int ly = (int) (cy + (Mth.sin(angle) * (radius + 14)));

            RadialData data = points.get(i);
            Component text = Component.literal(data.label);
            int width = font.width(text);

            graphics.drawCenteredString(font, text, lx, ly - 4, 0xFFFFFF);

            if (mx >= lx - width / 2 && mx <= lx + width / 2 && my >= ly - 10 && my <= ly + 5) {
                graphics.renderComponentTooltip(font, data.toolTip, mx, my);
            }
        }
    }

    public static class RadialData {
        public final String label;
        public final double value;
        public final List<Component> toolTip;

        public RadialData(String label, double value, List<Component> toolTip) {
            this.label = label;
            this.value = value;
            this.toolTip = toolTip == null ? new ArrayList<>() : toolTip;
        }
    }
}