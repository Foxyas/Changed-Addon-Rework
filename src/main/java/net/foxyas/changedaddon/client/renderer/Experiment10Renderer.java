package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.Experiment10Model;
import net.foxyas.changedaddon.client.renderer.layers.BloodLayer;
import net.foxyas.changedaddon.client.renderer.layers.Exp10EmissiveBodyLayer;
import net.foxyas.changedaddon.entity.bosses.Experiment10Entity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Experiment10Renderer extends AdvancedHumanoidRenderer<Experiment10Entity, Experiment10Model> {
    public Experiment10Renderer(EntityRendererProvider.Context context) {
        super(context, new Experiment10Model(context.bakeLayer(Experiment10Model.LAYER_LOCATION)),
                ArmorLatexFemaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new Exp10EmissiveBodyLayer<>(this, ChangedAddonMod.textureLoc("textures/entities/experiment_10/experiment_10_glow.png")));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(new BloodLayer<>(this, ChangedAddonMod.textureLoc("textures/entities/experiment_10/experiment_10_phase2.png")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Experiment10Entity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/experiment_10/experiment_10.png");
    }

}
