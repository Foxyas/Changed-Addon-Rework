package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.FengQIWolfModel;
import net.foxyas.changedaddon.entity.FengQIWolfEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FengQIWolfRenderer extends AdvancedHumanoidRenderer<FengQIWolfEntity, FengQIWolfModel, ArmorLatexMaleWolfModel<FengQIWolfEntity>> {
    public FengQIWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new FengQIWolfModel(context.bakeLayer(FengQIWolfModel.LAYER_LOCATION)),
                ArmorLatexMaleWolfModel.MODEL_SET, 0.5F);
        //this.addLayer(new LatexParticlesLayer<>(this, this.getModel()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FengQIWolfEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/fengqi_wolf.png");
    }
}
