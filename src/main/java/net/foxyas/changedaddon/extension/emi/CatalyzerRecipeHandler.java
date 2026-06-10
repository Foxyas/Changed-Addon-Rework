package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class CatalyzerRecipeHandler implements StandardRecipeHandler<CatalyzerGuiMenu> {

    @Override
    public List<Slot> getInputSources(CatalyzerGuiMenu handler) {

        int maxInv = handler.getLeftSlot().index - 1;
        List<Slot> invSlots = new ArrayList<>();
        for (int i = 0; i < maxInv; i++) {
            invSlots.add(handler.getSlot(i));
        }
        return invSlots;
    }

    @Override
    public List<Slot> getCraftingSlots(CatalyzerGuiMenu handler) {
        return List.of(handler.getLeftSlot());
    }

    @Override
    public boolean supportsRecipe(EmiRecipe recipe) {
        return recipe.getCategory() == CARecipesCategories.UNIFUSER_CATEGORY && recipe.supportsRecipeTree();
    }
}
