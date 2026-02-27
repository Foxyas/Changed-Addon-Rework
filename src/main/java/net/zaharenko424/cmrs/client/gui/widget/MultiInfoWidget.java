package net.zaharenko424.cmrs.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MultiInfoWidget extends InfoWidget {

    public static class InfoEntry {
        public Component title;
        public Component description;
        public Color lineColor;

        public InfoEntry(Component title, Component description, Color lineColor) {
            this.title = title;
            this.description = description;
            this.lineColor = lineColor;
        }
    }

    public MultiInfoWidget() {
    }

    protected final List<InfoEntry> entries = new ArrayList<>();

    public MultiInfoWidget addEntry(Component title, Component description, Color lineColor) {
        entries.add(new InfoEntry(title, description, lineColor));
        return this;
    }

    public MultiInfoWidget clearEntries() {
        entries.clear();
        return this;
    }

    /**
     * No need to rebuild mesh after this call. <p> Origin is in the middle of the button.
     */
    public MultiInfoWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public MultiInfoWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public MultiInfoWidget setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public MultiInfoWidget setLineSize(float width, float height) {
        this.lineWidth = width;
        this.lineHeight = height;
        return this;
    }

    public MultiInfoWidget setLineColor(Color argb) {
        this.lineColor = argb;
        return this;
    }

    public MultiInfoWidget setTextInfo(Component title, Component description) {
        this.title = title;
        this.description = description;
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var font = Minecraft.getInstance().font;

        float currentY = this.origin.y;
        float x = this.origin.x;

        for (InfoEntry entry : entries) {

            // 🔹 Title
            guiGraphics.drawString(font, entry.title, (int)x, (int)currentY, 0xFFFFFF, false);
            currentY += font.lineHeight + 4;

            // 🔹 Line
            drawLine(guiGraphics, x, currentY, lineWidth, lineHeight, entry.lineColor.getRGB());
            currentY += lineHeight + 5;

            // 🔹 Description
            int descHeight = font.wordWrapHeight(entry.description, (int)width);
            guiGraphics.drawWordWrap(font, entry.description, (int)x, (int)currentY, (int)width, 0xCCCCCC);

            currentY += descHeight + 10; // Espaço entre blocos
        }
    }
}