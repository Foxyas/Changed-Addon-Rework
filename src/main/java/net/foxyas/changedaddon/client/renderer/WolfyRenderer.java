package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.WolfyModel;
import net.foxyas.changedaddon.entity.WolfyEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WolfyRenderer extends AdvancedHumanoidRenderer<WolfyEntity, WolfyModel, ArmorLatexMaleWolfModel<WolfyEntity>> {
    public WolfyRenderer(EntityRendererProvider.Context context) {
        super(context, new WolfyModel(context.bakeLayer(WolfyModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotMask));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer.fixedColor(Color3.parseHex("#242424")), CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(WolfyEntity entity) {

        if (entity.getHealth() / entity.getMaxHealth() <= 0.50 && entity.getHealth() / entity.getMaxHealth() > 0.30) {
            return new ResourceLocation("changed_addon:textures/entities/noitems50hp.png");
        }
        if (entity.getHealth() / entity.getMaxHealth() <= 0.30) {
            return new ResourceLocation("changed_addon:textures/entities/noitems1hp.png");
        }
        return new ResourceLocation("changed_addon:textures/entities/noitems.png");
    }
}
