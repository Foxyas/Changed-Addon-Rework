package net.foxyas.changedaddon.effect.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ThunderParticle extends Particle {
    private final Vec3 targetPos;
    private final Vec3 originPos;
    private final float speed;
    private final boolean rooted;
    private final Vector3f shake;
    private final Vector3f color;
    private final float sizeMultiplier;
    private final long seed;

    public ThunderParticle(ClientLevel level, double x, double y, double z, ThunderParticleOptions options) {
        super(level, x, y, z);
        this.originPos = options.getStartPos();
        this.targetPos = options.getEndPos();
        this.speed = options.getSpeed();
        this.rooted = options.isRooted();
        this.shake = options.getShake();
        this.color = options.getColor();
        this.sizeMultiplier = options.getSize();
        this.seed = level.random.nextLong();

        double distance = originPos.distanceTo(targetPos);
        this.lifetime = Math.max(1, (int) (distance / Math.max(0.01f, speed)));

        this.x = originPos.x;
        this.y = originPos.y;
        this.z = originPos.z;

        // Fix Culling: Expand bounding box to encompass the entire bolt span plus shake offsets
        updateBoundingBox();
    }

    private void updateBoundingBox() {
        double margin = Math.max(shake.x(), Math.max(shake.y(), shake.z())) + sizeMultiplier * 0.5f + 1.0;
        AABB box = new AABB(originPos, targetPos).inflate(margin);
        this.setBoundingBox(box);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        if (!rooted) {
            float progress = (float) this.age / (float) this.lifetime;
            Vec3 delta = targetPos.subtract(originPos).scale(progress);
            Vec3 currentRoot = originPos.add(delta);
            this.x = currentRoot.x;
            this.y = currentRoot.y;
            this.z = currentRoot.z;

            // Recalculate bounding box if particle root moves
            Vec3 currentEnd = currentRoot.add(targetPos.subtract(originPos));
            double margin = Math.max(shake.x(), Math.max(shake.y(), shake.z())) + sizeMultiplier * 0.5f + 1.0;
            this.setBoundingBox(new AABB(currentRoot, currentEnd).inflate(margin));
        }
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera camera, float partialTicks) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        renderLightingBolt(bufferSource, camera, partialTicks);
    }

    public void renderLightingBolt(MultiBufferSource.BufferSource bufferSource, Camera camera, float partialTicks) {
        Vec3 camPos = camera.getPosition();

        // 1. Calculate absolute world positions
        double curX = this.xo + (this.x - this.xo) * partialTicks;
        double curY = this.yo + (this.y - this.yo) * partialTicks;
        double curZ = this.zo + (this.z - this.zo) * partialTicks;

        Vec3 startWorld = new Vec3(curX, curY, curZ);
        Vec3 endWorld = startWorld.add(targetPos.subtract(originPos));

        // 2. Subtract camera position to get local camera space coordinates
        Vec3 startRel = startWorld.subtract(camPos);
        Vec3 endRel = endWorld.subtract(camPos);

        PoseStack poseStack = new PoseStack();
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lightning());

        RandomSource random = RandomSource.create(this.seed);
        int segments = 8;
        Vec3[] points = new Vec3[segments + 1];
        points[0] = startRel;
        points[segments] = endRel;

        // 3. Jagged path offsets computed in camera space
        for (int i = 1; i < segments; i++) {
            double subProgress = (double) i / segments;
            Vec3 interp = startRel.lerp(endRel, subProgress);

            double offX = (random.nextDouble() - 0.5D) * shake.x();
            double offY = (random.nextDouble() - 0.5D) * shake.y();
            double offZ = (random.nextDouble() - 0.5D) * shake.z();

            points[i] = interp.add(offX, offY, offZ);
        }

        Vec3 boltDir = endRel.subtract(startRel);
        if (boltDir.lengthSqr() < 1E-6) {
            return;
        }
        boltDir = boltDir.normalize();

        // 4. Direction to camera in camera-relative space (where camera is at 0,0,0)
        Vec3 midPoint = startRel.add(endRel).scale(0.5);
        Vec3 toCam = midPoint.scale(-1.0); // Vector pointing back to local camera origin (0,0,0)

        if (toCam.lengthSqr() < 1E-6) {
            toCam = new Vec3(0, 0, 1);
        } else {
            toCam = toCam.normalize();
        }

        Vec3 axisA = boltDir.cross(toCam);
        if (axisA.lengthSqr() < 1E-6) {
            axisA = boltDir.cross(new Vec3(0, 1, 0));
            if (axisA.lengthSqr() < 1E-6) {
                axisA = new Vec3(1, 0, 0);
            }
        }
        axisA = axisA.normalize();

        Vec3 axisB = boltDir.cross(axisA).normalize();

        float centerRadius = sizeMultiplier * 0.03f;
        float firstOutline = sizeMultiplier * 0.08f;
        float secondOutline = sizeMultiplier * 0.13f;
        float thirdOutline = sizeMultiplier * 0.18f;

        // 5. Draw segments
        for (int i = 0; i < segments; i++) {
            Vec3 p1 = points[i];
            Vec3 p2 = points[i + 1];
            // Inner Bright Core Tube (rendered second inside the glow)
            // Old: renderTubeSegment(matrix, consumer, p1, p2, axisA.scale(centerRadius), axisB.scale(centerRadius), color.x(), color.y(), color.z(), 1.0f);
            // Outer Glow Tube (rendered first)
            // Old: renderTubeSegment(matrix, consumer, p1, p2, axisA.scale(firstOutline), axisB.scale(firstOutline), color.x() * 0.5f, color.y() * 0.5f, color.z() * 0.5f, 0.3f);

            renderTubeSegment(matrix, consumer, p1, p2, axisA.scale(centerRadius), axisB.scale(centerRadius), color.x(), color.y(), color.z(), 1.0f);
            renderTubeSegment(matrix, consumer, p1, p2, axisA.scale(firstOutline), axisB.scale(firstOutline), color.x(), color.y(), color.z(), 0.3f);
            renderTubeSegment(matrix, consumer, p1, p2, axisA.scale(secondOutline), axisB.scale(secondOutline), color.x(), color.y(), color.z(), 0.3f);
            renderTubeSegment(matrix, consumer, p1, p2, axisA.scale(thirdOutline), axisB.scale(thirdOutline), color.x(), color.y(), color.z(), 0.3f);
        }

        // 6. CRITICAL: Flush the lightning batch buffer so it actually renders on screen
        bufferSource.endBatch(RenderType.lightning());
    }

    // Replaces renderQuad: draws a 4-sided 3D tube section between start and end
    private static void renderTubeSegment(
            Matrix4f matrix, VertexConsumer consumer,
            Vec3 start, Vec3 end,
            Vec3 axisA, Vec3 axisB,
            float r, float g, float b, float alpha) {

        // Compute 4 corner offsets surrounding the central segment line
        Vec3 c0 = axisA.add(axisB);
        Vec3 c1 = axisA.reverse().add(axisB);
        Vec3 c2 = axisA.reverse().add(axisB.reverse());
        Vec3 c3 = axisA.add(axisB.reverse());

        // Side 1
        drawQuad(matrix, consumer, start.add(c0), start.add(c1), end.add(c1), end.add(c0), r, g, b, alpha);
        // Side 2
        drawQuad(matrix, consumer, start.add(c1), start.add(c2), end.add(c2), end.add(c1), r, g, b, alpha);
        // Side 3
        drawQuad(matrix, consumer, start.add(c2), start.add(c3), end.add(c3), end.add(c2), r, g, b, alpha);
        // Side 4
        drawQuad(matrix, consumer, start.add(c3), start.add(c0), end.add(c0), end.add(c3), r, g, b, alpha);
    }

    private static void drawQuad(Matrix4f matrix, VertexConsumer consumer, Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, float r, float g, float b, float alpha) {
        consumer.vertex(matrix, (float) p0.x, (float) p0.y, (float) p0.z).color(r, g, b, alpha).endVertex();
        consumer.vertex(matrix, (float) p1.x, (float) p1.y, (float) p1.z).color(r, g, b, alpha).endVertex();
        consumer.vertex(matrix, (float) p2.x, (float) p2.y, (float) p2.z).color(r, g, b, alpha).endVertex();
        consumer.vertex(matrix, (float) p3.x, (float) p3.y, (float) p3.z).color(r, g, b, alpha).endVertex();
    }

    private static void renderQuad(Matrix4f matrix, VertexConsumer consumer, Vec3 start, Vec3 end, Vec3 normal, float r, float g, float b, float alpha) {
        consumer.vertex(matrix, (float) (start.x - normal.x), (float) (start.y - normal.y), (float) (start.z - normal.z)).color(r, g, b, alpha).endVertex();
        consumer.vertex(matrix, (float) (start.x + normal.x), (float) (start.y + normal.y), (float) (start.z + normal.z)).color(r, g, b, alpha).endVertex();
        consumer.vertex(matrix, (float) (end.x + normal.x), (float) (end.y + normal.y), (float) (end.z + normal.z)).color(r, g, b, alpha).endVertex();
        consumer.vertex(matrix, (float) (end.x - normal.x), (float) (end.y - normal.y), (float) (end.z - normal.z)).color(r, g, b, alpha).endVertex();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Provider implements ParticleProvider<ThunderParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(
                @NotNull ThunderParticleOptions options,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            return new ThunderParticle(level, x, y, z, options);
        }
    }
}