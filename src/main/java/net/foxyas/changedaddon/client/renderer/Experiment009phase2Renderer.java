
package net.foxyas.changedaddon.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.RenderType;

import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.client.model.Modeljack_model;

public class Experiment009phase2Renderer extends MobRenderer<Experiment009phase2Entity, Modeljack_model<Experiment009phase2Entity>> {
	public Experiment009phase2Renderer(EntityRendererProvider.Context context) {
		super(context, new Modeljack_model(context.bakeLayer(Modeljack_model.LAYER_LOCATION)), 0.5f);
		this.addLayer(new EyesLayer<Experiment009phase2Entity, Modeljack_model<Experiment009phase2Entity>>(this) {
			@Override
			public RenderType renderType() {
				return RenderType.eyes(new ResourceLocation("changed_addon:textures/entities/glow_jack_entity_phase2.png"));
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(Experiment009phase2Entity entity) {
		return new ResourceLocation("changed_addon:textures/entities/jackmodeltexture.png");
	}
}
