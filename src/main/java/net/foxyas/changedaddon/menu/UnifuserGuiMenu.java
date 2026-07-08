package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.foxyas.changedaddon.init.ChangedAddonTags;
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
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UnifuserGuiMenu extends AbstractMenu {

    public static final int POWER_BUTTON_ID = 0;

    public final Level level;
    public final Player entity;
    private final ContainerLevelAccess access;
    private final UnifuserBlockEntity unifuser;
    private final BlockPos blockPos;

    public final RecipeManager recipeManager;

    public final NonNullList<Slot> playerInvSlots = NonNullList.create();
    public final NonNullList<Slot> menuInvSlots = NonNullList.create();

    protected final SimpleItemHandlerInputSlot topSlot;
    protected final SimpleItemHandlerInputSlot bottomSlot;
    protected final SimpleItemHandlerInputSlot syringeSlot;
    protected final SimpleBrewingResultSlot outputSLot;

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

        createPlayerHotbar(inv, 0, 0);
        createPlayerInventory(inv, 0, 0);

        playerInvSlots.addAll(this.slots);

        this.recipeManager = inv.player.level().getRecipeManager();

        SimpleItemHandlerInputSlot slot1 = new SimpleItemHandlerInputSlot(internal, 0, 26, 17);
        this.topSlot = (SimpleItemHandlerInputSlot) addSlot(slot1);

        SimpleItemHandlerInputSlot slot2 = new SimpleItemHandlerInputSlot(internal, 1, 26, 53);
        this.bottomSlot = (SimpleItemHandlerInputSlot) addSlot(slot2);

        SimpleItemHandlerInputSlot slot3 = new SimpleItemHandlerInputSlot(internal, 2, 53, 35) {
            @Override
            public boolean mayPlace(@NotNull ItemStack itemstack) {
                boolean hasRecipe = UnifuserGuiMenu.this.recipeManager.getAllRecipesFor(ChangedAddonRecipeTypes.UNIFUSER_RECIPE_TYPE.get()).stream().anyMatch((recipe) -> recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(itemstack)));
                return itemstack.is(ChangedAddonTags.Items.UNIFUSER_RECIPE_CATALYST) || hasRecipe;
            }
        };
        this.syringeSlot = (SimpleItemHandlerInputSlot) addSlot(slot3);

        SimpleBrewingResultSlot slot4 = new SimpleBrewingResultSlot(entity, unifuser, internal, 3, 116, syringeSlot.y); // y35
        this.outputSLot = (SimpleBrewingResultSlot) addSlot(slot4);

        menuInvSlots.addAll(List.of(topSlot, bottomSlot, syringeSlot));
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

    @Override
    public boolean clickMenuButton(@NotNull Player player, int pId) {
        if (pId == POWER_BUTTON_ID) {
            Component customName = unifuser.getCustomName();
            if (customName == null) customName = unifuser.getDisplayName();
            String name = customName.getString();
            unifuser.startRecipe = !unifuser.startRecipe;

            if (unifuser.startRecipe) {
                player.displayClientMessage(Component.literal("you start the " + name), true);
            } else {
                player.displayClientMessage(Component.literal("you stop the " + name), true);
            }
            //unifuser.setChanged();
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

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Slot getOutputSlot() {
        return outputSLot;
    }

    public Slot getSyringeSlot() {
        return syringeSlot;
    }

    public Slot getBottomSlot() {
        return bottomSlot;
    }

    public Slot getTopSlot() {
        return topSlot;
    }
}
