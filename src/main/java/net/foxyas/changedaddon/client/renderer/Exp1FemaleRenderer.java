package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.FemaleExp1Model;
import net.foxyas.changedaddon.entity.Exp1FemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class Exp1FemaleRenderer extends AdvancedHumanoidRenderer<Exp1FemaleEntity, FemaleExp1Model, ArmorLatexFemaleWolfModel<Exp1FemaleEntity>> {
    public Exp1FemaleRenderer(EntityRendererProvider.Context context) {
        super(context, new FemaleExp1Model(context.bakeLayer(FemaleExp1Model.LAYER_LOCATION)), ArmorLatexFemaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(Exp1FemaleEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/latex_snowfox_female_new.png");
    }
}