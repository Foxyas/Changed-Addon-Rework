package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.entity.api.IColorableLightingBolt;
import net.foxyas.changedaddon.entity.api.IScalableLightingBolt;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.world.entity.LightningBolt;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

@Mixin(LightningBoltRenderer.class)
public abstract class LightningBoltRendererMixin extends EntityRenderer<LightningBolt> {

    @Unique
    private static final Vector3f DEFAULT_SCALE = new Vector3f(1f, 1f, 1f);

    protected LightningBoltRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;last()Lcom/mojang/blaze3d/vertex/PoseStack$Pose;"))
    private PoseStack.Pose scaleSize(PoseStack poseStack, Operation<PoseStack.Pose> original, @Local(argsOnly = true) LightningBolt lightningBolt) {
        if (lightningBolt instanceof IScalableLightingBolt scalableLightingBolt) {
            poseStack.pushPose();
            Vector3f renderScale = scalableLightingBolt.getRenderScale();
            if (renderScale.equals(DEFAULT_SCALE)) {
                poseStack.scale(scalableLightingBolt.getScale(), scalableLightingBolt.getScale(), scalableLightingBolt.getScale());
            } else {
                poseStack.scale(renderScale.x, renderScale.y, renderScale.z);
            }
            PoseStack.Pose call = original.call(poseStack);
            poseStack.popPose();
            return call;
        }
        return original.call(poseStack);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LightningBoltRenderer;quad(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFIFFFFFFFZZZZ)V",
                    ordinal = 0)
    )
    private void changeThunderQuadColor$0(Matrix4f pMatrix, VertexConsumer pConsumer, float pX1, float pZ1, int pIndex, float pX2, float pZ2, float pRed, float pGreen, float pBlue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_, Operation<Void> original, @Local(argsOnly = true) LightningBolt lightningBolt) {
        if (lightningBolt instanceof IColorableLightingBolt iColorableLightingBolt) {
            Color thunderColor = iColorableLightingBolt.getThunderColor();
            if (thunderColor != IColorableLightingBolt.DEFAULT_COLOR) {
                pBlue = thunderColor.getBlue() / 255f;
                pRed = thunderColor.getRed() / 255f;
                pGreen = thunderColor.getGreen() / 255f;
            }
        }

        original.call(pMatrix, pConsumer, pX1, pZ1, pIndex, pX2, pZ2, pRed, pGreen, pBlue, p_115283_, p_115284_, p_115285_, p_115286_, p_115287_, p_115288_);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LightningBoltRenderer;quad(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFIFFFFFFFZZZZ)V",
                    ordinal = 1)
    )
    private void changeThunderQuadColor$1(Matrix4f pMatrix, VertexConsumer pConsumer, float pX1, float pZ1, int pIndex, float pX2, float pZ2, float pRed, float pGreen, float pBlue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_, Operation<Void> original, @Local(argsOnly = true) LightningBolt lightningBolt) {
        if (lightningBolt instanceof IColorableLightingBolt iColorableLightingBolt) {
            Color thunderColor = iColorableLightingBolt.getThunderColor();
            if (thunderColor != IColorableLightingBolt.DEFAULT_COLOR) {
                pBlue = thunderColor.getBlue() / 255f;
                pRed = thunderColor.getRed() / 255f;
                pGreen = thunderColor.getGreen() / 255f;
            }
        }

        original.call(pMatrix, pConsumer, pX1, pZ1, pIndex, pX2, pZ2, pRed, pGreen, pBlue, p_115283_, p_115284_, p_115285_, p_115286_, p_115287_, p_115288_);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LightningBoltRenderer;quad(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFIFFFFFFFZZZZ)V",
                    ordinal = 2)
    )
    private void changeThunderQuadColor$2(Matrix4f pMatrix, VertexConsumer pConsumer, float pX1, float pZ1, int pIndex, float pX2, float pZ2, float pRed, float pGreen, float pBlue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_, Operation<Void> original, @Local(argsOnly = true) LightningBolt lightningBolt) {
        if (lightningBolt instanceof IColorableLightingBolt iColorableLightingBolt) {
            Color thunderColor = iColorableLightingBolt.getThunderColor();
            if (thunderColor != IColorableLightingBolt.DEFAULT_COLOR) {
                pBlue = thunderColor.getBlue() / 255f;
                pRed = thunderColor.getRed() / 255f;
                pGreen = thunderColor.getGreen() / 255f;
            }
        }

        original.call(pMatrix, pConsumer, pX1, pZ1, pIndex, pX2, pZ2, pRed, pGreen, pBlue, p_115283_, p_115284_, p_115285_, p_115286_, p_115287_, p_115288_);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LightningBoltRenderer;quad(Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFIFFFFFFFZZZZ)V",
                    ordinal = 3)
    )
    private void changeThunderQuadColor$3(Matrix4f pMatrix, VertexConsumer pConsumer, float pX1, float pZ1, int pIndex, float pX2, float pZ2, float pRed, float pGreen, float pBlue, float p_115283_, float p_115284_, boolean p_115285_, boolean p_115286_, boolean p_115287_, boolean p_115288_, Operation<Void> original, @Local(argsOnly = true) LightningBolt lightningBolt) {
        if (lightningBolt instanceof IColorableLightingBolt iColorableLightingBolt) {
            Color thunderColor = iColorableLightingBolt.getThunderColor();
            if (thunderColor == IColorableLightingBolt.DEFAULT_COLOR) {
                original.call(pMatrix, pConsumer, pX1, pZ1, pIndex, pX2, pZ2, pRed, pGreen, pBlue, p_115283_, p_115284_, p_115285_, p_115286_, p_115287_, p_115288_);
                return;
            } else {
                pBlue = thunderColor.getBlue() / 255f;
                pRed = thunderColor.getRed() / 255f;
                pGreen = thunderColor.getGreen() / 255f;
            }
        }
        original.call(pMatrix, pConsumer, pX1, pZ1, pIndex, pX2, pZ2, pRed, pGreen, pBlue, p_115283_, p_115284_, p_115285_, p_115286_, p_115287_, p_115288_);
    }
}
