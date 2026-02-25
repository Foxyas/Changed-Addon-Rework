package net.zaharenko424.cmrs.client.geom;

import net.minecraft.core.Direction;
import net.zaharenko424.cmrs.client.api.ISimpleVertexConsumer;
import net.zaharenko424.cmrs.client.geom.builder.UVData;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Quad {

    public final Vertex[] vertices;
    public final Vector3f normal;
    public final Vector3f transformedNormal;

    public Quad(Vertex[] vertices) {
        this.vertices = vertices;

        normal = new Vector3f();
        Vector3f vec = new Vector3f();
        for (int i = 0; i < 4; i++) {
            normal.add(vertices[i].pos().cross(vertices[(i + 1) % 4].pos(), vec));
        }
        normal.normalize();
        transformedNormal = new Vector3f(Float.POSITIVE_INFINITY);
    }

    /// For cubes only.
    public Quad(Vertex[] vertices, UVData uv, float textureWidth, float textureHeight, Direction direction) {
        this.vertices = vertices;
        vertices[0] = vertices[0].remap(uv.u2() / textureWidth, uv.v1() / textureHeight);
        vertices[1] = vertices[1].remap(uv.u1() / textureWidth, uv.v1() / textureHeight);
        vertices[2] = vertices[2].remap(uv.u1() / textureWidth, uv.v2() / textureHeight);
        vertices[3] = vertices[3].remap(uv.u2() / textureWidth, uv.v2() / textureHeight);

        normal = direction.step();
        transformedNormal = new Vector3f(Float.POSITIVE_INFINITY);
    }

    public void resetTransform() {
        transformedNormal.set(Float.POSITIVE_INFINITY);
    }

    void transformAndUpdateNormal(Matrix4f pose) {
        transformedNormal.set(0);
        Vector3f tmp = Reusable.VEC3F.get();
        for (int i = 0; i < 4; i++) {
            transformedNormal.add(vertices[i].data().transformOrGet(pose).cross(vertices[(i + 1) % 4].data().transformOrGet(pose), tmp));
        }
        transformedNormal.normalize();
    }

    Vector3f transformOrGetNormal(Matrix3f pose) {
        if (transformedNormal.isFinite()) return transformedNormal;
        return transformedNormal.set(normal).mul(pose);
    }

    public void compile(Matrix4f posTransform, Matrix3f normalTransform, ISimpleVertexConsumer consumer) {
        Vector3f vector3f = transformOrGetNormal(normalTransform);
        VertexData data;
        for (Vertex vertex : vertices) {
            data = vertex.data();
            consumer.addVertex(data.transformOrGet(posTransform),
                    data.normal != null ? data.transformOrGetNormal(normalTransform) : vector3f,
                    vertex.u(), vertex.v());
        }
    }
}
