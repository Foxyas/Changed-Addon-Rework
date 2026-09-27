package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.PuroKindMaleModel;
import net.foxyas.changedaddon.entity.simple.PuroKindMaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class PuroKindMaleRenderer extends AdvancedHumanoidRenderer<PuroKindMaleEntity, PuroKindMaleModel> {
    public PuroKindMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new PuroKindMaleModel(context.bakeLayer(PuroKindMaleModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotMask));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer.fixedColor(Color3.parseHex("#242424"))).withLeftIris(CustomEyesLayer::glowingIrisColorLeft).withRightIris(CustomEyesLayer::glowingIrisColorRight).build());
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }


    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PuroKindMaleEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/puro_kind_texture.png");
    }
}