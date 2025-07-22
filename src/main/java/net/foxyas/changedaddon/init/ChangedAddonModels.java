
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

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
import net.foxyas.changedaddon.client.renderer.blockEntitys.ContainmentContainerRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.SnepPlushBlockEntityRenderer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorModel;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModels {
	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ModelAccessories.LAYER_LOCATION, ModelAccessories::createBodyLayer);
		event.registerLayerDefinition(ModelHazmat_Suit.LAYER_LOCATION, ModelHazmat_Suit::createBodyLayer);
		event.registerLayerDefinition(Modeljack_model.LAYER_LOCATION, Modeljack_model::createBodyLayer);
		event.registerLayerDefinition(Modelempty_model.LAYER_LOCATION, Modelempty_model::createBodyLayer);
		event.registerLayerDefinition(ModelHazardArmorCustomArms.LAYER_LOCATION, ModelHazardArmorCustomArms::createBodyLayer);
		event.registerLayerDefinition(ModelProtoTypeMob.LAYER_LOCATION, ModelProtoTypeMob::createBodyLayer);
		event.registerLayerDefinition(ModelNewHyperFlower.LAYER_LOCATION, ModelNewHyperFlower::createBodyLayer);
		event.registerLayerDefinition(ModelLuminarCrystalSpearModel.LAYER_LOCATION, ModelLuminarCrystalSpearModel::createBodyLayer);
		event.registerLayerDefinition(ModelFoxyasModel.LAYER_LOCATION, ModelFoxyasModel::createBodyLayer);
		event.registerLayerDefinition(ModelHyper_Flower.LAYER_LOCATION, ModelHyper_Flower::createBodyLayer);

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
		ArmorLatexSquidTigerSharkModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);
		ArmorLatexDragonSnowLeopardSharkModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);

		//Non Anthro Entities Model
		event.registerLayerDefinition(LatexSnepModel.LAYER_LOCATION, LatexSnepModel::createBodyLayer);


		//Armors Models
		event.registerLayerDefinition(DarkLatexCoatModel.LAYER_LOCATION, DarkLatexCoatModel::createBodyLayer);


		// Block Entities Custom Models
		event.registerLayerDefinition(SnepPlushBlockEntityRenderer.SnepPlushExtraModel.LAYER_LOCATION, SnepPlushBlockEntityRenderer.SnepPlushExtraModel::createBodyLayer);
		event.registerLayerDefinition(ContainmentContainerRenderer.FluidModelPart.LAYER_LOCATION, ContainmentContainerRenderer.FluidModelPart::createBodyLayer);
	}
}
