package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class UnifuserGuiMenu extends AbstractMenu {

    public final Level level;
    public final Player entity;
    private final ContainerLevelAccess access;
    private final UnifuserBlockEntity unifuser;
    private final BlockPos blockPos;

    protected final SlotItemHandler slot1;
    protected final SlotItemHandler slot2;
    protected final SlotItemHandler slot3;
    protected final SlotItemHandler slot4;

    public UnifuserGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, extraData.readBlockPos());
    }

    public UnifuserGuiMenu(int id, Inventory inv, BlockPos pos) {
        super(ChangedAddonMenus.UNIFUSER_MENU.get(), id);
        this.entity = inv.player;
        this.level = inv.player.level;

        this.blockPos = pos;
        access = ContainerLevelAccess.create(level, pos);

        if (!(level.getBlockEntity(pos) instanceof UnifuserBlockEntity be)) throw new IllegalStateException();
        unifuser = be;
        IItemHandler internal = unifuser.getCapability(ForgeCapabilities.ITEM_HANDLER, null).resolve().orElseThrow();

        createPlayerHotbar(inv, 12, 21);
        createPlayerInventory(inv, 12, 21);


        //35 is the last slot before this
        SlotItemHandler slot1 = new SlotItemHandler(internal, 0, 15, 45) { //36

            @Override
            public boolean mayPlace(@NotNull ItemStack itemstack) {
                return true;
            }
        };

        this.slot1 = (SlotItemHandler) addSlot(slot1);

        SlotItemHandler slot2 = new SlotItemHandler(internal, 1, 15, 70);
        this.slot2 = (SlotItemHandler) addSlot(slot2); //37

        SlotItemHandler slot3 = new SlotItemHandler(internal, 2, 50, 57) { //38

            @Override
            public boolean mayPlace(@NotNull ItemStack itemstack) {
                return itemstack.isEmpty() || itemstack.getItem() == ChangedAddonItems.CATALYZED_DNA.get() || itemstack.is(ChangedItems.BLOOD_SYRINGE.get())
                        || itemstack.is(ChangedItems.LATEX_SYRINGE.get());
            }
        };
        this.slot3 = (SlotItemHandler) addSlot(slot3);

        SlotItemHandler slot4 = new SlotItemHandler(internal, 3, 155, 57) { //39

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };
        this.slot4 = (SlotItemHandler) addSlot(slot4);

    }

    public UnifuserBlockEntity getUnifuser() {
        return unifuser;
    }

    public boolean isSlotEmpty(int slot) {
        return getSlot(slot).getItem().isEmpty();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, this.unifuser.getBlockState().getBlock());
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public SlotItemHandler getOutputSlot() {
        return slot4;
    }

    public SlotItemHandler getSyringeSlot() {
        return slot3;
    }

    public SlotItemHandler getBottomSlot() {
        return slot2;
    }

    public SlotItemHandler getTopSlot() {
        return slot1;
    }
}
