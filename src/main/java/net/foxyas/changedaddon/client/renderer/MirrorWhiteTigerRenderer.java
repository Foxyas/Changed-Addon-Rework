package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelMirrorWhiteTiger;
import net.foxyas.changedaddon.entity.simple.MirrorWhiteTigerEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MirrorWhiteTigerRenderer extends AdvancedHumanoidRenderer<MirrorWhiteTigerEntity, ModelMirrorWhiteTiger> {
    public MirrorWhiteTigerRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelMirrorWhiteTiger(context.bakeLayer(ModelMirrorWhiteTiger.LAYER_LOCATION)),
                ArmorLatexFemaleCatModel.MODEL_SET, 0.5f);
        //this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::irisColorLeft).withRightIris(CustomEyesLayer::irisColorRight).build());
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MirrorWhiteTigerEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/mirror_white_tiger_female.png");
    }
}
