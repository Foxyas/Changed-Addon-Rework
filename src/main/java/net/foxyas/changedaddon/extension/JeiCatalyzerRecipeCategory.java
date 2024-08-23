package net.foxyas.changedaddon.extension;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@Deprecated
public class JeiCatalyzerRecipeCategory implements IRecipeCategory<JeiCatalyzerRecipe> {
    public final static ResourceLocation UID = new ResourceLocation("changed_addon", "jei_catalyzer");
    public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/jei_catalyzer_screen.png");
    private final IDrawable background;
    private final IDrawable icon;

    public JeiCatalyzerRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonModBlocks.CATLYZER.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<JeiCatalyzerRecipe> getRecipeType() {
        return JeiSuport.JeiCatalyzer_Type;
    }

    @Override
    public Component getTitle() {
        return new TextComponent((new TranslatableComponent("block.changed_addon.catlyzer").getString()));
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
    public Class<? extends JeiCatalyzerRecipe> getRecipeClass() {
        return JeiCatalyzerRecipe.class;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiCatalyzerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 12, 18).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());
    }
}
