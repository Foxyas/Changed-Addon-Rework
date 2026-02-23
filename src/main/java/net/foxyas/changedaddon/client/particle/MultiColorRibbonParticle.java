package net.foxyas.changedaddon.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.awt.*;

public class MultiColorRibbonParticle extends RibbonParticle {
    protected final Color[] colors;

    public MultiColorRibbonParticle(ClientLevel pLevel, Entity target, Color[] colors, int segments, float length, float scaleY, float rotationRad) {
        super(pLevel, target, 0 /* coloque 0 pois override render color */, segments, length, scaleY, rotationRad);
        this.colors = colors;
    }

    // Interpola color baseado na posição "t" de 0 até 1
    private float[] getColorForSegment(float t) {
        if (colors.length == 0) return new float[]{1, 1, 1, 1};
        if (colors.length == 1) return colors[0].getRGBComponents(new float[4]);

        // qual par de cores usar?
        float scaled = t * (colors.length - 1);
        int idx = Math.min((int) scaled, colors.length - 2);
        float frac = scaled - idx;

        float[] c0 = colors[idx].getRGBComponents(new float[4]);
        float[] c1 = colors[idx + 1].getRGBComponents(new float[4]);
        return new float[]{
                c0[0] + (c1[0] - c0[0]) * frac,
                c0[1] + (c1[1] - c0[1]) * frac,
                c0[2] + (c1[2] - c0[2]) * frac,
                c0[3] + (c1[3] - c0[3]) * frac
        };
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        Pair<Vector3f, Vector3f> segment, prevSegment;
        Vec3 camPos = pRenderInfo.getPosition();
        int length = segments.length;

        for (int i = 0; i < length; i++) {
            float t = (float) i / (length - 1);
            float[] col = getColorForSegment(t);
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
                pBuffer.vertex(lerpSegment.x - segmentUp.x, lerpSegment.y - segmentUp.y, lerpSegment.z - segmentUp.z)
                        .color(col[0], col[1], col[2], col[3])
                        .endVertex();
                pBuffer.vertex(lerpSegment.x + segmentUp.x, lerpSegment.y + segmentUp.y, lerpSegment.z + segmentUp.z)
                        .color(col[0], col[1], col[2], col[3])
                        .endVertex();
                if (i + 1 == length) break;//last
            }

            //start next quad
            pBuffer.vertex(lerpSegment.x + segmentUp.x, lerpSegment.y + segmentUp.y, lerpSegment.z + segmentUp.z)
                    .color(col[0], col[1], col[2], col[3])
                    .endVertex();
            pBuffer.vertex(lerpSegment.x - segmentUp.x, lerpSegment.y - segmentUp.y, lerpSegment.z - segmentUp.z)
                    .color(col[0], col[1], col[2], col[3])
                    .endVertex();
        }
    }

    /* ========================= PROVIDER ========================= */

    public static class Provider implements ParticleProvider<MultiColorRibbonParticleOption> {

        @Override
        public @Nullable Particle createParticle(
                @NotNull MultiColorRibbonParticleOption options,
                @NotNull ClientLevel level,
                double x, double y, double z,
                double xs, double ys, double zs
        ) {
            Color[] colorObjs = new Color[options.colors().length];
            for (int i = 0; i < colorObjs.length; i++) {
                colorObjs[i] = new Color(options.colors()[i], true);
            }

            MultiColorRibbonParticle multiColorRibbonParticle = new MultiColorRibbonParticle(
                    level,
                    options.target(),
                    colorObjs,
                    options.segments(),
                    options.length(),
                    options.sizeY(),
                    options.rotationRad()
            );

            multiColorRibbonParticle.offset = new Vec3(xs, ys, zs);
            return multiColorRibbonParticle;
        }
    }
}
