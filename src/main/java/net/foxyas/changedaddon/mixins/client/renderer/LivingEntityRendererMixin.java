package net.foxyas.changedaddon.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    @Unique
    private float defaultValue;

    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Shadow
    public abstract boolean addLayer(RenderLayer<T, M> pLayer);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addExtraLayers(EntityRendererProvider.Context pContext, M pModel, float pShadowRadius, CallbackInfo ci) {
        LivingEntityRenderer<T, M> self = (LivingEntityRenderer<T, M>) (Object) this;
        this.addLayer(new SonarOutlineLayer<>(self));
        this.defaultValue = pShadowRadius;
    }

    @Inject(method = "scale", at = @At("RETURN"))
    private void applyAlphaScaleCompatibility(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime, CallbackInfo ci) {
        if (ChangedAddonClientConfiguration.ALPHA_COMPATIBILITY_MODE_RENDER.get()) return;
        if (pLivingEntity instanceof IAlphaAbleEntity alphaAbleEntity) {
            if (alphaAbleEntity.isAlpha()) {
                pPoseStack.scale(alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender());
                this.shadowRadius = defaultValue * alphaAbleEntity.alphaScaleForRender();
            } else this.shadowRadius = defaultValue;
        }

    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", shift = At.Shift.AFTER))
    private void applyAlphaScaleRenderHook(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        if (!ChangedAddonClientConfiguration.ALPHA_COMPATIBILITY_MODE_RENDER.get()) return;
        if (pEntity instanceof IAlphaAbleEntity alphaAbleEntity) {
            if (alphaAbleEntity.isAlpha()) {
                pPoseStack.scale(alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender(), alphaAbleEntity.alphaScaleForRender());
                this.shadowRadius = defaultValue * alphaAbleEntity.alphaScaleForRender();
            } else this.shadowRadius = defaultValue;
        }

    }
}
