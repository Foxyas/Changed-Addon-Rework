package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.block.entity.IRecipeRewarder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SimpleBrewingResultSlot extends SlotItemHandler {
    private final Player player;
    private int removeCount;

    public SimpleBrewingResultSlot(Player pPlayer, IItemHandler itemHandler, int pSlot, int pXPosition, int pYPosition) {
        super(itemHandler, pSlot, pXPosition, pYPosition);
        this.player = pPlayer;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack pStack) {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
     */
    public ItemStack remove(int pAmount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(pAmount, this.getItem().getCount());
        }

        return super.remove(pAmount);
    }

    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        super.onTake(pPlayer, pStack);
    }

    /**
     * Typically increases an internal count, then calls {@code onCrafting(item)}.
     * @param pStack the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    /**
     *
     * @param pStack the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void checkTakeAchievements(ItemStack pStack) {
        pStack.onCraftedBy(this.player.level(), this.player, this.removeCount);
        Player player = this.player;
        if (player instanceof ServerPlayer serverplayer) {
            Container container = this.container;
            if (container instanceof IRecipeRewarder iRecipeRewarder) {
                iRecipeRewarder.awardUsedRecipes(serverplayer, pStack);
            }
        }

        this.removeCount = 0;
    }
}
