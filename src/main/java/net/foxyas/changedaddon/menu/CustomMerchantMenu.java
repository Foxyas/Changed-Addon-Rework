package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.entity.api.CustomMerchant;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class CustomMerchantMenu extends AbstractContainerMenu {

    protected static final int PAYMENT1_SLOT = 0;
    protected static final int PAYMENT2_SLOT = 1;
    protected static final int RESULT_SLOT = 2;
    private static final int INV_SLOT_START = 3;
    private static final int USE_ROW_SLOT_START = 30;
    private static final int USE_ROW_SLOT_END = 39;

    private final CustomMerchant merchant;
    private final CustomMerchantContainer tradeContainer;

    public CustomMerchantMenu(int containerId, Inventory inv, FriendlyByteBuf buf) {
        this(containerId, inv, offersFromBuf(inv.player, buf));
    }

    public CustomMerchantMenu(int containerId, Inventory inv, CustomMerchant merchant) {
        super(ChangedAddonMenus.MERCHANT_MENU.get(), containerId);
        this.merchant = merchant;
        tradeContainer = new CustomMerchantContainer(merchant);
        addSlot(new Slot(tradeContainer, PAYMENT1_SLOT, 136, 37));
        addSlot(new Slot(tradeContainer, PAYMENT2_SLOT, 162, 37));
        addSlot(new MerchantResultSlot(inv.player, merchant, tradeContainer, RESULT_SLOT, 220, 37));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(inv, j + i * 9 + 9, 108 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            addSlot(new Slot(inv, k, 108 + k * 18, 142));
        }
    }

    private static CustomMerchant offersFromBuf(Player player, FriendlyByteBuf buf) {
        CustomMerchant merchant = new CustomMerchant.Client(player);
        merchant.overrideOffers(CustomMerchantOffers.createFromStream(buf));
        return merchant;
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void slotsChanged(@NotNull Container pInventory) {
        tradeContainer.updateSellItem();
        super.slotsChanged(pInventory);
    }

    public void setSelectionHint(int pCurrentRecipeIndex) {
        tradeContainer.setSelectionHint(pCurrentRecipeIndex);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean stillValid(@NotNull Player player) {
        return merchant.getTradingPlayer() == player;
    }

    public boolean canRestock() {
        return true;
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in is
     * null for the initial slot that was double-clicked.
     */
    public boolean canTakeItemForPickAll(@NotNull ItemStack stack, @NotNull Slot slot) {
        return false;
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == RESULT_SLOT) {
                if (!moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
                playTradeSound();
            } else if (index != PAYMENT1_SLOT && index != PAYMENT2_SLOT) {
                if (index >= INV_SLOT_START && index < USE_ROW_SLOT_START) {
                    if (!moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !moveItemStackTo(itemstack1, INV_SLOT_START, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private void playTradeSound() {
        if (merchant.isClientSide()) return;

        Entity entity = (Entity) merchant;
        entity.level().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), merchant.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
    }

    /**
     * Called when the container is closed.
     */
    public void removed(@NotNull Player player) {
        super.removed(player);
        merchant.setTradingPlayer(null);
        if (merchant.isClientSide()) return;

        if (!player.isAlive() || player instanceof ServerPlayer && ((ServerPlayer) player).hasDisconnected()) {
            ItemStack itemstack = tradeContainer.removeItemNoUpdate(PAYMENT1_SLOT);
            if (!itemstack.isEmpty()) {
                player.drop(itemstack, false);
            }

            itemstack = tradeContainer.removeItemNoUpdate(PAYMENT2_SLOT);
            if (!itemstack.isEmpty()) {
                player.drop(itemstack, false);
            }
        } else if (player instanceof ServerPlayer) {
            player.getInventory().placeItemBackInInventory(tradeContainer.removeItemNoUpdate(0));
            player.getInventory().placeItemBackInInventory(tradeContainer.removeItemNoUpdate(PAYMENT2_SLOT));
        }
    }

    public void tryMoveItems(int selectedMerchantRecipe) {
        CustomMerchantOffers offers = getOffers();
        if (offers.size() <= selectedMerchantRecipe) return;

        ItemStack itemstack = tradeContainer.getItem(PAYMENT1_SLOT);
        if (!itemstack.isEmpty()) {
            if (!moveItemStackTo(itemstack, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                return;
            }

            tradeContainer.setItem(PAYMENT1_SLOT, itemstack);
        }

        ItemStack itemstack1 = tradeContainer.getItem(PAYMENT2_SLOT);
        if (!itemstack1.isEmpty()) {
            if (!moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                return;
            }

            tradeContainer.setItem(PAYMENT2_SLOT, itemstack1);
        }

        if (tradeContainer.getItem(PAYMENT1_SLOT).isEmpty() && tradeContainer.getItem(PAYMENT2_SLOT).isEmpty()) {
            CustomMerchantOffer offer = offers.get(selectedMerchantRecipe);
            moveFromInventoryToPaymentSlot(PAYMENT1_SLOT, offer.getCostA());
            moveFromInventoryToPaymentSlot(PAYMENT2_SLOT, offer.getCostB());
        }
    }

    private void moveFromInventoryToPaymentSlot(int paymentSlotIndex, Ingredient paymentSlot) {
        if (paymentSlot.isEmpty()) return;

        ItemStack matchingIngredient = null, stackO, stack;
        for (int i = INV_SLOT_START; i < USE_ROW_SLOT_END; ++i) {
            ItemStack itemstack = slots.get(i).getItem();
            if (itemstack.isEmpty()) continue;

            if (matchingIngredient == null) {
                for (ItemStack requiredItem : paymentSlot.getItems()) {
                    if (!ItemStack.isSameItemSameTags(requiredItem, itemstack)) continue;

                    matchingIngredient = requiredItem;
                    break;
                }
            }

            if (matchingIngredient == null || !ItemStack.isSameItemSameTags(matchingIngredient, itemstack)) continue;

            stackO = tradeContainer.getItem(paymentSlotIndex);
            int j = stackO.isEmpty() ? 0 : stackO.getCount();
            int k = Math.min(matchingIngredient.getMaxStackSize() - j, itemstack.getCount());
            stack = itemstack.copy();
            int l = j + k;
            itemstack.shrink(k);
            stack.setCount(l);
            tradeContainer.setItem(paymentSlotIndex, stack);
            if (l >= matchingIngredient.getMaxStackSize()) break;
        }
    }

    public CustomMerchantOffers getOffers() {
        return merchant.getOffers();
    }

    public CustomMerchant getMerchant() {
        return merchant;
    }

    static class MerchantResultSlot extends Slot {

        private final CustomMerchantContainer slots;
        private final Player player;
        private final CustomMerchant merchant;
        private int removeCount;

        public MerchantResultSlot(Player player, CustomMerchant merchant, CustomMerchantContainer container, int slot, int x, int y) {
            super(container, slot, x, y);
            this.player = player;
            this.merchant = merchant;
            this.slots = container;
        }

        /**
         * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
         */
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }

        /**
         * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new stack.
         */
        public @NotNull ItemStack remove(int amount) {
            if (hasItem()) {
                removeCount += Math.min(amount, getItem().getCount());
            }

            return super.remove(amount);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
         * internal count then calls onCrafting(item).
         */
        protected void onQuickCraft(@NotNull ItemStack stack, int amount) {
            removeCount += amount;
            checkTakeAchievements(stack);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
         */
        protected void checkTakeAchievements(ItemStack stack) {
            stack.onCraftedBy(player.level, player, removeCount);
            removeCount = 0;
        }

        public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
            checkTakeAchievements(stack);
            CustomMerchantOffer offer = slots.getActiveOffer();
            if (offer == null) return;

            ItemStack itemstack = slots.getItem(0);
            ItemStack itemstack1 = slots.getItem(1);
            if (offer.take(itemstack, itemstack1) || offer.take(itemstack1, itemstack)) {
                merchant.notifyTrade(offer);
                player.awardStat(Stats.TRADED_WITH_VILLAGER);
                slots.setItem(0, itemstack);
                slots.setItem(1, itemstack1);
            }
        }
    }
}
