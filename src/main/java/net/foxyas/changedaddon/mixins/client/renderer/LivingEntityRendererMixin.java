package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.renderer.layers.api.IDynamicRenderLayer;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {

    @Unique
    private float defaultValue;

    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Shadow
    public abstract boolean addLayer(RenderLayer<T, M> pLayer);

    @Shadow
    @Final
    protected List<RenderLayer<T, M>> layers;

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

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.BEFORE),
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void injectDelayedHeldEntityLayer(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci, @Local(name = "f5") float limbSwing, @Local(name = "f8") float limbSwingAmount, @Local(name = "pPartialTicks") float partialTicks, @Local(name = "f7") float ageInTicks, @Local(name = "f2") float netHeadYaw, @Local(name = "f6") float headPitch) {
//        if (!ClientVars.delayedHeldEntityRender) return;
//
//        LatexHeldEntityLayer<ChangedEntity, ?> layer = null;
//        for (RenderLayer<?, ?> l : layers) {
//            if (!(l instanceof LatexHeldEntityLayer<?,?> heldEntityLayer)) continue;
//
//            layer = (LatexHeldEntityLayer<ChangedEntity, ?>) heldEntityLayer;
//            break;
//        }
//
//        if (layer == null) return;
//
//        layer.render(pPoseStack, pBuffer, pPackedLight, (ChangedEntity) pEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        for (RenderLayer<T, M> layer : this.layers) {
            if (layer instanceof IDynamicRenderLayer IDynamicRenderLayer) {
                IDynamicRenderLayer.renderAfter(pPoseStack, pBuffer, pPackedLight, pEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
            }
        }
    }

    /* this eats fps... it is a cool feature but for performance’s sake I'm going to keep it disabled
    @WrapOperation(method = "getRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"))
    private ResourceLocation injectDynamicTextures(LivingEntityRenderer<T, M> instance, Entity entity, Operation<ResourceLocation> original) {
        ResourceLocation location = original.call(instance, entity);

        if (entity instanceof IAlphaAbleEntity alphaEntity && alphaEntity.isAlpha()) {
            // Multiplicamos por 100 para trabalhar com inteiros no nome do arquivo
            int scale = (int) (alphaEntity.alphaAdditionalScale() * 100);
            String baseNamespace = location.getNamespace();
            String basePath = location.getPath().replace(".png", "");

            // 1. Tenta a versão com Escala (Descendo do atual até 1)
            for (int s = scale; s > 0; s--) {
            }
            s = scale
            ResourceLocation scaleLoc = ResourceLocation.fromNamespaceAndPath(
                    baseNamespace,
                    basePath + "_alpha_" + s + ".png"
            );

            if (changed_Addon_Rework$resourceExists(scaleLoc)) {
                return scaleLoc;
            }

            // 2. Se não achou nenhuma escala, tenta a versão Alpha genérica
            ResourceLocation alphaLoc = ResourceLocation.fromNamespaceAndPath(
                    baseNamespace,
                    basePath + "_alpha.png"
            );

            if (changed_Addon_Rework$resourceExists(alphaLoc)) {
                return alphaLoc;
            }
        }

        // Se não for Alpha ou não achar arquivos extras, retorna o original (normal)
        return location;
    }*/

    @Unique
    private boolean changed_Addon_Rework$resourceExists(ResourceLocation loc) {
        // Note: in 1.20.1+, ResourceManager is Client side only
        return Minecraft.getInstance().getResourceManager().getResource(loc).isPresent();
    }
}
