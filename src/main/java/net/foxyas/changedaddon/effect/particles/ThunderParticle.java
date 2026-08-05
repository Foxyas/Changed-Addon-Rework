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

        double curX = this.xo + (this.x - this.xo) * partialTicks;
        double curY = this.yo + (this.y - this.yo) * partialTicks;
        double curZ = this.zo + (this.z - this.zo) * partialTicks;

        Vec3 startWorld = new Vec3(curX, curY, curZ);
        Vec3 endWorld = startWorld.add(targetPos.subtract(originPos));

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

        Vec3 midPoint = startRel.add(endRel).scale(0.5);
        Vec3 toCam = midPoint.scale(-1.0);

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

        // Fix Z-Fighting: Create rotated diagonal cross vectors for glow quads to prevent coplanar rendering
        Vec3 axisGlowA = axisA.add(axisB).normalize();
        Vec3 axisGlowB = axisA.subtract(axisB).normalize();

        float coreRadius = sizeMultiplier * 0.05f;
        float glowRadius = sizeMultiplier * 0.15f;

        for (int i = 0; i < segments; i++) {
            Vec3 p1 = points[i];
            Vec3 p2 = points[i + 1];

            // Outer glow rendered first on offset diagonal axes (eliminates Z-fighting depth flickering)
            renderQuad(matrix, consumer, p1, p2, axisGlowA.scale(glowRadius), color.x() * 0.6f, color.y() * 0.6f, color.z() * 0.6f, 0.35f);
            renderQuad(matrix, consumer, p1, p2, axisGlowB.scale(glowRadius), color.x() * 0.6f, color.y() * 0.6f, color.z() * 0.6f, 0.35f);

            // Inner core rendered second on primary orthogonal axes
            renderQuad(matrix, consumer, p1, p2, axisA.scale(coreRadius), color.x(), color.y(), color.z(), 1.0f);
            renderQuad(matrix, consumer, p1, p2, axisB.scale(coreRadius), color.x(), color.y(), color.z(), 1.0f);
        }

        bufferSource.endBatch(RenderType.lightning());
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