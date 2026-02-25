package net.zaharenko424.cmrs.client.gui;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.geom.*;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class GuiMeshGenerator {

    public static void genArcMesh(ImmutableList.Builder<VertexData> builder, List<Quad> quads, float radius, float thickness, float radSize, float radQuadStep){
        genArcMesh(builder, quads, radius, thickness, 0, radSize, radQuadStep);
    }

    public static void genArcMesh(ImmutableList.Builder<VertexData> builder, List<Quad> quads, float radius, float thickness, float radOffset, float radSize, float radQuadStep){
        genArcMesh(builder, quads, Reusable.VEC3F.get().set(0), radius, thickness, radOffset, radSize, radQuadStep);
    }

    public static void genArcMesh(ImmutableList.Builder<VertexData> builder, List<Quad> quads, Vector3f offset, float radius, float thickness, float radOffset, float radSize, float radQuadStep){
        float x, y, x1, y1;
        Vector3f vec;
        boolean started = false, finish = false;
        Vertex[] arr = null;
        VertexData last1, last2;
        float radMax = radSize + radOffset;
        for(float rad = radOffset;; rad += radQuadStep){
            if(rad > radMax) {
                rad = radMax;
                finish = true;
            }

            x = Mth.cos(rad) * radius;
            y = (float) (Math.sin(rad) * radius);
            vec = new Vector3f(x * 16, y * 16, 0);
            offset.mulAdd(16, vec, vec);
            last1 = new VertexData(vec, new Quad[0]);
            builder.add(last1);

            x1 = Mth.cos(rad) * (radius + thickness);
            y1 = (float) (Math.sin(rad) * (radius + thickness));
            vec = new Vector3f(x1 * 16, y1 * 16, 0);
            offset.mulAdd(16, vec, vec);
            last2 = new VertexData(vec, new Quad[0]);
            builder.add(last2);

            if(!started){
                arr = new Vertex[4];
                arr[0] = new Vertex(last2);
                arr[1] = new Vertex(last1);
                started = true;
            } else {
                arr[2] = new Vertex(last1);
                arr[3] = new Vertex(last2);
                quads.add(new Quad(arr));
                if(finish) break;

                arr = new Vertex[4];
                arr[0] = new Vertex(last2);
                arr[1] = new Vertex(last1);
            }
        }
    }

    public static void genRay(ImmutableList.Builder<VertexData> builder, List<Quad> quads, float rayRad, float offset, float length, float outlineThickness, boolean flip){
        VertexData[] arr = new VertexData[4];

        float x, y, o = flip ? -1 : 1;

        x = Mth.cos(rayRad) * (length - outlineThickness);//Outer left
        y = Mth.sin(rayRad) * (length - outlineThickness);
        arr[0] = new VertexData(new Vector3f(x * 16, y * 16, 0), new Quad[0]);

        x = Mth.cos(rayRad + Mth.HALF_PI)* o * outlineThickness + x;//Outer right
        y = Mth.sin(rayRad + Mth.HALF_PI)* o * outlineThickness + y;
        arr[1] = new VertexData(new Vector3f(x * 16, y * 16, 0), new Quad[0]);

        x = Mth.cos(rayRad) * (offset + outlineThickness * .9f);//Inner left
        y = Mth.sin(rayRad) * (offset + outlineThickness * .9f);
        arr[3] = new VertexData(new Vector3f(x * 16, y * 16, 0), new Quad[0]);

        x = Mth.cos(rayRad + Mth.HALF_PI) * o * outlineThickness  * .9f + x;//Inner right
        y = Mth.sin(rayRad + Mth.HALF_PI) * o * outlineThickness  * .9f + y;
        arr[2] = new VertexData(new Vector3f(x * 16, y * 16, 0), new Quad[0]);

        builder.add(arr);
        if(!flip) {
            quads.add(new Quad(new Vertex[]{new Vertex(arr[3]), new Vertex(arr[2]), new Vertex(arr[1]), new Vertex(arr[0])}));
        } else quads.add(new Quad(new Vertex[]{new Vertex(arr[0]), new Vertex(arr[1]), new Vertex(arr[2]), new Vertex(arr[3])}));
    }

    public static void genQuad(ImmutableList.Builder<VertexData> builder, List<Quad> quads, Vector3f vert0, Vector3f vert1, Vector3f vert2, Vector3f vert3){
        VertexData[] arr = new VertexData[4];
        arr[0] = new VertexData(vert0.mul(16), new Quad[0]);
        arr[1] = new VertexData(vert1.mul(16), new Quad[0]);
        arr[2] = new VertexData(vert2.mul(16), new Quad[0]);
        arr[3] = new VertexData(vert3.mul(16), new Quad[0]);

        builder.add(arr);
        quads.add(new Quad(new Vertex[]{new Vertex(arr[3]), new Vertex(arr[2]),new Vertex(arr[1]),new Vertex(arr[0])}));
    }
}
