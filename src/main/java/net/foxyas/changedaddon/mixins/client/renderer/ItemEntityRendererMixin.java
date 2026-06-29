package net.foxyas.changedaddon.mixins.client.renderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.item.api.ICustomGlowingOutline;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.awt.*;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
    protected ItemEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }


    @WrapOperation(method = "render(Lnet/minecraft/world/entity/item/ItemEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"))
    private void renderHook(ItemRenderer instance, ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel, Operation<Void> original, ItemEntity itemEntity) {
        if (pBuffer instanceof OutlineBufferSource outlineBufferSource && pItemStack.getItem() instanceof ICustomGlowingOutline outlineItem) {
            if (itemEntity.isCurrentlyGlowing()) {
                Color entityItemOutlineColor = outlineItem.getEntityItemOutlineColor();
                outlineBufferSource.setColor(entityItemOutlineColor.getRed(), entityItemOutlineColor.getGreen(), entityItemOutlineColor.getBlue(), entityItemOutlineColor.getAlpha());
            }
        }
        original.call(instance, pItemStack, pDisplayContext, pLeftHand, pPoseStack, pBuffer, pCombinedLight, pCombinedOverlay, pModel);
    }
}
