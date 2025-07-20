package net.foxyas.changedaddon.extension.jeiSuport;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipes.CatalyzerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class JeiCatalyzerRecipeCategory implements IRecipeCategory<CatalyzerRecipe> {
    public final static ResourceLocation UID = new ResourceLocation("changed_addon", "jei_catalyzer");
    public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/jei_catalyzer_screen.png");
    private final IDrawable background;
    private final IDrawable icon;

    public JeiCatalyzerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonBlocks.CATALYZER.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<CatalyzerRecipe> getRecipeType() {
        return JeiSuport.JeiCatalyzer_Type;
    }

    @Override
    public Component getTitle() {
        return new TextComponent((new TranslatableComponent("block.changed_addon.catalyzer").getString()));
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @SuppressWarnings("removal")
    @Deprecated
    @Override
    public @NotNull Class<? extends CatalyzerRecipe> getRecipeClass() {
        return CatalyzerRecipe.class;
    }

    @SuppressWarnings("removal")
    @Override
    public @NotNull ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CatalyzerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 18).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());

        // Exibir o campo progress como um texto ou barra de progresso
        float progressSpeed = recipe.getProgressSpeed();
        float nitrogenUsage = recipe.getNitrogenUsage();

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 51, 36).addItemStack(new ItemStack(ChangedAddonItems.CATALYZER_BLOCK_ILLUSTRATIVE_ITEM.get())) // Substitua por um item adequado
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    // Adiciona uma nova linha ao tooltip com o progresso da receita
                    tooltip.add(new TranslatableComponent("changed_addon.gui.catalyzer.nitrogen_usage", progressSpeed, nitrogenUsage));
                });
    }
}
