package net.foxyas.changedaddon.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.List;

public class RenderUtil {

    private static final int FULL_LIGHT = LightTexture.FULL_BRIGHT; // Lightmap value for full brightness

    /**
     * Renders a path line with world-space anchoring and HSV gradient.
     */
    public static void renderPathAsLine(PoseStack poseStack, Vec3 camPos, Path path) {
        poseStack.pushPose();

        // Anchor the rendering to world coordinates 0,0,0
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        //RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.lineWidth(5.0f);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        bufferbuilder.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < path.getNodeCount(); ++i) {
            Node node = path.getNode(i);

            // Generate a gradient based on the node index
            float f = (float) i / (float) path.getNodeCount() * 0.33F;
            int colorRGB = i == 0 ? 0x00FF00 : Mth.hsvToRgb(f, 0.9F, 0.9F);
            int r = (colorRGB >> 16) & 255;
            int g = (colorRGB >> 8) & 255;
            int b = colorRGB & 255;

            // Render vertex with a small Y offset to prevent Z-fighting with the floor
            bufferbuilder.vertex(poseStack.last().pose(),
                            (float) node.x + 0.5f,
                            (float) node.y + 0.1f,
                            (float) node.z + 0.5f)
                    .color(r, g, b, 255)
                    .endVertex();
        }

