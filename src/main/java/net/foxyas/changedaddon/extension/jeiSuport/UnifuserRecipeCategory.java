package net.foxyas.changedaddon.extension.jeiSuport;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipes.UnifuserRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@Deprecated
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
    public mezz.jei.api.recipe.RecipeType<UnifuserRecipe> getRecipeType() {
        return JeiSuport.JeiUnifuser_Type;
    }

    @Override
    public Component getTitle() {
        return new TextComponent((new TranslatableComponent("block.changed_addon.unifuser").getString()));
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Deprecated
    @Override
    @SuppressWarnings("removal")
    public Class<? extends UnifuserRecipe> getRecipeClass() {
        return UnifuserRecipe.class;
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, UnifuserRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 18).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 36).addIngredients(recipe.getIngredients().get(1));

        // Exibir o campo progress como um texto ou barra de progresso
        float progressSpeed = recipe.getProgressSpeed();
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 36).addItemStack(new ItemStack(ChangedAddonItems.UNIFUSER_BLOCK_ILLUSTRATIVE_ITEM.get())) // Substitua por um item adequado
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    // Adiciona uma nova linha ao tooltip com o progresso da receita
                    tooltip.add(new TranslatableComponent("changed_addon.gui.recipe_progress", progressSpeed));
                });
    }
}
