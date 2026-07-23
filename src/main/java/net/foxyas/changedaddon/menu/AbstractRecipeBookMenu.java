package net.foxyas.changedaddon.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//copy from a_changed
public abstract class AbstractRecipeBookMenu<T extends Container> extends RecipeBookMenu<T> {

    protected AbstractRecipeBookMenu(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    protected void createPlayerHotbar(Inventory playerInv) {
        createPlayerHotbar(playerInv, 0, 0);
    }

    protected void createPlayerHotbar(Inventory playerInv, int xOffset, int yOffset) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + (column * 18) + xOffset, 142 + yOffset));
        }
    }

    protected void createPlayerInventory(Inventory playerInv) {
        createPlayerInventory(playerInv, 0, 0);
    }

    protected void createPlayerInventory(Inventory playerInv, int xOffset, int yOffset) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + column + (row * 9), 8 + (column * 18) + xOffset, 84 + (row * 18) + yOffset));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
