package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.HimalayanCrystalGasCatModel;
import net.foxyas.changedaddon.entity.simple.CrystalGasCatMaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class CrystalGasCatMaleRenderer extends AdvancedHumanoidRenderer<CrystalGasCatMaleEntity, HimalayanCrystalGasCatModel, ArmorLatexMaleCatModel<CrystalGasCatMaleEntity>> {
    public CrystalGasCatMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new HimalayanCrystalGasCatModel(context.bakeLayer(HimalayanCrystalGasCatModel.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon:textures/entities/crystal_emission.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(CrystalGasCatMaleEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/himalayan_crystal_gas_cat.png");
    }
}
