package net.foxyas.changedaddon.client.renderer.layers.api;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public interface IDynamicRenderLayer<T extends Entity> {

    /**
     * This code run after all the normal layers. which in same cases are needed to be to avoid bugs
     */
    default void renderAfter(PoseStack pose, MultiBufferSource bufferSource, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }
}
