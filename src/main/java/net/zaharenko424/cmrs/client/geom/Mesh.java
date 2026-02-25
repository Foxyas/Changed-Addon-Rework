package net.zaharenko424.cmrs.client.geom;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.zaharenko424.cmrs.client.api.ISimpleVertexConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Mesh {

    protected final ImmutableList<VertexData> vertexData;
    protected final Quad[] quads;
    protected final Vector3f offset = new Vector3f();

    public Mesh(ImmutableList<VertexData> vertexData, Quad[] quads) {
        this.vertexData = vertexData;
        this.quads = quads;
    }

    public Mesh(float[] vertices, float[] quads, float textureWidth, float textureHeight) {
        ImmutableList.Builder<VertexData> builder = ImmutableList.builder();
        for (int vrt = 0; vrt < vertices.length / 3; vrt++) {
            builder.add(createData(readVec(vertices, vrt * 3)));
        }
        vertexData = builder.build();

        int size = quads.length / 12;
        this.quads = new Quad[size];

        int q = 0, i;
        Vertex[] ar;
        Quad quad;
        while (q < size) {
            ar = new Vertex[4];
            for (i = 0; i < 4; i++) {
                ar[i] = vertex(quads, q * 12 + i * 3, textureWidth, textureHeight);
            }

            quad = new Quad(ar);
            this.quads[q++] = quad;

            for(Vertex vert : ar){
                vert.data().quads.add(quad);
            }
        }
    }

    protected @NotNull VertexData createData(Vector3f pos) {
        return new VertexData(pos, null);
    }

    protected Vector3f readVec(float[] array, int start) {
        return new Vector3f(array[start], array[start + 1], array[start + 2]);
    }

    private @NotNull Vertex vertex(float[] quads, int vertexIndex, float textureWidth, float textureHeight) {
        int vertexDataIndex = (int) quads[vertexIndex];//index of vector in vertices[]

        VertexData data = vertexData.get(vertexDataIndex);
        return new Vertex(data, quads[vertexIndex + 1] / textureWidth, quads[vertexIndex + 2] / textureHeight);
    }

    public void resetTransform() {
        for (VertexData data : vertexData) data.resetTransform();
        for (Quad quad : quads) quad.resetTransform();
        offset.set(0);
    }

    public void compile(PoseStack.Pose matrix, ISimpleVertexConsumer consumer) {
        Matrix4f poseM = matrix.pose();
        Matrix3f normal = matrix.normal();
        for (Quad quad : this.quads) {
            if (quad.transformedNormal.x == Float.NEGATIVE_INFINITY) quad.transformAndUpdateNormal(poseM);
            quad.compile(poseM, normal, consumer);
        }
    }
}
