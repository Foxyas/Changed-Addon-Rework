package net.zaharenko424.cmrs.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class InfoWidget {

    private final Component title;
    private final Component description;

    private int x;
    private int y;
    private int width;

    private int lineColor = 0xFFFFFFFF; // Branco padrão

    public InfoWidget(Component title, Component description, int x, int y, int width) {
        this.title = title;
        this.description = description;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public InfoWidget setLineColor(int argb) {
        this.lineColor = argb;
        return this;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var font = Minecraft.getInstance().font;

        int currentY = y;

        // 🔹 Render Title
        guiGraphics.drawString(font, title, x, currentY, 0xFFFFFF, false);
        currentY += font.lineHeight + 4;

        // 🔹 Render Diagonal Line
        drawLine(guiGraphics, x, currentY, width, 6, lineColor);
        currentY += 10;

        // 🔹 Render Description (quebra automática)
        guiGraphics.drawWordWrap(font, description, x, currentY, width, 0xCCCCCC);
    }

    private void drawLine(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        guiGraphics.fill(
                x,
                y,
                x + width,
                y + height,
                color
        );
    }
}