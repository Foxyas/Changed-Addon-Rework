
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.FemaleExp2Model;
import net.foxyas.changedaddon.entity.Exp2FemaleEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.Exp2FemaleEntity;

public class Exp2FemaleRenderer extends LatexHumanoidRenderer<Exp2FemaleEntity, FemaleExp2Model, ArmorLatexFemaleCatModel<Exp2FemaleEntity>> {
	public Exp2FemaleRenderer(EntityRendererProvider.Context context) {
		super(context, new FemaleExp2Model(context.bakeLayer(FemaleExp2Model.LAYER_LOCATION)),
				ArmorLatexFemaleCatModel::new, ArmorLatexFemaleCatModel.INNER_ARMOR, ArmorLatexFemaleCatModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}
	@Override
	public ResourceLocation getTextureLocation(Exp2FemaleEntity entity) {
		if(entity.getUnderlyingPlayer() == null){
		return new ResourceLocation("changed_addon:textures/entities/femaleexp2_snow_leopard.png");
		}

		if (entity.getUnderlyingPlayer() != null && entity.getHealth() / entity.getMaxHealth() <= 0.60 ){
			return new ResourceLocation("changed_addon:textures/entities/femaleexp2_snow_leopard_hurt.png");
		}
		if (entity.getUnderlyingPlayer() != null && !(entity.getHealth() / entity.getMaxHealth() <= 0.60 )){
			return new ResourceLocation("changed_addon:textures/entities/femaleexp2_snow_leopard.png");
		}

		return new ResourceLocation("changed_addon:textures/entities/femaleexp2_snow_leopard.png");
	}
}
