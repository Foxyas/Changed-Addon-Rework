package net.zaharenko424.cmrs.client.geom;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.zaharenko424.cmrs.client.api.ISimpleVertexConsumer;
import net.zaharenko424.cmrs.util.Pool;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SimpleVertexConsumer implements ISimpleVertexConsumer, Pool.Poolable {

    protected VertexConsumer consumer;
    protected int overlay = OverlayTexture.NO_OVERLAY;
    protected int light = LightTexture.FULL_BRIGHT;
    protected int color = -1;

    public VertexConsumer consumer(){
        return consumer;
    }

    public SimpleVertexConsumer wrap(VertexConsumer consumer){
        this.consumer = consumer;
        return this;
    }

    public SimpleVertexConsumer wrap(VertexConsumer consumer, int overlay, int light, int color){
        this.consumer = consumer;
        this.overlay = overlay;
        this.light = light;
        this.color = color;
        return this;
    }

    public SimpleVertexConsumer overlay(int overlay){
        this.overlay = overlay;
        return this;
    }

    public SimpleVertexConsumer light(int light){
        this.light = light;
        return this;
    }

    public SimpleVertexConsumer color(int color){
        this.color = color;
        return this;
    }

    public void reset(){
        consumer = null;
        overlay = OverlayTexture.NO_OVERLAY;
        light = LightTexture.FULL_BRIGHT;
        color = -1;
    }

    public void addVertex(Vector3f pos, Vector3f normal, float u, float v){
        if(consumer == null) throw new IllegalStateException("SimpleVertexConsumer not set up!");
        consumer.vertex(pos.x, pos.y, pos.z)
                .color(color)
                .uv(u, v)
                .overlayCoords(overlay).uv2(light)
                .normal(normal.x, normal.y, normal.z)
                .endVertex();
    }
}
