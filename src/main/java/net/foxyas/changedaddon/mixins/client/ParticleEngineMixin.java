package net.foxyas.changedaddon.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.client.particle.ICustomRenderParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Queue;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Shadow
    @Final
    private Map<ParticleRenderType, Queue<Particle>> particles;

    // Helper method to draw the lightning geometry and flush the buffer
    private void ChangedAddon$renderCustomParticles(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LightTexture lightTexture, Camera camera, float partialTicks, @Nullable Frustum frustum) {
        Queue<Particle> customParticles = this.particles.get(ParticleRenderType.CUSTOM);
        if (customParticles == null || customParticles.isEmpty()) {
            return;
        }

        if (customParticles.stream().noneMatch(particle -> particle instanceof ICustomRenderParticle)) {
            return;
        }

        boolean renderedAny = false;
        for (Particle particle : customParticles) {
            if (particle instanceof ICustomRenderParticle thunderParticle) {
                thunderParticle.renderCustom(poseStack, bufferSource, lightTexture, camera, partialTicks, frustum);
                renderedAny = true;
            }
        }

        if (renderedAny) {
            bufferSource.endBatch();
        }
    }

    // 1. Target the 6-parameter render method (with Frustum)
    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V")
    )
    private void onRenderWithFrustum(
            PoseStack poseStack,
            MultiBufferSource.BufferSource bufferSource,
            LightTexture lightTexture,
            Camera camera,
            float partialTicks,
            @Nullable Frustum frustum,
            CallbackInfo ci
    ) {
        ChangedAddon$renderCustomParticles(poseStack, bufferSource, lightTexture, camera, partialTicks, frustum);
    }
}