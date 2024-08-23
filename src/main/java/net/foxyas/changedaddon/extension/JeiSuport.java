package net.foxyas.changedaddon.extension;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@JeiPlugin
public class JeiSuport implements IModPlugin {
    public static mezz.jei.api.recipe.RecipeType<JeiCatalyzerRecipe> JeiCatalyzer_Type = new mezz.jei.api.recipe.RecipeType<>(JeiCatalyzerRecipeCategory.UID, JeiCatalyzerRecipe.class);
    public static mezz.jei.api.recipe.RecipeType<JeiUnifuserRecipe> JeiUnifuser_Type = new mezz.jei.api.recipe.RecipeType<>(JeiUnifuserRecipeCategory.UID, JeiUnifuserRecipe.class);


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("changed_addon:jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JeiCatalyzerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JeiUnifuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<JeiCatalyzerRecipe> JeiCatalyzerRecipes = recipeManager.getAllRecipesFor(JeiCatalyzerRecipe.Type.INSTANCE);
        registration.addRecipes(JeiCatalyzer_Type, JeiCatalyzerRecipes);
        List<JeiUnifuserRecipe> JeiUnifuserRecipes = recipeManager.getAllRecipesFor(JeiUnifuserRecipe.Type.INSTANCE);
        registration.addRecipes(JeiUnifuser_Type, JeiUnifuserRecipes);

        //Items Info
        JeiDescriptionHandler.registerDescriptions(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.CATLYZER.get().asItem()), JeiCatalyzer_Type);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.UNIFUSER.get().asItem()), JeiUnifuser_Type);
    }
}


class JeiDescriptionHandler {
    public static void registerDescriptions(IRecipeRegistration registration) {
        // Item Information
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.TRANSFUR_TOTEM.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("changed_addon.jei_descriptions.latex_totem"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.EXPERIMENT_009DNA.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("changed_addon.jei_descriptions.exp9_dna"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.SYRINGEWITHLITIXCAMMONIA.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("changed_addon.jei_descriptions.litixcammonia_syringe"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.LAETHIN_SYRINGE.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("changed_addon.jei_descriptions.laethin_syringe"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.POTWITHCAMONIA.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("changed_addon.jei_descriptions.potwithcammonia"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.DIFFUSION_SYRINGE.get()), VanillaTypes.ITEM_STACK, new TranslatableComponent("changed_addon.jei_descriptions.diffusion_syringe"));


        // Enchant Information
        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(Map.of(ChangedAddonModEnchantments.SOLVENT.get(), 5), enchantedBook);
        registration.addIngredientInfo(enchantedBook, VanillaTypes.ITEM_STACK, new TranslatableComponent("enchantment.changed_addon.solvent.desc"));

    }
}

