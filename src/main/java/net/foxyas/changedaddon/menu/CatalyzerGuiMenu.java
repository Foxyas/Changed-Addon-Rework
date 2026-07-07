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

    private final SimpleItemHandlerInputSlot slot1;
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

        slot1 = (SimpleItemHandlerInputSlot) addSlot(new SimpleItemHandlerInputSlot(internal, 0, 44, 44));
        slot2 = (SimpleBrewingResultSlot) addSlot(new SimpleBrewingResultSlot(entity, catalyzer, internal, 1, 116, 44));

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

            // 1. Lógica de movimentação (Shift-Clique)
            if (pIndex < 36) { // Se veio do inventário do jogador -> vai para os slots do bloco
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else { // Se veio dos slots do bloco -> vai para o inventário do jogador
                if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // 2. DISPARAR EVENTOS DE CRAFT (Apenas se a transferência acima deu certo)
            // Isso deve rodar ANTES do itemstack1 ser zerado ou modificado pelo slot.set()
            if (slot instanceof SimpleBrewingResultSlot resultSlot) {
                itemstack1.getItem().onCraftedBy(itemstack1, pPlayer.level(), pPlayer);
                resultSlot.onQuickCraft(itemstack1, itemstack);
            }

            // 3. Atualizar o estado do Slot de origem
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            // 4. Verificação de segurança obrigatória do Minecraft
            // Se o tamanho do pack não mudou nada, significa que não havia espaço para mover
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            // 5. Notificar o Slot que o jogador efetivamente "retirou" o item dali
            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }
}
