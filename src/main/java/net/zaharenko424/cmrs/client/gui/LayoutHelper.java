package net.zaharenko424.cmrs.client.gui;

import net.zaharenko424.cmrs.client.gui.widget.ScrollableContainer;
import net.zaharenko424.cmrs.client.gui.widget.SizedWidget;
import net.zaharenko424.cmrs.client.gui.widget.Widget;

import java.util.Collection;

public class LayoutHelper {

    public static <W extends Widget & SizedWidget> void listLayout(ScrollableContainer container, Collection<W> widgets, float spacing){
        listLayout(container, widgets, 0, 0, spacing);
    }

    public static <W extends Widget & SizedWidget> void listLayout(ScrollableContainer container, Collection<W> widgets, float topX, float leftY, float spacing){
        if(widgets.isEmpty()){
            container.setActualHeight(0);
            return;
        }

        float top = -container.getHeight() / 2f;
        float actualHeight = leftY;

        for(W w : widgets){
            w.setOrigin(topX,  top + actualHeight + w.getHeight() / 2f, 0);
            container.addWidget(w);

            actualHeight += w.getHeight() + spacing;
        }

        actualHeight -= spacing;
        container.setActualHeight(actualHeight);
    }

    public static <W extends Widget & SizedWidget> void tileLayout(ScrollableContainer container, Collection<W> widgets, float topX, float leftY, float width, float paddingX, float paddingY){
        float f = 0;
        float height = 0;
        float f1 = 0;
        for (W widget : widgets) {
            if (width - f < widget.getWidth()) {//do next row
                f = widget.getWidth() + paddingX;
                height += f1 + paddingY;
                f1 = widget.getHeight();

                widget.setOrigin(topX + widget.getWidth() / 2, leftY + height + widget.getHeight() / 2, 0);
                continue;
            }

            widget.setOrigin(topX + f + widget.getWidth() / 2, leftY + height + widget.getHeight() / 2, 0);
            f += widget.getWidth() + paddingX;
            if (widget.getHeight() > f1) f1 = widget.getHeight();
        }

        container.setActualHeight(height + f1);
    }
}
