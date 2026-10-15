package net.foxyas.changedaddon.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventoryUtils {

    /**
     * Transfers as many items as possible from a source item handler to a destination item handler.
     */
    public static boolean transferHandlerContents(IItemHandler from, IItemHandler to) {
        boolean movedAny = false;
        
        for (int i = 0; i < from.getSlots(); i++) {
            ItemStack stack = from.getStackInSlot(i);
            if (stack.isEmpty()) continue;

            ItemStack remainder = ItemHandlerHelper.insertItem(to, stack, true);
            int canAccept = stack.getCount() - remainder.getCount();

            if (canAccept > 0) {
                ItemStack extracted = from.extractItem(i, canAccept, false);
                ItemHandlerHelper.insertItem(to, extracted, false);
                movedAny = true;
            }
        }
        
        return movedAny;
    }

    /**
     * Moves items from a specific slot in a source handler into any available slot in a destination handler.
     */
    public static boolean transferSlot(IItemHandler from, int fromSlot, IItemHandler to) {
        ItemStack stack = from.getStackInSlot(fromSlot);
        if (stack.isEmpty()) return false;

        ItemStack remainder = ItemHandlerHelper.insertItem(to, stack, true);
        int canAccept = stack.getCount() - remainder.getCount();

        if (canAccept > 0) {
            ItemStack extracted = from.extractItem(fromSlot, canAccept, false);
            ItemHandlerHelper.insertItem(to, extracted, false);
            return true;
        }
        
        return false;
    }

    /**
     * Moves items from a specific slot in a source handler into a specific slot in a destination handler.
     * 
     * @param from      The source inventory.
     * @param fromSlot  The slot index in the source inventory.
     * @param to        The destination inventory.
     * @param toSlot    The target slot index in the destination inventory.
     * @return          True if items were successfully transferred.
     */
    public static boolean transferSlot(IItemHandler from, int fromSlot, IItemHandler to, int toSlot) {
        ItemStack sourceStack = from.getStackInSlot(fromSlot);
        if (sourceStack.isEmpty()) return false;

        // Check if destination slot can accept this item
        ItemStack destinationStack = to.getStackInSlot(toSlot);
        
        // If destination is full and doesn't match the item, we can't insert here
        if (!destinationStack.isEmpty() && !ItemHandlerHelper.canItemStacksStack(sourceStack, destinationStack)) {
            return false;
        }

        // Calculate how much the specific slot can accept
        int maxStackSize = Math.min(to.getSlotLimit(toSlot), sourceStack.getMaxStackSize());
        int currentInDest = destinationStack.isEmpty() ? 0 : destinationStack.getCount();
        int spaceAvailable = maxStackSize - currentInDest;

        if (spaceAvailable <= 0) return false;

        int transferAmount = Math.min(sourceStack.getCount(), spaceAvailable);

        // Extract from source and insert directly into the target slot
        ItemStack extracted = from.extractItem(fromSlot, transferAmount, true); // Simulate first
        if (extracted.isEmpty()) return false;

        extracted = from.extractItem(fromSlot, transferAmount, false); // Real extraction
        to.insertItem(toSlot, extracted, false); // Real insertion

        return true;
    }
}