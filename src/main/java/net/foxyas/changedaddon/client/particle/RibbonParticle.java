package net.foxyas.changedaddon.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class RibbonParticle extends Particle {

    public static final ParticleRenderType POS_COLOR_TRANSLUCENT = new ParticleRenderType() {

        @Override
        public void begin(@NotNull BufferBuilder pBuilder, @NotNull TextureManager pTextureManager) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            RenderSystem.disableCull();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            pBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            //GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);//Wireframe
        }

        @Override
        public void end(@NotNull Tesselator pTesselator) {
            pTesselator.end();
            RenderSystem.enableCull();

            //GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
        }
    };
    protected static final Vector3fc UP = new Vector3f(0, 1, 0);
    protected static final Vector3f segmentUp = new Vector3f();
    protected static final Vector3f tmp = new Vector3f(), tmp1 = new Vector3f();
    protected static final Vector3f lerpSegment = new Vector3f(), lerpPrev = new Vector3f();
    protected static final Matrix4f mat = new Matrix4f();
    protected final Pair<Vector3f, Vector3f>[] segments;//first prev tick, second current for lerping
    public Vec3 offset = Vec3.ZERO;
    protected Entity target;
    protected int color;
    protected float length;
    protected float segmentLength;
    protected float segmentLengthSqr;
    protected float scaleY;
    protected float rotationRad;
    protected RibbonParticle(ClientLevel pLevel, Entity target, int color, int segments, float length, float scaleY, float rotationRad) {
        super(pLevel, target.getX(), target.getY(), target.getZ());
        this.target = target;
        this.color = color;
        assert segments > 1;
        this.segments = new Pair[segments];

        for (int i = 0; i < segments; i++) {
            this.segments[i] = Pair.of(new Vector3f((float) x, (float) y, (float) z), new Vector3f((float) x, (float) y, (float) z));
        }

        this.length = length;
        if (isLengthBased()) {
            segmentLength = length / segments;
            segmentLengthSqr = segmentLength * segmentLength;
        } else segmentLength = segmentLengthSqr = 0;

        this.scaleY = scaleY;
        this.rotationRad = rotationRad;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    public boolean isLengthBased() {
        return length > 0;
    }

    public boolean hasRotation() {
        return rotationRad != 0;
    }

    @Override
    public void tick() {
        if (target.isRemoved()) {
            remove();
            return;
        }

        Vec3 targetPos = target.position();
        if (target instanceof LivingEntity livingEntity) {
            Vec3 offsetRotated = offset.yRot((float) Math.toRadians(-livingEntity.yBodyRotO));
            setPos(targetPos.x + offsetRotated.x, targetPos.y + target.getBbHeight() / 2 + offsetRotated.y, targetPos.z + offsetRotated.z);
        } else
            setPos(targetPos.x + offset.x, targetPos.y + target.getBbHeight() / 2 + offset.y, targetPos.z + offset.z);

        Pair<Vector3f, Vector3f> curr;
        for (int i = 0; i < segments.length; i++) {
            curr = segments[i];
            curr.first().set(curr.second());

            if (i == 0) {
                curr.second().set(x, y, z);
            } else {
                if (isLengthBased()) {
                    if (tmp.isFinite()) curr.second().set(tmp);
                } else curr.second().set(segments[i - 1].first());
            }

            if (isLengthBased()) {
                tmp.set(Float.POSITIVE_INFINITY);
                if (i + 1 == segments.length || segments[i + 1].second().distanceSquared(curr.second()) <= segmentLengthSqr)
                    continue;

                curr.second().sub(curr.second().sub(segments[i + 1].second(), tmp).normalize(segmentLength), tmp);
            }
        }
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        Pair<Vector3f, Vector3f> segment, prevSegment;
        Vec3 camPos = pRenderInfo.getPosition();
        int length = segments.length;

        for (int i = 0; i < length; i++) {
            segment = segments[i];
            segment.first().lerp(segment.second(), pPartialTicks, lerpSegment);

            if (i == 0) {
                lerpPrev.set(segment.second());
            } else {
                prevSegment = segments[i - 1];
                prevSegment.first().lerp(segment.second(), pPartialTicks, lerpPrev);
            }

            if (!lerpSegment.equals(lerpPrev)) {//equal when not moving so need replacement segmentUP
                lerpPrev.sub(lerpSegment, tmp);
                tmp.cross(UP, tmp1);
                tmp.cross(tmp1, segmentUp);
            } else segmentUp.set(UP).mul(-1);

            segmentUp.normalize().mul(scaleY * outQuadratic(1 - (float) (i + 1) / length));

            if (hasRotation()) {
                if (lerpSegment.equals(lerpPrev)) lerpPrev.sub(segments[1].first(), tmp);
                mat.identity().rotate(rotationRad, tmp.normalize());
                segmentUp.mulPosition(mat);
            }

            lerpSegment.sub((float) camPos.x, (float) camPos.y, (float) camPos.z);

            //write 2 verts
            if (i != 0) {//finish prev
                pBuffer.vertex(lerpSegment.x - segmentUp.x, lerpSegment.y - segmentUp.y, lerpSegment.z - segmentUp.z).color(color).endVertex();
                pBuffer.vertex(lerpSegment.x + segmentUp.x, lerpSegment.y + segmentUp.y, lerpSegment.z + segmentUp.z).color(color).endVertex();
                if (i + 1 == length) break;//last
            }

            //start next quad
            pBuffer.vertex(lerpSegment.x + segmentUp.x, lerpSegment.y + segmentUp.y, lerpSegment.z + segmentUp.z).color(color).endVertex();
            pBuffer.vertex(lerpSegment.x - segmentUp.x, lerpSegment.y - segmentUp.y, lerpSegment.z - segmentUp.z).color(color).endVertex();
        }
    }

    protected float outQuadratic(float t) {
        return -t * (t - 2);
    }

    protected float outCubic(float t) {
        return outX(t, 3);
    }

    protected float outX(float t, float pow) {
        return (float) (1 - Math.pow(1 - t, pow));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return POS_COLOR_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<RibbonParticleOption> {

        @Override
        public @Nullable Particle createParticle(@NotNull RibbonParticleOption ribbonParticleOption, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            RibbonParticle ribbonParticle = new RibbonParticle(pLevel, ribbonParticleOption.target(), ribbonParticleOption.color(), ribbonParticleOption.segments(), ribbonParticleOption.length(), ribbonParticleOption.sizeY(), ribbonParticleOption.rotationRad());
            ribbonParticle.offset = new Vec3(pXSpeed, pYSpeed, pZSpeed);
            return ribbonParticle;
        }
    }
}
