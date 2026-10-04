package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.MaleLuminaraCrystalBeingModel;
import net.foxyas.changedaddon.client.renderer.layers.EmissivePulseLayer;
import net.foxyas.changedaddon.client.renderer.layers.PulsingColorFunction;
import net.foxyas.changedaddon.entity.simple.FemaleLuminaraCrystalBeing;
import net.foxyas.changedaddon.entity.simple.MaleLuminaraCrystalBeing;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer.ColorFunction;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static net.foxyas.changedaddon.client.renderer.layers.PulsingColorFunction.fromColorFunction;

public class MaleLuminaraCrystalBeingRenderer extends AdvancedHumanoidRenderer<MaleLuminaraCrystalBeing, MaleLuminaraCrystalBeingModel> {

    public static final ResourceLocation TEXTURE_BASE = ChangedAddonMod.texLoc("entities/male_luminara_crystal_being/base");
    public static final ResourceLocation TEXTURE_GLOW = ChangedAddonMod.texLoc("entities/male_luminara_crystal_being/glow");

    public MaleLuminaraCrystalBeingRenderer(EntityRendererProvider.Context context) {
        super(context, new MaleLuminaraCrystalBeingModel(context.bakeLayer(MaleLuminaraCrystalBeingModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new EmissivePulseLayer<>(this, TEXTURE_GLOW, (e) -> true));
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::scleraColor).withLeftIris(CustomEyesLayer::irisColorLeft).withRightIris(CustomEyesLayer::irisColorRight).build());
        this.addLayer(CustomEyesLayer.builder(this, context.getModelSet()).withSclera(CustomEyesLayer::noRender)
                .withLeftIris(getFromDefault(CustomEyesLayer.fixedColorGlowing(Color3.getColor("b473e9"))))
                .withRightIris(getFromDefault(CustomEyesLayer.fixedColorGlowing(Color3.getColor("b473e9"))))
                .build()
        );
    }

    public PulsingColorFunction<MaleLuminaraCrystalBeing> getFromDefault(ColorFunction<MaleLuminaraCrystalBeing> function) {
        return fromColorFunction(function);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MaleLuminaraCrystalBeing entity) {
        return TEXTURE_BASE;
    }
}