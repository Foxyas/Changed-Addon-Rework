
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelFemaleSnowFox;
import net.foxyas.changedaddon.client.model.ModelSnowFox;
import net.foxyas.changedaddon.entity.LatexSnowFoxFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexWolfModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.LatexSnowFoxFemaleEntity;
import net.foxyas.changedaddon.client.model.ModelFoxyasModel;

public class LatexSnowFoxFemaleRenderer extends LatexHumanoidRenderer<LatexSnowFoxFemaleEntity, ModelFemaleSnowFox, ArmorLatexWolfModel.RemodelFemale<LatexSnowFoxFemaleEntity>> {
	public LatexSnowFoxFemaleRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelFemaleSnowFox(context.bakeLayer(ModelSnowFox.LAYER_LOCATION)),ArmorLatexWolfModel.RemodelFemale::new,ArmorLatexWolfModel.RemodelFemale.INNER_ARMOR,ArmorLatexWolfModel.RemodelFemale.OUTER_ARMOR, 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnowFoxFemaleEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latexsnowfoxfemale.png");
	}
}