package net.foxyas.changedaddon.client.renderer.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.model.advanced.AvaliModel;
import net.foxyas.changedaddon.client.model.armors.ArmorAvaliModel;
import net.foxyas.changedaddon.entity.advanced.AvaliZerGodMasterEntity;
import net.ltxprogrammer.changed.client.renderer.AdvancedHumanoidRenderer;
import net.ltxprogrammer.changed.client.renderer.layers.CustomEyesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.GasMaskLayer;
import net.ltxprogrammer.changed.client.renderer.layers.LatexParticlesLayer;
import net.ltxprogrammer.changed.client.renderer.layers.TransfurCapeLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AvaliZerGodMasterRenderer extends AdvancedHumanoidRenderer<AvaliZerGodMasterEntity, AvaliModel<AvaliZerGodMasterEntity>> {
    public AvaliZerGodMasterRenderer(EntityRendererProvider.Context context) {
        super(context, new AvaliModel<>(context.bakeLayer(AvaliModel.LAYER_LOCATION)), ArmorAvaliModel.MODEL_SET, 0.5f);
        this.addLayer(new LatexParticlesLayer<>(this, getModel()));
        this.addLayer(new CustomEyesLayer<>(this, context.getModelSet()));
        this.addLayer(TransfurCapeLayer.normalCape(this, context.getModelSet()));
        this.addLayer(new GasMaskLayer<>(this, context.getModelSet()));
    }

    @Override
    protected void scale(@NotNull AvaliZerGodMasterEntity avaliEntity, @NotNull PoseStack poseStack, float partialTick) {
        super.scale(avaliEntity, poseStack, partialTick);
        this.shadowRadius *= avaliEntity.getDimensionScale();
        poseStack.scale(avaliEntity.getDimensionScale(), avaliEntity.getDimensionScale(), avaliEntity.getDimensionScale());
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AvaliZerGodMasterEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/entities/avali_male/avali_zergodmaster.png");
    }
}
