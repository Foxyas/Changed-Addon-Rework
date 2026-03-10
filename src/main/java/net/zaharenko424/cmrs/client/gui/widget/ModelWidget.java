package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import net.zaharenko424.cmrs.client.geom.Reusable;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public abstract class ModelWidget extends Widget {

    protected float width = 100, height = 150;

    protected Vector2f rotation = new Vector2f();
    protected Vector2f accumulatedRotation = new Vector2f();
    protected Vector3f translation = new Vector3f();
    protected float zoom = 50;


    public ModelWidget(){
        setInteractable(false);
    }

    public ModelWidget setSize(float width, float height){
        this.width = width;
        this.height = height;
        return this;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public ModelWidget setInteractable(boolean interactable){
        super.setInteractable(interactable);
        return this;
    }

    public ModelWidget setZoom(float zoom){
        this.zoom = zoom;
        return this;
    }

    public ModelWidget setRotation(float radX, float radY){
        rotation.sub(radX, radY, accumulatedRotation);
        accumulatedRotation.negate();
        return this;
    }

    public ModelWidget setTranslation(float x, float y, float z){
        translation.set(x, y, z);
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!isVisible()) return;

        rotation.add(accumulatedRotation.x * .5f, accumulatedRotation.y * .5f);
        accumulatedRotation.sub(accumulatedRotation.x * .5f, accumulatedRotation.y * .5f);

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        //Flush before scissors
        guiGraphics.bufferSource().endLastBatch();

        //Transform rect to screen coordinates to apply scissors
        Vector3f vec = Reusable.VEC3F.get();
        guiGraphics.enableScissor((int) vec.set(- width / 2f, - height / 2f, 0).mulPosition(stack.last().pose()).x, (int) vec.y,
                (int) vec.set(width / 2f, height / 2f, 0).mulPosition(stack.last().pose()).x, (int) vec.y);

        Matrix4f viewportMat = Reusable.MAT4F.get().identity();
        viewportMat.rotateX(rotation.x);
        viewportMat.rotateY(rotation.y);
        viewportMat.translate(translation);
        viewportMat.translate(0, zoom, 0);
        MatrixStack.mul(stack, viewportMat);

        stack.scale(zoom, -zoom, zoom);

        Lighting.setupForEntityInInventory();
        renderModel(guiGraphics, partialTick);
        Lighting.setupFor3DItems();

        //first transform mouse to local than apply inverse of viewport matrix & mul by 16 as model unit is 16 times smaller
        //v.set(mouseX- origin.x, mouseY - origin.y, 0).mulPosition(viewportMat.identity().rotateX(rotation.x).rotateY(rotation.y).translate(translation).translate(0, zoom, 0).scale(zoom, -zoom, zoom).invert()).mul(16)
        //now just need to find the closest model part to camera (while also applying animations to model...)

        //Flush scissors
        guiGraphics.bufferSource().endLastBatch();

        guiGraphics.disableScissor();
        MatrixStack.pop(stack);
    }

    protected abstract void renderModel(GuiGraphics graphics, float partialTick);

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(!isInteractable()) return false;

        if(button == InputConstants.MOUSE_BUTTON_RIGHT){
            Matrix4f m = Reusable.MAT4F.get().identity();
            m.rotateX(rotation.x);
            m.rotateY(rotation.y);
            Vector3f v = Reusable.VEC3F.get().set(dragX, dragY, 0);
            v.mulPosition(m);

            translation.add(v.x, v.y, -v.z);
        }

        if(button == InputConstants.MOUSE_BUTTON_LEFT){
            accumulatedRotation.add((float) -dragY * Mth.DEG_TO_RAD, (float) dragX * Mth.DEG_TO_RAD);
        }

        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        if(!isInteractable()) return false;
        zoom += (float) scrollY * 2;
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isHovering() && isInteractable();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(isClickThrough()) return false;
        float halfWidth = width / 2 * scale.x;
        float halfHeight = height / 2 * scale.y;

        return mouseX >= origin.x - halfWidth && mouseX <= origin.x + halfWidth
                && mouseY >= origin.y - halfHeight && mouseY <= origin.y + halfHeight;
    }
}
