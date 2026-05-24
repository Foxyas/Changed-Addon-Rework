package net.foxyas.changedaddon.menu;

import net.foxyas.changedaddon.block.entity.UnifuserBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class UnifuserGuiMenu extends AbstractMenu {

    public final Level level;
    public final Player entity;
    private final ContainerLevelAccess access;
    private final UnifuserBlockEntity unifuser;
    private final BlockPos blockPos;

    public final RecipeManager recipeManager;

    protected final SlotItemHandler topSlot;
    protected final SlotItemHandler bottomSlot;
    protected final SlotItemHandler syringeSlot;
    protected final SlotItemHandler outputSLot;

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

        this.recipeManager = inv.player.level().getRecipeManager();

        SlotItemHandler slot1 = new SlotItemHandler(internal, 0, 26, 20) {

            @Override
            public boolean mayPlace(@NotNull ItemStack itemstack) {
                return true;
            }
        };

        this.topSlot = (SlotItemHandler) addSlot(slot1);

        SlotItemHandler slot2 = new SlotItemHandler(internal, 1, 26, 56);
        this.bottomSlot = (SlotItemHandler) addSlot(slot2);

        SlotItemHandler slot3 = new SlotItemHandler(internal, 2, 116, 38) {

            @Override
            public boolean mayPlace(@NotNull ItemStack itemstack) {
                boolean hasRecipe = UnifuserGuiMenu.this.recipeManager.getAllRecipesFor(ChangedAddonRecipeTypes.UNIFUSER_RECIPE_TYPE.get()).stream().anyMatch((recipe) -> recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(itemstack)));
                return itemstack.is(ChangedAddonTags.Items.UNIFUSER_RECIPE_CATALYST) || hasRecipe;
            }
        };
        this.syringeSlot = (SlotItemHandler) addSlot(slot3);

        SlotItemHandler slot4 = new SlotItemHandler(internal, 3, 155, 57) {

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        };
        this.outputSLot = (SlotItemHandler) addSlot(slot4);

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
