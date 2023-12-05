
package net.foxyas.changedaddon.client.renderer;

import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexWolfModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.foxyas.changedaddon.client.model.ModelSnowFox;
import net.foxyas.changedaddon.entity.FoxyasEntity;
import net.foxyas.changedaddon.client.model.ModelFoxyasModel;

public class FoxyasRenderer extends LatexHumanoidRenderer<FoxyasEntity,ModelSnowFox, ArmorLatexWolfModel<FoxyasEntity>> {
	public FoxyasRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelSnowFox(context.bakeLayer(ModelSnowFox.LAYER_LOCATION)),ArmorLatexWolfModel::new,ArmorLatexWolfModel.INNER_ARMOR,ArmorLatexWolfModel.OUTER_ARMOR, 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(FoxyasEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/foxyas_texture.png");
	}
}
