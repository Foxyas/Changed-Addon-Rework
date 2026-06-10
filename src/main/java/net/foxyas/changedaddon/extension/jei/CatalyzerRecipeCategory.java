package net.foxyas.changedaddon.extension.jei;

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
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CatalyzerRecipeCategory implements IRecipeCategory<CatalyzerRecipe> {
    public final static ResourceLocation UID = ChangedAddonMod.resourceLoc("catalyzer");
    public final static ResourceLocation TEXTURE = ChangedAddonMod.textureLoc("textures/screens/jei_catalyzer_screen");
    private final IDrawable background;
    private final IDrawable icon;

    public CatalyzerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonBlocks.CATALYZER.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.@NotNull RecipeType<CatalyzerRecipe> getRecipeType() {
        return CARecipeTypes.CATALYZER_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("block.changed_addon.catalyzer");
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
    public void draw(CatalyzerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
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
    public @NotNull Class<? extends CatalyzerRecipe> getRecipeClass() {
        return CatalyzerRecipe.class;
    }

    /// --- End of the for removal classes ---


    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CatalyzerRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 18).addIngredients(recipe.getIngredients().get(0));
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem(level.registryAccess()));

        // Exibir o campo progress como um texto ou barra de progresso
        float progressSpeed = recipe.getProgressSpeed();
        float nitrogenUsage = recipe.getNitrogenUsage();

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 51, 36).addItemStack(new ItemStack(ChangedAddonItems.CATALYZER_BLOCK_ILLUSTRATIVE_ITEM.get())) // Substitua por um item adequado
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    // Adiciona uma nova linha ao tooltip com o progresso da receita
                    tooltip.add(Component.translatable("gui.changed_addon.catalyzer.nitrogen_usage", progressSpeed, nitrogenUsage));
                });
    }
}
