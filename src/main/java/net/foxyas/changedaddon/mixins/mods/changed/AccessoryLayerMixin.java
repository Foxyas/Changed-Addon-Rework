package net.foxyas.changedaddon.mixins.mods.changed;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.entity.advanced.AbstractDazedEntity;
import net.ltxprogrammer.changed.client.renderer.layers.AccessoryLayer;
import net.ltxprogrammer.changed.client.renderer.layers.FirstPersonLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = AccessoryLayer.class)
public abstract class AccessoryLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> implements FirstPersonLayer<T> {

    public AccessoryLayerMixin(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @WrapMethod(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", remap = false)
    private void stopRendering(PoseStack poseStack, MultiBufferSource buffers, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Operation<Void> original) {
        if (entity instanceof AbstractDazedEntity abstractDazedEntity && abstractDazedEntity.isMorphed()) {
            return;
        }
        original.call(poseStack, buffers, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }

}
