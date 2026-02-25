package net.zaharenko424.cmrs.client.gui.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WidgetContainer extends Widget implements ContainerEventHandler, SizedWidget {

    protected static final Comparator<Widget> HIGHEST_Z = Comparator.<Widget, Float>comparing(widget -> widget.origin.z, Float::compare).reversed();

    protected final List<Widget> children = new ArrayList<>(4);

    private float width, height;
    private boolean dragging;
    private Widget focused;

    @Override
    public WidgetContainer setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    public WidgetContainer setWidth(float width){
        this.width = width;
        return this;
    }

    public WidgetContainer setHeight(float height){
        this.height = height;
        return this;
    }

    public WidgetContainer setSize(float width, float height){
        this.width = width;
        this.height = height;
        return this;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public void addWidget(@NotNull Widget child){
        if(child == this) return;
        children.add(child);
    }

    public void removeWidget(@NotNull Widget child){
        children.remove(child);
    }

    public void clearWidgets(){
        children.clear();
    }

    public void sortWidgets(){
        children.sort(HIGHEST_Z);
    }

    public void init(){
        sortWidgets();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!isVisible() || children.isEmpty()) return;

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        mouseX = (int) scaleX(mouseX);
        mouseY = (int) scaleY(mouseY);

        for(Widget widget : Lists.reverse(children)){
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        MatrixStack.pop(stack);
    }

    protected double scaleX(double mouseX){
        return (mouseX - origin.x) / scale.x;
    }

    protected double scaleY(double mouseY){
        return (mouseY - origin.y) / scale.y;
    }

    protected float scaleWidth(){
        return width / scale.x;
    }

    protected float scaleHeight(){
        return height / scale.y;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if(isClickThrough()) return false;
        float halfWidth = scaleWidth() / 2f;
        float halfHeight = scaleHeight() / 2f;
        mouseX = scaleX(mouseX);
        mouseY = scaleY(mouseY);
        return mouseX >= -halfWidth && mouseX <= halfWidth
                && mouseY >= -halfHeight && mouseY <= halfHeight;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        if(!isHovering()){
            for(Widget widget : children) widget.mouseMoved(Double.NaN, Double.NaN);
            return;
        }

        mouseX = scaleX(mouseX);
        mouseY = scaleY(mouseY);

        boolean consumed = false;
        for(Widget widget : children){
            if(consumed) {//Tell widget to unHover
                if(widget.isHovering()) widget.mouseMoved(Double.NaN, Double.NaN);
            } else {
                widget.mouseMoved(mouseX, mouseY);
                consumed = widget.isHovering();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mouseX = scaleX(mouseX);
        mouseY = scaleY(mouseY);
        for (GuiEventListener listener : children()) {
            if(!listener.mouseClicked(mouseX, mouseY, button)) continue;

            setFocused(listener);
            setDragging(true);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseX = scaleX(mouseX);
        mouseY = scaleY(mouseY);
        if(isDragging()) {
            setDragging(false);
            if(getFocused() != null) {
                return getFocused().mouseReleased(mouseX, mouseY, button);
            }
        }

        double finalMouseX = mouseX;
        double finalMouseY = mouseY;
        return getChildAt(mouseX, mouseY).filter(p_94708_ -> p_94708_.mouseReleased(finalMouseX, finalMouseY, button)).isPresent();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return getFocused() != null && isDragging() && getFocused().mouseDragged(scaleX(mouseX), scaleY(mouseY), button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        return ContainerEventHandler.super.mouseScrolled(scaleX(mouseX), scaleY(mouseY), scrollY);
    }

    @Override
    public @NotNull List<? extends Widget> children() {
        return children;
    }

    @Override
    public boolean isDragging() {
        return dragging;
    }

    @Override
    public void setDragging(boolean isDragging) {
        this.dragging = isDragging;
    }

    @Override
    public @Nullable Widget getFocused() {
        return focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if(focused == null){
            if(this.focused != null) this.focused.setFocused(false);
            this.focused = null;
            return;
        }

        if(!(focused instanceof Widget widget)) return;

        if(this.focused != null) this.focused.setFocused(false);

        this.focused = widget;
        widget.setFocused(true);
    }
}
