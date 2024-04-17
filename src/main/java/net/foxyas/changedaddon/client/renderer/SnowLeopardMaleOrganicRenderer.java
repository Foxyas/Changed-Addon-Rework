
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.OrganicSnowLeopardMaleModel;
import net.foxyas.changedaddon.entity.SnowLeopardMaleOrganicEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.SnowLeopardMaleOrganicEntity;

public class SnowLeopardMaleOrganicRenderer extends LatexHumanoidRenderer<SnowLeopardMaleOrganicEntity, OrganicSnowLeopardMaleModel, ArmorLatexMaleCatModel<SnowLeopardMaleOrganicEntity>> {
	public SnowLeopardMaleOrganicRenderer(EntityRendererProvider.Context context) {
		super(context, new OrganicSnowLeopardMaleModel(context.bakeLayer(OrganicSnowLeopardMaleModel.LAYER_LOCATION)),
				ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(SnowLeopardMaleOrganicEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/snowleopardmaleorganic.png");
	}
}