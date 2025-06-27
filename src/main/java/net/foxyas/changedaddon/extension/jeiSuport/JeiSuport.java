package net.foxyas.changedaddon.extension.jeiSuport;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.recipes.CatalyzerRecipe;
import net.foxyas.changedaddon.recipes.UnifuserRecipe;
import net.minecraft.client.Minecraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@JeiPlugin
public class JeiSuport implements IModPlugin {
    public static mezz.jei.api.recipe.RecipeType<CatalyzerRecipe> JeiCatalyzer_Type = new mezz.jei.api.recipe.RecipeType<>(JeiCatalyzerRecipeCategory.UID, CatalyzerRecipe.class);
    public static mezz.jei.api.recipe.RecipeType<UnifuserRecipe> JeiUnifuser_Type = new mezz.jei.api.recipe.RecipeType<>(JeiUnifuserRecipeCategory.UID, UnifuserRecipe.class);


    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.parse("changed_addon:jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JeiCatalyzerRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new JeiUnifuserRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level()).getRecipeManager();
        List<CatalyzerRecipe> catalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);
        registration.addRecipes(JeiCatalyzer_Type, catalyzerRecipes);
        List<UnifuserRecipe> unifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);
        registration.addRecipes(JeiUnifuser_Type, unifuserRecipes);

        //Items Info
        JeiDescriptionHandler.registerDescriptions(registration);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.CATLYZER.get().asItem()), JeiCatalyzer_Type);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.ADVANCED_CATALYZER.get().asItem()), JeiCatalyzer_Type);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.UNIFUSER.get().asItem()), JeiUnifuser_Type);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonModBlocks.ADVANCED_UNIFUSER.get().asItem()), JeiUnifuser_Type);
    }
}


class JeiDescriptionHandler {
    public static void registerDescriptions(IRecipeRegistration registration) {
        // Item Information
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.TRANSFUR_TOTEM.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.latex_totem"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.EXPERIMENT_009DNA.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.exp9_dna"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.SYRINGEWITHLITIXCAMMONIA.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.litixcammonia_syringe"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.LAETHIN_SYRINGE.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.laethin_syringe"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.POTWITHCAMONIA.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.potwithcammonia"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.DIFFUSION_SYRINGE.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.diffusion_syringe"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.IRIDIUM.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.iridium_use"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.INFORMANTBLOCK.get()), VanillaTypes.ITEM_STACK, Component.translatable("changed_addon.jei_descriptions.informantblock"));
        registration.addIngredientInfo(new ItemStack(ChangedAddonModItems.LUNARROSE_HELMET.get()), VanillaTypes.ITEM_STACK, new TextComponent(Component.translatable("changed_addon.jei_descriptions.lunar_rose").getString().replace("#","\n")));


        addSharedDescriptions(registration, List.of(
                ChangedAddonModItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get(),
                ChangedAddonModItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get(),
                ChangedAddonModItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get(),
                ChangedAddonModItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get()
        ), "item.changed_addon.colorful_wolf_crystal_fragment_desc");

        // Enchant Information
        registerSolventDescriptions(registration);
        registerChangedLureDescriptions(registration);
    }

    private static void registerSolventDescriptions(IRecipeRegistration registration) {
        ItemStack enchantedBookWithSolvent = new ItemStack(Items.ENCHANTED_BOOK);
        for (int i = 1; i < 6; i++) { // Começa em 1 para ignorar o nível 0
            float math = SolventMath(i) * 100;
            EnchantmentHelper.setEnchantments(Map.of(ChangedAddonModEnchantments.SOLVENT.get(), i), enchantedBookWithSolvent);
            String text = Component.translatable("enchantment.changed_addon.solvent.jei_desc", math).getString().replace(" T ","% ");
            registration.addIngredientInfo(enchantedBookWithSolvent, VanillaTypes.ITEM_STACK, new TextComponent(text));
        }
    }

    private static void registerChangedLureDescriptions(IRecipeRegistration registration) {
        ItemStack enchantedBookWithChangedLure = new ItemStack(Items.ENCHANTED_BOOK);
        for (int i = 1; i < 6; i++) { // Começa em 1 para ignorar o nível 0
            EnchantmentHelper.setEnchantments(Map.of(ChangedAddonModEnchantments.CHANGED_LURE.get(), i), enchantedBookWithChangedLure);
            registration.addIngredientInfo(enchantedBookWithChangedLure, VanillaTypes.ITEM_STACK, Component.translatable("enchantment.changed_addon.changed_lure.desc"));
        }
    }

    private static float SolventMath(float EnchantLevel) {
        /*if (EnchantLevel == 1) {
            return 1.5f;
        } else if (EnchantLevel == 0) {
            return 1f;
        } else if (EnchantLevel == 2) {
            return 3f;
        } else if (EnchantLevel == 3) {
            return 4f;
        } else if (EnchantLevel == 4) {
            return 4.5f;
        } else if (EnchantLevel == 5) {
            return 5f;
        } else {
            return EnchantLevel - 0.5f;
        }*/
        return 1.0f + (EnchantLevel) * 0.20f;
    }

    private static void addSharedDescriptions(IRecipeRegistration registration, List<Item> items, String translationKey) {
        items.forEach(item ->
                registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK, Component.translatable(translationKey))
        );
    }
}