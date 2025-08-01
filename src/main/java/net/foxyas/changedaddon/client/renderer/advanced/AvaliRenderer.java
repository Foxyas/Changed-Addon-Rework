package net.foxyas.changedaddon.client.renderer.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.BagelModel;
import net.foxyas.changedaddon.client.model.advanced.AvaliModel;
import net.foxyas.changedaddon.client.renderer.layers.AvaliColorsLayer;
import net.foxyas.changedaddon.entity.BagelEntity;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.*;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleDragonModel;
import net.ltxprogrammer.changed.client.renderer.model.armor.ArmorLatexMaleWolfModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AvaliRenderer extends AdvancedHumanoidRenderer<AvaliEntity, AvaliModel, ArmorLatexMaleDragonModel<AvaliEntity>> {
    public AvaliRenderer(EntityRendererProvider.Context context) {
        super(context, new AvaliModel(context.bakeLayer(AvaliModel.LAYER_LOCATION)), ArmorLatexMaleDragonModel::new, ArmorLatexMaleDragonModel.INNER_ARMOR, ArmorLatexMaleDragonModel.OUTER_ARMOR, 0.5f);
        //this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(new AvaliColorsLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/avali_gender/avali_gender_primary.png"), 0));
        this.addLayer(new AvaliColorsLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/avali_gender/avali_gender_secondary.png"), 1));
        this.addLayer(new AvaliColorsLayer<>(this, this.getModel(), new ResourceLocation("changed_addon:textures/entities/avali_gender/avali_gender_stripes.png"), 2));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    protected void scale(@NotNull AvaliEntity avaliEntity, @NotNull PoseStack poseStack, float partialTick) {
        super.scale(avaliEntity, poseStack, partialTick);
        this.shadowRadius *= avaliEntity.getDimensionScale();
        poseStack.scale(avaliEntity.getDimensionScale(), avaliEntity.getDimensionScale(), avaliEntity.getDimensionScale());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AvaliEntity entity) {
        return new ResourceLocation("changed_addon:textures/entities/avali.png");
    }
}
