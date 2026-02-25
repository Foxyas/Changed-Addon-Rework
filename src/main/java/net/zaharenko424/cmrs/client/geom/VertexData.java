package net.zaharenko424.cmrs.client.geom;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * normal !null -> transformedNormal !null
 */
public class VertexData {

    public final Vector3f pos;
    public final Vector3f transformedPos = new Vector3f(Float.POSITIVE_INFINITY);
    public final Vector3f normal;
    public final Vector3f transformedNormal;
    protected final List<Quad> quads = new ArrayList<>();

    public VertexData(Vector3f pos, @Nullable Vector3f normal, Quad[] quads){
        this.pos = pos;
        this.normal = normal;
        this.transformedNormal = normal != null ? new Vector3f(Float.POSITIVE_INFINITY) : null;
        if(quads != null) this.quads.addAll(List.of(quads));
    }

    public VertexData(Vector3f pos, Quad[] quads) {
        this(pos, null, quads);
    }

    public void resetTransform() {
        transformedPos.set(Float.POSITIVE_INFINITY);
        if (normal != null) transformedNormal.set(Float.POSITIVE_INFINITY);
    }

    public Vector3f transformOrGet(Matrix4f pose) {
        if (transformedPos.isFinite()) return transformedPos;
        return transformedPos.set(pos.x / 16, pos.y / 16, pos.z / 16).mulPosition(pose);
    }

    public Vector3f transformOrGetNormal(Matrix3f pose) {
        if (normal == null || transformedNormal.isFinite()) return transformedNormal;
        return pose.transform(normal, transformedNormal);
    }
}
