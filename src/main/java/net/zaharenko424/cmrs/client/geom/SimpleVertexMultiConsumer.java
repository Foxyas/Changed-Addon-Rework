package net.zaharenko424.cmrs.client.geom;

import net.zaharenko424.cmrs.client.api.ISimpleVertexConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ParametersAreNonnullByDefault
public class SimpleVertexMultiConsumer implements ISimpleVertexConsumer, Iterable<ISimpleVertexConsumer> {

    private final List<ISimpleVertexConsumer> consumers = new ArrayList<>();

    public SimpleVertexMultiConsumer add(ISimpleVertexConsumer consumer){
        this.consumers.add(consumer);
        return this;
    }

    public boolean isEmpty(){
        return consumers.isEmpty();
    }

    public void clear(){
        consumers.clear();
    }

    @Override
    public @NotNull Iterator<ISimpleVertexConsumer> iterator() {
        return consumers.iterator();
    }

    public void addVertex(Vector3f pos, Vector3f normal, float u, float v){
        if(consumers.isEmpty()) return;
        consumers.forEach(consumer -> consumer.addVertex(pos, normal, u, v));
    }
}
