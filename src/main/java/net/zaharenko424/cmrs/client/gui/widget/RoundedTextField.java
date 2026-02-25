package net.zaharenko424.cmrs.client.gui.widget;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.zaharenko424.cmrs.client.api.MatrixStack;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.util.Consumer4;
import net.zaharenko424.cmrs.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public class RoundedTextField extends RoundedRectWidget {

    protected int maxLength = 32;//Width 230 height 40
    protected Component defText;

    protected boolean active;
    protected boolean editable = true;
    protected StringBuilder builder = new StringBuilder();
    protected String text, selection;
    protected int cursorPos, selectionPos;//position of "|" in text
    protected long blinkMillis;

    public static final int defSelectionColor = Utils.color(64, Color.BLUE.getRGB());
    public static final int defCursorColor = Color.RED.getRGB();
    protected BiConsumer<RoundedTextField, PoseStack> renderTransform;
    protected Consumer4<RoundedTextField, GuiGraphics, Float, Float> onRender;
    protected Consumer<RoundedTextField> onChanged;

    /**
     * No need to rebuild mesh after this call. <p> Origin is in the middle of the button.
     */
    public RoundedTextField setOrigin(float x, float y, float z) {
        super.setOrigin(x, y, z);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setScale(float x, float y, float z) {
        super.setScale(x, y, z);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedTextField setSize(float width, float height){
        super.setSize(Math.max(10, width), height);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedTextField setRoundingRadius(float roundingRadius){
        super.setRoundingRadius(roundingRadius);
        return this;
    }

    /**
     * Mesh has to be rebuilt for this to take effect.
     */
    public RoundedTextField setOutlineThickness(float outlineThickness){
        super.setOutlineThickness(outlineThickness);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setMaxTextLength(int maxLength){
        this.maxLength = Mth.clamp(maxLength, 0, Short.MAX_VALUE);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setDefText(@Nullable Component defText){
        this.defText = defText;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setOutlineColorFunc(@Nullable ToIntFunction<RoundedRectWidget> outlineColor){
        super.setOutlineColorFunc(outlineColor);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setInsideColorFunc(@Nullable ToIntFunction<RoundedRectWidget> insideColor){
        super.setInsideColorFunc(insideColor);
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setRenderTransform(BiConsumer<RoundedTextField, PoseStack> renderTransform){
        this.renderTransform = renderTransform;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setOnRender(Consumer4<RoundedTextField, GuiGraphics, Float, Float> onRender){
        this.onRender = onRender;
        return this;
    }

    /**
     * No need to rebuild mesh after this call.
     */
    public RoundedTextField setOnContentsChanged(Consumer<RoundedTextField> onChanged){
        this.onChanged = onChanged;
        return this;
    }

    public RoundedTextField setEditable(boolean editable){
        this.editable = editable;
        return this;
    }

    public boolean isEditable(){
        return editable;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if(!shouldRender() && defText == null && getText().isEmpty()) return;

        PoseStack stack = guiGraphics.pose();
        MatrixStack.push(stack);
        MatrixStack.translate(stack, origin);
        MatrixStack.scale(stack, scale);
        if(renderTransform != null) renderTransform.accept(this, stack);

        render(stack, guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.bufferSource().endLastBatch();

        String text = getText();
        Font font = Minecraft.getInstance().font;
        float textOffset = -width / 2f + roundingRadius / 2f + 2;
        if(!text.isBlank()) {
            WidgetHelper.renderScrollingString(guiGraphics, font, text, textOffset, textOffset, -4.5f, -textOffset, 4.5f, Color.BLACK.getRGB(), false);
        } else if(defText != null) {
            WidgetHelper.renderScrollingComp(guiGraphics, font, defText, textOffset, textOffset, -4.5f, -textOffset, 4.5f, active ? Color.DARK_GRAY.getRGB() : Color.BLACK.getRGB(), false);
        }

        if(active) {
            if(selectionPos != cursorPos) {
                int selStart = Math.min(selectionPos, cursorPos);
                int selEnd = Math.max(selectionPos, cursorPos);
                WidgetHelper.fill(stack, font.width(text.substring(0, selStart)) + textOffset, -6f, font.width(text.substring(0, selEnd)) + textOffset, 4f, 0, defSelectionColor);
            }

            long currMillis = Util.getMillis();
            long diff = currMillis - blinkMillis;
            if(diff > 800) {
                blinkMillis = currMillis + 800;
            } else if(diff >= 0) {
                String str = text.substring(0, cursorPos);
                int width = font.width(str);
                WidgetHelper.fill(stack, textOffset + width - 1, -6f, textOffset + width, 4f, 0, defCursorColor);
            }
        }

        if(onRender != null) {
            guiGraphics.bufferSource().endBatch(RenderType.gui());
            onRender.accept(this, guiGraphics, 0f, 0f);
        }
        MatrixStack.pop(stack);
    }

    public @NotNull String getText(){
        if(text == null) text = builder.toString();
        return text;
    }

    public String getSelected(){
        if(selection == null){
            int selStart = Math.min(selectionPos, cursorPos);
            int selEnd = Math.max(selectionPos, cursorPos);
            selection = builder.substring(selStart, selEnd);
        }

        return selection;
    }

    @CanIgnoreReturnValue
    public RoundedTextField moveCursorPos(int delta){
        return moveCursorPos(delta, false);
    }

    @CanIgnoreReturnValue
    public RoundedTextField moveCursorPos(int delta, boolean select){
        setCursorPos(cursorPos + delta);
        if(!select) setSelectionPos(cursorPos);
        return this;
    }

    @CanIgnoreReturnValue
    public RoundedTextField moveCursorPosTo(int pos, boolean select){
        setCursorPos(pos);
        if(!select) setSelectionPos(cursorPos);
        return this;
    }

    @CanIgnoreReturnValue
    public RoundedTextField setCursorPos(int pos){
        int newPos = Mth.clamp(pos, 0, builder.length());
        if(cursorPos != newPos){
            selection = null;//Reset selection
            cursorPos = newPos;
            blinkMillis = Util.getMillis();
        }
        return this;
    }

    @CanIgnoreReturnValue
    public RoundedTextField setSelectionPos(int pos){
        int newPos = Mth.clamp(pos, 0, builder.length());
        if(newPos != selectionPos){
            selection = null;//Reset selection
            selectionPos = newPos;
        }
        return this;
    }

    @CanIgnoreReturnValue
    public RoundedTextField insertAtCursor(String text){
        if(builder.length() >= maxLength || text.isEmpty()) return this;

        text = SharedConstants.filterText(text);
        if(text.length() + builder.length() > maxLength){
            text = text.substring(0, maxLength - builder.length());
        }
        if(text.isEmpty()) return this;

        builder.insert(cursorPos, text);
        moveCursorPos(text.length());
        this.text = null;
        if(onChanged != null) onChanged.accept(this);
        return this;
    }

    public RoundedTextField clearText(){
        cursorPos = selectionPos = 0;

        text = "";
        builder = new StringBuilder();
        if(onChanged != null) onChanged.accept(this);
        return this;
    }

    protected void insertOrReplaceText(String text){
        if(selectionPos == cursorPos) {
            insertAtCursor(text);
            return;
        }

        text = SharedConstants.filterText(text);

        int selStart = Math.min(selectionPos, cursorPos);
        int selEnd = Math.max(selectionPos, cursorPos);
        int diff = text.length() - (selEnd - selStart);

        if(diff + builder.length() > maxLength){
            text = text.substring(0, text.length() - diff);
            diff = 0;
        }

        builder.replace(selStart, selEnd, text);

        if(cursorPos > selectionPos) {
            moveCursorPos(diff);
        }
        setSelectionPos(cursorPos);
        this.text = null;
        if(onChanged != null) onChanged.accept(this);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if(!active || !isEditable() || builder.length() >= maxLength || !SharedConstants.isAllowedChatCharacter(codePoint)) return false;

        if(selectionPos != cursorPos){
            insertOrReplaceText(String.valueOf(codePoint));
            return true;
        }

        builder.insert(cursorPos, codePoint);
        moveCursorPos(1);
        text = null;

        if(onChanged != null) onChanged.accept(this);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return switch (keyCode){
            case InputConstants.KEY_LEFT -> {
                if(selectionPos != cursorPos && !Screen.hasShiftDown()){
                    setSelectionPos(cursorPos);
                    yield true;
                }
                if(Screen.hasControlDown()){
                    moveCursorPosTo(StringSplitter.getWordPosition(getText(), -1, cursorPos, true), Screen.hasShiftDown());
                    yield true;
                }
                moveCursorPos(-1, Screen.hasShiftDown());
                yield true;
            }//Min is before first char
            case InputConstants.KEY_RIGHT -> {
                if(selectionPos != cursorPos && !Screen.hasShiftDown()){
                    setSelectionPos(cursorPos);
                    yield true;
                }
                if(Screen.hasControlDown()){
                    moveCursorPosTo(StringSplitter.getWordPosition(getText(), 1, cursorPos, true), Screen.hasShiftDown());
                    yield true;
                }
                moveCursorPos(1, Screen.hasShiftDown());
                yield true;
            }//Max at last char
            case InputConstants.KEY_BACKSPACE -> {
                if(cursorPos == 0 || !isEditable()) yield false;//If less than 0 something is very wrong
                if(selectionPos != cursorPos){
                    insertOrReplaceText("");
                    yield true;
                }
                builder.deleteCharAt(cursorPos - 1);
                moveCursorPos(-1);
                text = null;
                if(onChanged != null) onChanged.accept(this);
                yield true;
            }
            case InputConstants.KEY_DELETE -> {
                if(!isEditable() || builder.length() < cursorPos || builder.isEmpty()) yield false;
                builder.deleteCharAt(cursorPos);
                text = null;
                if(onChanged != null) onChanged.accept(this);
                yield true;
            }
            default -> {
                if(Screen.isSelectAll(keyCode)){
                    cursorPos = builder.length();
                    setSelectionPos(0);
                    yield true;
                }

                if(Screen.isCopy(keyCode)){
                    Minecraft.getInstance().keyboardHandler.setClipboard(getSelected());
                    yield true;
                }

                if(!isEditable()) yield false;

                if(Screen.isPaste(keyCode)){
                    insertOrReplaceText(TextFieldHelper.getClipboardContents(Minecraft.getInstance()));
                    yield true;
                }

                if(Screen.isCut(keyCode)) {
                    Minecraft.getInstance().keyboardHandler.setClipboard(getSelected());
                    insertOrReplaceText("");
                    yield true;
                }

                yield false;
            }
        };
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(!active || width <= roundingRadius * 2) return false;

        float localMouseX = (float) (mouseX - origin.x + 4);
        Font font = Minecraft.getInstance().font;
        setCursorPos(font.plainSubstrByWidth(getText(), (int) ((width / 2 - roundingRadius) + localMouseX / scale.x)).length());
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!isInteractable()) return false;
        active = isHovering();
        if(!active || width <= roundingRadius * 2) return false;

        float localMouseX = (float) (mouseX - origin.x + 4);
        Font font = Minecraft.getInstance().font;
        moveCursorPosTo(font.plainSubstrByWidth(getText(), (int) ((width / 2 - roundingRadius) + localMouseX / scale.x)).length(), Screen.hasShiftDown());
        return true;
    }
}
