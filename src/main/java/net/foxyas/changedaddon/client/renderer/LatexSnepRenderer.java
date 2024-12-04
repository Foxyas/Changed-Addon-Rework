
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.LatexSnepModel;
import net.foxyas.changedaddon.client.model.ModelMirrorWhiteTiger;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorNoneModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.HumanoidModel;

import net.foxyas.changedaddon.entity.LatexSnepEntity;

public class LatexSnepRenderer extends AdvancedHumanoidRenderer<LatexSnepEntity, LatexSnepModel, ArmorNoneModel<LatexSnepEntity>> {
	public LatexSnepRenderer(EntityRendererProvider.Context context) {
		super(context, new LatexSnepModel(context.bakeLayer(ModelMirrorWhiteTiger.LAYER_LOCATION)),
				ArmorNoneModel::new, ArmorNoneModel.INNER_ARMOR, ArmorNoneModel.OUTER_ARMOR, 0.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(LatexSnepEntity entity) {
		return new ResourceLocation("changed_addon:textures/entities/latexsnep.png");
	}
}
