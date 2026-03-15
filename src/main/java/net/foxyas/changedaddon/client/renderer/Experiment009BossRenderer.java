package net.foxyas.changedaddon.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.Experiment009BossModel;
import net.foxyas.changedaddon.client.renderer.layers.Exp9EmissiveBodyLayer;
import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Experiment009BossRenderer extends AdvancedHumanoidRenderer<Experiment009BossEntity, Experiment009BossModel> {
    public Experiment009BossRenderer(EntityRendererProvider.Context context) {
        super(context, new Experiment009BossModel(context.bakeLayer(Experiment009BossModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new Exp9EmissiveBodyLayer<>(this, ChangedAddonMod.textureLoc("textures/entities/experiment_9/experiment_009_glow_layer")));
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#8dcfff")), CustomEyesLayer.fixedColorGlowing(Color3.parseHex("#8dcfff")), CustomEyesLayer::noRender, CustomEyesLayer::noRender));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Experiment009BossEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/experiment_9/experiment_009.png");
    }

}