        // Restore states to prevent graphical glitches in other parts of the game
        tesselator.end();
        poseStack.popPose();
        // RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    /**
     * Renders a single vertex with full light and no overlay.
     *
     * @param builder      The vertex consumer
     * @param poseMatrix   The pose matrix (4x4)
     * @param normalMatrix The normal matrix (3x3)
     * @param x            X position
     * @param y            Y position
     * @param z            Z position
     * @param r            Red (0–255)
     * @param g            Green (0–255)
     * @param b            Blue (0–255)
     * @param u            Texture U coordinate
     * @param v            Texture V coordinate
     */
    public static void vertex(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                              float x, float y, float z, int r, int g, int b, float u, float v) {
        builder.vertex(poseMatrix, x, y, z)
                .color(r, g, b, 255)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(FULL_LIGHT)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    /**
     * Same as vertex(), but allows setting alpha.
     */
    public static void vertex(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                              float x, float y, float z, int r, int g, int b, int a, float u, float v) {
        builder.vertex(poseMatrix, x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(FULL_LIGHT)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    /**
     * Advanced version allowing control over light and overlay.
     */
    public static void vertex(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                              float x, float y, float z, int r, int g, int b, int a, float u, float v,
                              int overlay, int light) {
        builder.vertex(poseMatrix, x, y, z)
                .color(r, g, b, a)
                .uv(u, v)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    /**
     * Draws a flat quad on the XZ plane (horizontal floor) facing upward (Y+).
     *
     * @param origin The bottom-left corner of the quad
     * @param width  Width along the X axis
     * @param depth  Depth along the Z axis
     */
    public static void drawQuadXZ(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 origin, float width, float depth, int r, int g, int b) {
        float x = (float) origin.x;
        float y = (float) origin.y;
        float z = (float) origin.z;

        vertex(builder, poseMatrix, normalMatrix, x, y, z, r, g, b, 0f, 1f);
        vertex(builder, poseMatrix, normalMatrix, x + width, y, z, r, g, b, 1f, 1f);
        vertex(builder, poseMatrix, normalMatrix, x + width, y, z + depth, r, g, b, 1f, 0f);
        vertex(builder, poseMatrix, normalMatrix, x, y, z + depth, r, g, b, 0f, 0f);
    }

    /**
     * Draws a flat quad on the XY plane (vertical wall facing Z direction).
     *
     * @param origin The bottom-left corner of the quad
     * @param width  Width along the X axis
     * @param height Height along the Y axis
     */
    public static void drawQuadXY(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 origin, float width, float height, int r, int g, int b) {
        float x = (float) origin.x;
        float y = (float) origin.y;
        float z = (float) origin.z;

        vertex(builder, poseMatrix, normalMatrix, x, y, z, r, g, b, 0f, 1f);
        vertex(builder, poseMatrix, normalMatrix, x + width, y, z, r, g, b, 1f, 1f);
        vertex(builder, poseMatrix, normalMatrix, x + width, y + height, z, r, g, b, 1f, 0f);
        vertex(builder, poseMatrix, normalMatrix, x, y + height, z, r, g, b, 0f, 0f);
    }

    /**
     * Draws a flat quad on the YZ plane (vertical wall facing X direction).
     *
     * @param origin The bottom-left corner of the quad
     * @param height Height along the Y axis
     * @param depth  Depth along the Z axis
     */
    public static void drawQuadYZ(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 origin, float height, float depth, int r, int g, int b) {
        float x = (float) origin.x;
        float y = (float) origin.y;
        float z = (float) origin.z;

        vertex(builder, poseMatrix, normalMatrix, x, y, z, r, g, b, 0f, 1f);
        vertex(builder, poseMatrix, normalMatrix, x, y, z + depth, r, g, b, 1f, 1f);
        vertex(builder, poseMatrix, normalMatrix, x, y + height, z + depth, r, g, b, 1f, 0f);
        vertex(builder, poseMatrix, normalMatrix, x, y + height, z, r, g, b, 0f, 0f);
    }

    public static void vertex(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                              float x, float y, float z, Color color, float u, float v) {
        builder.vertex(poseMatrix, x, y, z)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(FULL_LIGHT)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    /**
     * Draws a flat quad on the XZ plane (horizontal floor) facing upward (Y+).
     */
    public static void drawQuadXZ(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 origin, float width, float depth, List<Vec2> uvs, Color fromColor, Color toColor) {
        float x = (float) origin.x;
        float y = (float) origin.y;
        float z = (float) origin.z;

        vertex(builder, poseMatrix, normalMatrix, x, y, z, fromColor, uvs.get(0).x, uvs.get(0).y);
        vertex(builder, poseMatrix, normalMatrix, x + width, y, z, fromColor, uvs.get(1).x, uvs.get(1).y);
        vertex(builder, poseMatrix, normalMatrix, x + width, y, z + depth, toColor, uvs.get(2).x, uvs.get(2).y);
        vertex(builder, poseMatrix, normalMatrix, x, y, z + depth, toColor, uvs.get(3).x, uvs.get(3).y);
    }

    /**
     * Draws a flat quad on the XY plane (vertical wall facing Z direction).
     */
    public static void drawQuadXY(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 origin, float width, float height, List<Vec2> uvs, Color fromColor, Color toColor) {
        float x = (float) origin.x;
        float y = (float) origin.y;
        float z = (float) origin.z;
        vertex(builder, poseMatrix, normalMatrix, x, y, z, fromColor, uvs.get(0).x, uvs.get(0).y);
        vertex(builder, poseMatrix, normalMatrix, x + width, y, z, fromColor, uvs.get(1).x, uvs.get(1).y);
        vertex(builder, poseMatrix, normalMatrix, x + width, y + height, z, toColor, uvs.get(2).x, uvs.get(2).y);
        vertex(builder, poseMatrix, normalMatrix, x, y + height, z, toColor, uvs.get(3).x, uvs.get(3).y);
    }

    /**
     * Draws a flat quad on the YZ plane (vertical wall facing X direction).
     */
    public static void drawQuadYZ(VertexConsumer builder, Matrix4f poseMatrix, Matrix3f normalMatrix,
                                  Vec3 origin, float height, float depth, List<Vec2> uvs, Color fromColor, Color toColor) {
        float x = (float) origin.x;
        float y = (float) origin.y;
        float z = (float) origin.z;

        vertex(builder, poseMatrix, normalMatrix, x, y, z, fromColor, uvs.get(0).x, uvs.get(0).y);
        vertex(builder, poseMatrix, normalMatrix, x, y, z + depth, fromColor, uvs.get(1).x, uvs.get(1).y);
        vertex(builder, poseMatrix, normalMatrix, x, y + height, z + depth, toColor, uvs.get(2).x, uvs.get(2).y);
        vertex(builder, poseMatrix, normalMatrix, x, y + height, z, toColor, uvs.get(3).x, uvs.get(3).y);
    }

    /**
     * Renderiza uma barra de progresso circular.
     *
     * @param guiGraphics Instância do GuiGraphics do 1.20.1
     * @param texture     ResourceLocation da textura (ex: textura da barra cheia)
     * @param x           Centro X da barra na tela
     * @param y           Centro Y da barra na tela
     * @param radius      Raio da barra em pixels
     * @param progress    Progresso de 0.0f a 1.0f (0% a 100%)
     * @param segments    Número de segmentos para suavidade (ex: 32 a 64)
     */
    public static void drawCircularProgressBar(GuiGraphics guiGraphics, ResourceLocation texture, float x, float y, float radius, float progress, int segments) {
        if (progress <= 0.0f) return;
        if (progress > 1.0f) progress = 1.0f;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();

        Matrix4f matrix = guiGraphics.pose().last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        // Inicia o desenho usando TRIANGLE_FAN
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX);

        // Vertice central (ponto de origem da pizza)
        buffer.vertex(matrix, x, y, 0).uv(0.5f, 0.5f).endVertex();

        // Ângulo total preenchido (em radianos)
        // Começa no topo (-90 graus / -PI/2) e roda no sentido horário
        float maxAngle = (float) (progress * 2 * Math.PI);
        int currentSegments = Math.max(1, (int) (segments * progress));

        for (int i = 0; i <= currentSegments; i++) {
            float angle = (float) (-Math.PI / 2) + (maxAngle * i / currentSegments);
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);

            // Posição no HUD/Screen
            float vx = x + cos * radius;
            float vy = y + sin * radius;

            // Mapeamento de UV (assumindo que o centro da textura circular é 0.5, 0.5)
            float u = 0.5f + cos * 0.5f;
            float v = 0.5f + sin * 0.5f;

            buffer.vertex(matrix, vx, vy, 0).uv(u, v).endVertex();
        }

        tesselator.end();
        RenderSystem.disableBlend();
    }
}
