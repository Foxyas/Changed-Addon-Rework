package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.HimalayanCrystalGasCatFemaleModel;
import net.foxyas.changedaddon.entity.CrystalGasCatFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrystalGasCatFemaleRenderer extends AdvancedHumanoidRenderer<CrystalGasCatFemaleEntity, HimalayanCrystalGasCatFemaleModel, ArmorLatexMaleCatModel<CrystalGasCatFemaleEntity>> {
    public CrystalGasCatFemaleRenderer(EntityRendererProvider.Context context) {
        super(context, new HimalayanCrystalGasCatFemaleModel(context.bakeLayer(HimalayanCrystalGasCatFemaleModel.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon:textures/entities/crystal_emission.png")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CrystalGasCatFemaleEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/himalayan_crystal_gas_cat_female.png");
    }
}
