package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.HimalayanCrystalGasCatMaleModel;
import net.foxyas.changedaddon.entity.simple.CrystalGasCatMaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrystalGasCatMaleRenderer extends AdvancedHumanoidRenderer<CrystalGasCatMaleEntity, HimalayanCrystalGasCatMaleModel> {
    public CrystalGasCatMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new HimalayanCrystalGasCatMaleModel(context.bakeLayer(HimalayanCrystalGasCatMaleModel.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::glowingIrisColorLeft).withRightIris(CustomEyesLayer::glowingIrisColorRight).build());
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, ResourceLocation.parse("changed_addon:textures/entities/crystal_cats/male/crystal_layer.png")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CrystalGasCatMaleEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/crystal_cats/male/himalayan_crystal_cat_male.png");
    }
}
