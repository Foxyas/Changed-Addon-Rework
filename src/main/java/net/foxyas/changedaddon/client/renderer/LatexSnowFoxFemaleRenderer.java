package net.foxyas.changedaddon.client.renderer;

import net.foxyas.changedaddon.client.model.ModelFemaleSnowFox;
import net.foxyas.changedaddon.entity.simple.LatexSnowFoxFemaleEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexFemaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LatexSnowFoxFemaleRenderer extends AdvancedHumanoidRenderer<LatexSnowFoxFemaleEntity, ModelFemaleSnowFox, ArmorLatexFemaleWolfModel<LatexSnowFoxFemaleEntity>> {
    public LatexSnowFoxFemaleRenderer(EntityRendererProvider.Context context) {
        super(context, new ModelFemaleSnowFox(context.bakeLayer(ModelFemaleSnowFox.LAYER_LOCATION)), ArmorLatexFemaleWolfModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(GasMaskLayer.forSnouted(this, context.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LatexSnowFoxFemaleEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/latex_snowfox_female_new.png");
    }
}