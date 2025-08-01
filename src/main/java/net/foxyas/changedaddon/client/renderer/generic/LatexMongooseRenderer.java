package net.foxyas.changedaddon.client.renderer.generic;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.simple.LatexMongooseModel;
import net.foxyas.changedaddon.entity.simple.LatexMongooseEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LatexMongooseRenderer extends AdvancedHumanoidRenderer<LatexMongooseEntity, LatexMongooseModel, ArmorLatexMaleCatModel<LatexMongooseEntity>> {
    private static final ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/entities/mongoose/mongoose");

    public LatexMongooseRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexMongooseModel(context.bakeLayer(LatexMongooseModel.LAYER_LOCATION)), ArmorLatexMaleCatModel.MODEL_SET, 0.5F);
        this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull LatexMongooseEntity p_114482_) {
        return TEXTURE;
    }
}
