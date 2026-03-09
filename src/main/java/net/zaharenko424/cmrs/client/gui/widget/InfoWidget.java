package net.zaharenko424.cmrs.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class InfoWidget extends Widget implements SizedWidget {

    protected Component title;
    protected Component description;

    protected float width;
    protected float height;
    protected float lineHeight;
    protected float lineWidth;

    protected Color lineColor = Color.WHITE; // Branco padrão

    public InfoWidget() {
    }

    /**
     * No need to rebuild mesh after this call. <p> Origin is in the middle of the button.
     */
    public InfoWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public InfoWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public InfoWidget setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public InfoWidget setLineSize(float width, float height) {
        this.lineWidth = width;
        this.lineHeight = height;
        return this;
    }

    public InfoWidget setLineColor(Color argb) {
        this.lineColor = argb;
        return this;
    }

    public InfoWidget setTextInfo(Component title, Component description) {
        this.title = title;
        this.description = description;
        return this;
    }

    public InfoWidget setDescription(Component description) {
        this.description = description;
        return this;
    }

    public InfoWidget setTitle(Component title) {
        this.title = title;
        return this;
    }

    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var font = Minecraft.getInstance().font;

        float currentY = this.origin.y;

        // 🔹 Render Title
        float x = this.origin.x;
        guiGraphics.drawString(font, title.getVisualOrderText(), x, currentY, 0xFFFFFF, false);
        currentY += font.lineHeight + 4;

        // 🔹 Render Line
        drawLine(guiGraphics, x, currentY, lineWidth, lineHeight, lineColor.getRGB());
        currentY += lineHeight + 5;

        // 🔹 Render Description (quebra automática)
        guiGraphics.drawWordWrap(font, description, (int) x, (int) currentY, (int) width, 0xCCCCCC);
    }

    protected void drawLine(GuiGraphics guiGraphics, float x, float y, float width, float height, int color) {
        guiGraphics.fill(
                (int) x,
                (int) y,
                (int) (x + width),
                (int) (y + height),
                color
        );
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }
}