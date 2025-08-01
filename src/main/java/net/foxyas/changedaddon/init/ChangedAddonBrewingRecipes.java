package net.foxyas.changedaddon.init;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class ChangedAddonBrewingRecipes implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation("changed_addon:brewing_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
        ItemStack potion = new ItemStack(Items.POTION);
        ItemStack potion2 = new ItemStack(Items.POTION);
        PotionUtils.setPotion(potion, Potions.AWKWARD);
        PotionUtils.setPotion(potion2, ChangedAddonPotions.LITIX_CAMMONIA_EFFECT.get());
        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get())), potion.copy(), potion2.copy()));
        PotionUtils.setPotion(potion, Potions.AWKWARD);
        PotionUtils.setPotion(potion2, ChangedAddonPotions.TRANSFUR_SICKNESS_POTION.get());
        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(ChangedAddonItems.LAETHIN.get())), potion.copy(), potion2.copy()));
        registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
    }
}
