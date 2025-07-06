package net.foxyas.changedaddon.client.renderer.simple;

import net.foxyas.changedaddon.client.model.simple.LatexCalicoCatModel;
import net.foxyas.changedaddon.entity.simple.LatexCalicoCatEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleCatModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LatexCalicoCatRenderer extends AdvancedHumanoidRenderer<LatexCalicoCatEntity, LatexCalicoCatModel, ArmorLatexMaleCatModel<LatexCalicoCatEntity>> {
    public LatexCalicoCatRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexCalicoCatModel(context.bakeLayer(LatexCalicoCatModel.LAYER_LOCATION)), ArmorLatexMaleCatModel::new, ArmorLatexMaleCatModel.INNER_ARMOR, ArmorLatexMaleCatModel.OUTER_ARMOR, 0.5F);
        this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet(), CustomEyesLayer::scleraColor, CustomEyesLayer::glowingIrisColorLeft, CustomEyesLayer::glowingIrisColorRight, CustomEyesLayer::noRender, CustomEyesLayer::noRender));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull LatexCalicoCatEntity p_114482_) {
        return new ResourceLocation("changed_addon","textures/entities/latex_calico_cat/latex_calico_cat.png");
    }
}
