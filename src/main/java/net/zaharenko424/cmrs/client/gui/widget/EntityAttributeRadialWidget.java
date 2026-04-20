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
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityAttributeRadialWidget extends Widget {
    private int radius;
    private LivingEntity reference;
    private LivingEntity comparator;

    public Color backGroundColor = new Color(40, 40, 40, 150);
    public Color referenceColor = new Color(255, 255, 0, 180);
    public Color comparisonColor = new Color(0, 255, 150, 180);

    public EntityAttributeRadialWidget(int radius) {
        this.radius = radius;
        this.reference = Minecraft.getInstance().player;
    }

    public EntityAttributeRadialWidget setBackGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
        return this;
    }

    public EntityAttributeRadialWidget setReferenceColor(Color referenceColor) {
        this.referenceColor = referenceColor;
        return this;
    }

    public EntityAttributeRadialWidget setComparisonColor(Color comparisonColor) {
        this.comparisonColor = comparisonColor;
        return this;
    }

    public EntityAttributeRadialWidget setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public EntityAttributeRadialWidget setComparator(LivingEntity comparator) {
        this.comparator = comparator;
        return this;
    }

    public EntityAttributeRadialWidget setReference(LivingEntity reference) {
        this.reference = reference;
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mx, int my, float partialTick) {
        if (reference == null || comparator == null) return;

        List<AttributeInstance> sharedAttrs = reference.getAttributes().getSyncableAttributes().stream()
                .filter(i -> comparator.getAttributes().hasAttribute(i.getAttribute()))
                .toList();

        if (sharedAttrs.size() < 3) return;

        float cx = this.getOrigin().x + radius;
        float cy = this.getOrigin().y + radius;
        Matrix4f matrix = graphics.pose().last().pose();

        // --- RENDERIZAÇÃO GEOMÉTRICA ---
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        renderRadialLayer(matrix, cx, cy, sharedAttrs, true, backGroundColor, 0.0f);
        renderRadialLayer(matrix, cx, cy, sharedAttrs, false, referenceColor, 0.01f);
        renderRadialLayer(matrix, cx, cy, sharedAttrs, false, comparisonColor, 0.02f);
        renderRadialOutline(matrix, cx, cy, sharedAttrs, backGroundColor.brighter(), 0.03f);

        RenderSystem.enableCull();

        // --- TEXTOS, PONTOS E TOOLTIPS ---
        renderLabelsAndDots(graphics, cx, cy, sharedAttrs, mx, my);

        RenderSystem.disableBlend();
    }

    private void renderLabelsAndDots(GuiGraphics graphics, float cx, float cy, List<AttributeInstance> attributes, int mx, int my) {
        Font font = Minecraft.getInstance().font;
        int size = attributes.size();
        float step = (float) (Math.PI * 2) / size;

        for (int i = 0; i < size; i++) {
            AttributeInstance attr = attributes.get(i);
            float angle = i * step - (float) Math.PI / 2;

            // Posição do Texto (um pouco além do raio)
            int lx = (int) (cx + (Mth.cos(angle) * (radius + 18)));
            int ly = (int) (cy + (Mth.sin(angle) * (radius + 18)));

            // Posição do "Dot" no gráfico (baseado no valor do Comparator)
            double refVal = attr.getValue();
            double compVal = comparator.getAttributeValue(attr.getAttribute());
            float scale = (refVal > 0) ? (float) Math.min(compVal / refVal, 1.0f) : 0.1f;

            int dx = (int) (cx + (Mth.cos(angle) * (radius * scale)));
            int dy = (int) (cy + (Mth.sin(angle) * (radius * scale)));

            // Desenha o nome do atributo simplificado
            String label = Component.translatable(attr.getAttribute().getDescriptionId()).getString();
            // Corta nomes muito longos se necessário
            if (label.length() > 10) label = label.substring(0, 8) + "..";

            graphics.drawCenteredString(font, label, lx, ly - 4, 0xFFFFFF);

            // Desenha um pequeno ponto visual no vértice do comparador
            graphics.fill(dx - 1, dy - 1, dx + 1, dy + 1, comparisonColor.getRGB());

            // Detecção de Mouse para Tooltip (No texto ou no Dot)
            int textWidth = font.width(label);
            boolean hoveringText = mx >= lx - textWidth/2 && mx <= lx + textWidth/2 && my >= ly - 8 && my <= ly + 8;
            boolean hoveringDot = mx >= dx - 3 && mx <= dx + 3 && my >= dy - 3 && my <= dy + 3;

            if (hoveringText || hoveringDot) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(attr.getAttribute().getDescriptionId()).withStyle(s -> s.withBold(true).withColor(comparisonColor.getRGB())));
                tooltip.add(Component.literal("Target: ").append(String.format("%.2f", compVal)).withStyle(s -> s.withColor(0xFFFFFF)));
                tooltip.add(Component.literal("Player: ").append(String.format("%.2f", refVal)).withStyle(s -> s.withColor(0xAAAAAA)));

                graphics.renderComponentTooltip(font, tooltip, mx, my);
            }
        }
    }

    // - Mantendo a lógica de renderização corrigida anteriormente
    private void renderRadialLayer(Matrix4f matrix, float cx, float cy, List<AttributeInstance> attributes, boolean isBg, Color color, float zOffset) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        float r = color.getRed() / 255f, g = color.getGreen() / 255f, b = color.getBlue() / 255f, a = color.getAlpha() / 255f;

        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, zOffset).color(r, g, b, a).endVertex();

        int size = attributes.size();
        float step = (float) (Math.PI * 2) / size;
        for (int i = 0; i <= size; i++) {
            AttributeInstance attr = attributes.get(i % size);
            float scale = isBg ? 1.0f : (float) Math.min(comparator.getAttributeValue(attr.getAttribute()) / attr.getValue(), 1.0f);
            float angle = i * step - (float) Math.PI / 2;
            buffer.vertex(matrix, cx + (Mth.cos(angle) * (radius * scale)), cy + (Mth.sin(angle) * (radius * scale)), zOffset).color(r, g, b, a).endVertex();
        }
        tesselator.end();
    }

    private void renderRadialOutline(Matrix4f matrix, float cx, float cy, List<AttributeInstance> attributes, Color color, float zOffset) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        int size = attributes.size();
        float step = (float) (Math.PI * 2) / size;
        for (int i = 0; i <= size; i++) {
            float angle = (i % size) * step - (float) Math.PI / 2;
            buffer.vertex(matrix, cx + (Mth.cos(angle) * radius), cy + (Mth.sin(angle) * radius), zOffset)
                    .color(color.getRed()/255f, color.getGreen()/255f, color.getBlue()/255f, 1.0f).endVertex();
        }
        tesselator.end();
    }
}