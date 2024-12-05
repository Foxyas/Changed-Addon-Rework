
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import org.jetbrains.annotations.NotNull;

public class LatexSnepRenderer extends AdvancedHumanoidRenderer<LatexSnepEntity, LatexSnepModel, ArmorNoneModel<LatexSnepEntity>> {
	public LatexSnepRenderer(EntityRendererProvider.Context context) {
		super(context, new LatexSnepModel(context.bakeLayer(LatexSnepModel.LAYER_LOCATION)),
				ArmorNoneModel::new, ArmorNoneModel.INNER_ARMOR, ArmorNoneModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new CustomCatEyesLayer<>(this, new ResourceLocation("changed_addon:textures/entities/latex_snep_right_eye.png"), new ResourceLocation("changed_addon:textures/entities/latex_snep_left_eye.png") , new ResourceLocation("changed_addon:textures/entities/latex_snep_sclera.png")));
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnepEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_snep.png");
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
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer2, 15728640, OverlayTexture.NO_OVERLAY, Colors.getRightIrisColor().red(), Colors.getRightIrisColor().green(), Colors.getRightIrisColor().blue(), 1);
			VertexConsumer vertexConsumer3 = bufferSource.getBuffer(this.ScleraRenderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer3, 15728640, OverlayTexture.NO_OVERLAY,Colors.getScleraColor().red(), Colors.getScleraColor().green(), Colors.getScleraColor().blue(), 1.0F);
		}


		@Override
		public @NotNull RenderType renderType() {
			return this.rightEyesRenderType;
		}
	}
}
