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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RPG-style Radial Widget that automatically compares attributes between two entities.
 * No manual lists required.
 */
public class DynamicRadialWidget extends Widget {
    private final int radius;
    private int mainColor;
    private int compColor = 0x60555555;
    private int bgColor = 0x801A1A1A;

    private LivingEntity reference;
    private LivingEntity comparator;

    public DynamicRadialWidget(int radius, int mainColor) {
        this.radius = radius;
        this.mainColor = mainColor;
        this.reference = Minecraft.getInstance().player;
    }

    public DynamicRadialWidget setEntities(LivingEntity reference, LivingEntity comparator) {
        this.reference = reference;
        this.comparator = comparator;
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (reference == null) return;

        // Get all active attributes from the reference entity
        Collection<AttributeInstance> instances = reference.getAttributes().getSyncableAttributes();
        List<AttributeInstance> relevantAttrs = instances.stream()
                .filter((i) -> comparator.getAttributes().hasAttribute(i.getAttribute()))
                //.limit(8) // Limit to 8 to keep the RPG mesh readable
                .collect(Collectors.toList());

        int count = relevantAttrs.size();
        if (count < 3) return;

        float cx = this.getOrigin().x + radius;
        float cy = this.getOrigin().y + radius;
        float step = (float) (Math.PI * 2) / count;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();

        // 1. Background Mesh (Full Shape)
        renderMesh(graphics, cx, cy, step, null, bgColor, 1.0f, relevantAttrs, true);

        // 2. Comparator Mesh (Enemy/Reference)
        if (comparator != null) {
            renderMesh(graphics, cx, cy, step, comparator, compColor, 0.5f, relevantAttrs, false);
        }

        // 3. Reference Mesh (Player/Target)
        renderMesh(graphics, cx, cy, step, reference, mainColor, 0.8f, relevantAttrs, false);

        // 4. Overlay (Dots, Names and Tooltips)
        renderOverlay(graphics, cx, cy, step, mouseX, mouseY, relevantAttrs);

        RenderSystem.enableDepthTest();
    }

    private void renderMesh(GuiGraphics graphics, float cx, float cy, float step, LivingEntity entity,
                            int color, float alpha, List<AttributeInstance> attrs, boolean isBg) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        float a = ((color >> 24 & 255) / 255.0F) * alpha;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // Solid Fill
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, 0).color(r, g, b, a).endVertex();

        for (int i = 0; i <= attrs.size(); i++) {
            int idx = i % attrs.size();
            Attribute attr = attrs.get(idx).getAttribute();
            // Use a dynamic max value based on the attribute's base or common RPG caps
            double max = getDynamicMax(attr);
            float ratio = isBg ? 1.0f : getRatio(entity, attr, max);
            float angle = idx * step - (float) Math.PI / 2;

            buffer.vertex(matrix, cx + (Mth.cos(angle) * (radius * ratio)),
                            cy + (Mth.sin(angle) * (radius * ratio)), 0)
                    .color(r, g, b, a).endVertex();
        }
        tesselator.end();
    }

    private float getRatio(LivingEntity entity, Attribute attr, double max) {
        if (entity == null) return 0.05f;
        AttributeInstance inst = entity.getAttribute(attr);
        if (inst == null) return 0.05f;
        double val = inst.getValue();
        if (attr == Attributes.MOVEMENT_SPEED) val *= 10.0;
        return (float) Mth.clamp(val / max, 0.05, 1.0);
    }

    private double getDynamicMax(Attribute attr) {
        if (attr == Attributes.MAX_HEALTH) return 40.0;
        if (attr == Attributes.ARMOR) return 20.0;
        if (attr == Attributes.ATTACK_DAMAGE) return 15.0;
        if (attr == Attributes.MOVEMENT_SPEED) return 4.0; // Scaled
        return 20.0; // Default RPG scale
    }

    private void renderOverlay(GuiGraphics graphics, float cx, float cy, float step, int mx, int my, List<AttributeInstance> attrs) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < attrs.size(); i++) {
            Attribute attr = attrs.get(i).getAttribute();
            float angle = i * step - (float) Math.PI / 2;

            float dotX = cx + (Mth.cos(angle) * radius);
            float dotY = cy + (Mth.sin(angle) * radius);
            graphics.fill((int)dotX - 1, (int)dotY - 1, (int)dotX + 1, (int)dotY + 1, 0xFFFFFFFF);

            String name = Component.translatable(attr.getDescriptionId()).getString();
            int lx = (int) (cx + (Mth.cos(angle) * (radius + 18)));
            int ly = (int) (cy + (Mth.sin(angle) * (radius + 18)));
            graphics.drawCenteredString(font, name, lx, ly - 4, 0xCCCCCC);

            if (mx >= lx - 20 && mx <= lx + 20 && my >= ly - 10 && my <= ly + 10) {
                renderTooltip(graphics, font, mx, my, attr);
            }
        }
    }

    private void renderTooltip(GuiGraphics graphics, Font font, int mx, int my, Attribute attr) {
        List<Component> lines = new java.util.ArrayList<>();
        lines.add(Component.translatable(attr.getDescriptionId()).append(": " + getVal(reference, attr)));
        if (comparator != null) {
            lines.add(Component.literal("Comparator: " + getVal(comparator, attr)).withStyle(s -> s.withColor(0x777777)));
        }
        graphics.renderComponentTooltip(font, lines, mx, my);
    }

    private String getVal(LivingEntity e, Attribute a) {
        AttributeInstance i = e.getAttribute(a);
        if (i == null) return "0.0";
        double v = i.getValue();
        if (a == Attributes.MOVEMENT_SPEED) v *= 10.0;
        return String.format("%.1f", v);
    }
}