package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.LynxModel;
import net.foxyas.changedaddon.entity.simple.LynxEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LynxRenderer extends AdvancedHumanoidRenderer<LynxEntity, LynxModel, ArmorLatexMaleCatModel<LynxEntity>> {
    public LynxRenderer(EntityRendererProvider.Context context) {
        super(context, new LynxModel(context.bakeLayer(LynxModel.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);
        //this.addLayer(new LatexParticlesLayer<>(this, getModel())); Is Organic .-.
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::irisColorLeft, CustomEyesLayer::irisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
    }

    @Override
    public ResourceLocation getTextureLocation(LynxEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/lynx.png");
    }
}
