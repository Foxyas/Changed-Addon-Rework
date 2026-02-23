package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.entity.api.CustomMerchant;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CustomMerchantContainer implements Container {

    private final CustomMerchant merchant;
    private final NonNullList<ItemStack> itemStacks = NonNullList.withSize(3, ItemStack.EMPTY);
    @Nullable
    private CustomMerchantOffer activeOffer;
    private int selectionHint;

    public CustomMerchantContainer(CustomMerchant merchant) {
        this.merchant = merchant;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getContainerSize() {
        return itemStacks.size();
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : itemStacks) {
            if (!itemstack.isEmpty()) return false;
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    public @NotNull ItemStack getItem(int index) {
        return itemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public @NotNull ItemStack removeItem(int index, int count) {
        ItemStack itemstack = itemStacks.get(index);
        if (index == 2 && !itemstack.isEmpty()) {
            return ContainerHelper.removeItem(itemStacks, index, itemstack.getCount());
        } else {
            ItemStack itemstack1 = ContainerHelper.removeItem(itemStacks, index, count);
            if (!itemstack1.isEmpty() && isPaymentSlot(index)) {
                updateSellItem();
            }

            return itemstack1;
        }
    }

    /**
     * if par1 slot has changed, does resetRecipeAndSlots need to be called?
     */
    private boolean isPaymentSlot(int slot) {
        return slot == 0 || slot == 1;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(itemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setItem(int index, @NotNull ItemStack stack) {
        itemStacks.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }

        if (isPaymentSlot(index)) updateSellItem();
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean stillValid(@NotNull Player player) {
        return merchant.getTradingPlayer() == player;
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    public void setChanged() {
        updateSellItem();
    }

    public void updateSellItem() {
        activeOffer = null;
        ItemStack itemstack;
        ItemStack itemstack1;
        if (itemStacks.get(0).isEmpty()) {
            itemstack = itemStacks.get(1);
            itemstack1 = ItemStack.EMPTY;
        } else {
            itemstack = itemStacks.get(0);
            itemstack1 = itemStacks.get(1);
        }

        if (itemstack.isEmpty()) {
            setItem(2, ItemStack.EMPTY);
            return;
        }

        CustomMerchantOffers offers = merchant.getOffers();
        if (!offers.isEmpty()) {
            CustomMerchantOffer offer = offers.getRecipeFor(itemstack, itemstack1, selectionHint);
            if (offer == null || offer.isOutOfStock()) {
                activeOffer = offer;
                offer = offers.getRecipeFor(itemstack1, itemstack, selectionHint);
            }

            if (offer != null && !offer.isOutOfStock()) {
                activeOffer = offer;
                setItem(2, offer.assemble());
            } else {
                setItem(2, ItemStack.EMPTY);
            }
        }

        merchant.notifyTradeUpdated(getItem(2));
    }

    @Nullable
    public CustomMerchantOffer getActiveOffer() {
        return activeOffer;
    }

    public void setSelectionHint(int currentRecipeIndex) {
        selectionHint = currentRecipeIndex;
        updateSellItem();
    }

    public void clearContent() {
        itemStacks.clear();
    }
}
