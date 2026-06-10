package net.foxyas.changedaddon.extension.jei.itemTransfers;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.foxyas.changedaddon.extension.jei.CARecipeTypes;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UnifuserRecipeHandler implements IRecipeTransferInfo<UnifuserGuiMenu, UnifuserRecipe> {

    public UnifuserRecipeHandler() {
        super();
    }

    @Override
    public @NotNull Class<? extends UnifuserGuiMenu> getContainerClass() {
        return UnifuserGuiMenu.class;
    }

    @Override
    public @NotNull Optional<MenuType<UnifuserGuiMenu>> getMenuType() {
        return Optional.of(ChangedAddonMenus.UNIFUSER_MENU.get());
    }

    @Override
    public @NotNull RecipeType<UnifuserRecipe> getRecipeType() {
        return CARecipeTypes.UNIFUSER_RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(@NotNull UnifuserGuiMenu container, @NotNull UnifuserRecipe recipe) {
        return true;
    }

    @Override
    public @NotNull List<Slot> getRecipeSlots(@NotNull UnifuserGuiMenu container, @NotNull UnifuserRecipe recipe) {
        return List.of(container.getTopSlot(), container.getBottomSlot(), container.getSyringeSlot());

//        return container.menuInvSlots;
    }

    @Override
    public @NotNull List<Slot> getInventorySlots(@NotNull UnifuserGuiMenu container, @NotNull UnifuserRecipe recipe) {
        int maxInv = container.getTopSlot().index - 1;
        List<Slot> invSlots = new ArrayList<>();
        for (int i = 0; i < maxInv; i++) {
            invSlots.add(container.getSlot(i));
        }
        return invSlots;

//        return container.playerInvSlots;
    }
}
