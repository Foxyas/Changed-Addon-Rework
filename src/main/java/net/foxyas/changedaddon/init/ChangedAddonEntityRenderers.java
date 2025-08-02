package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.client.renderer.*;
import net.foxyas.changedaddon.client.renderer.advanced.*;
import net.foxyas.changedaddon.client.renderer.basic.*;
import net.foxyas.changedaddon.client.renderer.mobs.ErikRenderer;
import net.foxyas.changedaddon.client.renderer.mobs.FoxyasRenderer;
import net.foxyas.changedaddon.client.renderer.mobs.PrototypeRenderer;
import net.ltxprogrammer.changed.client.RegisterComplexRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ChangedAddonEntityRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(ChangedAddonEntities.LATEX_SNOW_FOX.get(), LatexSnowFoxRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_SNOW_FOX_FEMALE.get(), LatexSnowFoxFemaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LUMINAR_CRYSTAL_SPEAR.get(), LuminarCrystalSpearRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.DAZED.get(), DazedRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.PURO_KIND.get(), PuroKindRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.PURO_KIND_FEMALE.get(), PuroKindFemaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.BUNY.get(), BunyRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.SNOW_LEOPARD_FEMALE_ORGANIC.get(), SnowLeopardFemaleOrganicRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.KET_EXPERIMENT_009.get(), KetExperiment009Renderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.MIRROR_WHITE_TIGER.get(), MirrorWhiteTigerRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.SNOW_LEOPARD_MALE_ORGANIC.get(), SnowLeopardMaleOrganicRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXPERIMENT_10.get(), Experiment10Renderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXP_2_MALE.get(), Exp2MaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXP_2_FEMALE.get(), Exp2FemaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.WOLFY.get(), WolfyRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXP_6.get(), Exp6Renderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.REYN.get(), ReynRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.KET_EXPERIMENT_009_BOSS.get(), KetExperiment009BossRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), Experiment10BossRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXP_1_MALE.get(), Exp1MaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.EXP_1_FEMALE.get(), Exp1FemaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_SNEP.get(), LatexSnepRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LUMINARCTIC_LEOPARD.get(), LuminarcticLeopardRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.FEMALE_LUMINARCTIC_LEOPARD.get(), FemaleLuminarcticLeopardRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_SQUID_TIGER_SHARK.get(), LatexSquidTigerSharkRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LYNX.get(), LynxRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.FOXTA_FOXY.get(), FoxtaFoxyRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.SNEPSI_LEOPARD.get(), SnepsiLeopardRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.BAGEL.get(), BagelRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_DRAGON_SNOW_LEOPARD_SHARK.get(), LatexDragonSnowLeopardSharkRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.CRYSTAL_GAS_CAT_MALE.get(), CrystalGasCatMaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.CRYSTAL_GAS_CAT_FEMALE.get(), CrystalGasCatFemaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.VOID_FOX.get(), VoidFoxRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.FENGQI_WOLF.get(), FengQIWolfRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.HAYDEN_FENNEC_FOX.get(), HaydenFennecFoxRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.BLUE_LIZARD.get(), BlueLizardRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.AVALI.get(), AvaliRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_KITSUNE_FEMALE.get(), LatexKitsuneFemaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_KITSUNE_MALE.get(), LatexKitsuneMaleRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.LATEX_CALICO_CAT.get(), LatexCalicoCatRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.PROTOGEN.get(), ProtogenRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.MONGOOSE.get(), MongooseRenderer::new);

        // --- MONSTER/MOB ENTITIES ---
        event.registerEntityRenderer(ChangedAddonEntities.PROTOTYPE.get(), PrototypeRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.FOXYAS.get(), FoxyasRenderer::new);
        event.registerEntityRenderer(ChangedAddonEntities.ERIK.get(), ErikRenderer::new);

        // --- PROJECTILE ENTITIES ---
        event.registerEntityRenderer(ChangedAddonEntities.PARTICLE_PROJECTILE.get(),
                SimpleProjectileRenderer::new);
    }


    @SubscribeEvent
    public static void registerComplexEntityRenderers(RegisterComplexRenderersEvent event) {
        event.registerEntityRenderer(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL.get(), "default", SnowLeopardPartialRenderer.forModelSize(false));
        event.registerEntityRenderer(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL.get(), "slim", SnowLeopardPartialRenderer.forModelSize(true));
    }
}
