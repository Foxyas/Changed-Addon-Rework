
package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.Experiment10Model;
import net.foxyas.changedaddon.entity.Experiment10Entity;
import net.ltxprogrammer.changed.client.renderer.LatexHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class Experiment10Renderer extends LatexHumanoidRenderer<Experiment10Entity, Experiment10Model, ArmorLatexFemaleCatModel<Experiment10Entity>> {
	public Experiment10Renderer(EntityRendererProvider.Context context) {
		super(context, new Experiment10Model(context.bakeLayer(Experiment10Model.LAYER_LOCATION)),
				ArmorLatexFemaleCatModel::new, ArmorLatexFemaleCatModel.INNER_ARMOR, ArmorLatexFemaleCatModel.OUTER_ARMOR, 0.5f);
		this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon","textures/entities/exp-010_emissive.png")));
		this.addLayer(new LatexParticlesLayer<>(this, getModel()));
		this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
		this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer.fixedColor(Color3.parseHex("#252525")),CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#ed1c24"))));
	}

	@Override
	public ResourceLocation getTextureLocation(Experiment10Entity entity) {
		if (entity.getUnderlyingPlayer() == null && entity.isPhase2()) {
			return new ResourceLocation("changed_addon:textures/entities/exp-10-phase2.png");
		} else {
			return new ResourceLocation("changed_addon:textures/entities/exp-10.png");
		}
	}
}
