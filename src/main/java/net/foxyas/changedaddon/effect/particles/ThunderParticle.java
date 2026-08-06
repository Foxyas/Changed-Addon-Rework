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
    private final float speed;
    private final boolean rooted;
    private final Vector3f shake;
    private final Vector3f color;
    private final float sizeMultiplier;
    private final long seed;
    private float segmentsProgress;

    public ThunderParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ThunderParticleOptions options) {
        super(level, x, y, z);
        this.speed = options.getSpeed();
        this.rooted = options.isRooted();
        this.shake = options.getShake();
        this.color = options.getColor();
        this.sizeMultiplier = options.getSize();
        this.seed = level.random.nextLong();
        this.segmentsProgress = 0;

        targetPos = new Vec3(xSpeed, ySpeed, zSpeed);

//        double distance = getPos().distanceTo(targetPos);
//        (int) (distance / Math.max(0.01f, speed))
        this.lifetime = Math.max(1, options.getLifeTime());

        // Fix Culling: Expand bounding box to encompass the entire bolt span plus shake offsets
        updateBoundingBox();
    }

    private void updateBoundingBox() {
        double margin = Math.max(shake.x(), Math.max(shake.y(), shake.z())) + sizeMultiplier * 0.5f + 1.0;
        AABB box = new AABB(getPos(), targetPos).inflate(margin);
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

        this.segmentsProgress = Math.min(1.0f, ((float) this.age / (float) this.lifetime) * this.speed);

        if (!rooted) {
            float progress = (float) this.age / (float) this.lifetime;
            Vec3 delta = targetPos.subtract(getPos()).scale(progress);
            Vec3 currentRoot = getPos().add(delta);
            this.x = currentRoot.x;
            this.y = currentRoot.y;
            this.z = currentRoot.z;

            // Recalculate bounding box if particle root moves
            Vec3 currentEnd = currentRoot.add(targetPos.subtract(getPos()));
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
        Vec3 endWorld = startWorld.add(targetPos.subtract(getPos()).scale(segmentsProgress));

        Vec3 startRel = startWorld.subtract(camPos);
        Vec3 endRel = endWorld.subtract(camPos);

        PoseStack poseStack = new PoseStack();
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lightning());

        RandomSource random = RandomSource.create(this.seed);
        int segments = Math.max(1, (int) (8 * segmentsProgress));
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

        // Fixed world-aligned cross-section to eliminate camera-facing vertex twisting
        Vec3 axisA = boltDir.cross(new Vec3(0, 1, 0));
        if (axisA.lengthSqr() < 1E-6) {
            axisA = boltDir.cross(new Vec3(1, 0, 0));
        }
        axisA = axisA.normalize();
        Vec3 axisB = boltDir.cross(axisA).normalize();

        float centerRadius = sizeMultiplier * 0.03f;
        float firstOutline = sizeMultiplier * 0.08f;
        float secondOutline = sizeMultiplier * 0.13f;
        float thirdOutline = sizeMultiplier * 0.18f;

        float[] layers = {centerRadius, firstOutline, secondOutline, thirdOutline};
        float[] alphas = {0.3f, 0.3f, 0.3f, 0.3f};

        // Calculate the central axis vector of the bolt
        Vec3 mainCenter = startRel.add(endRel).scale(0.5);
        Vec3 halfSpan = endRel.subtract(startRel).scale(0.5);

        for (int l = 0; l < layers.length; l++) {
            float radius = layers[l];
            float alpha = alphas[l];

            Vec3 aScale = axisA.scale(radius);
            Vec3 bScale = axisB.scale(radius);

            int layer = (layers.length - 1) - l;
            float factor = 1.0f - (layer * 0.005f);

            // Correctly shorten the endpoints along the bolt line relative to mainCenter
            Vec3 layerStart = mainCenter.subtract(halfSpan.scale(factor));
            Vec3 layerEnd = mainCenter.add(halfSpan.scale(factor));

            for (int i = 0; i < segments; i++) {
                boolean isFirst = (i == 0);
                boolean isLast = (i == segments - 1);

                Vec3 p1;
                Vec3 p2;
                if (segments == 1) {
                    // Quando só tem 1 segmento, use layerStart e layerEnd diretamente sem p2 sobrescrever p1
                    p1 = layerStart;
                    p2 = layerEnd;
                } else  {
                    p1 = isFirst ? layerStart : points[i];
                    p2 = isLast ? layerEnd : points[i + 1];
                }


                renderTubeSegment(matrix, consumer, p1, p2, aScale, bScale, color.x(), color.y(), color.z(), alpha, isFirst, isLast);
            }
        }

        bufferSource.endBatch(RenderType.lightning());
    }

    private static void renderTubeSegment(
            Matrix4f matrix, VertexConsumer consumer,
            Vec3 start, Vec3 end,
            Vec3 axisA, Vec3 axisB,
            float r, float g, float b, float alpha,
            boolean renderStartCap, boolean renderEndCap) {

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

        // Optional End Caps
        if (renderStartCap) {
            drawQuad(matrix, consumer, start.add(c3), start.add(c2), start.add(c1), start.add(c0), r, g, b, alpha);
        }
        if (renderEndCap) {
            drawQuad(matrix, consumer, end.add(c0), end.add(c1), end.add(c2), end.add(c3), r, g, b, alpha);
        }
    }

    private static void renderTaperedSegment(
            Matrix4f matrix, VertexConsumer consumer,
            Vec3 start, Vec3 end,
            Vec3 axisAStart, Vec3 axisBStart,
            Vec3 axisAEnd, Vec3 axisBEnd,
            float r, float g, float b, float alpha) {

        Vec3 s0 = axisAStart.add(axisBStart);
        Vec3 s1 = axisAStart.reverse().add(axisBStart);
        Vec3 s2 = axisAStart.reverse().add(axisBStart.reverse());
        Vec3 s3 = axisAStart.add(axisBStart.reverse());

        Vec3 e0 = axisAEnd.add(axisBEnd);
        Vec3 e1 = axisAEnd.reverse().add(axisBEnd);
        Vec3 e2 = axisAEnd.reverse().add(axisBEnd.reverse());
        Vec3 e3 = axisAEnd.add(axisBEnd.reverse());

        // Side 1
        drawQuad(matrix, consumer, start.add(s0), start.add(s1), end.add(e1), end.add(e0), r, g, b, alpha);
        // Side 2
        drawQuad(matrix, consumer, start.add(s1), start.add(s2), end.add(e2), end.add(e1), r, g, b, alpha);
        // Side 3
        drawQuad(matrix, consumer, start.add(s2), start.add(s3), end.add(e3), end.add(e2), r, g, b, alpha);
        // Side 4
        drawQuad(matrix, consumer, start.add(s3), start.add(s0), end.add(e0), end.add(e3), r, g, b, alpha);
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
            return new ThunderParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }
}