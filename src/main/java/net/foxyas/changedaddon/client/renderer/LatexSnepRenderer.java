
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.model.LatexSnepModel;
import net.foxyas.changedaddon.client.model.ModelMirrorWhiteTiger;
import net.foxyas.changedaddon.entity.KetExperiment009BossEntity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
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
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.minecraft.world.entity.HumanoidArm;

public class LatexSnepRenderer extends AdvancedHumanoidRenderer<LatexSnepEntity, LatexSnepModel, ArmorNoneModel<LatexSnepEntity>> {
	public LatexSnepRenderer(EntityRendererProvider.Context context) {
		super(context, new LatexSnepModel(context.bakeLayer(LatexSnepModel.LAYER_LOCATION)),
				ArmorNoneModel::new, ArmorNoneModel.INNER_ARMOR, ArmorNoneModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new CustomCatEyesLayer<>(this, new ResourceLocation("changed_addon:textures/entities/latex_snep_eyes.png")));
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnepEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_snep.png");
	}

	private static class CustomCatEyesLayer<M extends EntityModel<T>, T extends ChangedEntity> extends EyesLayer<T, M> implements FirstPersonLayer<T> {
		private final RenderType renderType;
		private final ResourceLocation emissiveTexture;

		public CustomCatEyesLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture) {
			super(p_116964_);
			this.renderType = RenderType.eyes(emissiveTexture);
			this.emissiveTexture = emissiveTexture;
		}
		public ResourceLocation getEmissiveTexture() {
			return this.emissiveTexture;
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			VertexConsumer vertexConsumer = bufferSource.getBuffer(this.renderType());
			this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}


		public RenderType renderType() {
			return this.renderType;
		}

		public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector) {
			stack.pushPose();
			stack.scale(1.0002F, 1.0002F, 1.0002F);
			EntityModel<T> var8 = this.getParentModel();
			if (var8 instanceof AdvancedHumanoidModel<?> armedModel) {
				FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm), stackCorrector, stack, bufferSource.getBuffer(this.renderType()), 15728880, 1.0F);
			}
			stack.popPose();
		}
	}
}
