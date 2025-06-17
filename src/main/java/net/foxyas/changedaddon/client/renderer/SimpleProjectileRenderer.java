package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class SimpleProjectileRenderer<T extends AbstractArrow> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ChangedAddonMod.MODID, "textures/entities/empty.png");

    public SimpleProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.pushPose();

        // Obtem o tamanho da hitbox
        var box = entity.getBoundingBox();
        float width = (float)(box.maxX - box.minX);
        float height = (float)(box.maxY - box.minY);
        float depth = (float)(box.maxZ - box.minZ);

        // Centraliza o cubo na posição da entidade
        poseStack.translate(-width / 2.0, 0.0, -depth / 2.0);

        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer consumer = buffer.getBuffer(RenderType.lines());

        drawWireframeCube(matrix, consumer,
                0.0f, 0.0f, 0.0f,
                width, height, depth,
                1.0f, 1.0f, 1.0f, 1.0f); // Vermelho

        poseStack.popPose();
    }

    private void drawWireframeCube(Matrix4f matrix, VertexConsumer consumer,
                                   float minX, float minY, float minZ,
                                   float maxX, float maxY, float maxZ,
                                   float r, float g, float b, float a) {

        // Linhas da parte inferior
        consumer.vertex(matrix, minX, minY, minZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, minX, minY, minZ).color(r, g, b, a).endVertex();

        // Linhas da parte superior
        consumer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a).endVertex();

        // Linhas verticais
        consumer.vertex(matrix, minX, minY, minZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, minX, maxY, minZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, maxX, minY, minZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, maxX, maxY, minZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, maxX, maxY, maxZ).color(r, g, b, a).endVertex();

        consumer.vertex(matrix, minX, minY, maxZ).color(r, g, b, a).endVertex();
        consumer.vertex(matrix, minX, maxY, maxZ).color(r, g, b, a).endVertex();
    }



    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
