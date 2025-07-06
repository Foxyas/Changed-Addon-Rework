package net.foxyas.changedaddon.client.renderer.advanced;

import net.foxyas.changedaddon.client.model.advanced.LatexKitsuneMaleModel;
import net.foxyas.changedaddon.entity.advanced.LatexKitsuneMaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexKitsuneMaleRenderer extends AdvancedHumanoidRenderer<LatexKitsuneMaleEntity, LatexKitsuneMaleModel, ArmorLatexMaleWolfModel<LatexKitsuneMaleEntity>> {
    public LatexKitsuneMaleRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexKitsuneMaleModel(context.bakeLayer(LatexKitsuneMaleModel.LAYER_LOCATION)), ArmorLatexMaleWolfModel::new, ArmorLatexMaleWolfModel.INNER_ARMOR, ArmorLatexMaleWolfModel.OUTER_ARMOR, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon:textures/entities/latex_kitsune_male/latex_kitsune_male_stripes.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(LatexKitsuneMaleEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/latex_kitsune_male/latex_kitsune_male.png");
    }
}