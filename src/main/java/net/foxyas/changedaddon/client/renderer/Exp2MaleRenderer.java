
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.MaleExp2Model;
import net.foxyas.changedaddon.entity.Exp2FemaleEntity;
import net.foxyas.changedaddon.entity.Exp2MaleEntity;
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

import net.foxyas.changedaddon.entity.Exp2MaleEntity;

public class Exp2MaleRenderer extends LatexHumanoidRenderer<Exp2MaleEntity, MaleExp2Model, ArmorLatexMaleCatModel<Exp2MaleEntity>> {
	public Exp2MaleRenderer(EntityRendererProvider.Context context) {
		super(context, new MaleExp2Model(context.bakeLayer(MaleExp2Model.LAYER_LOCATION)),
				ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(Exp2MaleEntity entity) {
		if(entity.getUnderlyingPlayer() == null){
			return new ResourceLocation("changed_addon:textures/entities/maleexp2_snow_leopard.png");
		}

		if (entity.getUnderlyingPlayer() != null && entity.getHealth() / entity.getMaxHealth() <= 0.60 ){
			return new ResourceLocation("changed_addon:textures/entities/maleexp2_snow_leopard_hurt.png");
		}
		if (entity.getUnderlyingPlayer() != null && !(entity.getHealth() / entity.getMaxHealth() <= 0.60 )){
			return new ResourceLocation("changed_addon:textures/entities/maleexp2_snow_leopard.png");
		}

		return new ResourceLocation("changed_addon:textures/entities/maleexp2_snow_leopard.png");
	}
}
