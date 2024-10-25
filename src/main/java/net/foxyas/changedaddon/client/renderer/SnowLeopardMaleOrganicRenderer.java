
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.BioSynthSnowLeopardMaleModel;
import net.foxyas.changedaddon.client.renderer.layers.CustomHairColorLayer;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.entity.SnowLeopardMaleOrganicEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SnowLeopardMaleOrganicRenderer extends AdvancedHumanoidRenderer<SnowLeopardMaleOrganicEntity, BioSynthSnowLeopardMaleModel, ArmorLatexMaleCatModel<SnowLeopardMaleOrganicEntity>> {
	public SnowLeopardMaleOrganicRenderer(EntityRendererProvider.Context context) {
		super(context, new BioSynthSnowLeopardMaleModel(context.bakeLayer(BioSynthSnowLeopardMaleModel.LAYER_LOCATION)),
				ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel(),model::isPartNotArmFur));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomHairColorLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/male_snep_hair")));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(SnowLeopardMaleOrganicEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/biosynthsnowleopardmale.png");
	}
}