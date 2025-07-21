package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.entity.ReynEntity;
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
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class ProtogenDisplay<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {
    private final M model;
    private final RenderType GlowEyeRender;
    private final RenderType NormalEyeRender;
    private final RenderType GlowDisplayRender;
    private final RenderType NormalDisplayRender;
    private final boolean isOnlyHead;

    public ProtogenDisplay(RenderLayerParent<T, M> parent, M model, ResourceLocation eyePart, ResourceLocation displayPart) {
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

    public ProtogenDisplay(RenderLayerParent<T, M> parent, M model, ResourceLocation eyePart, ResourceLocation displayPart, boolean OnlyHead) {
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
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            RenderType renderType = GlowEyeRender;
            RenderType renderType2 = NormalDisplayRender;


            BasicPlayerInfo info = entity.getBasicPlayerInfo();
            Color3 displayColor = info.getScleraColor();  // Cor do display
            Color3 eyeColor = info.getRightIrisColor();    // Cor dos olhos
            int overlay = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

            // Renderiza apenas a cabeça do modelo
            if (isOnlyHead) {
                this.model.getHead().render(poseStack, bufferSource.getBuffer(renderType), packedLight, overlay, eyeColor.red(), eyeColor.green(), eyeColor.blue(), 1.0F);
                this.model.getHead().render(poseStack, bufferSource.getBuffer(renderType2), packedLight, overlay, displayColor.red(), displayColor.green(), displayColor.blue(), 1.0F);
            } else {
                this.model.renderToBuffer(poseStack, bufferSource.getBuffer(renderType), packedLight, overlay, eyeColor.red(), eyeColor.green(), eyeColor.blue(), 1.0F);
                this.model.renderToBuffer(poseStack, bufferSource.getBuffer(renderType2), packedLight, overlay, displayColor.red(), displayColor.green(), displayColor.blue(), 1.0F);
            }
        }
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PoseStack stackCorrector, float partialTick) {
        FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, stackCorrector, partialTick);
    }
}