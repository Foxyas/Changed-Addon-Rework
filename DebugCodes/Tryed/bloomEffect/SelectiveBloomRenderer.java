package net.foxyas.changedaddon.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;


//Todo: finish this;

/**
 * Queues arbitrary entities during the normal render pass, then explicitly
 * re-renders just those entities into a chosen offscreen target afterward.
 * This avoids hijacking the live draw-call stream (which corrupts chunk/
 * block-entity rendering) by doing the redirect as an isolated extra pass.
 */
public class SelectiveBloomRenderer {
    private static final List<QueuedGlow> QUEUE = new ArrayList<>();

    private record QueuedGlow(Entity entity, double x, double y, double z, float yaw, float partialTick) {}

    /** Call this from your render-pre hook for anything that should glow. */
    public static void queue(Entity entity, float partialTick) {
        double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
        double y = Mth.lerp(partialTick, entity.yOld, entity.getY());
        double z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
        QUEUE.add(new QueuedGlow(entity, x, y, z, entity.getYRot(), partialTick));
    }

    public static boolean hasQueued() {
        return !QUEUE.isEmpty();
    }

    /** Re-renders every queued entity into the given target, then clears the queue. */
    public static void flushTo(RenderTarget target) {
        if (QUEUE.isEmpty() || target == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        target.bindWrite(false);

        for (QueuedGlow g : QUEUE) {
            PoseStack poseStack = new PoseStack();
            int light = LevelRenderer.getLightColor(mc.level, g.entity.blockPosition());
            dispatcher.render(
                    g.entity,
                    g.x - cam.x, g.y - cam.y, g.z - cam.z,
                    g.yaw, g.partialTick,
                    poseStack, bufferSource, light
            );
        }
        bufferSource.endBatch();

        mc.getMainRenderTarget().bindWrite(false);
        QUEUE.clear();
    }
}