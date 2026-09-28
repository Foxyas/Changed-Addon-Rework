package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.PuroKindFemaleModel;
import net.foxyas.changedaddon.entity.simple.PuroKindFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class PuroKindFemaleRenderer extends AdvancedHumanoidRenderer<PuroKindFemaleEntity, PuroKindFemaleModel> {
    public PuroKindFemaleRenderer(EntityRendererProvider.Context context) {
        super(context, new PuroKindFemaleModel(context.bakeLayer(PuroKindFemaleModel.LAYER_LOCATION)), ArmorLatexFemaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotMask));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer.fixedColor(Color3.parseHex("#242424"))).withLeftIris(CustomEyesLayer::glowingIrisColorLeft).withRightIris(CustomEyesLayer::glowingIrisColorRight).build());
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PuroKindFemaleEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/puro_kind_female_texture.png");
    }
}