package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.Color;
import java.util.List;

public class EntityAttributeRadialWidget extends Widget {

    private int radius;
    private LivingEntity reference;
    private LivingEntity comparator;

    // Cores usando java.awt.Color
    public Color backGroundColor = new Color(128, 128, 128, 255); // Cinza transparente
    public Color referenceColor = new Color(255, 255, 0, 255); // Cinza transparente
    public Color comparisonColor = new Color(0, 255, 150, 255);   // Verde/Ciano para os dados

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
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (reference == null || comparator == null) return;

        // Filtra atributos que ambas possuem
        List<AttributeInstance> sharedAttrs = reference.getAttributes().getSyncableAttributes().stream()
                .filter(i -> comparator.getAttributes().hasAttribute(i.getAttribute()))
                .toList();

        int segments = sharedAttrs.size();
        if (segments < 3) return; // Precisa de pelo menos 3 para formar uma face

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        Matrix4f matrix = pGuiGraphics.pose().last().pose();

        // --- CAMADA 0: FULL BACKGROUND (Base da Reference) ---
        renderRadialLayer(bufferBuilder, matrix, sharedAttrs, true, backGroundColor);
        tesselator.end();

        // --- CAMADA 1: BASE (Base da Reference) ---
        renderRadialLayer(bufferBuilder, matrix, sharedAttrs, false, referenceColor);
        tesselator.end();

        // --- CAMADA 2: OVERLAY (Valores da Comparator) ---
        renderRadialLayer(bufferBuilder, matrix, sharedAttrs, false, comparisonColor);
        tesselator.end();

        RenderSystem.disableBlend();
    }

    /**
     * @param isReference Se true, usa o valor da 'reference'. Se false, usa da 'comparator'.
     */
    private void renderRadialLayer(BufferBuilder buffer, Matrix4f matrix, List<AttributeInstance> attributes, boolean isReference, Color color) {
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        // Centro do Polígono
        buffer.vertex(matrix, 0, 0, 0)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();

        int segments = attributes.size();
        for (int i = 0; i <= segments; i++) {
            AttributeInstance currentAttr = attributes.get(i % segments);

            // Lógica de escala: Quão longe do centro o ponto fica?
            // Se for a referência (fundo), usamos o raio máximo (1.0).
            // Se for o comparador, calculamos a razão entre os valores.
            float scale = 1.0f;
            if (!isReference) {
                double refVal = currentAttr.getValue();
                double compVal = comparator.getAttributeValue(currentAttr.getAttribute());
                // Evita divisão por zero e limita o gráfico ao raio máximo
                scale = (refVal > 0) ? (float) Math.min(compVal / refVal, 1.2f) : 0.1f;
            }

            float angle = (float) (i * 2 * Math.PI / segments);
            float x = (float) (Math.cos(angle) * (radius * scale));
            float y = (float) (Math.sin(angle) * (radius * scale));

            buffer.vertex(matrix, x, y, 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .endVertex();
        }
    }
}