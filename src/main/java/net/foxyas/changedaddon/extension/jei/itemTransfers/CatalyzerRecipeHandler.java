package net.foxyas.changedaddon.extension.jei.itemTransfers;

import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.foxyas.changedaddon.extension.jei.CARecipeTypes;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatalyzerRecipeHandler implements IRecipeTransferInfo<CatalyzerGuiMenu, CatalyzerRecipe> {

    public CatalyzerRecipeHandler() {
        super();
    }

    @Override
    public @NotNull Class<? extends CatalyzerGuiMenu> getContainerClass() {
        return CatalyzerGuiMenu.class;
    }

    @Override
    public @NotNull Optional<MenuType<CatalyzerGuiMenu>> getMenuType() {
        return Optional.of(ChangedAddonMenus.CATALYZER_MENU.get());
    }

    @Override
    public @NotNull RecipeType<CatalyzerRecipe> getRecipeType() {
        return CARecipeTypes.CATALYZER_RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(@NotNull CatalyzerGuiMenu container, @NotNull CatalyzerRecipe recipe) {
        return true;
    }

    @Override
    public @NotNull List<Slot> getRecipeSlots(@NotNull CatalyzerGuiMenu container, @NotNull CatalyzerRecipe recipe) {
        return List.of(container.getLeftSlot());
//        return container.menuInvSlots;
    }

    @Override
    public @NotNull List<Slot> getInventorySlots(@NotNull CatalyzerGuiMenu container, @NotNull CatalyzerRecipe recipe) {
        int maxInv = container.getLeftSlot().index - 1;
        List<Slot> invSlots = new ArrayList<>();
        for (int i = 0; i < maxInv; i++) {
            invSlots.add(container.getSlot(i));
        }
        return invSlots;
//        return container.playerInvSlots;
    }
}
