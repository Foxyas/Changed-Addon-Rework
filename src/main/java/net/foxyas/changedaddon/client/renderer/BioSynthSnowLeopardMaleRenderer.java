package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.BioSynthSnowLeopardMaleModel;
import net.foxyas.changedaddon.client.renderer.layers.CustomHairColorLayer;
import net.foxyas.changedaddon.entity.simple.BioSynthSnowLeopardMaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BioSynthSnowLeopardMaleRenderer extends AdvancedHumanoidRenderer<BioSynthSnowLeopardMaleEntity, BioSynthSnowLeopardMaleModel> {
    public BioSynthSnowLeopardMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new BioSynthSnowLeopardMaleModel(context.bakeLayer(BioSynthSnowLeopardMaleModel.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotArmFur));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomHairColorLayer<>(this, this.getModel(), ResourceLocation.parse("changed_addon:textures/entities/male_snep_hair")));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::glowingIrisColorLeft).withRightIris(CustomEyesLayer::glowingIrisColorRight).build());
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BioSynthSnowLeopardMaleEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/biosynth_snow_leopard_male.png");
    }
}