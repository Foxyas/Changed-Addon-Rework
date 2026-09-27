package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.HimalayanCrystalGasCatFemaleModel;
import net.foxyas.changedaddon.entity.simple.CrystalGasCatFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrystalGasCatFemaleRenderer extends AdvancedHumanoidRenderer<CrystalGasCatFemaleEntity, HimalayanCrystalGasCatFemaleModel> {
    public CrystalGasCatFemaleRenderer(EntityRendererProvider.Context context) {
        super(context, new HimalayanCrystalGasCatFemaleModel(context.bakeLayer(HimalayanCrystalGasCatFemaleModel.LAYER_LOCATION)),
                ArmorLatexFemaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
                this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::glowingIrisColorLeft).withRightIris(CustomEyesLayer::glowingIrisColorRight).build());
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, ResourceLocation.parse("changed_addon:textures/entities/crystal_cats/female/crystal_layer.png")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CrystalGasCatFemaleEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/crystal_cats/female/himalayan_crystal_cat_female.png");
    }
}
