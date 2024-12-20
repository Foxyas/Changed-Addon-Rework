package net.foxyas.changedaddon.client.renderer.layers;

import net.foxyas.changedaddon.entity.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class CustomDisplay<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    private final M model;
    private final RenderType GlowEyeRender;
    private final RenderType NormalEyeRender;
    private final RenderType GlowDisplayRender;
    private final RenderType NormalDisplayRender;
    private final boolean isOnlyHead;

    public CustomDisplay(RenderLayerParent<T, M> parent, M model, ResourceLocation eyePart, ResourceLocation displayPart) {
        super(parent);
        this.model = model;
        // RenderType Glow type
        this.GlowEyeRender = RenderType.eyes(eyePart);
        this.GlowDisplayRender = RenderType.eyes(displayPart);
        // RenderType normal
        this.NormalEyeRender = RenderType.entityCutoutNoCull(eyePart);
        this.NormalDisplayRender = RenderType.entityCutoutNoCull(displayPart);
        this.isOnlyHead = false;
    }

    public CustomDisplay(RenderLayerParent<T, M> parent, M model, ResourceLocation eyePart, ResourceLocation displayPart, boolean OnlyHead) {
        super(parent);
        this.model = model;
        // RenderType Glow type
        this.GlowEyeRender = RenderType.eyes(eyePart);
        this.GlowDisplayRender = RenderType.eyes(displayPart);
        // RenderType normal
        this.NormalEyeRender = RenderType.entityCutoutNoCull(eyePart);
        this.NormalDisplayRender = RenderType.entityCutoutNoCull(displayPart);
        this.isOnlyHead = OnlyHead;
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            RenderType renderType;
            RenderType renderType2;

            // Seleciona renderização com base na luz ao redor da entidade
            if (entity instanceof ReynEntity reynEntity){
                    renderType = GlowEyeRender;
                    renderType2 = GlowDisplayRender;
            } else {
                if(packedLight > 5){
                    renderType = GlowEyeRender;
                    renderType2 = GlowDisplayRender;
                } else {
                    renderType = NormalEyeRender;
                    renderType2 = NormalDisplayRender;
                }
            }


            BasicPlayerInfo info = entity.getBasicPlayerInfo();
            Color3 displayColor = info.getHairColor();  // Cor do display
            Color3 eyeColor = info.getScleraColor();    // Cor dos olhos
            int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

            // Renderiza apenas a cabeça do modelo
            if (isOnlyHead){
                this.model.getHead().render(pose, bufferSource.getBuffer(renderType), packedLight, overlay, eyeColor.red(), eyeColor.green(), eyeColor.blue(), 1.0F);
                this.model.getHead().render(pose, bufferSource.getBuffer(renderType2), packedLight, overlay, displayColor.red(), displayColor.green(), displayColor.blue(), 1.0F);
            } else {
                this.model.renderToBuffer(pose, bufferSource.getBuffer(renderType), packedLight, overlay, eyeColor.red(), eyeColor.green(), eyeColor.blue(), 1.0F);
                this.model.renderToBuffer(pose, bufferSource.getBuffer(renderType2), packedLight, overlay, displayColor.red(), displayColor.green(), displayColor.blue(), 1.0F);
            }
        }
    }
}