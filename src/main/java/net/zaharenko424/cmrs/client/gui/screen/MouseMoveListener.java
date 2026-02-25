package net.zaharenko424.cmrs.client.gui.screen;

import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.zaharenko424.cmrs.client.gui.widget.Widget;

public interface MouseMoveListener extends ContainerEventHandler {

    @Override
    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiEventListener listener : children()) {
            if(!listener.mouseClicked(mouseX, mouseY, button)) continue;

            setFocused(listener);
            setDragging(true);
            return true;
        }

        return false;
    }

    @Override
    default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return getFocused() != null && isDragging() && getFocused().mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    default boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(isDragging()) {
            setDragging(false);
            if(getFocused() != null) {
                return getFocused().mouseReleased(mouseX, mouseY, button);
            }
        }

        return getChildAt(mouseX, mouseY).filter(p_94708_ -> p_94708_.mouseReleased(mouseX, mouseY, button)).isPresent();
    }

    @Override
    default void mouseMoved(double mouseX, double mouseY) {
        //Assume highest first
        boolean consumed = false;
        for(GuiEventListener listener : children()){
            if(!(listener instanceof Widget widget)) continue;
            if(consumed) {//Tell widget to unHover
                if(widget.isHovering()) widget.mouseMoved(Double.NaN, Double.NaN);
            } else {
                widget.mouseMoved(mouseX, mouseY);
                consumed = widget.isHovering();
            }
        }
    }
}
