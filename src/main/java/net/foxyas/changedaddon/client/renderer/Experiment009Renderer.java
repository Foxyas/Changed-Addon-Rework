package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.Experiment009Model;
import net.foxyas.changedaddon.client.renderer.layers.Exp9EmissiveBodyLayer;
import net.foxyas.changedaddon.entity.bosses.Experiment009Entity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Experiment009Renderer extends AdvancedHumanoidRenderer<Experiment009Entity, Experiment009Model> {
    public Experiment009Renderer(EntityRendererProvider.Context context) {
        super(context, new Experiment009Model(context.bakeLayer(Experiment009Model.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new Exp9EmissiveBodyLayer<>(this, ChangedAddonMod.textureLoc("textures/entities/experiment_9/experiment_009_glow_layer")));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#8dcfff")), CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#8dcfff")), CustomEyesLayer::noRender, CustomEyesLayer::noRender));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Experiment009Entity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/experiment_9/experiment_009.png");
    }

}


