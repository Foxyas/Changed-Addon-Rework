package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.LatexSnepSharkModel;
import net.foxyas.changedaddon.entity.LatexSnepSharkEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleSharkModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class LatexSnepSharkRenderer extends AdvancedHumanoidRenderer<LatexSnepSharkEntity, LatexSnepSharkModel, ArmorLatexMaleSharkModel<LatexSnepSharkEntity>> {
    public LatexSnepSharkRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexSnepSharkModel(context.bakeLayer(LatexSnepSharkModel.LAYER_LOCATION)), ArmorLatexMaleSharkModel::new, ArmorLatexMaleSharkModel.INNER_ARMOR, ArmorLatexMaleSharkModel.OUTER_ARMOR, 0.5F);
        this.addLayer(new LatexParticlesLayer(this, this.model));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forLargeSnouted(this, context.getModelSet()));
    }

    public ResourceLocation getTextureLocation(LatexSnepSharkEntity p_114482_) {
        return new ResourceLocation("changed_addon:textures/entities/latex_snep_shark.png");
    }
}