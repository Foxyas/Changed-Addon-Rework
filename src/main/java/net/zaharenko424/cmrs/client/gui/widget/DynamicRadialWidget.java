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
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    public DynamicRadialWidget setComparator(LivingEntity comparator) {
        this.comparator = comparator;
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (reference == null) return;

        // Filtra atributos que ambas as entidades possuem
        Collection<AttributeInstance> instances = reference.getAttributes().getSyncableAttributes();
        List<AttributeInstance> relevantAttrs = instances.stream()
                .filter((i) -> comparator != null && comparator.getAttributes().hasAttribute(i.getAttribute()))
                .collect(Collectors.toList());

        int count = relevantAttrs.size();
        if (count < 3) return;

        float cx = this.getOrigin().x + radius;
        float cy = this.getOrigin().y + radius;
        float step = (float) (Math.PI * 2) / count;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        // 1. Background Mesh (Sempre valor 1.0 = Full Dot)
        renderMesh(graphics, cx, cy, step, null, bgColor, 1.0f, relevantAttrs, true);
        renderBackground(graphics, cx, cy, step, count);

        // 2. Comparator Mesh
        if (comparator != null) {
            renderMesh(graphics, cx, cy, step, comparator, compColor, 0.5f, relevantAttrs, false);
        }

        // 3. Reference Mesh
        renderMesh(graphics, cx, cy, step, reference, mainColor, 0.8f, relevantAttrs, false);

        // 4. Overlay (Dots e Nomes)
        renderOverlay(graphics, cx, cy, step, mouseX, mouseY, relevantAttrs);

        RenderSystem.disableBlend();
    }

    private void renderBackground(GuiGraphics graphics, float cx, float cy, float step, int count) {
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

            for (int j = 0; j <= count; j++) {
                float angle = (j % count) * step - (float) Math.PI / 2;
                float vx = cx + (Mth.cos(angle) * currentRadius);
                float vy = cy + (Mth.sin(angle) * currentRadius);
                buffer.vertex(matrix, vx, vy, 0).color(brightness, brightness, brightness, alpha).endVertex();
            }
            tesselator.end();
        }

        // 2. GRID LINES (WEB)
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        for (int i = 0; i < count; i++) {
            float angle = i * step - (float) Math.PI / 2;
            float x2 = cx + (Mth.cos(angle) * radius);
            float y2 = cy + (Mth.sin(angle) * radius);

            // Axis lines
            buffer.vertex(matrix, cx, cy, 0).color(1f, 1f, 1f, 0.2f).endVertex();
            buffer.vertex(matrix, x2, y2, 0).color(1f, 1f, 1f, 0.2f).endVertex();
        }
        tesselator.end();
    }

    private void renderMesh(GuiGraphics graphics, float cx, float cy, float step, LivingEntity entity,
                            int color, float alphaMult, List<AttributeInstance> attrs, boolean isBg) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        Matrix4f matrix = graphics.pose().last().pose();

        float a = ((color >> 24 & 255) / 255.0F) * alphaMult;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        // Garante que o shader de cor esteja ativo para este buffer
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        buffer.vertex(matrix, cx, cy, 0).color(r, g, b, a).endVertex();

        for (int i = 0; i <= attrs.size(); i++) {
            int idx = i % attrs.size();
            // Lógica de escala: 1.0 (ou valor do atributo) direto para o multiplicador do raio
            float ratio = isBg ? 1.0f : getRawValue(entity, attrs.get(idx).getAttribute());
            float angle = idx * step - (float) Math.PI / 2;

            float vx = cx + (Mth.cos(angle) * (radius * ratio));
            float vy = cy + (Mth.sin(angle) * (radius * ratio));
            buffer.vertex(matrix, vx, vy, 0).color(r, g, b, a).endVertex();
        }
        tesselator.end();
    }

    private float getRawValue(LivingEntity entity, Attribute attr) {
        if (entity == null) return 0.0f;
        AttributeInstance inst = entity.getAttribute(attr);
        // Retorna o valor bruto. Se for 1.0, encosta no dot. Se for 2.0, passa do dot.
        return inst != null ? (float) inst.getValue() : 0.0f;
    }

    private void renderOverlay(GuiGraphics graphics, float cx, float cy, float step, int mx, int my, List<AttributeInstance> attrs) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < attrs.size(); i++) {
            Attribute attr = attrs.get(i).getAttribute();
            float angle = i * step - (float) Math.PI / 2;

            // Dots fixos na borda do "background" (raio 1.0)
            float dotX = cx + (Mth.cos(angle) * radius);
            float dotY = cy + (Mth.sin(angle) * radius);
            graphics.fill((int) dotX - 1, (int) dotY - 1, (int) dotX + 1, (int) dotY + 1, 0xFFFFFFFF);

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
        lines.add(Component.translatable(attr.getDescriptionId()).append(": " + String.format("%.2f", getRawValue(reference, attr))));
        if (comparator != null) {
            lines.add(Component.literal("Comparator: " + String.format("%.2f", getRawValue(comparator, attr))).withStyle(s -> s.withColor(0x777777)));
        }
        graphics.renderComponentTooltip(font, lines, mx, my);
    }
}