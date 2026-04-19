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
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class DynamicRadialWidget extends Widget {
    private final List<AttributeDefinition> attributes = new ArrayList<>();
    private final int radius;

    private int mainColor;
    private int comparatorColor = 0x60555555; // Semi-transparent gray
    private int backgroundColor = 0x801A1A1A; // Default dark background

    private LivingEntity reference;
    private LivingEntity comparator;

    public DynamicRadialWidget(int radius, int mainColor) {
        this.radius = radius;
        this.mainColor = mainColor;
        this.reference = Minecraft.getInstance().player;
    }

    // --- Builder Methods ---

    public DynamicRadialWidget setReference(LivingEntity entity) {
        this.reference = entity;
        return this;
    }

    public DynamicRadialWidget setComparator(LivingEntity entity) {
        this.comparator = entity;
        return this;
    }

    public DynamicRadialWidget withMainColor(int color) {
        this.mainColor = color;
        return this;
    }

    public DynamicRadialWidget withComparatorColor(int color) {
        this.comparatorColor = color;
        return this;
    }

    /**
     * Set the color for the background circular grid.
     */
    public DynamicRadialWidget withBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public void addAttribute(Attribute attribute, String label, double maxValue) {
        this.attributes.add(new AttributeDefinition(attribute, label, maxValue));
    }

    public void clearAttributes() {
        this.attributes.clear();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (attributes.size() < 3) return;

        float cx = this.getOrigin().x + radius;
        float cy = this.getOrigin().y + radius;
        float step = (float) (Math.PI * 2) / attributes.size();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        // 1. Background Layer (Solid concentric circles)
        renderBackgroundGrid(graphics, cx, cy, step);

        // 2. Comparator Layer (Reference shadow)
        if (comparator != null) {
            renderEntityLayer(graphics, cx, cy, step, comparator, comparatorColor, 0.4f);
        }

        // 3. Target Layer (Main filled polygon)
        renderEntityLayer(graphics, cx, cy, step, reference, mainColor, 0.8f);

        // 4. Interaction Layer
        renderLabelsAndInteraction(graphics, cx, cy, step, mouseX, mouseY);

        RenderSystem.enableDepthTest();
    }

    private void renderBackgroundGrid(GuiGraphics graphics, float cx, float cy, float step) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        float a = (backgroundColor >> 24 & 255) / 255.0F;
        float r = (backgroundColor >> 16 & 255) / 255.0F;
        float g = (backgroundColor >> 8 & 255) / 255.0F;
        float b = (backgroundColor & 255) / 255.0F;

        float[] levels = {1.0f, 0.75f, 0.5f, 0.25f};
        for (float level : levels) {
            float currentRadius = radius * level;
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            // Start TRIANGLE_FAN from center to ensure it is filled
            buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            buffer.vertex(matrix, cx, cy, 0).color(r, g, b, a).endVertex();

            for (int j = 0; j <= attributes.size(); j++) {
                float angle = (j % attributes.size()) * step - (float) Math.PI / 2;
                float vx = cx + (Mth.cos(angle) * currentRadius);
                float vy = cy + (Mth.sin(angle) * currentRadius);
                buffer.vertex(matrix, vx, vy, 0).color(r, g, b, a).endVertex();
            }
            tesselator.end();
        }
    }

    private void renderEntityLayer(GuiGraphics graphics, float cx, float cy, float step, LivingEntity entity, int colorInt, float alphaMult) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        float a = ((colorInt >> 24 & 255) / 255.0F) * alphaMult;
        float r = (colorInt >> 16 & 255) / 255.0F;
        float g = (colorInt >> 8 & 255) / 255.0F;
        float b = (colorInt & 255) / 255.0F;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // --- FILLED POLYGON ---
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        // Center vertex ensures the polygon is "filled" (recheado)
        buffer.vertex(matrix, cx, cy, 0).color(r, g, b, a * 0.5f).endVertex();

        for (int i = 0; i <= attributes.size(); i++) {
            AttributeDefinition entry = attributes.get(i % attributes.size());
            float ratio = getAttributeRatio(entity, entry);
            float angle = (i % attributes.size()) * step - (float) Math.PI / 2;

            float vx = cx + (Mth.cos(angle) * (radius * ratio));
            float vy = cy + (Mth.sin(angle) * (radius * ratio));
            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, a).endVertex();
        }
        tesselator.end();

        // --- OUTLINE ---
        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i <= attributes.size(); i++) {
            AttributeDefinition entry = attributes.get(i % attributes.size());
            float ratio = getAttributeRatio(entity, entry);
            float angle = (i % attributes.size()) * step - (float) Math.PI / 2;

            float vx = cx + (Mth.cos(angle) * (radius * ratio));
            float vy = cy + (Mth.sin(angle) * (radius * ratio));
            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, 1.0f).endVertex();
        }
        tesselator.end();
    }

    private float getAttributeRatio(LivingEntity entity, AttributeDefinition entry) {
        if (entity == null) return 0.05f;
        AttributeInstance inst = entity.getAttribute(entry.attribute);
        if (inst == null) return 0.05f;

        double val = inst.getValue();
        // Speed normalization for fair comparison
        if (entry.attribute == Attributes.MOVEMENT_SPEED) val *= 10.0;

        return (float) Mth.clamp(val / entry.maxValue, 0.05, 1.0);
    }

    private void renderLabelsAndInteraction(GuiGraphics graphics, float cx, float cy, float step, int mx, int my) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < attributes.size(); i++) {
            AttributeDefinition entry = attributes.get(i);
            float angle = i * step - (float) Math.PI / 2;
            int lx = (int) (cx + (Mth.cos(angle) * (radius + 15)));
            int ly = (int) (cy + (Mth.sin(angle) * (radius + 15)));

            graphics.drawCenteredString(font, entry.label, lx, ly - 4, 0xFFFFFF);

            int width = font.width(entry.label);
            if (mx >= lx - width / 2 && mx <= lx + width / 2 && my >= ly - 10 && my <= ly + 5) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(entry.attribute.getDescriptionId()).append(": " + formatValue(reference, entry)));
                if (comparator != null) {
                    tooltip.add(Component.literal("Reference: " + formatValue(comparator, entry)).withStyle(s -> s.withColor(0xAAAAAA)));
                }
                graphics.renderComponentTooltip(font, tooltip, mx, my);
            }
        }
    }

    private String formatValue(LivingEntity entity, AttributeDefinition entry) {
        if (entity == null) return "0.00";
        AttributeInstance inst = entity.getAttribute(entry.attribute);
        if (inst == null) return "0.00";
        double val = inst.getValue();
        if (entry.attribute == Attributes.MOVEMENT_SPEED) val *= 10.0;
        return String.format("%.2f", val);
    }

    private record AttributeDefinition(Attribute attribute, String label, double maxValue) {}
}