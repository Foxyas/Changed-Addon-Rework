package net.foxyas.changedaddon.mixins.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(value = LivingEntityRenderer.class)
public interface LivingEntityRendererAccessor {

    @Accessor("layers")
    <T extends LivingEntity, M extends EntityModel<T>> List<RenderLayer<T, M>> getLayers();

    @Invoker("getBob")
    <T extends LivingEntity> float callGetBob(T pLivingBase, float pPartialTick);

    @Invoker("setupRotations")
    <T extends LivingEntity> void callSetupRotations(T pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks);

    @Invoker("scale")
    <T extends LivingEntity> void callScale(T pLivingEntity, PoseStack pPoseStack, float pPartialTickTime);
}
