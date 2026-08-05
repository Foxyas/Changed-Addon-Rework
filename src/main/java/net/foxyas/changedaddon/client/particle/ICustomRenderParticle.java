package net.foxyas.changedaddon.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import org.jetbrains.annotations.Nullable;

public interface ICustomRenderParticle {
    void renderCustom(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LightTexture lightTexture, Camera camera, float partialTicks, @Nullable Frustum frustum);
}
