
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.foxyas.changedaddon.client.renderer.WolfyRenderer;
import net.foxyas.changedaddon.client.renderer.SnowLeopardMaleOrganicRenderer;
import net.foxyas.changedaddon.client.renderer.SnowLeopardFemaleOrganicRenderer;
import net.foxyas.changedaddon.client.renderer.ReynRenderer;
import net.foxyas.changedaddon.client.renderer.PuroKindRenderer;
import net.foxyas.changedaddon.client.renderer.PuroKindFemaleRenderer;
import net.foxyas.changedaddon.client.renderer.PrototypeRenderer;
import net.foxyas.changedaddon.client.renderer.MirrorWhiteTigerRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnowFoxRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnowFoxFemaleRenderer;
import net.foxyas.changedaddon.client.renderer.KetExperiment009Renderer;
import net.foxyas.changedaddon.client.renderer.KetExperiment009BossRenderer;
import net.foxyas.changedaddon.client.renderer.FoxyasRenderer;
import net.foxyas.changedaddon.client.renderer.Experiment10Renderer;
import net.foxyas.changedaddon.client.renderer.Experiment10BossRenderer;
import net.foxyas.changedaddon.client.renderer.Experiment009phase2Renderer;
import net.foxyas.changedaddon.client.renderer.Experiment009Renderer;
import net.foxyas.changedaddon.client.renderer.Exp6Renderer;
import net.foxyas.changedaddon.client.renderer.Exp2MaleRenderer;
import net.foxyas.changedaddon.client.renderer.Exp2FemaleRenderer;
import net.foxyas.changedaddon.client.renderer.Exp1MaleRenderer;
import net.foxyas.changedaddon.client.renderer.Exp1FemaleRenderer;
import net.foxyas.changedaddon.client.renderer.ErikRenderer;
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
		event.registerEntityRenderer(ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC.get(), SnowLeopardFemaleOrganicRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.KET_EXPERIMENT_009.get(), KetExperiment009Renderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.MIRROR_WHITE_TIGER.get(), MirrorWhiteTigerRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC.get(), SnowLeopardMaleOrganicRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXPERIMENT_10.get(), Experiment10Renderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXP_2_MALE.get(), Exp2MaleRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXP_2_FEMALE.get(), Exp2FemaleRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.WOLFY.get(), WolfyRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.ERIK.get(), ErikRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXP_6.get(), Exp6Renderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.REYN.get(), ReynRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.KET_EXPERIMENT_009_BOSS.get(), KetExperiment009BossRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXPERIMENT_10_BOSS.get(), Experiment10BossRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXP_1_MALE.get(), Exp1MaleRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.EXP_1_FEMALE.get(), Exp1FemaleRenderer::new);
	}
}
