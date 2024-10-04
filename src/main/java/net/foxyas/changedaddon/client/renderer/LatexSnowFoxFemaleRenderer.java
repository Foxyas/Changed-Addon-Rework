
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelFemaleSnowFox;
import net.foxyas.changedaddon.client.model.ModelSnowFox;
import net.foxyas.changedaddon.entity.LatexSnowFoxFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleWolfModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.LatexSnowFoxFemaleEntity;
import net.foxyas.changedaddon.client.model.ModelFoxyasModel;

public class LatexSnowFoxFemaleRenderer extends AdvancedHumanoidRenderer<LatexSnowFoxFemaleEntity, ModelFemaleSnowFox, ArmorLatexFemaleWolfModel<LatexSnowFoxFemaleEntity>> {
	public LatexSnowFoxFemaleRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelFemaleSnowFox(context.bakeLayer(ModelFemaleSnowFox.LAYER_LOCATION)),ArmorLatexFemaleWolfModel::new,ArmorLatexFemaleWolfModel.INNER_ARMOR,ArmorLatexFemaleWolfModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
		this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnowFoxFemaleEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latex_snowfox_female_new.png");
	}
}