package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.client.model.*;
import net.foxyas.changedaddon.client.model.advanced.*;
import net.foxyas.changedaddon.client.model.armors.*;
import net.foxyas.changedaddon.client.model.clothes.HazardBodySuitLayers;
import net.foxyas.changedaddon.client.model.clothes.LatexHumanHazardBodySuitModel;
import net.foxyas.changedaddon.client.model.projectile.SimpleProjectileModel;
import net.foxyas.changedaddon.client.model.simple.*;
import net.foxyas.changedaddon.client.renderer.blockEntitys.ContainmentContainerRenderer;
import net.foxyas.changedaddon.client.renderer.blockEntitys.SnepPlushyBlockEntityRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonModels {

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        // --- ARMORS MODELS ---
        event.registerLayerDefinition(ModelAccessories.LAYER_LOCATION, ModelAccessories::createBodyLayer);
        event.registerLayerDefinition(ModelHazmatSuit.LAYER_LOCATION, ModelHazmatSuit::createBodyLayer);
        event.registerLayerDefinition(ModelHazardArmorCustomArms.LAYER_LOCATION, ModelHazardArmorCustomArms::createBodyLayer);
        event.registerLayerDefinition(ModelNewHyperFlower.LAYER_LOCATION, ModelNewHyperFlower::createBodyLayer);
        event.registerLayerDefinition(DarkLatexCoatModel.LAYER_LOCATION, DarkLatexCoatModel::createBodyLayer);

        // --- CLOTHING MODELS ---
        event.registerLayerDefinition(HazardBodySuitLayers.PLAYER, () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.05f), false), 64, 64));
        event.registerLayerDefinition(HazardBodySuitLayers.PLAYER_SLIM, () -> LayerDefinition.create(PlayerModel.createMesh(new CubeDeformation(0.05f), true), 64, 64));

        event.registerLayerDefinition(LatexHumanHazardBodySuitModel.LATEX_PLAYER, () -> LatexHumanHazardBodySuitModel.createBodyLayer(new CubeDeformation(0.05f), false));
        event.registerLayerDefinition(LatexHumanHazardBodySuitModel.LATEX_PLAYER_SLIM, () -> LatexHumanHazardBodySuitModel.createBodyLayer(new CubeDeformation(0.05f), true));


        event.registerLayerDefinition(ModelLuminarCrystalSpearModel.LAYER_LOCATION, ModelLuminarCrystalSpearModel::createBodyLayer);

        // --- MONSTER/MOB ENTITIES MODELS---
        event.registerLayerDefinition(ModelFoxyasModel.LAYER_LOCATION, ModelFoxyasModel::createBodyLayer);

        // --- CHANGED ENTITIES MODELS---
        event.registerLayerDefinition(LatexSnowFoxMaleModel.LAYER_LOCATION, LatexSnowFoxMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexSnowFoxFemaleModel.LAYER_LOCATION, LatexSnowFoxFemaleModel::createBodyLayer);
        event.registerLayerDefinition(WhiteFoxModel.LAYER_LOCATION, WhiteFoxModel::createBodyLayer);
        event.registerLayerDefinition(LatexDazedModel.LAYER_LOCATION, LatexDazedModel::createBodyLayer);
        event.registerLayerDefinition(BuffLatexDazedModel.LAYER_LOCATION, BuffLatexDazedModel::createBodyLayer);
        event.registerLayerDefinition(PuroKindMaleModel.LAYER_LOCATION, PuroKindMaleModel::createBodyLayer);
        event.registerLayerDefinition(PuroKindFemaleModel.LAYER_LOCATION, PuroKindFemaleModel::createBodyLayer);
        event.registerLayerDefinition(BunyModel.LAYER_LOCATION, BunyModel::createBodyLayer);
        event.registerLayerDefinition(BioSynthSnowLeopardMaleModel.LAYER_LOCATION, BioSynthSnowLeopardMaleModel::createBodyLayer);
        event.registerLayerDefinition(BioSynthSnowLeopardFemaleModel.LAYER_LOCATION, BioSynthSnowLeopardFemaleModel::createBodyLayer);
        event.registerLayerDefinition(Experiment009Model.LAYER_LOCATION, Experiment009Model::createBodyLayer);
        event.registerLayerDefinition(Experiment009BossModel.LAYER_LOCATION, Experiment009BossModel::createBodyLayer);
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
        event.registerLayerDefinition(LuminarcticLeopardMaleModel.LAYER_LOCATION, LuminarcticLeopardMaleModel::createBodyLayer);
        event.registerLayerDefinition(LuminarcticLeopardFemaleModel.LAYER_LOCATION, LuminarcticLeopardFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexSquidTigerSharkModel.LAYER_LOCATION, LatexSquidTigerSharkModel::createBodyLayer);
        event.registerLayerDefinition(LynxModel.LAYER_LOCATION, LynxModel::createBodyLayer);
        event.registerLayerDefinition(SnepsiLeopardModel.LAYER_LOCATION, SnepsiLeopardModel::createBodyLayer);
        event.registerLayerDefinition(FoxtaFoxyModel.LAYER_LOCATION, FoxtaFoxyModel::createBodyLayer);
        event.registerLayerDefinition(FengQIWolfModel.LAYER_LOCATION, FengQIWolfModel::createBodyLayer);
        event.registerLayerDefinition(BagelModel.LAYER_LOCATION, BagelModel::createBodyLayer);
        event.registerLayerDefinition(LatexDragonSnowLeopardSharkModel.LAYER_LOCATION, LatexDragonSnowLeopardSharkModel::createBodyLayer);
        event.registerLayerDefinition(HimalayanCrystalGasCatMaleModel.LAYER_LOCATION, HimalayanCrystalGasCatMaleModel::createBodyLayer);
        event.registerLayerDefinition(HimalayanCrystalGasCatFemaleModel.LAYER_LOCATION, HimalayanCrystalGasCatFemaleModel::createBodyLayer);
        event.registerLayerDefinition(VoidFoxModel.LAYER_LOCATION, VoidFoxModel::createBodyLayer);
        event.registerLayerDefinition(HaydenFennecFoxModel.LAYER_LOCATION, HaydenFennecFoxModel::createBodyLayer);
        event.registerLayerDefinition(BlueLizardModel.LAYER_LOCATION, BlueLizardModel::createBodyLayer);
        event.registerLayerDefinition(AvaliModel.LAYER_LOCATION, AvaliModel::createBodyLayer);
        event.registerLayerDefinition(LatexKitsuneMaleModel.LAYER_LOCATION, LatexKitsuneMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexKitsuneFemaleModel.LAYER_LOCATION, LatexKitsuneFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexCalicoCatModel.LAYER_LOCATION, LatexCalicoCatModel::createBodyLayer);
        event.registerLayerDefinition(ProtogenModel.LAYER_LOCATION, ProtogenModel::createBodyLayer);
        event.registerLayerDefinition(MongooseModel.LAYER_LOCATION, MongooseModel::createBodyLayer);
        event.registerLayerDefinition(BorealisMaleModel.LAYER_LOCATION, BorealisMaleModel::createBodyLayer);
        event.registerLayerDefinition(BorealisFemaleModel.LAYER_LOCATION, BorealisFemaleModel::createBodyLayer);
        event.registerLayerDefinition(PrototypeModel.LAYER_LOCATION, PrototypeModel::createBodyLayer);
        event.registerLayerDefinition(PinkCyanSkunkModel.LAYER_LOCATION, PinkCyanSkunkModel::createBodyLayer);
        event.registerLayerDefinition(LatexWindCatMaleModel.LAYER_LOCATION, LatexWindCatMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWindCatFemaleModel.LAYER_LOCATION, LatexWindCatFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWhiteSnowLeopardMaleModel.LAYER_LOCATION, LatexWhiteSnowLeopardMaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexWhiteSnowLeopardFemaleModel.LAYER_LOCATION, LatexWhiteSnowLeopardFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexCheetahFemaleModel.LAYER_LOCATION, LatexCheetahFemaleModel::createBodyLayer);
        event.registerLayerDefinition(LatexCheetahMaleModel.LAYER_LOCATION, LatexCheetahMaleModel::createBodyLayer);
        event.registerLayerDefinition(LuminaraFlowerBeastModel.LAYER_LOCATION, LuminaraFlowerBeastModel::createBodyLayer);
        event.registerLayerDefinition(Protogen0senia0Model.LAYER_LOCATION, Protogen0senia0Model::createBodyLayer);
        event.registerLayerDefinition(LatexKaylaSharkModel.LAYER_LOCATION, LatexKaylaSharkModel::createBodyLayer);
        event.registerLayerDefinition(LatexSnowFoxFoxyasModel.LAYER_LOCATION, LatexSnowFoxFoxyasModel::createBodyLayer);
        event.registerLayerDefinition(LatexBorderCollieModel.LAYER_LOCATION, LatexBorderCollieModel::createBodyLayer);
        event.registerLayerDefinition(DarkLatexYufengQueenModel.LAYER_LOCATION, DarkLatexYufengQueenModel::createBodyLayer);


        // --- PROJECTILES ENTITIES MODELS ---
        event.registerLayerDefinition(SimpleProjectileModel.LAYER_LOCATION, SimpleProjectileModel::createBodyLayer);

        // --- CUSTOM CHANGED ENTITIES ARMOR MODEL ---
        ArmorLatexSquidTigerSharkModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);
        ArmorLatexDragonSnowLeopardSharkModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);
        ArmorLuminaraFlowerBeastModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);
        ArmorLatexDazedModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);
        ArmorAvaliModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);
        ArmorProtogen0senia0.ARMOR.registerDefinitions(event::registerLayerDefinition);
        ArmorLatexFemaleDragonAltTailModel.MODEL_SET.registerDefinitions(event::registerLayerDefinition);

        // --- Non Anthro Entities Model ---
        event.registerLayerDefinition(LatexSnepModel.LAYER_LOCATION, LatexSnepModel::createBodyLayer);

        // --- Block Entities Custom Models ---
        event.registerLayerDefinition(SnepPlushyBlockEntityRenderer.SnepPlushExtraModel.LAYER_LOCATION, SnepPlushyBlockEntityRenderer.SnepPlushExtraModel::createBodyLayer);
        event.registerLayerDefinition(ContainmentContainerRenderer.FluidModelPart.LAYER_LOCATION, ContainmentContainerRenderer.FluidModelPart::createBodyLayer);
    }
}
