package net.foxyas.changedaddon.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.ltxprogrammer.changed.client.FormRenderHandler;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class EntityOutlineLayer<M extends AdvancedHumanoidModel<T>, T extends ChangedEntity> extends RenderLayer<T, M> implements FirstPersonLayer<T> {

    private final Function<T, ResourceLocation> textureLocationFunction;

    public EntityOutlineLayer(RenderLayerParent<T, M> parent, Function<T, ResourceLocation> textureLocationFunction) {
        super(parent);
        this.textureLocationFunction = textureLocationFunction;
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation(@NotNull T pEntity) {
        return textureLocationFunction.apply(pEntity);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, @NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.scale(1.02f, 1.02f, 1.02f);
        poseStack.translate(0, 0, 0); // Small tweak in the outline value

        float r = 1f, g = 1f, b = 1f, a = 1f;
        VertexConsumer outlineBuffer = bufferSource.getBuffer(ChangedAddonRenderTypes.emissiveReverseCull(this.getTextureLocation(entity)));
        this.getParentModel().renderToBuffer(
                poseStack,
                outlineBuffer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                r, g, b, a
        );

        poseStack.popPose();
    }

    @Override
    public void renderFirstPersonOnArms(PoseStack stack, MultiBufferSource bufferSource, int packedLight, T entity, HumanoidArm arm, PartPose armPose, float partialTick) {
        FirstPersonLayer.super.renderFirstPersonOnArms(stack, bufferSource, packedLight, entity, arm, armPose, partialTick);
        stack.pushPose();
        stack.scale(1.01F, 1.01F, 1.01F);
        VertexConsumer outlineBuffer = bufferSource.getBuffer(ChangedAddonRenderTypes.outlineWithDepth(this.getTextureLocation(entity)));
        AdvancedHumanoidModel<T> armedModel = this.getParentModel();
        ModelPart armPart = armedModel.getArm(arm);
        armPart.loadPose(armPose);
        FormRenderHandler.renderModelPartWithTexture(armedModel.getArm(arm),
                stack,
                outlineBuffer,
                packedLight,
                1,
                1,
                1,
                1.0F);

        stack.popPose();
    }
}
