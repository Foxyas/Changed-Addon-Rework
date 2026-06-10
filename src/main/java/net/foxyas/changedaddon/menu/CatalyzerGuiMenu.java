package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.foxyas.changedaddon.menu.UnifuserGuiMenu.POWER_BUTTON_ID;

public class CatalyzerGuiMenu extends AbstractMenu {
    public final Level level;
    public final Player entity;
    private final ContainerLevelAccess access;
    private final CatalyzerBlockEntity catalyzer;
    private final BlockPos blockPos;

    public final NonNullList<Slot> playerInvSlots = NonNullList.create();
    public final NonNullList<Slot> menuInvSlots = NonNullList.create();

    private final SlotItemHandler slot1;
    private final SimpleBrewingResultSlot slot2;

    public CatalyzerGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, extraData.readBlockPos());
    }

    public CatalyzerGuiMenu(int id, Inventory inv, BlockPos pos) {
        super(ChangedAddonMenus.CATALYZER_MENU.get(), id);
        this.entity = inv.player;
        this.level = inv.player.level;

        this.blockPos = pos;
        access = ContainerLevelAccess.create(level, pos);

        if (!(level.getBlockEntity(pos) instanceof CatalyzerBlockEntity be)) throw new IllegalStateException();

        catalyzer = be;
        IItemHandler internal = catalyzer.getCapability(ForgeCapabilities.ITEM_HANDLER, null).resolve().orElseThrow();

        createPlayerHotbar(inv, 0, 0);
        createPlayerInventory(inv, 0, 0);

        slot1 = (SlotItemHandler) addSlot(new SlotItemHandler(internal, 0, 44, 44) {
            @Override
            public boolean mayPickup(Player playerIn) {
                return true;
            }
        });
        slot2 = (SimpleBrewingResultSlot) addSlot(new SimpleBrewingResultSlot(entity, internal, 1, 116, 44));

        menuInvSlots.addAll(List.of(slot1, slot2));
    }

    public CatalyzerBlockEntity getCatalyzer() {
        return catalyzer;
    }

    public boolean isSlotEmpty(int slot) {
        return getSlot(slot).getItem().isEmpty();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, this.catalyzer.getBlockState().getBlock());
    }

    public Slot getLeftSlot() {
        return slot1;
    }

    public Slot getOutputSlot() {
        return slot2;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public boolean clickMenuButton(@NotNull Player player, int pId) {
        if (pId == POWER_BUTTON_ID) {
            Component customName = catalyzer.getCustomName();
            if (customName == null) customName = catalyzer.getDisplayName();
            String name = customName.getString();
            catalyzer.startRecipe = !catalyzer.startRecipe;

            if (catalyzer.startRecipe) {
                player.displayClientMessage(Component.literal("you start the " + name), true);
            } else {
                player.displayClientMessage(Component.literal("you stop the " + name), true);
            }
            //catalyzer.setChanged();
            return true;
        }
        return super.clickMenuButton(player, pId);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size() - 1, false)) {
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
