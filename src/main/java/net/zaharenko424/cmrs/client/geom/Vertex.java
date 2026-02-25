package net.zaharenko424.cmrs.client.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record Vertex(VertexData data, float u, float v) {

    public Vertex(VertexData data) {
        this(data, 0, 0);
    }

    public Vector3f pos(){
        return data.pos;
    }

    @Contract("_, _ -> new")
    public @NotNull Vertex remap(float u, float v) {
        return new Vertex(data, u, v);
    }
}
