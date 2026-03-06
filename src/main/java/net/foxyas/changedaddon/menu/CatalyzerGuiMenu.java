package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.block.entity.CatalyzerBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
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

public class CatalyzerGuiMenu extends AbstractMenu {

    public final Level level;
    public final Player entity;
    private final ContainerLevelAccess access;
    private final CatalyzerBlockEntity catalyzer;
    private final BlockPos blockPos;
    private final SlotItemHandler slot1;
    private final SlotItemHandler slot2;

    public CatalyzerGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, extraData.readBlockPos());
    }

    public CatalyzerGuiMenu(int id, Inventory inv, BlockPos pos) {
        super(ChangedAddonMenus.CATALYZER_GUI.get(), id);
        this.entity = inv.player;
        this.level = inv.player.level;

        this.blockPos = pos;
        access = ContainerLevelAccess.create(level, pos);

        if (!(level.getBlockEntity(pos) instanceof CatalyzerBlockEntity be)) throw new IllegalStateException();

        catalyzer = be;
        IItemHandler internal = catalyzer.getCapability(ForgeCapabilities.ITEM_HANDLER, null).resolve().orElseThrow();

        createPlayerHotbar(inv, 12, 4);
        createPlayerInventory(inv, 12, 4);

        slot1 = new SlotItemHandler(internal, 0, 23, 44);
        addSlot(slot1); // 36
        slot2 = new SlotItemHandler(internal, 1, 153, 44) { // 37

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };
        addSlot(slot2);
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

    public SlotItemHandler getLeftSlot() {
        return slot1;
    }

    public SlotItemHandler getOutputSlot() {
        return slot2;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
