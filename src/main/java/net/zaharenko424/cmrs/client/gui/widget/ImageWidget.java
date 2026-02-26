package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import org.jetbrains.annotations.NotNull;

public class ImageWidget extends Widget implements SizedWidget {

    protected ResourceLocation texture;
    protected float texWidth, texHeight;
    protected float uWidth = -1, vHeight = -1;
    protected float uOffset, vOffset;
    protected float width = 100, height = 100;

    public ImageWidget setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    public ImageWidget setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    public ImageWidget setTex(ResourceLocation texture, float texWidth, float texHeight) {
        return setTex(texture, 0, 0, -1, -1, texWidth, texHeight);
    }

    public ImageWidget setTex(ResourceLocation texture, float uWidth, float vHeight, float texWidth, float texHeight) {
        return setTex(texture, 0, 0, uWidth, vHeight, texWidth, texHeight);
    }

    public ImageWidget setTex(ResourceLocation texture, float uOffset, float vOffset, float uWidth, float vHeight, float texWidth, float texHeight) {
        this.texture = texture;
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        this.uWidth = uWidth;
        this.vHeight = vHeight;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        return this;
    }

    public ImageWidget setSize(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isVisible() || texture == null) return;

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);

        if (uWidth != -1 && vHeight != -1) {
            WidgetHelper.blit(texture, stack, -width / 2, -height / 2, width, height, uOffset, vOffset, uWidth, vHeight, texWidth, texHeight);
        } else WidgetHelper.blit(texture, stack, -width / 2, -height / 2, width, height, texWidth, texHeight);

        MatrixStack.pop(stack);
    }
}
