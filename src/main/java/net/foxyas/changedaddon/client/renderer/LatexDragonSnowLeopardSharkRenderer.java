package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.LatexDragonSnowLeopardSharkModel;
import net.foxyas.changedaddon.client.model.armors.ArmorLatexDragonSnowLeopardSharkModel;
import net.foxyas.changedaddon.entity.advanced.LatexDragonSnowLeopardSharkEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LatexDragonSnowLeopardSharkRenderer extends AdvancedHumanoidRenderer<LatexDragonSnowLeopardSharkEntity, LatexDragonSnowLeopardSharkModel, ArmorLatexDragonSnowLeopardSharkModel<LatexDragonSnowLeopardSharkEntity>> {
    public LatexDragonSnowLeopardSharkRenderer(EntityRendererProvider.Context context) {
        super(context, new LatexDragonSnowLeopardSharkModel(context.bakeLayer(LatexDragonSnowLeopardSharkModel.LAYER_LOCATION)),
                ArmorLatexDragonSnowLeopardSharkModel.MODEL_SET, 0.5F);
        this.addLayer(new LatexParticlesLayer<>(this, this.model));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forLargeSnouted(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LatexDragonSnowLeopardSharkEntity p_114482_) {
        return new ResourceLocation("changed_addon:textures/entities/latex_dragon_snep_shark.png");
    }
}