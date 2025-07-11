package net.foxyas.changedaddon.registers;

import net.foxyas.changedaddon.client.model.*;
import net.foxyas.changedaddon.client.model.advanced.AvaliModel;
import net.foxyas.changedaddon.client.model.advanced.LatexKitsuneFemaleModel;
import net.foxyas.changedaddon.client.model.advanced.LatexKitsuneMaleModel;
import net.foxyas.changedaddon.client.model.advanced.ProtogenModel;
import net.foxyas.changedaddon.client.model.armors.ArmorLatexDragonSnowLeopardSharkModel;
import net.foxyas.changedaddon.client.model.armors.ArmorLatexSquidTigerSharkModel;
import net.foxyas.changedaddon.client.model.armors.DarkLatexCoatModel;
import net.foxyas.changedaddon.client.model.projectile.SimpleProjectileModel;
import net.foxyas.changedaddon.client.model.simple.LatexCalicoCatModel;
import net.foxyas.changedaddon.client.renderer.BlueLizardRenderer;
import net.foxyas.changedaddon.client.renderer.SimpleProjectileRenderer;
import net.foxyas.changedaddon.client.renderer.SnowLeopardPartialRenderer;
import net.foxyas.changedaddon.client.renderer.advanced.AvaliRenderer;
import net.foxyas.changedaddon.client.renderer.advanced.LatexKitsuneFemaleRenderer;
import net.foxyas.changedaddon.client.renderer.advanced.LatexKitsuneMaleRenderer;
import net.foxyas.changedaddon.client.renderer.advanced.ProtogenRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.ContainmentContainerRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.SnepPlushBlockEntityRenderer;
import net.foxyas.changedaddon.client.renderer.simple.LatexCalicoCatRenderer;
import net.ltxprogrammer.changed.client.RegisterComplexRenderersEvent;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModelsRegister {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelSnowFox.LAYER_LOCATION, ModelSnowFox::createBodyLayer);
        event.registerLayerDefinition(ModelFemaleSnowFox.LAYER_LOCATION, ModelFemaleSnowFox::createBodyLayer);
        event.registerLayerDefinition(DazedLatexModel.LAYER_LOCATION, DazedLatexModel::createBodyLayer);
        event.registerLayerDefinition(PuroKindModel.LAYER_LOCATION, PuroKindModel::createBodyLayer);
        event.registerLayerDefinition(PuroKindFemaleModel.LAYER_LOCATION, PuroKindFemaleModel::createBodyLayer);
        event.registerLayerDefinition(BunyModel.LAYER_LOCATION, BunyModel::createBodyLayer);
        event.registerLayerDefinition(BioSynthSnowLeopardMaleModel.LAYER_LOCATION, BioSynthSnowLeopardMaleModel::createBodyLayer);
        event.registerLayerDefinition(OrganicSnowLeopardMaleModel.LAYER_LOCATION, OrganicSnowLeopardMaleModel::createBodyLayer);
        event.registerLayerDefinition(BioSynthSnowLeopardFemaleModel.LAYER_LOCATION, BioSynthSnowLeopardFemaleModel::createBodyLayer);
        event.registerLayerDefinition(OrganicSnowLeopardFemaleModel.LAYER_LOCATION, OrganicSnowLeopardFemaleModel::createBodyLayer);
        event.registerLayerDefinition(KetModel.LAYER_LOCATION, KetModel::createBodyLayer);
        event.registerLayerDefinition(KetBossModel.LAYER_LOCATION, KetBossModel::createBodyLayer);
        event.registerLayerDefinition(Experiment10BossModel.LAYER_LOCATION, Experiment10BossModel::createBodyLayer);
        event.registerLayerDefinition(ModelMirrorWhiteTiger.LAYER_LOCATION, ModelMirrorWhiteTiger::createBodyLayer);
        event.registerLayerDefinition(Experiment10Model.LAYER_LOCATION, Experiment10Model::createBodyLayer);
        event.registerLayerDefinition(MaleExp1Model.LAYER_LOCATION, MaleExp1Model::createBodyLayer);
        event.registerLayerDefinition(FemaleExp1Model.LAYER_LOCATION, FemaleExp1Model::createBodyLayer);
        event.registerLayerDefinition(MaleExp2Model.LAYER_LOCATION, MaleExp2Model::createBodyLayer);
        event.registerLayerDefinition(FemaleExp2Model.LAYER_LOCATION, FemaleExp2Model::createBodyLayer);
        event.registerLayerDefinition(WolfyModel.LAYER_LOCATION, WolfyModel::createBodyLayer);
        event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_HUMAN, () -> SnowLeopardPartialModel.createHumanLayer(false));
        event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_LATEX, () -> SnowLeopardPartialModel.createLatexLayer(false));
        event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_HUMAN_SLIM, () -> SnowLeopardPartialModel.createHumanLayer(true));
        event.registerLayerDefinition(SnowLeopardPartialModel.LAYER_LOCATION_LATEX_SLIM, () -> SnowLeopardPartialModel.createLatexLayer(true));
        event.registerLayerDefinition(GrapeSnowLeopardModel.LAYER_LOCATION, GrapeSnowLeopardModel::createBodyLayer);
        event.registerLayerDefinition(ReynModel.LAYER_LOCATION, ReynModel::createBodyLayer);
        event.registerLayerDefinition(LuminarcticLeopardModel.LAYER_LOCATION, LuminarcticLeopardModel::createBodyLayer);
        event.registerLayerDefinition(LuminarcticFemaleLeopardModel.LAYER_LOCATION, LuminarcticFemaleLeopardModel::createBodyLayer);
        event.registerLayerDefinition(LatexSquidTigerSharkModel.LAYER_LOCATION, LatexSquidTigerSharkModel::createBodyLayer);
        event.registerLayerDefinition(LynxModel.LAYER_LOCATION, LynxModel::createBodyLayer);
        event.registerLayerDefinition(SnepsiLeopardModel.LAYER_LOCATION, SnepsiLeopardModel::createBodyLayer);
        event.registerLayerDefinition(FoxtaFoxyModel.LAYER_LOCATION, FoxtaFoxyModel::createBodyLayer);
        event.registerLayerDefinition(FengQIWolfModel.LAYER_LOCATION, FengQIWolfModel::createBodyLayer);
        event.registerLayerDefinition(BagelModel.LAYER_LOCATION, BagelModel::createBodyLayer);
        event.registerLayerDefinition(LatexDragonSnowLeopardSharkModel.LAYER_LOCATION, LatexDragonSnowLeopardSharkModel::createBodyLayer);
        event.registerLayerDefinition(HimalayanCrystalGasCatModel.LAYER_LOCATION, HimalayanCrystalGasCatModel::createBodyLayer);
        event.registerLayerDefinition(HimalayanCrystalGasCatFemaleModel.LAYER_LOCATION, HimalayanCrystalGasCatFemaleModel::createBodyLayer);
        event.registerLayerDefinition(VoidFoxModel.LAYER_LOCATION, VoidFoxModel::createBodyLayer);
        event.registerLayerDefinition(HaydenFennecFoxModel.LAYER_LOCATION, HaydenFennecFoxModel::createBodyLayer);
        event.registerLayerDefinition(BlueLizardModel.LAYER_LOCATION, BlueLizardModel::createBodyLayer);
        event.registerLayerDefinition(AvaliModel.LAYER_LOCATION, AvaliModel::createBodyLayer);
        event.registerLayerDefinition(LatexKitsuneMaleModel.LAYER_LOCATION, LatexKitsuneMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexKitsuneFemaleModel.LAYER_LOCATION, LatexKitsuneFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexCalicoCatModel.LAYER_LOCATION, LatexCalicoCatModel::createBodyLayer);
        event.registerLayerDefinition(ProtogenModel.LAYER_LOCATION, ProtogenModel::createBodyLayer);





        //Projectiles
        event.registerLayerDefinition(SimpleProjectileModel.LAYER_LOCATION, SimpleProjectileModel::createBodyLayer);

        //Armors
        event.registerLayerDefinition(ArmorLatexSquidTigerSharkModel.INNER_ARMOR, () -> ArmorLatexSquidTigerSharkModel.createArmorLayer(ArmorModel.INNER));
        event.registerLayerDefinition(ArmorLatexSquidTigerSharkModel.OUTER_ARMOR, () -> ArmorLatexSquidTigerSharkModel.createArmorLayer(ArmorModel.OUTER));
        event.registerLayerDefinition(ArmorLatexDragonSnowLeopardSharkModel.INNER_ARMOR, () -> ArmorLatexDragonSnowLeopardSharkModel.createArmorLayer(ArmorModel.INNER));
        event.registerLayerDefinition(ArmorLatexDragonSnowLeopardSharkModel.OUTER_ARMOR, () -> ArmorLatexDragonSnowLeopardSharkModel.createArmorLayer(ArmorModel.OUTER));

        //Non Anthro Entities Model
        event.registerLayerDefinition(LatexSnepModel.LAYER_LOCATION, LatexSnepModel::createBodyLayer);


        //Armors Models
        event.registerLayerDefinition(DarkLatexCoatModel.LAYER_LOCATION, DarkLatexCoatModel::createBodyLayer);


        // Block Entities Custom Models
        event.registerLayerDefinition(SnepPlushBlockEntityRenderer.SnepPlushExtraModel.LAYER_LOCATION, SnepPlushBlockEntityRenderer.SnepPlushExtraModel::createBodyLayer);
        event.registerLayerDefinition(ContainmentContainerRenderer.FluidModelPart.LAYER_LOCATION, ContainmentContainerRenderer.FluidModelPart::createBodyLayer);
    }


     @SubscribeEvent
     public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
         event.registerEntityRenderer(ChangedAddonEntities.BLUE_LIZARD.get(),
                 BlueLizardRenderer::new);
         event.registerEntityRenderer(ChangedAddonEntities.AVALI.get(),
                 AvaliRenderer::new);
         event.registerEntityRenderer(ChangedAddonEntities.LATEX_KITSUNE_FEMALE.get(),
                 LatexKitsuneFemaleRenderer::new);
         event.registerEntityRenderer(ChangedAddonEntities.LATEX_KITSUNE_MALE.get(),
                 LatexKitsuneMaleRenderer::new);
         event.registerEntityRenderer(ChangedAddonEntities.LATEX_CALICO_CAT.get(),
                 LatexCalicoCatRenderer::new);
         event.registerEntityRenderer(ChangedAddonEntities.PROTOGEN.get(),
                 ProtogenRenderer::new);

         event.registerEntityRenderer(ChangedAddonEntities.PARTICLE_PROJECTILE.get(),
                 SimpleProjectileRenderer::new);
     }


    @SubscribeEvent
    public static void registerComplexEntityRenderers(RegisterComplexRenderersEvent event) {
        event.registerEntityRenderer(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL.get(), "default", SnowLeopardPartialRenderer.forModelSize(false));
        event.registerEntityRenderer(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL.get(), "slim", SnowLeopardPartialRenderer.forModelSize(true));
    }

	/*
	private static Map<String, EntityRenderer<? extends SnowLeopardPartialEntity>> partialRenderers = ImmutableMap.of();
	
	@Nullable
	public static <T extends Entity> EntityRenderer<? super T> getRenderer(T entity) {
		if (entity instanceof SnowLeopardPartialEntity partial) {
			String s = partial.getModelName();
			EntityRenderer<? extends SnowLeopardPartialEntity> entityrenderer = partialRenderers.get(s);
			return (EntityRenderer) (entityrenderer != null ? entityrenderer : partialRenderers.get("default"));
		}
			return null; // Default to registered renderer
	}

		public static void registerComplexRenderers(EntityRendererProvider.Context context) {
		partialRenderers = new ImmutableMap.Builder<String, EntityRenderer<? extends SnowLeopardPartialEntity>>()
				.put("default", new SnowLeopardPartialRenderer(context, false))
				.put("slim", new SnowLeopardPartialRenderer(context, true)).build();
	}*/
}