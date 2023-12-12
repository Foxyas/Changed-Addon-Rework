
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelSnowFox;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexWolfModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.LatexSnowFoxEntity;
import net.foxyas.changedaddon.client.model.ModelFoxyasModel;

public class LatexSnowFoxRenderer extends LatexHumanoidRenderer<LatexSnowFoxEntity, ModelSnowFox, ArmorLatexWolfModel.RemodelMale<LatexSnowFoxEntity>> {
	public LatexSnowFoxRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelSnowFox(context.bakeLayer(ModelSnowFox.LAYER_LOCATION)),ArmorLatexWolfModel.RemodelMale::new,ArmorLatexWolfModel.RemodelMale.INNER_ARMOR,ArmorLatexWolfModel.RemodelMale.OUTER_ARMOR, 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnowFoxEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latexsnowfox.png");
	}
}