package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.SnowLeopardPartialModel;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.entity.SnowLeopardPartialEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SnowLeopardPartialRenderer extends AdvancedHumanoidRenderer<SnowLeopardPartialEntity, SnowLeopardPartialModel, ArmorLatexMaleCatModel<SnowLeopardPartialEntity>> {

	public SnowLeopardPartialRenderer(EntityRendererProvider.Context context) {
		super(context, SnowLeopardPartialModel.human(context.bakeLayer(getLayerLocation())),
				ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5f);

		var partialModel = new LatexPartialLayer<>(this, SnowLeopardPartialModel.latex(context.bakeLayer(getLatexLayerLocation())), getTexture());
		this.addLayer(partialModel);
		this.addLayer(new LatexParticlesLayer<>(this).addModel(partialModel.getModel(), entity -> partialModel.getTexture()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new DarkLatexMaskLayer<>(this, context.getModelSet()));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	private static ModelLayerLocation getLayerLocation() {
		return ChangedAddonClientConfigsConfiguration.SLIMMODEL.get() ? SnowLeopardPartialModel.LAYER_LOCATION_HUMAN_SLIM : SnowLeopardPartialModel.LAYER_LOCATION_HUMAN;
	}

	private static ModelLayerLocation getLatexLayerLocation() {
		return ChangedAddonClientConfigsConfiguration.SLIMMODEL.get() ? SnowLeopardPartialModel.LAYER_LOCATION_LATEX_SLIM : SnowLeopardPartialModel.LAYER_LOCATION_LATEX;
	}

	private static ResourceLocation getTexture() {
		return new ResourceLocation("changed_addon", ChangedAddonClientConfigsConfiguration.SLIMMODEL.get() ? "textures/entities/snow_leopard_partial_slim.png" : "textures/entities/snow_leopard_partial.png");
	}

	@Override
	public ResourceLocation getTextureLocation(SnowLeopardPartialEntity partial) {
		return partial.getSkinTextureLocation();
	}

	@Override
	public void render(SnowLeopardPartialEntity latex, float yRot, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		if (latex.getUnderlyingPlayer() instanceof AbstractClientPlayer clientPlayer) {
			this.model.setModelProperties(clientPlayer);
		} else {
			this.model.defaultModelProperties();
		}
		super.render(latex, yRot, partialTicks, poseStack, bufferSource, packedLight);
	}

	@Override
	protected void scale(SnowLeopardPartialEntity entity, PoseStack pose, float partialTick) {
		float scale = 0.9375F;
		pose.scale(scale, scale, scale);
	}
}
