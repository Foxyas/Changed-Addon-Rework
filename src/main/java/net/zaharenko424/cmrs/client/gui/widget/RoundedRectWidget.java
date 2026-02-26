package net.zaharenko424.cmrs.client.gui.widget;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import net.zaharenko424.cmrs.client.geom.*;
import net.zaharenko424.cmrs.client.gui.GuiMeshGenerator;
import net.zaharenko424.cmrs.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

public class RoundedRectWidget extends Widget implements SizedWidget {

    protected Mesh outline;
    protected Mesh inside;
    protected float width = 100, height = 20;
    protected float roundingRadius = 5, outlineThickness = 2;

    public static final int defOutlineColor = Color.BLACK.getRGB();
    public static final int defInsideColor = Utils.color(196, Color.GRAY.getRGB());
    protected ToIntFunction<RoundedRectWidget> outlineColor;
    protected ToIntFunction<RoundedRectWidget> insideColor;

    /**
     * No need to rebuild mesh after this call. <p> Origin is in the middle of the button.
     */
    public RoundedRectWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedRectWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedRectWidget setSize(float width, float height){
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        return this;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedRectWidget setRoundingRadius(float roundingRadius){
        this.roundingRadius = Math.max(0, roundingRadius);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedRectWidget setOutlineThickness(float outlineThickness){
        this.outlineThickness = Math.max(0, outlineThickness);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedRectWidget setOutlineColorFunc(@Nullable ToIntFunction<RoundedRectWidget> outlineColor){
        this.outlineColor = outlineColor;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedRectWidget setInsideColorFunc(@Nullable ToIntFunction<RoundedRectWidget> insideColor){
        this.insideColor = insideColor;
        return this;
    }

    public void setSizeAndUpdate(float width, float height){
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.rebuildMesh();
    }

    public void rebuildMesh(){
        ImmutableList.Builder<VertexData> builder = new ImmutableList.Builder<>();
        List<Quad> quads = new ArrayList<>();

        float innerRadius = roundingRadius - outlineThickness;
        float doubleRadius = roundingRadius * 2;
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float actualHalfWidth = (width - doubleRadius) / 2;
        float actualHalfHeight = (height - doubleRadius) / 2;
        Vector3f offset = Reusable.VEC3F.get();
        boolean noWidth = width <= doubleRadius;
        boolean noHeight = height <= doubleRadius;

        if(outlineThickness > 0) {
            if(noWidth && noHeight){
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(0), innerRadius, outlineThickness, 0, Mth.TWO_PI, Mth.DEG_TO_RAD * 12);
            } else if(noWidth) {
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(0, actualHalfHeight, 0), innerRadius, outlineThickness, 0, Mth.PI, Mth.DEG_TO_RAD * 12);
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(0, -actualHalfHeight, 0), innerRadius, outlineThickness, Mth.PI, Mth.PI, Mth.DEG_TO_RAD * 12);
            } else if (noHeight) {
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(-actualHalfWidth, 0, 0), innerRadius, outlineThickness, Mth.HALF_PI, Mth.PI, Mth.DEG_TO_RAD * 12);
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(actualHalfWidth, 0, 0), innerRadius, outlineThickness, Mth.PI + Mth.HALF_PI, Mth.PI, Mth.DEG_TO_RAD * 12);
            } else {
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(actualHalfWidth, actualHalfHeight, 0), innerRadius, outlineThickness, 0, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(-actualHalfWidth, -actualHalfHeight, 0), innerRadius, outlineThickness, Mth.PI, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(-actualHalfWidth, actualHalfHeight, 0), innerRadius, outlineThickness, Mth.HALF_PI, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
                GuiMeshGenerator.genArcMesh(builder, quads, offset.set(actualHalfWidth, -actualHalfHeight, 0), innerRadius, outlineThickness, Mth.PI + Mth.HALF_PI, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
            }

            if(!noWidth){
                GuiMeshGenerator.genQuad(builder, quads,// -- T
                        new Vector3f(-actualHalfWidth, -halfHeight, 0), new Vector3f(actualHalfWidth, -halfHeight, 0),
                        new Vector3f(actualHalfWidth, -halfHeight + outlineThickness, 0), new Vector3f(-actualHalfWidth, -halfHeight + outlineThickness, 0));
                GuiMeshGenerator.genQuad(builder, quads,// -- B
                        new Vector3f(-actualHalfWidth, halfHeight - outlineThickness, 0), new Vector3f(actualHalfWidth, halfHeight - outlineThickness, 0),
                        new Vector3f(actualHalfWidth, halfHeight, 0), new Vector3f(-actualHalfWidth, halfHeight, 0));
            }

            if(!noHeight){
                GuiMeshGenerator.genQuad(builder, quads,// | L
                        new Vector3f(-halfWidth, -actualHalfHeight, 0), new Vector3f(-halfWidth + outlineThickness, -actualHalfHeight, 0),
                        new Vector3f(-halfWidth + outlineThickness, actualHalfHeight, 0), new Vector3f(-halfWidth, actualHalfHeight, 0));
                GuiMeshGenerator.genQuad(builder, quads,// | R
                        new Vector3f(halfWidth - outlineThickness, -actualHalfHeight, 0), new Vector3f(halfWidth, -actualHalfHeight, 0),
                        new Vector3f(halfWidth, actualHalfHeight, 0), new Vector3f(halfWidth - outlineThickness, actualHalfHeight, 0));
            }

            outline = new Mesh(builder.build(), quads.toArray(new Quad[0]));

            builder = new ImmutableList.Builder<>();
            quads = new ArrayList<>();
        }

        if(noWidth && noHeight){
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(0), 0, innerRadius, 0, Mth.TWO_PI, Mth.DEG_TO_RAD * 12);
        } else if(noWidth){
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(0, actualHalfHeight, 0), 0, innerRadius, 0, Mth.PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(0, -actualHalfHeight, 0), 0, innerRadius, Mth.PI, Mth.PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genQuad(builder, quads,
                    new Vector3f(-halfWidth + outlineThickness, -actualHalfHeight, 0), new Vector3f(halfWidth - outlineThickness, -actualHalfHeight, 0),
                    new Vector3f(halfWidth - outlineThickness, actualHalfHeight, 0), new Vector3f(-halfWidth + outlineThickness, actualHalfHeight, 0));
        } else if(noHeight){
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(-actualHalfWidth, 0, 0), 0, innerRadius, Mth.HALF_PI, Mth.PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(actualHalfWidth, 0, 0), 0, innerRadius, Mth.PI + Mth.HALF_PI, Mth.PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genQuad(builder, quads,
                    new Vector3f(-actualHalfWidth, -halfHeight + outlineThickness, 0), new Vector3f(actualHalfWidth, -halfHeight + outlineThickness, 0),
                    new Vector3f(actualHalfWidth, halfHeight - outlineThickness, 0), new Vector3f(-actualHalfWidth, halfHeight - outlineThickness, 0));
        } else {
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(actualHalfWidth, actualHalfHeight, 0), 0, innerRadius, 0, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(-actualHalfWidth, -actualHalfHeight, 0), 0, innerRadius, Mth.PI, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(-actualHalfWidth, actualHalfHeight, 0), 0, innerRadius, Mth.HALF_PI, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);
            GuiMeshGenerator.genArcMesh(builder, quads, offset.set(actualHalfWidth, -actualHalfHeight, 0), 0, innerRadius, Mth.PI + Mth.HALF_PI, Mth.HALF_PI, Mth.DEG_TO_RAD * 12);

