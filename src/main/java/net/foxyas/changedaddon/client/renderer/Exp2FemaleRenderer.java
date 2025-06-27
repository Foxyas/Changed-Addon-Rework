
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.FemaleExp2Model;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfigsConfiguration;
import net.foxyas.changedaddon.client.renderer.layers.CustomHairColorLayer;
import net.foxyas.changedaddon.entity.Exp2FemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class Exp2FemaleRenderer extends AdvancedHumanoidRenderer<Exp2FemaleEntity, FemaleExp2Model, ArmorLatexFemaleCatModel<Exp2FemaleEntity>> {
	public Exp2FemaleRenderer(EntityRendererProvider.Context context) {
		super(context, new FemaleExp2Model(context.bakeLayer(FemaleExp2Model.LAYER_LOCATION)),
				ArmorLatexFemaleCatModel::new, ArmorLatexFemaleCatModel.INNER_ARMOR, ArmorLatexFemaleCatModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel(),model::isPartNotArmFur));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomHairColorLayer<>(this, this.getModel(), ResourceLocation.parse("changed_addon:textures/entities/female_snep_hair")));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}
	
	@Override
	public ResourceLocation getTextureLocation(Exp2FemaleEntity entity) {
		if(entity.getUnderlyingPlayer() == null){
		return ResourceLocation.parse("changed_addon:textures/entities/femaleexp2_snow_leopard.png");
		}

		if (entity.getUnderlyingPlayer() != null && entity.getHealth() / entity.getMaxHealth() <= 0.60 && entity.getHealth() / entity.getMaxHealth() > 0.30){
			return ResourceLocation.parse("changed_addon:textures/entities/femaleexp2_snow_leopard_hurt.png");
		}
		if (entity.getUnderlyingPlayer() != null && entity.getHealth() / entity.getMaxHealth() <= 0.30){
			return ResourceLocation.parse("changed_addon:textures/entities/femaleexp2_snow_leopard_badly_hurt.png");
		}
		if (entity.getUnderlyingPlayer() != null && !(entity.getHealth() / entity.getMaxHealth() <= 0.60)){
			return ResourceLocation.parse("changed_addon:textures/entities/femaleexp2_snow_leopard.png");
		}
		


		return ResourceLocation.parse("changed_addon:textures/entities/femaleexp2_snow_leopard.png");
	}
}
