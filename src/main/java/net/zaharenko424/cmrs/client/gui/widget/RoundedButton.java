package net.zaharenko424.cmrs.client.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import org.apache.commons.lang3.function.ToBooleanBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

public class RoundedButton extends RoundedRectWidget {

    protected Component comp;
    protected BiConsumer<RoundedButton, PoseStack> renderTransform;
    protected ToBooleanBiFunction<RoundedButton, Integer> onClick;

    /**
     * No need to rebuild mesh after this call. <p> Origin is in the middle of the button.
     */
    public RoundedButton setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedButton setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedButton setSize(float width, float height){
        super.setSize(width, height);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedButton setRoundingRadius(float roundingRadius){
        super.setRoundingRadius(roundingRadius);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedButton setOutlineThickness(float outlineThickness){
        super.setOutlineThickness(outlineThickness);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedButton setOutlineColorFunc(@Nullable ToIntFunction<RoundedRectWidget> outlineColor){
        super.setOutlineColorFunc(outlineColor);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedButton setInsideColorFunc(@Nullable ToIntFunction<RoundedRectWidget> insideColor){
        super.setInsideColorFunc(insideColor);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedButton setText(@Nullable Component comp){
        this.comp = comp;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedButton setRenderTransform(@Nullable BiConsumer<RoundedButton, PoseStack> renderTransform){
        this.renderTransform = renderTransform;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     * @param onClick Return true if click is consumed.
     */
    public RoundedButton setOnClick(@Nullable ToBooleanBiFunction<RoundedButton, Integer> onClick){
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!shouldRender()) return;

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);
        if(renderTransform != null) renderTransform.accept(this, stack);

        render(stack, guiGraphics, mouseX, mouseY, partialTick);

        if(comp != null) {
            guiGraphics.bufferSource().endLastBatch();
            float textOffset;
            if(width < roundingRadius * 2){
                textOffset = -roundingRadius / 2f;
            } else textOffset = -width / 2f + roundingRadius / 2f + 2;
            WidgetHelper.renderScrollingComp(guiGraphics, Minecraft.getInstance().font, comp, 0, textOffset, -4.5f, -textOffset, 4.5f, Color.BLACK.getRGB(), false);
        }
        MatrixStack.pop(stack);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isHovering() && isInteractable() && onClick != null && onClick.applyAsBoolean(this, button);
    }
}
