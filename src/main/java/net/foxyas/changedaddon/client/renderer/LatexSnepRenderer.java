
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.LatexSnepModel;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorNoneModel;
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
		this.addLayer(new CustomCatEyesLayer<>(this, new ResourceLocation("changed_addon:textures/entities/latex_snep_eyes.png") , new ResourceLocation("changed_addon:textures/entities/latex_snep_sclera.png")));
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnepEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_snep.png");
	}

	private static class CustomCatEyesLayer<M extends EntityModel<T>, T extends ChangedEntity> extends EyesLayer<T, M> implements FirstPersonLayer<T> {
		private final RenderType EyesRenderType;
		private final ResourceLocation eyesTexture;
		private final RenderType ScleraRenderType;
		private final ResourceLocation scleraTexture;

		public CustomCatEyesLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation eyesTexture, ResourceLocation scleraTexture) {
			super(p_116964_);
			this.EyesRenderType = RenderType.eyes(eyesTexture);
			this.eyesTexture = eyesTexture;
			this.ScleraRenderType = RenderType.eyes(scleraTexture);
			this.scleraTexture = scleraTexture;
		}
		public ResourceLocation getTexture(int i) {
			return i == 1 ? this.eyesTexture : this.scleraTexture;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer vertexConsumer = bufferSource.getBuffer(this.EyesRenderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			VertexConsumer vertexConsumer2 = bufferSource.getBuffer(this.ScleraRenderType);
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer2, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}


		@Override
		public @NotNull RenderType renderType() {
			return this.EyesRenderType;
		}

		public RenderType getEyesRenderType() {
			return EyesRenderType;
		}

		public RenderType getScleraRenderType() {
			return ScleraRenderType;
		}

		public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector) {
			stack.pushPose();
			stack.scale(1.0002F, 1.0002F, 1.0002F);
			EntityModel<T> var8 = this.getParentModel();
			if (var8 instanceof AdvancedHumanoidModel<?> armedModel) {
				FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.EyesRenderType), 15728880, 1.0F);
				FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.ScleraRenderType), 15728880, 1.0F);
			}
			stack.popPose();
		}
	}
}
