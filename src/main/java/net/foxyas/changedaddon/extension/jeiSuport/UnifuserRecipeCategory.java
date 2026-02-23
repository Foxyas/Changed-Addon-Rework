package net.foxyas.changedaddon.extension.jeiSuport;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UnifuserRecipeCategory implements IRecipeCategory<UnifuserRecipe> {

    public final static ResourceLocation UID = ChangedAddonMod.resourceLoc("jei_unifuser");
    public final static ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/screens/jei_unifuser_screen");
    private final IDrawable background;
    private final IDrawable icon;

    public UnifuserRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonBlocks.UNIFUSER.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.@NotNull RecipeType<UnifuserRecipe> getRecipeType() {
        return ChangedAddonJeiPlugin.UNIFUSER_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.changed_addon.unifuser");
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public void draw(UnifuserRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }


    /**
     * getUid and getRecipeClass are marked to removal
     * you are supposed to get the info from getRecipeType() method now
     */

    @Deprecated
    public @NotNull Class<? extends UnifuserRecipe> getRecipeClass() {
        return UnifuserRecipe.class;
    }

    /// --- End of the for removal classes ---

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, UnifuserRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 18).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 36).addIngredients(recipe.getIngredients().get(1));

        // Exibir o campo progress como um texto ou barra de progresso
        float progressSpeed = recipe.getProgressSpeed();
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 36).addItemStack(new ItemStack(ChangedAddonItems.UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM.get())) // Substitua por um item adequado
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    // Adiciona uma nova linha ao tooltip com o progresso da receita
                    tooltip.add(Component.translatable("gui.changed_addon.recipe_progress", progressSpeed));
                });
    }
}
