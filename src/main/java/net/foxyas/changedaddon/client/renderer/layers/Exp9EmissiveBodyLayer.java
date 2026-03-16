package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.bosses.Experiment009Entity;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

public class Exp9EmissiveBodyLayer<M extends AdvancedHumanoidModel<T>, T extends Experiment009Entity> extends EyesLayer<T, M> implements FirstPersonLayer<T> {
    private final RenderType renderType;
    private final RenderType renderType2;

    public Exp9EmissiveBodyLayer(RenderLayerParent<T, M> layerParent, ResourceLocation emissiveTexture) {
        super(layerParent);
        this.renderType = RenderType.eyes(emissiveTexture);
        this.renderType2 = RenderType.entityCutoutNoCull(emissiveTexture);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.shouldShowGlow()) {

//            ModList modList = ModList.get();
//            if (modList.isLoaded("oculus") || modList.isLoaded("embeddium")) {
//                VertexConsumer vertexconsumer = bufferSource.getBuffer(this.renderType2());
//                M parentModel = this.getParentModel();
//                parentModel.renderToBuffer(poseStack, vertexconsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//            } check FormRenderHandleMixin.java

            super.render(poseStack, bufferSource, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }


    public @NotNull RenderType renderType() {
        return this.renderType;
    }

    public @NotNull RenderType renderType2() {
        return this.renderType2;
    }

    @Override
    public void renderFirstPersonOnFace(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, Camera camera) {
        FirstPersonLayer.super.renderFirstPersonOnFace(stack, bufferSource, packedLight, entity, camera);
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
        FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose, partialTick);
        if (entity.shouldShowGlow()) {
            stack.pushPose();
            stack.scale(1.0002F, 1.0002F, 1.0002F);
            M model = this.getParentModel();
            FormRenderHandler.renderModelPartWithTexture(model.getArm(arm), stack, bufferSource.getBuffer(this.renderType()), LightTexture.FULL_BRIGHT, 1.0F);
            stack.popPose();
        }
    }
}