package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.MaleExp2Model;
import net.foxyas.changedaddon.client.renderer.layers.CustomHairColorLayer;
import net.foxyas.changedaddon.entity.Exp2MaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class Exp2MaleRenderer extends AdvancedHumanoidRenderer<Exp2MaleEntity, MaleExp2Model, ArmorLatexMaleCatModel<Exp2MaleEntity>> {
    public Exp2MaleRenderer(EntityRendererProvider.Context context) {
        super(context, new MaleExp2Model(context.bakeLayer(MaleExp2Model.LAYER_LOCATION)),
                ArmorLatexMaleCatModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel(), model::isPartNotArmFur));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomHairColorLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/male_snep_hair")));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(Exp2MaleEntity entity) {
        if (entity.getUnderlyingPlayer() == null) {
            return new ResourceLocation("changed_addon:textures/entities/male_exp2_snow_leopard.png");
        }

        if (entity.getUnderlyingPlayer() != null && entity.getHealth() / entity.getMaxHealth() <= 0.60 && entity.getHealth() / entity.getMaxHealth() > 0.30) {
            return new ResourceLocation("changed_addon:textures/entities/male_exp2_snow_leopard_hurt.png");
        }
        if (entity.getUnderlyingPlayer() != null && entity.getHealth() / entity.getMaxHealth() <= 0.30) {
            return new ResourceLocation("changed_addon:textures/entities/male_exp2_snow_leopard_badly_hurt.png");
        }
        if (entity.getUnderlyingPlayer() != null && !(entity.getHealth() / entity.getMaxHealth() <= 0.60)) {
            return new ResourceLocation("changed_addon:textures/entities/male_exp2_snow_leopard.png");
        }

        return new ResourceLocation("changed_addon:textures/entities/male_exp2_snow_leopard.png");
    }
}
