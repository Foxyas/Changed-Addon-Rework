package net.foxyas.changedaddon.client.renderer.basic;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.simple.MongooseModel;
import net.foxyas.changedaddon.entity.simple.MongooseEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MongooseRenderer extends AdvancedHumanoidRenderer<MongooseEntity, MongooseModel, ArmorLatexMaleCatModel<MongooseEntity>> {
    private static final ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/entities/mongoose/mongoose");

    public MongooseRenderer(EntityRendererProvider.Context context) {
        super(context, new MongooseModel(context.bakeLayer(MongooseModel.LAYER_LOCATION)), ArmorLatexMaleCatModel.MODEL_SET, 0.5F);
        //is not a gooey entity
        //this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull MongooseEntity mongooseEntity) {
        return TEXTURE;
    }
}
