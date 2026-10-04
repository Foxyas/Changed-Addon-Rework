package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.FemaleLuminaraCrystalBeingModel;
import net.foxyas.changedaddon.client.renderer.layers.EmissivePulseLayer;
import net.foxyas.changedaddon.client.renderer.layers.PulsingColorFunction;
import net.foxyas.changedaddon.entity.simple.FemaleLuminaraCrystalBeing;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.ColorFunction;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import static net.foxyas.changedaddon.client.renderer.layers.PulsingColorFunction.fromColorFunction;

public class FemaleLuminaraCrystalBeingRenderer extends AdvancedHumanoidRenderer<FemaleLuminaraCrystalBeing, FemaleLuminaraCrystalBeingModel> {

    public static final ResourceLocation TEXTURE_BASE = ChangedAddonMod.texLoc("entities/female_luminara_crystal_being/base");
    public static final ResourceLocation TEXTURE_GLOW = ChangedAddonMod.texLoc("entities/female_luminara_crystal_being/glow");

    public FemaleLuminaraCrystalBeingRenderer(EntityRendererProvider.Context context) {
        super(context, new FemaleLuminaraCrystalBeingModel(context.bakeLayer(FemaleLuminaraCrystalBeingModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new EmissivePulseLayer<>(this, TEXTURE_GLOW, (e) -> true));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::irisColorLeft).withRightIris(CustomEyesLayer::irisColorRight).build());
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(getFromDefault(CustomEyesLayer::glowingIrisColorLeft)).withRightIris(getFromDefault(CustomEyesLayer::glowingIrisColorRight)).build());
    }

    public PulsingColorFunction<FemaleLuminaraCrystalBeing> getFromDefault(ColorFunction<FemaleLuminaraCrystalBeing> function) {
        return fromColorFunction(function);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FemaleLuminaraCrystalBeing entity) {
        return TEXTURE_BASE;
    }
}