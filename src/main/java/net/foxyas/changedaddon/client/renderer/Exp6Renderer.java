package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.GrapeSnowLeopardModel;
import net.foxyas.changedaddon.entity.Exp6Entity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class Exp6Renderer extends AdvancedHumanoidRenderer<Exp6Entity, GrapeSnowLeopardModel, ArmorLatexFemaleCatModel<Exp6Entity>> {
    public Exp6Renderer(EntityRendererProvider.Context context) {
        super(context, new GrapeSnowLeopardModel(context.bakeLayer(GrapeSnowLeopardModel.LAYER_LOCATION)),
                ArmorLatexFemaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotArmFur));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        //this.addLayer(new CustomHairColorLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/female_snep_hair"),true));
        //this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor,CustomEyesLayer::glowingIrisColorLeft,CustomEyesLayer::glowingIrisColorRight));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Exp6Entity entity) {
        return new ResourceLocation("changed_addon:textures/entities/exp6_texture.png");
    }
}
