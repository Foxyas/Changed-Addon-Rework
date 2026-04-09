package net.foxyas.changedaddon.extension.jeiSuport;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipe.special.KeycardColorRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeycardColorRecipeCategory implements IRecipeCategory<KeycardColorRecipe> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ChangedAddonMod.MODID, "keycard_coloring");
    private static final ResourceLocation TEX = ChangedAddonMod.textureLoc("textures/misc/gui_vanilla");
    private final IDrawable icon;
    private final IDrawable background;

    public KeycardColorRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonItems.KEYCARD_ITEM.get()));
        background = guiHelper.drawableBuilder(TEX, 0, 0, 116, 54).setTextureSize(116, 54).build();
    }

    @Override
    public @NotNull RecipeType<KeycardColorRecipe> getRecipeType() {
        return ChangedAddonJeiPlugin.KEYCARD_COLOR_RECIPE_TYPE;
    }

    /// --- End of the for removal classes ---

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.changed_addon.keycard_color");
    }

//    @Override
//    public @NotNull IDrawable getBackground() {
//        return background;
//    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public void draw(KeycardColorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull KeycardColorRecipe recipe, @NotNull IFocusGroup focus) {
        ItemStack keyCard = ChangedAddonItems.KEYCARD_ITEM.get().getDefaultInstance();
        List<ItemStack> dyes = new ArrayList<>(List.of(Ingredient.of(Tags.Items.DYES).getItems()));
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (y == 1 && x == 1) {
                    builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1).addItemStack(keyCard);
                } else {
                    Collections.shuffle(dyes);
                    builder.addSlot(RecipeIngredientRole.INPUT, x * 18 + 1, y * 18 + 1).addItemStacks(dyes);
                }
            }
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 19).addItemStack(keyCard);
    }
}
