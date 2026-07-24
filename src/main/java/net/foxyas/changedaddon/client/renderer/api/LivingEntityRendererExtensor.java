package net.foxyas.changedaddon.client.renderer.api;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface LivingEntityRendererExtensor<T extends LivingEntity, M extends EntityModel<T>> {

    @Nullable RenderType getOverrideRenderType();

    void setOverrideRenderType(@Nullable RenderType renderType);

//    float CA$getBob(T pLivingBase, float pPartialTick);
//
//    void CA$setupRotations(T pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks);
//
//    void CA$scale(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime);
}
