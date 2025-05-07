
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.client.model.LatexSnepModel;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorNoneModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import org.jetbrains.annotations.NotNull;

public class LatexSnepRenderer extends AdvancedHumanoidRenderer<LatexSnepEntity, LatexSnepModel, ArmorNoneModel<LatexSnepEntity>> {
	public LatexSnepRenderer(EntityRendererProvider.Context context) {
		super(context, new LatexSnepModel(context.bakeLayer(LatexSnepModel.LAYER_LOCATION)),
				ArmorNoneModel::new, ArmorNoneModel.INNER_ARMOR, ArmorNoneModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new CustomCatEyesLayer<>(this, new ResourceLocation("changed_addon:textures/entities/latex_snep_right_eye.png"), new ResourceLocation("changed_addon:textures/entities/latex_snep_left_eye.png") , new ResourceLocation("changed_addon:textures/entities/latex_snep_sclera.png")));
	}


	/*
	@Override
	public void render(LatexSnepEntity entity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		super.render(entity, yRot, partialTicks, poseStack, bufferSource, packedLight);
		poseStack.pushPose();
		if (entity.getPose() == Pose.SWIMMING || entity.getPose() == Pose.FALL_FLYING) {
			poseStack.mulPose(Vector3f.XP.rotationDegrees(DEBUG.HeadPosX));
			poseStack.mulPose(Vector3f.XN.rotationDegrees(DEBUG.HeadPosY));
			poseStack.mulPose(Vector3f.YP.rotationDegrees(DEBUG.HeadPosZ));
			poseStack.mulPose(Vector3f.YN.rotationDegrees(DEBUG.HeadPosT));
			poseStack.mulPose(Vector3f.ZP.rotationDegrees(DEBUG.HeadPosV));
			poseStack.mulPose(Vector3f.ZN.rotationDegrees(DEBUG.HeadPosB));
		}
		poseStack.popPose();
	}
	*/

	@Override
	public ResourceLocation getTextureLocation(LatexSnepEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_snep.png");
	}


	@Override
	protected float getFlipDegrees(LatexSnepEntity entity) {
		return super.getFlipDegrees(entity);
	}

	@Override
	protected boolean isEntityUprightType(@NotNull LatexSnepEntity entity) {
		return false;
	}



	private static class CustomCatEyesLayer<M extends EntityModel<T>, T extends ChangedEntity> extends EyesLayer<T, M> {
		private final RenderType rightEyesRenderType;
		private final ResourceLocation rightEyeTexture;

		private final RenderType leftEyesRenderType;
		private final ResourceLocation leftEyeTexture;
		private final RenderType ScleraRenderType;
		private final ResourceLocation scleraTexture;

		public CustomCatEyesLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation rightEyeTexture, ResourceLocation leftEyeTexture, ResourceLocation scleraTexture) {
			super(p_116964_);
			this.rightEyesRenderType = RenderType.eyes(rightEyeTexture);
			this.rightEyeTexture = rightEyeTexture;
			this.leftEyesRenderType = RenderType.eyes(leftEyeTexture);
			this.leftEyeTexture = leftEyeTexture;
			this.ScleraRenderType = RenderType.eyes(scleraTexture);
			this.scleraTexture = scleraTexture;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			var Colors = entity.getBasicPlayerInfo();
			VertexConsumer vertexConsumer = bufferSource.getBuffer(this.rightEyesRenderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, Colors.getRightIrisColor().red(), Colors.getRightIrisColor().green(), Colors.getRightIrisColor().blue(), 1);
			VertexConsumer vertexConsumer2 = bufferSource.getBuffer(this.leftEyesRenderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer2, 15728640, OverlayTexture.NO_OVERLAY, Colors.getLeftIrisColor().red(), Colors.getLeftIrisColor().green(), Colors.getLeftIrisColor().blue(), 1);
			VertexConsumer vertexConsumer3 = bufferSource.getBuffer(this.ScleraRenderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer3, 15728640, OverlayTexture.NO_OVERLAY,Colors.getScleraColor().red(), Colors.getScleraColor().green(), Colors.getScleraColor().blue(), 1.0F);
		}


		@Override
		public @NotNull RenderType renderType() {
			return this.rightEyesRenderType;
		}
	}
}
