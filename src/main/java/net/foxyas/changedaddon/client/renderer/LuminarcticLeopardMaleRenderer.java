package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.model.LuminarcticLeopardMaleModel;
import net.foxyas.changedaddon.client.renderer.layers.LuminarcticLeopardsConditionalLayers.GlowFelineEyesLayer;
import net.foxyas.changedaddon.client.renderer.layers.LuminarcticLeopardsConditionalLayers.GlowLayer;
import net.foxyas.changedaddon.client.renderer.layers.LuminarcticLeopardsConditionalLayers.LuminarcticCustomEyesLayer;
import net.foxyas.changedaddon.entity.bosses.LuminarcticLeopardMaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.EmissiveBodyLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LuminarcticLeopardMaleRenderer extends AdvancedHumanoidRenderer<LuminarcticLeopardMaleEntity, LuminarcticLeopardMaleModel> {

    private static final ResourceLocation TEXTURE = texture("male/luminarctic_leopard");
    private static final ResourceLocation TEXTURE_NO_EYES = texture("male/luminarctic_leopard_no_eyes");

    public LuminarcticLeopardMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new LuminarcticLeopardMaleModel(context.bakeLayer(LuminarcticLeopardMaleModel.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);

        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotArmFur));
        this.addLayer(LuminarcticCustomEyesLayer.create(this, context.getModelSet()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new GlowLayer<>(this, texture("male/luminarctic_leopard_ability_active")));
        this.addLayer(new GlowFelineEyesLayer<>(this, texture("male/luminarctic_leopard_feline_eyes_male")));
        this.addLayer(new EmissiveBodyLayer<>(this, texture("crystals_layer")));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LuminarcticLeopardMaleEntity entity) {
        return entity.getUnderlyingPlayer() != null ? TEXTURE_NO_EYES : TEXTURE;
    }

    private static ResourceLocation texture(String path) {
        if (path.endsWith(".png")) {
            return ChangedAddonMod.resourceLoc("textures/entities/luminarctic_leopards/" + path);
        } else {
            return ChangedAddonMod.textureLoc("textures/entities/luminarctic_leopards/" + path);
        }
    }
}