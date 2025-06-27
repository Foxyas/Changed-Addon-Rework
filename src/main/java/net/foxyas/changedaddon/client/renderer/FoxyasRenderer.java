
package net.foxyas.changedaddon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.foxyas.changedaddon.entity.FoxyasEntity;
import net.foxyas.changedaddon.client.model.ModelFoxyasModel;

public class FoxyasRenderer extends MobRenderer<FoxyasEntity, ModelFoxyasModel<FoxyasEntity>> {
	public FoxyasRenderer(EntityRendererProvider.Context context) {
		super(context, new ModelFoxyasModel(context.bakeLayer(ModelFoxyasModel.LAYER_LOCATION)), 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(FoxyasEntity entity) {
		return ResourceLocation.parse("changed_addon:textures/entities/foxyas_texture.png");
	}
}
