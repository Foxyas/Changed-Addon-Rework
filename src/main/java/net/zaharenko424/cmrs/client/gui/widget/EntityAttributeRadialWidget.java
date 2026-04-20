package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
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
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
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

    /**
     * Ajusta valores técnicos para escala visual.
     * Multiplica Movement Speed por 10 para alinhar com a percepção do jogador (0.1 -> 1.0).
     */
    private double mayTweakValueToMatchPlayerTransfur(LivingEntity entity, Attribute attr, double rawValue) {
        if (entity instanceof Player player && attr == Attributes.MOVEMENT_SPEED) {
            return rawValue * 10.0;
        }
        return rawValue;
    }

    private float getScaleValue(LivingEntity entity, AttributeInstance refAttr) {
        if (entity == null) return 0.0f;

        Attribute attr = refAttr.getAttribute();
        double vRef = reference.getAttributeValue(attr);
        double vComp = comparator.getAttributeValue(attr);

        // 1. Definimos o "Teto" do gráfico.
        // Usamos o maior valor entre os dois, mas adicionamos uma margem de 20%
        // para o ponto não ficar "colado" na linha do outline.
        double highestCurrent = Math.max(vRef, vComp);

        // 2. Fallback para MaxValue (se disponível e razoável)
        double absoluteMax = 100000; // Limite de sanidade
        if (attr instanceof RangedAttribute ranged) {
            absoluteMax = ranged.getMaxValue();
        }

        // Se o valor real for muito menor que o MaxValue (ex: Vida de 20 vs Max de 1024)
        // a escala relativa (highestCurrent) é melhor.
        // Se o MaxValue for pequeno (ex: Armadura que o max é 30), usamos o MaxValue.
        double effectiveMax;
        if (absoluteMax > highestCurrent * 2 || absoluteMax > 1000) {
            effectiveMax = highestCurrent * 1.2; // 20% de margem extra
        } else {
            effectiveMax = absoluteMax;
        }

        if (highestCurrent >= absoluteMax) {
            effectiveMax = absoluteMax;
        }

        if (effectiveMax <= 0) effectiveMax = 1.0;

        // 3. Tweaks e Clamping
        double tweakedCurrent = mayTweakValueToMatchPlayerTransfur(entity, attr, entity.getAttributeValue(attr));
        double tweakedMax = effectiveMax;//mayTweakValueToMatchPlayerTransfur(entity, attr, effectiveMax);

        // Clamp de 0.1 para não sumir no centro e 1.0 para não sair do círculo
        return (float) Mth.clamp(tweakedCurrent / tweakedMax, 0.1, 1.0);

        // This Code make the Radial Max Scale.
        // this uses a math formula to make it never reach 100% unless if truly 100% of the attribute.
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

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // Camadas: Fundo -> Player -> Comparator -> Outline
        renderRadialLayer(matrix, cx, cy, null, sharedAttrs, true, backGroundColor, 0.0f);
        renderRadialLayer(matrix, cx, cy, reference, sharedAttrs, false, referenceColor, 0.01f);
        renderRadialLayer(matrix, cx, cy, comparator, sharedAttrs, false, comparisonColor, 0.02f);
        renderRadialOutline(matrix, cx, cy, sharedAttrs, backGroundColor.brighter(), 0.03f);

        RenderSystem.enableCull();
        renderLabelsAndDots(graphics, cx, cy, sharedAttrs, mx, my);
        RenderSystem.disableBlend();
    }

    private void renderLabelsAndDots(GuiGraphics graphics, float cx, float cy, List<AttributeInstance> attributes, int mx, int my) {
        Font font = Minecraft.getInstance().font;
        int size = attributes.size();
        float step = (float) (Math.PI * 2) / size;

        for (int i = 0; i < size; i++) {
            AttributeInstance attrInst = attributes.get(i);
            Attribute attr = attrInst.getAttribute();
            float angle = i * step - (float) Math.PI / 2;

            float scaleComp = getScaleValue(comparator, attrInst);
            float cos = Mth.cos(angle);
            float sin = Mth.sin(angle);

            int dx = (int) (cx + (cos * (radius * scaleComp)));
            int dy = (int) (cy + (sin * (radius * scaleComp)));
            int lx = (int) (cx + (cos * (radius + 20)));
            int ly = (int) (cy + (sin * (radius + 20)));

            // Label
            String label = Component.translatable(attr.getDescriptionId()).getString();
            if (label.length() > 10) label = label.substring(0, 8) + "..";
            graphics.drawCenteredString(font, label, lx, ly - 4, 0xFFFFFF);

            // Dot
            Color dotColor = comparisonColor.darker();
            graphics.fill(dx - 2, dy - 2, dx + 2, dy + 2, dotColor.getRGB());

            // Tooltip com valores tweakados para exibição
            if (mx >= lx - 20 && mx <= lx + 20 && my >= ly - 10 && my <= ly + 10 || (mx >= dx - 3 && mx <= dx + 3 && my >= dy - 3 && my <= dy + 3)) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(Component.translatable(attr.getDescriptionId()).withStyle(s -> s.withBold(true).withColor(dotColor.getRGB())));
                tooltip.add(Component.literal("Target: ").append(String.format("%.2f", comparator.getAttributeValue(attr))));
                tooltip.add(Component.literal("Player: ").append(String.format("%.2f", reference.getAttributeValue(attr) * (attr == Attributes.MOVEMENT_SPEED ? 10 : 1))).withStyle(s -> s.withColor(0xAAAAAA)));
                graphics.renderComponentTooltip(font, tooltip, mx, my);
            }
        }
    }

    private void renderRadialLayer(Matrix4f matrix, float cx, float cy, LivingEntity entity, List<AttributeInstance> attributes, boolean isBg, Color color, float zOffset) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        float r = color.getRed() / 255f, g = color.getGreen() / 255f, b = color.getBlue() / 255f, a = color.getAlpha() / 255f;

        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, zOffset).color(r, g, b, a).endVertex();

        int size = attributes.size();
        float step = (float) (Math.PI * 2) / size;
        for (int i = 0; i <= size; i++) {
            AttributeInstance attrInst = attributes.get(i % size);
            float scale = isBg ? 1.0f : getScaleValue(entity, attrInst);
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