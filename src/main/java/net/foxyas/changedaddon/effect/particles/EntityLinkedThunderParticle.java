package net.foxyas.changedaddon.effect.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class EntityLinkedThunderParticle extends Particle {

    private static final int TOTAL_SEGMENTS = 8;

    private final Vec3 targetPos;
    private final float speed;
    private final boolean rooted;
    private final boolean staticBody;
    private final Vector3f shake;
    private final Vector3f color;
    private final float sizeMultiplier;
    private final long seed;

    private final int bodyShakeFrequency;
    private final Entity target;

    private float segmentsProgress;
    private float retractProgress;

    public EntityLinkedThunderParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, EntityLinkedThunderParticleOptions options) {
        super(level, x, y, z);
        this.target = level.getEntity(options.getTargetId());
        this.speed = options.getSpeed();
        this.rooted = options.isRooted();
        this.shake = options.getShake();
        this.color = options.getColor();
        this.sizeMultiplier = options.getSize();
        this.seed = level.random.nextLong();
        this.segmentsProgress = 0;
        this.retractProgress = 0;
        this.bodyShakeFrequency = options.getBodyShakeFrequency();
        this.staticBody = options.isStaticBody();

        targetPos = target != null && options.shouldUseTargetPosAsBaseForDeltas() ? target.position().add(new Vec3(xSpeed, ySpeed, zSpeed)) : new Vec3(xSpeed, ySpeed, zSpeed);
        this.lifetime = Math.max(1, options.getLifeTime());

        updateBoundingBox();
    }

    private void updateBoundingBox() {
        double margin = Math.max(shake.x(), Math.max(shake.y(), shake.z())) + sizeMultiplier * 0.5f + 1.0;
        AABB box = new AABB(getPos(), targetPos).inflate(margin);
        this.setBoundingBox(box);
    }

    @Override
    public void tick() {
        if (target != null) this.setPos(target.getX(), target.getY(), target.getZ());
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float halfLife = this.lifetime / 2.0f;

        if (!rooted) {
            // Phase 1: Growth (Age from 0 to halfLife)
            this.segmentsProgress = Math.min(1.0f, (this.age / halfLife) * this.speed);

            // Phase 2: Retraction (Age from halfLife to lifetime)
            if (segmentsProgress >= 1) {
                float retractAge = this.age - halfLife; // Resets age counter to 0 for the second half
                this.retractProgress = Mth.clamp((retractAge / halfLife) * this.speed, 0, 1);
            } else {
                this.retractProgress = 0.0f;
            }
        } else {
            this.segmentsProgress = Mth.clamp((this.age / (float) this.lifetime) * this.speed, 0, 1);
            this.retractProgress = 0.0f;
        }
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera camera, float partialTicks) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        renderLightingBolt(bufferSource, camera, partialTicks);
    }

    public void renderLightingBolt(MultiBufferSource.BufferSource bufferSource, Camera camera, float partialTicks) {
        if (!this.isAlive()) return;

        Vec3 camPos = camera.getPosition();

        double curX = this.xo + (this.x - this.xo) * partialTicks;
        double curY = this.yo + (this.y - this.yo) * partialTicks;
        double curZ = this.zo + (this.z - this.zo) * partialTicks;

        Vec3 startWorld = new Vec3(curX, curY, curZ);
        Vec3 fullEndWorld = startWorld.add(targetPos.subtract(getPos()));

        Vec3 startRel = startWorld.subtract(camPos);
        Vec3 fullEndRel = fullEndWorld.subtract(camPos);

        PoseStack poseStack = new PoseStack();
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer consumer = bufferSource.getBuffer(ChangedAddonRenderTypes.lightningNoShort());

        RandomSource random = RandomSource.create(this.seed);

        if (segmentsProgress >= 1 && !staticBody) {
            int safeFreq = Math.max(1, this.bodyShakeFrequency);
            long flashSeed = this.seed + (this.age / safeFreq);
            random = RandomSource.create(flashSeed);
        }

        Vec3[] fullPoints = new Vec3[TOTAL_SEGMENTS + 1];
        fullPoints[0] = startRel;
        fullPoints[TOTAL_SEGMENTS] = fullEndRel;

        for (int i = 1; i < TOTAL_SEGMENTS; i++) {
            double subProgress = (double) i / TOTAL_SEGMENTS;
            Vec3 interp = startRel.lerp(fullEndRel, subProgress);

            double offX = (random.nextDouble() - 0.5D) * shake.x();
            double offY = (random.nextDouble() - 0.5D) * shake.y();
            double offZ = (random.nextDouble() - 0.5D) * shake.z();

            fullPoints[i] = interp.add(offX, offY, offZ);
        }

        // Determine segment bounds based on growth and retraction
        float exactEndSegment = TOTAL_SEGMENTS * segmentsProgress;
        float exactStartSegment = (!rooted && segmentsProgress >= 1.0f) ? (TOTAL_SEGMENTS * retractProgress) : 0.0f;

        int startSegIndex = Math.max(0, Math.min(TOTAL_SEGMENTS, (int) Math.floor(exactStartSegment)));
        int endSegIndex = Math.max(1, Math.min(TOTAL_SEGMENTS, (int) Math.ceil(exactEndSegment)));

        if (startSegIndex >= endSegIndex || exactStartSegment >= exactEndSegment) {
            return; // Fully collapsed/retracted
        }

        int activeSegmentCount = endSegIndex - startSegIndex;
        Vec3[] points = new Vec3[activeSegmentCount + 1];

        // Populate stable base positions
        System.arraycopy(fullPoints, startSegIndex, points, 0, activeSegmentCount + 1);

        // Retracting tail tip interpolation (shrinks away from root)
        float startFraction = exactStartSegment - startSegIndex;
        if (startFraction > 0.0f && startSegIndex < TOTAL_SEGMENTS) {
            Vec3 pStart = fullPoints[startSegIndex];
            Vec3 pNext = fullPoints[startSegIndex + 1];
            points[0] = pStart.lerp(pNext, startFraction);
        }

        // Advancing front tip interpolation (grows toward target)
        float endFraction = exactEndSegment - (endSegIndex - 1);
        if (endFraction < 1.0f && (endSegIndex - 1) < TOTAL_SEGMENTS) {
            Vec3 pPrev = fullPoints[endSegIndex - 1];
            Vec3 pEnd = fullPoints[endSegIndex];
            points[activeSegmentCount] = pPrev.lerp(pEnd, endFraction);
        }

        int segments = activeSegmentCount;

        // --- FIX ROTATION ISSUE ---
        // Compute primary orientation axis from the FULL bolt span rather than dynamic endpoints
        Vec3 fullBoltDir = fullEndRel.subtract(startRel);
        if (fullBoltDir.lengthSqr() < 1E-6) {
            return;
        }
        fullBoltDir = fullBoltDir.normalize();

        Vec3 axisA = fullBoltDir.cross(new Vec3(0, 1, 0));
        if (axisA.lengthSqr() < 1E-6) {
            axisA = fullBoltDir.cross(new Vec3(1, 0, 0));
        }
        axisA = axisA.normalize();
        Vec3 axisB = fullBoltDir.cross(axisA).normalize();

        float centerRadius = sizeMultiplier * 0.03f;
        float firstOutline = sizeMultiplier * 0.08f;
        float secondOutline = sizeMultiplier * 0.13f;
        float thirdOutline = sizeMultiplier * 0.18f;

        float[] layers = {centerRadius, firstOutline, secondOutline, thirdOutline};
        float[] alphas = {0.3f, 0.3f, 0.3f, 0.3f};

        for (int l = 0; l < layers.length; l++) {
            float radius = layers[l];
            float alpha = alphas[l];

            Vec3 aScale = axisA.scale(radius);
            Vec3 bScale = axisB.scale(radius);

            int layer = (layers.length - 1) - l;
            float factor = 1.0f - (layer * 0.025f);

            // --- FIX INNER TUBE ISSUE ---
            // Uniform forward loop preserves correct winding order and layer scaling during retraction
            for (int i = 0; i < segments; i++) {
                boolean isFirst = (i == 0);
                boolean isLast = (i == segments - 1);

                Vec3 p1 = points[i];
                Vec3 p2 = points[i + 1];

                Vec3 segVec = p2.subtract(p1);
                Vec3 segCenter = p1.add(p2).scale(0.5);
                Vec3 segHalfSpan = segVec.scale(0.5);

                if (isFirst) {
                    p1 = segCenter.subtract(segHalfSpan.scale(factor));
                }
                if (isLast) {
                    p2 = segCenter.add(segHalfSpan.scale(factor));
                }

                renderTubeSegment(matrix, consumer, p1, p2, aScale, bScale, color.x(), color.y(), color.z(), alpha, isFirst, isLast);
            }
        }

        bufferSource.endBatch();
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

    private static void renderTubeSegmentWithBackFace(
            Matrix4f matrix, VertexConsumer consumer,
            Vec3 start, Vec3 end,
            Vec3 axisA, Vec3 axisB,
            float r, float g, float b, float alpha,
            boolean renderStartCap, boolean renderEndCap) {

        Vec3 c0 = axisA.add(axisB);
        Vec3 c1 = axisA.reverse().add(axisB);
        Vec3 c2 = axisA.reverse().add(axisB.reverse());
        Vec3 c3 = axisA.add(axisB.reverse());

        // --- SIDES (Front & Back Faces) ---
        // Side 1
        drawQuad(matrix, consumer, start.add(c0), start.add(c1), end.add(c1), end.add(c0), r, g, b, alpha);
        drawQuad(matrix, consumer, end.add(c0), end.add(c1), start.add(c1), start.add(c0), r, g, b, alpha);

        // Side 2
        drawQuad(matrix, consumer, start.add(c1), start.add(c2), end.add(c2), end.add(c1), r, g, b, alpha);
        drawQuad(matrix, consumer, end.add(c1), end.add(c2), start.add(c2), start.add(c1), r, g, b, alpha);

        // Side 3
        drawQuad(matrix, consumer, start.add(c2), start.add(c3), end.add(c3), end.add(c2), r, g, b, alpha);
        drawQuad(matrix, consumer, end.add(c2), end.add(c3), start.add(c3), start.add(c2), r, g, b, alpha);

        // Side 4
        drawQuad(matrix, consumer, start.add(c3), start.add(c0), end.add(c0), end.add(c3), r, g, b, alpha);
        drawQuad(matrix, consumer, end.add(c3), end.add(c0), start.add(c0), start.add(c3), r, g, b, alpha);

        // --- END CAPS (Front & Back Faces) ---
        if (renderStartCap) {
            // Front face (facing outward from start)
            drawQuad(matrix, consumer, start.add(c3), start.add(c2), start.add(c1), start.add(c0), r, g, b, alpha);
            // Back face (facing inward)
            drawQuad(matrix, consumer, start.add(c0), start.add(c1), start.add(c2), start.add(c3), r, g, b, alpha);
        }

        if (renderEndCap) {
            // Front face (facing outward from end)
            drawQuad(matrix, consumer, end.add(c0), end.add(c1), end.add(c2), end.add(c3), r, g, b, alpha);
            // Back face (facing inward)
            drawQuad(matrix, consumer, end.add(c3), end.add(c2), end.add(c1), end.add(c0), r, g, b, alpha);
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

        // Back face (Double-sided rendering if Cull state is active)
//        consumer.vertex(matrix, (float) p3.x, (float) p3.y, (float) p3.z).color(r, g, b, alpha).endVertex();
//        consumer.vertex(matrix, (float) p2.x, (float) p2.y, (float) p2.z).color(r, g, b, alpha).endVertex();
//        consumer.vertex(matrix, (float) p1.x, (float) p1.y, (float) p1.z).color(r, g, b, alpha).endVertex();
//        consumer.vertex(matrix, (float) p0.x, (float) p0.y, (float) p0.z).color(r, g, b, alpha).endVertex();
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

    public static class Provider implements ParticleProvider<EntityLinkedThunderParticleOptions> {

        @Nullable
        @Override
        public Particle createParticle(
                @NotNull EntityLinkedThunderParticleOptions options,
                @NotNull ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed
        ) {
            Entity entity = level.getEntity(options.getTargetId());
            if (entity != null) {
                return new EntityLinkedThunderParticle(level, entity.getX(), entity.getY(), entity.getZ(), xSpeed, ySpeed, zSpeed, options);
            }
            return new EntityLinkedThunderParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, options);
        }
    }
}