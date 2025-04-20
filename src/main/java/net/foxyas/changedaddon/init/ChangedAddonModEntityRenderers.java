
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
import net.foxyas.changedaddon.client.renderer.LynxRenderer;
import net.foxyas.changedaddon.client.renderer.LuminarcticLeopardRenderer;
import net.foxyas.changedaddon.client.renderer.LuminarCrystalSpearRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSquidTigerSharkRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnowFoxRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnowFoxFemaleRenderer;
import net.foxyas.changedaddon.client.renderer.LatexSnepRenderer;
import net.foxyas.changedaddon.client.renderer.KetExperiment009Renderer;
import net.foxyas.changedaddon.client.renderer.KetExperiment009BossRenderer;
import net.foxyas.changedaddon.client.renderer.FoxyasRenderer;
import net.foxyas.changedaddon.client.renderer.FemaleLuminarcticLeopardRenderer;
import net.foxyas.changedaddon.client.renderer.Experiment10Renderer;
import net.foxyas.changedaddon.client.renderer.Experiment10BossRenderer;
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
		event.registerEntityRenderer(ChangedAddonModEntities.LATEX_SNEP.get(), LatexSnepRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.LUMINARCTIC_LEOPARD.get(), LuminarcticLeopardRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.FEMALE_LUMINARCTIC_LEOPARD.get(), FemaleLuminarcticLeopardRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.LATEX_SQUID_TIGER_SHARK.get(), LatexSquidTigerSharkRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.LUMINAR_CRYSTAL_SPEAR.get(), LuminarCrystalSpearRenderer::new);
		event.registerEntityRenderer(ChangedAddonModEntities.LYNX.get(), LynxRenderer::new);
	}
}
