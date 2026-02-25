package net.zaharenko424.cmrs.client.api;

import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ISimpleVertexConsumer {

    void addVertex(Vector3f pos, Vector3f normal, float u, float v);
}
