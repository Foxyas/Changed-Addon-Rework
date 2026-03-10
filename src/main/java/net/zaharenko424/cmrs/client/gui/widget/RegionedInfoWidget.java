package net.zaharenko424.cmrs.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
//TODO why is this needed? remove?
public class RegionedInfoWidget extends InfoWidget implements SizedWidget {

    protected final WidgetContainer region;

    public RegionedInfoWidget(WidgetContainer region) {
        this.region = region;
    }

    /**
     * No need to rebuild mesh after this call. <p> Origin is in the middle of the button.
     */
    public RegionedInfoWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RegionedInfoWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RegionedInfoWidget setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RegionedInfoWidget setLineSize(float width, float height) {
        this.lineWidth = width;
        this.lineHeight = height;
        return this;
    }

    public RegionedInfoWidget setLineColor(Color argb) {
        this.lineColor = argb;
        return this;
    }

    public RegionedInfoWidget setTextInfo(Component title, Component description) {
        this.title = title;
        this.description = description;
        return this;
    }

    public RegionedInfoWidget setDescription(Component description) {
        this.description = description;
        return this;
    }

    public RegionedInfoWidget setTitle(Component title) {
        this.title = title;
        return this;
    }

    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
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
        guiGraphics.drawWordWrap(font, description, (int) x, (int) currentY, (int) region.getWidth(), 0xCCCCCC);
    }

    protected void drawLine(GuiGraphics guiGraphics, float x, float y, float width, float height, int color) {
        guiGraphics.fill(
                (int) x,
                (int) y,
                (int) (x + Math.min(width, region.getWidth())),
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