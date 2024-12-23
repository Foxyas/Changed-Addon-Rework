
package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.client.model.LuminarcticLeopardModel;
import net.foxyas.changedaddon.client.model.MaleExp2Model;
import net.foxyas.changedaddon.entity.DazedEntity;
import net.foxyas.changedaddon.entity.Exp2MaleEntity;
import net.foxyas.changedaddon.entity.LuminarcticLeopardEntity;
import net.foxyas.changedaddon.entity.LuminarcticLeopardEntity;
import net.ltxprogrammer.changed.ability.HypnosisAbility;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class LuminarcticLeopardRenderer extends AdvancedHumanoidRenderer<LuminarcticLeopardEntity, LuminarcticLeopardModel, ArmorLatexMaleCatModel<LuminarcticLeopardEntity>> {
	public LuminarcticLeopardRenderer(EntityRendererProvider.Context context) {
		super(context, new LuminarcticLeopardModel(context.bakeLayer(LuminarcticLeopardModel.LAYER_LOCATION)),
				ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5f);

		this.addLayer(new LatexParticlesLayer<>(this, getModel(),model::isPartNotArmFur));

		Color3 RED = new Color3(255,0,0);
		this.addLayer(new thisConditionalLayers.thisCustomEyesLayer<>(this,
				new CustomEyesLayer<>(this, context.getModelSet(),
						CustomEyesLayer::scleraColor,
						CustomEyesLayer::glowingIrisColorLeft,
						CustomEyesLayer::glowingIrisColorRight,
						CustomEyesLayer::noRender,
						CustomEyesLayer::noRender)
				,
				new CustomEyesLayer<>(this, context.getModelSet(),
						CustomEyesLayer::scleraColor,
						CustomEyesLayer.fixedColorGlowing(RED),
						CustomEyesLayer.fixedColorGlowing(RED),
						CustomEyesLayer::noRender,
						CustomEyesLayer::noRender)
		));

		this.addLayer(new thisConditionalLayers.ConditionalCustomLayers<>(this,
				TransfurCapeLayer.normalCape(this, context.getModelSet()),
				new GasMaskLayer<>(this, context.getModelSet()),
				new LatexParticlesLayer<>(this, getModel(),model::isPartNotArmFur))
		);

		this.addLayer(new thisConditionalLayers.thisGlowLayer<>(this, new ResourceLocation("changed_addon:textures/entities/luminarctic_leopard_ability_active.png")));
	}

	@Override
	public void render(LuminarcticLeopardEntity entity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (entity.DodgeAnimTicks != 0) {
			poseStack.pushPose();

			// Calcula a rotação com base no progresso da animação de esquiva
			float dodgeProgress;
			if (entity.DodgeAnimTicks < 0){
				dodgeProgress = (float) entity.DodgeAnimTicks / -entity.DodgeAnimMaxTicks;
				float rotationAngle = 90.0F * dodgeProgress; // Rotaciona até 90° conforme o progresso da animação
				// Aplica a rotação no eixo X
				poseStack.mulPose(Vector3f.YN.rotationDegrees(rotationAngle));
			} else {
				dodgeProgress = (float) entity.DodgeAnimTicks / entity.DodgeAnimMaxTicks;
				float rotationAngle = 90.0F * dodgeProgress; // Rotaciona até 90° conforme o progresso da animação

				// Aplica a rotação no eixo X
				poseStack.mulPose(Vector3f.YP.rotationDegrees(rotationAngle));
			}

			// Renderiza o modelo normalmente (chama o super ou código adicional aqui)
			super.render(entity, yRot, partialTicks, poseStack, bufferSource, packedLight);
			poseStack.popPose(); // Restaura a pose original
		} else {
			super.render(entity, yRot, partialTicks, poseStack, bufferSource, packedLight);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(LuminarcticLeopardEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/luminarctic_leopard.png");
	}

	private static class thisConditionalLayers {

		private static class thisGlowLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M,T> implements FirstPersonLayer<T> {
			public thisGlowLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture) {
				super(p_116964_, emissiveTexture);
			}

			@Override
			public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.getUnderlyingPlayer() != null){
					Player player = entity.getUnderlyingPlayer();
					var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
					if (instance != null && instance.ability instanceof HypnosisAbility ability){
						if (instance.getController().getHoldTicks() > 0){
							super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
						}
					} else if (entity instanceof LuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					}
				}

				if (entity instanceof LuminarcticLeopardEntity WILD_LUMI && WILD_LUMI.getTarget() != null){
					super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}

			@Override
			public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
				if (entity.getUnderlyingPlayer() != null){
					Player player = entity.getUnderlyingPlayer();
					var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
					if (instance != null && instance.ability instanceof HypnosisAbility ability){
						if (instance.getController().getHoldTicks() > 0){
							super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
						}
					} else if (entity instanceof LuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
					}
				}

				if (entity instanceof LuminarcticLeopardEntity WILD_LUMI && WILD_LUMI.getTarget() != null){
					super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
				}
			}
		}

		private static class thisCustomEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

			private final CustomEyesLayer<M, T> customEyesLayer, fixedEyesColor;

			public thisCustomEyesLayer(RenderLayerParent<T, M> parent, CustomEyesLayer<M, T> customEyesLayer, CustomEyesLayer<M,T> fixedEyesColor) {
				super(parent);
				this.customEyesLayer = customEyesLayer;
				this.fixedEyesColor = fixedEyesColor;
			}

			@Override
			public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.getUnderlyingPlayer() != null){
					Player player = entity.getUnderlyingPlayer();
					var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
					if (instance != null && instance.ability instanceof HypnosisAbility ability){
						if (instance.getController().getHoldTicks() > 0){
							this.fixedEyesColor.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
						}
					} else if (entity instanceof LuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						this.fixedEyesColor.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
					} else {
						customEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
					}
				} else {
					this.fixedEyesColor.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		}

		public static class ConditionalCustomLayers<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

			private final TransfurCapeLayer<T,M> transfurCapeLayer;
			private final GasMaskLayer<T,M> gasMaskLayer;
			private final LatexParticlesLayer<T,M> latexParticlesLayer;

			public ConditionalCustomLayers(
					RenderLayerParent<T, M> parent,
					TransfurCapeLayer<T,M> transfurCapeLayer,
					GasMaskLayer<T,M> gasMaskLayer,
					LatexParticlesLayer<T,M> latexParticlesLayer) {

				super(parent);
				this.transfurCapeLayer = transfurCapeLayer;
				this.gasMaskLayer = gasMaskLayer;
				this.latexParticlesLayer = latexParticlesLayer;
			}

			@Override
			public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity instanceof DazedEntity dazedEntity
						&& !dazedEntity.isMorphed()) {
					transfurCapeLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
					gasMaskLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
					latexParticlesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount,partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		}

	}
}