            GuiMeshGenerator.genQuad(builder, quads,
                    new Vector3f(-actualHalfWidth, -halfHeight + outlineThickness, 0), new Vector3f(actualHalfWidth, -halfHeight + outlineThickness, 0),
                    new Vector3f(actualHalfWidth, -actualHalfHeight, 0), new Vector3f(-actualHalfWidth, -actualHalfHeight, 0));
            GuiMeshGenerator.genQuad(builder, quads,
                    new Vector3f(-actualHalfWidth, actualHalfHeight, 0), new Vector3f(actualHalfWidth, actualHalfHeight, 0),
                    new Vector3f(actualHalfWidth, halfHeight - outlineThickness, 0), new Vector3f(-actualHalfWidth, halfHeight - outlineThickness, 0));
            GuiMeshGenerator.genQuad(builder, quads,
                    new Vector3f(-halfWidth + outlineThickness, -actualHalfHeight, 0), new Vector3f(halfWidth - outlineThickness, -actualHalfHeight, 0),
                    new Vector3f(halfWidth - outlineThickness, actualHalfHeight, 0), new Vector3f(-halfWidth + outlineThickness, actualHalfHeight, 0));
        }

        inside = new Mesh(builder.build(), quads.toArray(new Quad[0]));
    }

    public boolean shouldRender(){
        return isVisible() && (outline != null || inside != null);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!shouldRender()) return;

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        render(stack, guiGraphics, mouseX, mouseY, partialTick);
        MatrixStack.pop(stack);
    }

    protected void render(@NotNull PoseStack stack, @NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick){
        if(inside == null && outline == null) return;
        PoseStack.Pose matrix = stack.last();
        SimpleVertexConsumer consumer = Reusable.SIMPLE_CONSUMER.get().wrap(graphics.bufferSource().getBuffer(RenderType.gui()));

        if(inside != null){
            inside.resetTransform();
            inside.compile(matrix, consumer.color(insideColor != null ? insideColor.applyAsInt(this) : defInsideColor));
        }

        if(outline != null){
            outline.resetTransform();
            outline.compile(matrix, consumer.color(outlineColor != null ? outlineColor.applyAsInt(this) : defOutlineColor));
        }

        consumer.reset();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(isClickThrough()) return false;

        mouseX = (mouseX - origin.x) / scale.x;//Transform to relative space
        mouseY = (mouseY - origin.y) / scale.y;

        float doubleRadius = roundingRadius * 2;
        boolean noWidth = width <= doubleRadius;
        boolean noHeight = height <= doubleRadius;
        float radiusSqr = roundingRadius * roundingRadius;

        if(noWidth && noHeight){
            return Vector2f.distanceSquared((float) mouseX, (float) mouseY, 0, 0) <= radiusSqr;
        }

        float halfWidth = width / 2;
        float halfHeight = height / 2;

        if(mouseX < -halfWidth || mouseX > halfWidth || mouseY < -halfHeight || mouseY > halfHeight) return false;

        float actualHalfWidth = Math.max(0, (width - doubleRadius) / 2);
        float actualHalfHeight = Math.max(0, (height - doubleRadius) / 2);

        if(!noHeight && mouseY >= -actualHalfHeight && mouseY <= actualHalfHeight) return true;

        if(!noWidth && mouseX >= -actualHalfWidth && mouseX <= actualHalfWidth) return true;

        if(noWidth){
            return mouseY < -actualHalfHeight ?
                    Vector2f.distanceSquared((float) mouseX, (float) mouseY, 0, -actualHalfHeight) <= radiusSqr//top
                    : Vector2f.distanceSquared((float) mouseX, (float) mouseY, 0, actualHalfHeight) <= radiusSqr;//bottom
        }

        if(noHeight){
            return mouseX < -actualHalfWidth ?
                    Vector2f.distanceSquared((float) mouseX, (float) mouseY, -actualHalfWidth, 0) <= radiusSqr//left
                    : Vector2f.distanceSquared((float) mouseX, (float) mouseY, actualHalfWidth, 0) <= radiusSqr;//right
        }

        if(mouseX < -actualHalfWidth){
            return mouseY < -actualHalfHeight ?
                    Vector2f.distanceSquared((float) mouseX, (float) mouseY, -actualHalfWidth, -actualHalfHeight) <= radiusSqr//top left
                    : Vector2f.distanceSquared((float) mouseX, (float) mouseY, -actualHalfWidth, actualHalfHeight) <= radiusSqr;//bottom left
        }

        return mouseY < -actualHalfHeight ?
                Vector2f.distanceSquared((float) mouseX, (float) mouseY, actualHalfWidth, -actualHalfHeight) <= radiusSqr//top right
                : Vector2f.distanceSquared((float) mouseX, (float) mouseY, actualHalfWidth, actualHalfHeight) <= radiusSqr;//bottom right
    }
}
