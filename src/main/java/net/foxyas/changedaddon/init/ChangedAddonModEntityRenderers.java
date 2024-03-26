
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.foxyas.changedaddon.client.renderer.SnowLeopardMaleOrganicRenderer;
import net.foxyas.changedaddon.client.renderer.SnowLeopardFemaleOrganicRenderer;
import net.foxyas.changedaddon.client.renderer.PuroKindRenderer;
import net.foxyas.changedaddon.client.renderer.PuroKindFemaleRenderer;
import net.foxyas.changedaddon.client.renderer.PrototypeRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnowFoxRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnowFoxFemaleRenderer;
import net.foxyas.changedaddon.client.renderer.KetExperiment009Renderer;
import net.foxyas.changedaddon.client.renderer.FoxyasRenderer;
import net.foxyas.changedaddon.client.renderer.Experiment009phase2Renderer;
import net.foxyas.changedaddon.client.renderer.Experiment009Renderer;
import net.foxyas.changedaddon.client.renderer.DazedRenderer;
import net.foxyas.changedaddon.client.renderer.BunyRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ChangedAddonModEntities.PROTOTYPE.get(), PrototypeRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.FOXYAS.get(), FoxyasRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.LATEX_SNOW_FOX.get(), LatexSnowFoxRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.LATEX_SNOW_FOX_FEMALE.get(), LatexSnowFoxFemaleRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXPERIMENT_009.get(), Experiment009Renderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXPERIMENT_009_PHASE_2.get(), Experiment009phase2Renderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.DAZED.get(), DazedRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.PURO_KIND.get(), PuroKindRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.PURO_KIND_FEMALE.get(), PuroKindFemaleRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.BUNY.get(), BunyRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC.get(), SnowLeopardMaleOrganicRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC.get(), SnowLeopardFemaleOrganicRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.KET_EXPERIMENT_009.get(), KetExperiment009Renderer::new);
	}
}
