package net.zaharenko424.cmrs.client.gui.widget;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public abstract class Widget implements Renderable, GuiEventListener, NarratableEntry {

    protected final Vector3f origin = new Vector3f();
    protected final Vector3f scale = new Vector3f(1);

    protected boolean hovering;
    private boolean interactable = true;
    private boolean clickThrough = false;
    private boolean visible = true;

    public Widget setOrigin(float x, float y, float z){
        origin.set(x, y, z);
        return this;
    }

    public Widget resetScale(){
        scale.set(1);
        return this;
    }

    public Widget setScale(float x, float y, float z){
        scale.set(x, y, z);
        return this;
    }

    public boolean isHovering(){
        return hovering;
    }

    public boolean isInteractable(){
        return !isClickThrough() && interactable;
    }

    public Widget setInteractable(boolean interactable){
        this.interactable = interactable;
        return this;
    }

    public boolean isVisible(){
        return visible;
    }

    public Widget setVisible(boolean visible){
        this.visible = visible;
        return this;
    }

    public boolean isClickThrough(){
        return clickThrough || !isVisible();
    }

    public Widget setClickThrough(boolean clickThrough){
        this.clickThrough = clickThrough;
        return this;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        hovering = isMouseOver(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {}
}