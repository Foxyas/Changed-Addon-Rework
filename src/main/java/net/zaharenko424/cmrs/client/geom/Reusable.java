package net.zaharenko424.cmrs.client.geom;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Reusable {

    public static final ThreadLocal<Vector3f> VEC3F = ThreadLocal.withInitial(Vector3f::new);
    public static final ThreadLocal<Matrix3f> MAT3F = ThreadLocal.withInitial(Matrix3f::new);
    public static final ThreadLocal<Matrix4f> MAT4F = ThreadLocal.withInitial(Matrix4f::new);
    public static final ThreadLocal<Quaternionf> QUATERNION = ThreadLocal.withInitial(Quaternionf::new);
    /// reset() has to be called after usage.
    public static final ThreadLocal<SimpleVertexConsumer> SIMPLE_CONSUMER = ThreadLocal.withInitial(SimpleVertexConsumer::new);
}