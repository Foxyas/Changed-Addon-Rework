package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.FemaleLuminaraCrystalBeingModel;
import net.foxyas.changedaddon.entity.simple.FemaleLuminaraCrystalBeing;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FemaleLuminaraCrystalBeingRenderer extends AdvancedHumanoidRenderer<FemaleLuminaraCrystalBeing, FemaleLuminaraCrystalBeingModel> {

    public FemaleLuminaraCrystalBeingRenderer(EntityRendererProvider.Context context) {
        super(context, new FemaleLuminaraCrystalBeingModel(context.bakeLayer(FemaleLuminaraCrystalBeingModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::irisColorLeft).withRightIris(CustomEyesLayer::irisColorRight).build());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FemaleLuminaraCrystalBeing entity) {
        return ChangedAddonMod.texLoc("entity/female_luminara_crystal_being/base");
    }
}