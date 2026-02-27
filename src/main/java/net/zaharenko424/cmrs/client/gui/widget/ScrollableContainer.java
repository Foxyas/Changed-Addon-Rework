package net.zaharenko424.cmrs.client.gui.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class ScrollableContainer extends WidgetContainer {

    protected float scroll;
    protected final RoundedRectWidget scrollBar = new RoundedRectWidget(){
        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            scroll += (float) dragY / (ScrollableContainer.this.getHeight() * .8f);
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    };
    private float actualHeight;

    public RoundedRectWidget getScrollBar() {
        return scrollBar;
    }

    public void setActualHeight(float height){
        actualHeight = height;
    }

    public boolean isScrollEnabled(){
        return actualHeight > getHeight();
    }

    @Override
    public void init() {
        super.init();

        scrollBar.setSize(16, 50)
                .setRoundingRadius(8)
                .setOrigin(getWidth() * .45f, getHeight() * -.4f, 10);
        scrollBar.rebuildMesh();
    }

    float scroll1;

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!isVisible() || children.isEmpty()) return;

        scroll1 += scroll;
        scroll = 0;
        if(scroll1 < 0) scroll1 = 0;
        if(scroll1 > 1) scroll1 = 1;

        //Finish buffers before scissoring
        guiGraphics.bufferSource().endLastBatch();

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        //Transform rect to screen coordinates to apply scissors
        Vector3f v = new Vector3f(-getWidth() / 2f, -getHeight() / 2f, 0).mulPosition(stack.last().pose());
        Vector3f v1 = new Vector3f(getWidth() / 2f, getHeight() / 2f, 0).mulPosition(stack.last().pose());
        guiGraphics.enableScissor((int) v.x, (int) v.y, (int) v1.x, (int) v1.y);

        MatrixStack.push(stack);
        if(isScrollEnabled()) stack.translate(0, -scroll1 * (actualHeight - getHeight()), 0);

        mouseX = (int) scaleX(mouseX);
        mouseY = (int) scaleY(mouseY);

        for(Widget widget : Lists.reverse(children)){
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        MatrixStack.pop(stack);
        if(isScrollEnabled()) {
            stack.translate(0, scroll1 * getHeight() * .8f, 0);
            scrollBar.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        //Finish buffers before finishing scissoring
        guiGraphics.bufferSource().endLastBatch();
        guiGraphics.disableScissor();
        MatrixStack.pop(stack);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        hovering = isMouseOver(mouseX, mouseY);
        if(!isHovering()){
            for(Widget widget : children) widget.mouseMoved(Double.NaN, Double.NaN);
            return;
        }

        mouseX = scaleX(mouseX);
        mouseY = scaleY(mouseY + scroll1 * (actualHeight - getHeight()));

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
        if(isScrollEnabled() && scrollBar.isMouseOver(scaleX(mouseX), scaleY(mouseY - scroll1 * getHeight() * .8f))){
            setFocused(scrollBar);
            if (button == 0) {
                this.setDragging(true);
            }
            return true;
        }

        return super.mouseClicked(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(isScrollEnabled() && scrollBar.isMouseOver(scaleX(mouseX), scaleY(mouseY - scroll1 * getHeight() * .8f))){
            scrollBar.mouseDragged(scaleX(mouseX), scaleY(mouseY - scroll1 * getHeight() * .8f), button, dragX, dragY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        if(isScrollEnabled()) {
            scroll -= (float) scrollY / 10;
            if (getFocused() == scrollBar) {
                setFocused(null);
                setDragging(false);
            }
        }

        return super.mouseScrolled(mouseX, mouseY + scroll1 * (actualHeight - getHeight()), scrollY);
    }
}
