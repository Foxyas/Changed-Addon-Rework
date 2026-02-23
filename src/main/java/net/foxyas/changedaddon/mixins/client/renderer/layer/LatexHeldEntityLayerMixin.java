package net.foxyas.changedaddon.mixins.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.ltxprogrammer.changed.client.renderer.layers.LatexHeldEntityLayer;
import net.ltxprogrammer.changed.client.renderer.model.AdvancedHumanoidModel;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LatexHeldEntityLayer.class, remap = false)
public abstract class LatexHeldEntityLayerMixin<T extends ChangedEntity, M extends AdvancedHumanoidModel<T>> extends RenderLayer<T, M> {
    public LatexHeldEntityLayerMixin(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/ltxprogrammer/changed/entity/ChangedEntity;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    shift = At.Shift.AFTER
            ), remap = true
    )
    private void scaleAfterPush(
            PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci
    ) {
        if (entity instanceof IAlphaAbleEntity alpha && alpha.isAlpha()) {
            float reduction = (1 / alpha.alphaScaleForRender());
            pose.scale(reduction, reduction, reduction);
            pose.translate(0, 0.35, 0);
        }
    }
}
