package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.client.model.LuminarcticFemaleLeopardModel;
import net.foxyas.changedaddon.entity.DazedEntity;
import net.foxyas.changedaddon.entity.FemaleLuminarcticLeopardEntity;
import net.foxyas.changedaddon.entity.LuminarcticLeopardEntity;
import net.ltxprogrammer.changed.ability.HypnosisAbility;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class FemaleLuminarcticLeopardRenderer extends AdvancedHumanoidRenderer<FemaleLuminarcticLeopardEntity, LuminarcticFemaleLeopardModel, ArmorLatexFemaleCatModel<FemaleLuminarcticLeopardEntity>> {
	public FemaleLuminarcticLeopardRenderer(EntityRendererProvider.Context context) {
		super(context, new LuminarcticFemaleLeopardModel(context.bakeLayer(LuminarcticFemaleLeopardModel.LAYER_LOCATION)),
				ArmorLatexFemaleCatModel::new, ArmorLatexFemaleCatModel.INNER_ARMOR, ArmorLatexFemaleCatModel.OUTER_ARMOR, 0.5f);
				
		this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotArmFur));

		Color3 RED = new Color3(255, 0, 0);
		this.addLayer(new FemaleLuminarcticLeopardRenderer.thisConditionalLayers.thisCustomEyesLayer<>(this,
				new CustomEyesLayer<>(this, context.getModelSet(),
						CustomEyesLayer::scleraColor,
						CustomEyesLayer::irisColorLeft,
						CustomEyesLayer::irisColorRight,
						CustomEyesLayer::noRender,
						CustomEyesLayer::noRender)
				,
				new CustomEyesLayer<>(this, context.getModelSet(),
						CustomEyesLayer::scleraColor,
						CustomEyesLayer::glowingIrisColorLeft,
						CustomEyesLayer::glowingIrisColorRight,
						CustomEyesLayer::noRender,
						CustomEyesLayer::noRender)
		));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
		this.addLayer(new FemaleLuminarcticLeopardRenderer.thisConditionalLayers.thisGlowLayer<>(this, new ResourceLocation("changed_addon:textures/entities/luminarctic_leopard_female_ability_active.png")));
	}

	@Override
	public void render(FemaleLuminarcticLeopardEntity entity, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (entity.DodgeAnimTicks != 0) {
			poseStack.pushPose();

			// Calcula a rotação com base no progresso da animação de esquiva
			float dodgeProgress;
			if (entity.DodgeAnimTicks < 0) {
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
	public ResourceLocation getTextureLocation(FemaleLuminarcticLeopardEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/luminarctic_leopard_female.png");
	}

	private static class thisConditionalLayers {

		private static class thisGlowLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends EmissiveBodyLayer<M, T> implements FirstPersonLayer<T> {
			public thisGlowLayer(RenderLayerParent<T, M> p_116964_, ResourceLocation emissiveTexture) {
				super(p_116964_, emissiveTexture);
			}

			@Override
			public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.getUnderlyingPlayer() != null) {
					Player player = entity.getUnderlyingPlayer();
					var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
					if (instance != null && instance.ability instanceof HypnosisAbility ability) {
						if (instance.getController().getHoldTicks() > 0) {
							super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
						}
					} else if (entity instanceof FemaleLuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					}
				}

				if (entity instanceof FemaleLuminarcticLeopardEntity WILD_LUMI && WILD_LUMI.getTarget() != null) {
					super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}

			@Override
			public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
				if (entity.getUnderlyingPlayer() != null) {
					Player player = entity.getUnderlyingPlayer();
					var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
					if (instance != null && instance.ability instanceof HypnosisAbility ability) {
						if (instance.getController().getHoldTicks() > 0) {
							super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
						}
					} else if (entity instanceof FemaleLuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
					}
				}

				if (entity instanceof FemaleLuminarcticLeopardEntity WILD_LUMI && WILD_LUMI.getTarget() != null) {
					super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
				}
			}
		}

		private static class thisCustomEyesLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> {

			private final CustomEyesLayer<M, T> customEyesLayer, customGlowEyesLayer;

			public thisCustomEyesLayer(RenderLayerParent<T, M> parent, CustomEyesLayer<M, T> customEyesLayer, CustomEyesLayer<M, T> customGlowEyesLayer) {
				super(parent);
				this.customEyesLayer = customEyesLayer;
				this.customGlowEyesLayer = customGlowEyesLayer;
			}

			@Override
			public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
				if (entity.getUnderlyingPlayer() != null) {
					Player player = entity.getUnderlyingPlayer();
					var instance = ProcessTransfur.getPlayerTransfurVariant(player).getSelectedAbility();
					if (instance != null && instance.ability instanceof HypnosisAbility ability) {
						if (instance.getController().getHoldTicks() > 0) {
							this.customGlowEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
						}
					} else if (entity instanceof FemaleLuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						this.customGlowEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					} else {
						customEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					}
				} else {
					if (entity instanceof FemaleLuminarcticLeopardEntity LUMI && LUMI.isActivatedAbility()) {
						this.customGlowEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
					}
					this.customEyesLayer.render(pose, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		}
	}
}
