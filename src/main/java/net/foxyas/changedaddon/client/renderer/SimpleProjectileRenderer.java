package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.ModelLuminarCrystalSpearModel;
import net.foxyas.changedaddon.client.model.projectile.SimpleProjectileModel;
import net.foxyas.changedaddon.entity.LuminarCrystalSpearEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.AbstractArrow;

public class SimpleProjectileRenderer<T extends AbstractArrow, M extends EntityModel<T>> extends EntityRenderer<T> {

    private static final ResourceLocation TEXTURE = ResourceLocation.parse(ChangedAddonMod.MODID, "textures/entities/white_ball_projectile.png");
    private final EntityModel<T> model;

    public SimpleProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0F;
        this.model = new SimpleProjectileModel<>(context.bakeLayer(SimpleProjectileModel.LAYER_LOCATION));
    }

    @Override
    protected int getBlockLightLevel(T p_114496_, BlockPos p_114497_) {
        return super.getBlockLightLevel(p_114496_, p_114497_);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        float f = Mth.rotlerp(entity.yRotO, entity.getYRot(), partialTicks);
        float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float f2 = (float)entity.tickCount + partialTicks;
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.15F, 0.0D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(f2 * 0.15F) * 360.0F));
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.eyes(TEXTURE));

        // Renderiza o modelo corretamente
        this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }



        public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
