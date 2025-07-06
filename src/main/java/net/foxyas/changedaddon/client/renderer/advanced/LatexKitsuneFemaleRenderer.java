package net.foxyas.changedaddon.client.renderer.advanced;

import net.foxyas.changedaddon.client.model.advanced.LatexKitsuneFemaleModel;
import net.foxyas.changedaddon.entity.advanced.LatexKitsuneFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexKitsuneFemaleRenderer extends AdvancedHumanoidRenderer<LatexKitsuneFemaleEntity, LatexKitsuneFemaleModel, ArmorLatexFemaleWolfModel<LatexKitsuneFemaleEntity>> {
    public LatexKitsuneFemaleRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexKitsuneFemaleModel(context.bakeLayer(LatexKitsuneFemaleModel.LAYER_LOCATION)), ArmorLatexFemaleWolfModel::new, ArmorLatexFemaleWolfModel.INNER_ARMOR, ArmorLatexFemaleWolfModel.OUTER_ARMOR, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
        this.addLayer(new EmissiveBodyLayer<>(this, new ResourceLocation("changed_addon:textures/entities/latex_kitsune_female/latex_kitsune_female_stripes.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(LatexKitsuneFemaleEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/latex_kitsune_female/latex_kitsune_female.png");
    }
}