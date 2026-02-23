package net.foxyas.changedaddon.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

public class SuggestionHelper {
    private final EditBox editBox;
    private final Font font;
    private final List<String> baseList;             // Original List (names, ids, etc.)
    private final List<String> filtered = new ArrayList<>();

    private int maxSuggestions = 6;
    private int selectedIndex = -1;

    private boolean renderUpwards = true;
    private final Consumer<String> onSelect;

    public SuggestionHelper(EditBox editBox, List<String> list, Consumer<String> onSelect) {
        this.editBox = editBox;
        this.font = Minecraft.getInstance().font;
        this.baseList = list;
        this.onSelect = onSelect;
    }

    public void setRenderUpwards(boolean up) {
        this.renderUpwards = up;
    }

    public void setMaxSuggestions(int max) {
        this.maxSuggestions = max;
    }

    /**
     * Atualiza quando o texto muda
     */
    public void update() {
        String input = editBox.getValue().toLowerCase(Locale.ROOT);

        filtered.clear();
        selectedIndex = -1;

        if (input.isEmpty())
            return;

        baseList.stream()
                .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(input))
                .limit(maxSuggestions)
                .forEach(filtered::add);
    }

    /**
     * Navegação ↑ ↓
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (filtered.isEmpty()) return false;

        switch (keyCode) {
            case 265 -> { // UP
                selectedIndex = Math.max(0, selectedIndex - 1);
                return true;
            }
            case 264 -> { // DOWN
                selectedIndex = Math.min(filtered.size() - 1, selectedIndex + 1);
                return true;
            }
            case 258 -> { // TAB → CIRCLE and Auto fill
                // Autofill the "type bar"
                autoFillFromSuggestion();
                return true;
            }
            case 257, 335 -> { // Enter
                if (selectedIndex >= 0) {
                    accept(filtered.get(selectedIndex));
                    return true;
                }
            }
        }
        return false;
    }

    private void autoFillFromSuggestion() {
        if (selectedIndex < 0 || selectedIndex >= filtered.size()) return;

        String suggestion = filtered.get(selectedIndex);

        editBox.setValue(suggestion);
        update();
    }

    /**
     * Aceita o item escolhido
     */
    private void accept(String value) {
        editBox.setValue(value);
        filtered.clear();
        selectedIndex = -1;
        onSelect.accept(value);
    }

    /**
     * Clique do mouse
     */
    public boolean mouseClicked(double mouseX, double mouseY) {
        if (filtered.isEmpty()) return false;

        int x = editBox.getX();
        int w = editBox.getWidth();
        int h = 12;

        int startY = renderUpwards ?
                editBox.getY() - (filtered.size() * h) - 2 :
                editBox.getY() + editBox.getHeight() + 2;

        for (int i = 0; i < filtered.size(); i++) {
            int top = startY + (i * h);
            int bottom = top + h;

            if (mouseX >= x && mouseX <= x + w && mouseY >= top && mouseY <= bottom) {
                accept(filtered.get(i));
                return true;
            }
        }

        return false;
    }

    /**
     * Renderiza a lista
     */
    public void render(GuiGraphics guiGraphics) {
        if (filtered.isEmpty() || !editBox.isFocused()) return;

        int x = editBox.getX();
        int w = editBox.getWidth();
        int h = 12;

        int startY = renderUpwards ?
                editBox.getY() - (filtered.size() * h) - 2 :
                editBox.getY() + editBox.getHeight() + 2;

        for (int i = 0; i < filtered.size(); i++) {
            String s = filtered.get(i);
            int bg = (i == selectedIndex ? 0xAA2976FF : 0xAA000000);

            guiGraphics.fill(x, startY + i * h, x + w, startY + (i + 1) * h, bg);
            guiGraphics.drawString(font, s, x + 2, startY + i * h + 2, 0xFFFFFF);
        }
    }
}
