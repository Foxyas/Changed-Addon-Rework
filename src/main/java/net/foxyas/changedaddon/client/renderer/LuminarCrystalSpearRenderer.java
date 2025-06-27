package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.client.model.ModelLuminarCrystalSpearModel;
import net.foxyas.changedaddon.entity.LuminarCrystalSpearEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class LuminarCrystalSpearRenderer extends EntityRenderer<LuminarCrystalSpearEntity> {
	private static final ResourceLocation texture = ResourceLocation.parse("changed_addon:textures/entities/luminar_crystal_spear.png");
	private final ModelLuminarCrystalSpearModel<LuminarCrystalSpearEntity> model;

	public LuminarCrystalSpearRenderer(EntityRendererProvider.Context context) {
		super(context);
		model = new ModelLuminarCrystalSpearModel<LuminarCrystalSpearEntity>(context.bakeLayer(ModelLuminarCrystalSpearModel.LAYER_LOCATION));
	}

	@Override
	public void render(LuminarCrystalSpearEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		poseStack.pushPose();

		// Rotaciona igual ao tridente
		poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
		poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F));

		// Obtém o buffer de renderização correto, incluindo efeito de brilho para encantamentos
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, this.model.renderType(this.getTextureLocation(entity)), false, entity.isFoil());

		// Renderiza o modelo corretamente
		this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		poseStack.popPose();
		super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
	}


	@Override
	public ResourceLocation getTextureLocation(LuminarCrystalSpearEntity entity) {
		return texture;
	}
}
