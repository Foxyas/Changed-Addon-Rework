package net.foxyas.changedaddon.extension.jeiSuport;

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
public class JeiUnifuserRecipeCategory implements IRecipeCategory<JeiUnifuserRecipe> {
    public final static ResourceLocation UID = new ResourceLocation("changed_addon", "jei_unifuser");
    public final static ResourceLocation TEXTURE = new ResourceLocation("changed_addon", "textures/screens/jei_unifuser_screen.png");
    private final IDrawable background;
    private final IDrawable icon;

    public JeiUnifuserRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ChangedAddonModBlocks.UNIFUSER.get().asItem()));
    }

    @Override
    public mezz.jei.api.recipe.RecipeType<JeiUnifuserRecipe> getRecipeType() {
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
    public Class<? extends JeiUnifuserRecipe> getRecipeClass() {
        return JeiUnifuserRecipe.class;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiUnifuserRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 18).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 37, 18).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 36).addIngredients(recipe.getIngredients().get(1));
    }
}
