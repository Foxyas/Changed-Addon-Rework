package net.foxyas.changedaddon.client.renderer.depth_outline;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Collects silhouettes without drawing anything. Drawing only happens in drawAll().
 */
@OnlyIn(Dist.CLIENT)
public final class SilhouetteSource implements MultiBufferSource {
    private final Map<RenderType, RenderType> wrapped = new HashMap<>();
    private final Map<RenderType, BufferBuilder> builders = new LinkedHashMap<>();
    private int r = 255, g = 255, b = 255;

    public void setColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public @NotNull VertexConsumer getBuffer(@NotNull RenderType type) {
        RenderType base = type.isOutline() ? type : type.outline().orElse(null);
        if (base == null) return Noop.INSTANCE;

        RenderType mine = wrapped.computeIfAbsent(base, Wrapped::new);
        BufferBuilder bb = builders.computeIfAbsent(mine, t -> new BufferBuilder(t.bufferSize()));
        if (!bb.building()) bb.begin(mine.mode(), mine.format());
        DepthOutline.markQueued();
        return new Generator(bb, r, g, b);
    }

    void drawAll() {
        for (Map.Entry<RenderType, BufferBuilder> e : builders.entrySet())
            e.getKey().end(e.getValue(), RenderSystem.getVertexSorting());
    }

    /**
     * Reuses the vanilla outline shader/texture, but writes to my target with depth test.
     */
    private static final class Wrapped extends RenderType {
        Wrapped(RenderType base) {
            super("glowdepth_" + System.identityHashCode(base),
                    base.format(), base.mode(), base.bufferSize(), base.affectsCrumbling(), false,
                    () -> {
                        base.setupRenderState();
                        VIEW_OFFSET_Z_LAYERING.setupRenderState();   // same offset armor/elytra use
                        DepthOutline.target().bindWrite(false);
                        RenderSystem.enableDepthTest();
                        RenderSystem.depthFunc(GL11.GL_LEQUAL);
//                        RenderSystem.polygonOffset(DepthOutline.biasFactor, DepthOutline.biasUnits);
//                        RenderSystem.enablePolygonOffset();
                        NO_CULL.setupRenderState();
                    },
                    () -> {
                        base.clearRenderState();
                        VIEW_OFFSET_Z_LAYERING.clearRenderState();
//                        RenderSystem.disablePolygonOffset();
                        NO_CULL.clearRenderState();
                    });
        }
    }


    public static final class Tee implements MultiBufferSource {
        private final MultiBufferSource real;
        private final SilhouetteSource sil;

        public Tee(MultiBufferSource real, SilhouetteSource sil) {
            this.real = real;
            this.sil = sil;
        }

        @Override
        public @NotNull VertexConsumer getBuffer(@NotNull RenderType type) {
            VertexConsumer r = real.getBuffer(type);
            VertexConsumer s = sil.getBuffer(type);   // Noop if the type has no outline
            return VertexMultiConsumer.create(s, r);
        }
    }

    private static final class Generator implements VertexConsumer {
        private final VertexConsumer out;
        private final int r, g, b;
        private double x, y, z;
        private float u, v;

        Generator(VertexConsumer out, int r, int g, int b) {
            this.out = out;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public @NotNull VertexConsumer vertex(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        public @NotNull VertexConsumer color(int r, int g, int b, int a) {
            return this;
        }

        public @NotNull VertexConsumer uv(float u, float v) {
            this.u = u;
            this.v = v;
            return this;
        }

        public @NotNull VertexConsumer overlayCoords(int u, int v) {
            return this;
        }

        public @NotNull VertexConsumer uv2(int u, int v) {
            return this;
        }

        public @NotNull VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        public void endVertex() {
            out.vertex(x, y, z).color(r, g, b, 255).uv(u, v).endVertex();
        }

        public void defaultColor(int r, int g, int b, int a) {
        }

        public void unsetDefaultColor() {
        }
    }

    private enum Noop implements VertexConsumer {
        INSTANCE;

        public @NotNull VertexConsumer vertex(double x, double y, double z) {
            return this;
        }

        public @NotNull VertexConsumer color(int r, int g, int b, int a) {
            return this;
        }

        public @NotNull VertexConsumer uv(float u, float v) {
            return this;
        }

        public @NotNull VertexConsumer overlayCoords(int u, int v) {
            return this;
        }

        public @NotNull VertexConsumer uv2(int u, int v) {
            return this;
        }

        public @NotNull VertexConsumer normal(float x, float y, float z) {
            return this;
        }

        public void endVertex() {
        }

        public void defaultColor(int r, int g, int b, int a) {
        }

        public void unsetDefaultColor() {
        }
    }
}