package net.foxyas.changedaddon.process.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public class GuiUtils {
    /**
     * Copy this Method and paste in the place where you want to add the default player menu
     * @param inv is The Player Inventory data;
     * @param offsetY is the offset in height of the inventory
     * @param offsetX is the offset in width of the inventory
     */
    public static  void addPlayerDefaultInventory(Inventory inv, int offsetX, int offsetY) {
        /*for (int si = 0; si < 3; ++si) {
            for (int sj = 0; sj < 9; ++sj) {
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, offsetX + 8 + sj * 18, offsetY + 84 + si * 18));
            }
        }
        for (int si = 0; si < 9; ++si) {
            this.addSlot(new Slot(inv, si, offsetX + 8 + si * 18, 31 + 142));
        }*/
    }
}
