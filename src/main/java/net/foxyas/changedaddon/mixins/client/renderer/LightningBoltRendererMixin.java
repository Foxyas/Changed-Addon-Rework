package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.entity.api.IScalableLightingBolt;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LightningBoltRenderer;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightningBoltRenderer.class)
public abstract class LightningBoltRendererMixin extends EntityRenderer<LightningBolt> {

    protected LightningBoltRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @WrapOperation(method = "render(Lnet/minecraft/world/entity/LightningBolt;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;last()Lcom/mojang/blaze3d/vertex/PoseStack$Pose;"))
    private PoseStack.Pose scaleSize(PoseStack poseStack, Operation<PoseStack.Pose> original, @Local(argsOnly = true) LightningBolt lightningBolt) {
        if (lightningBolt instanceof IScalableLightingBolt scalableLightingBolt) {
            poseStack.pushPose();
            poseStack.scale(scalableLightingBolt.getScale(), scalableLightingBolt.getScale(), scalableLightingBolt.getScale());
            PoseStack.Pose call = original.call(poseStack);
            poseStack.popPose();
            return call;
        }
        return original.call(poseStack);
    }
}
