package net.foxyas.changedaddon.extension.jei.guisHandlers;

import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import net.foxyas.changedaddon.client.gui.FoxyasInventoryMenuScreen;
import net.foxyas.changedaddon.menu.FoxyasInventoryMenu;
import net.minecraft.client.renderer.Rect2i;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FoxyasGuiContainerHandler implements IGuiContainerHandler<FoxyasInventoryMenuScreen> {

    @Override
    public @NotNull List<Rect2i> getGuiExtraAreas(FoxyasInventoryMenuScreen gui) {
        int i = gui.getLeftPos() + FoxyasInventoryMenu.X_OFFSET;
        int j = gui.getTopPos() + FoxyasInventoryMenu.Y_OFFSET;
        int ExtraInvVHeight = 81;

        int extraX = i + 176;
        int extraY = j + 2;
        int extraWidth = gui.getXSize();
        int extraHeight = ExtraInvVHeight;

        Rect2i normalInventory = new Rect2i(i, j, gui.getXSize(), gui.getYSize());
        Rect2i extraInventory = new Rect2i(extraX, extraY, 227, 81);
        return List.of(normalInventory, extraInventory);
    }
}
