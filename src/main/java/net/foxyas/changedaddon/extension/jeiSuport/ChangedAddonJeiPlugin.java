package net.foxyas.changedaddon.extension.jeiSuport;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.*;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.FoxyasInventoryMenuScreen;
import net.foxyas.changedaddon.enchantment.TransfurAspectEnchantment;
import net.foxyas.changedaddon.extension.jeiSuport.guisHandlers.FoxyasGuiContainerHandler;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.foxyas.changedaddon.recipe.special.KeycardColorRecipe;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@JeiPlugin
public class ChangedAddonJeiPlugin implements IModPlugin {

    static final RecipeType<CatalyzerRecipe> CATALYZER_RECIPE_TYPE = new RecipeType<>(CatalyzerRecipeCategory.UID, CatalyzerRecipe.class);
    static final RecipeType<UnifuserRecipe> UNIFUSER_RECIPE_TYPE = new RecipeType<>(UnifuserRecipeCategory.UID, UnifuserRecipe.class);
    static final RecipeType<KeycardColorRecipe> KEYCARD_COLOR_RECIPE_TYPE = new RecipeType<>(KeycardColorRecipeCategory.ID, KeycardColorRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ChangedAddonMod.resourceLoc("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CatalyzerRecipeCategory(guiHelper));
        registration.addRecipeCategories(new UnifuserRecipeCategory(guiHelper));
        registration.addRecipeCategories(new KeycardColorRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registerBrewingRecipes(registration);

        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<CatalyzerRecipe> allCatalyzerRecipes = recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE);
        List<CatalyzerRecipe> publicCatalyzerRecipes = allCatalyzerRecipes.stream().filter((catalyzerRecipe -> !catalyzerRecipe.isHidden())).toList();
        registration.addRecipes(CATALYZER_RECIPE_TYPE, publicCatalyzerRecipes);
        List<UnifuserRecipe> allUnifuserRecipes = recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE);
        List<UnifuserRecipe> publicUnifuserRecipes = allUnifuserRecipes.stream().filter((unifuserRecipe) -> !unifuserRecipe.isHidden()).toList();
        registration.addRecipes(UNIFUSER_RECIPE_TYPE, publicUnifuserRecipes);

        List<CraftingRecipe> recipes = recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING);
        List<KeycardColorRecipe> colorRecipes = new ArrayList<>();

        for (CraftingRecipe recipe : recipes) {
            if (recipe instanceof KeycardColorRecipe keycardColorRecipe) colorRecipes.add(keycardColorRecipe);
        }

        registration.addRecipes(KEYCARD_COLOR_RECIPE_TYPE, colorRecipes);

        //Items Info
        ChangedAddonJeiDescriptionHandler.registerDescriptions(registration);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(UnifuserGuiMenu.class, ChangedAddonMenus.UNIFUSER_MENU.get(), UNIFUSER_RECIPE_TYPE, 36, 3, 0, 35);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonBlocks.CATALYZER.get().asItem()), CATALYZER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonBlocks.ADVANCED_CATALYZER.get().asItem()), CATALYZER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonBlocks.UNIFUSER.get().asItem()), UNIFUSER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ChangedAddonBlocks.ADVANCED_UNIFUSER.get().asItem()), UNIFUSER_RECIPE_TYPE);
    }


    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        ChangedAddonJeiGuiHandler.registerModMenusHandlers(registration);
    }


    private void registerBrewingRecipes(IRecipeRegistration registration) {
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

        // This may be a better way to add Brewing Recipes
    }

    public static class ChangedAddonJeiGuiHandler {
        public static void registerModMenusHandlers(IGuiHandlerRegistration registration) {
            registration.addGuiContainerHandler(FoxyasInventoryMenuScreen.class, new FoxyasGuiContainerHandler());
        }
    }

    public static class ChangedAddonJeiDescriptionHandler {
        public static void registerDescriptions(IRecipeRegistration registration) {
            // Item Information
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.TRANSFUR_TOTEM.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.latex_totem"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.EXPERIMENT_009_DNA.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.exp9_dna"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.litix_cammonia_syringe"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.LAETHIN_SYRINGE.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.laethin_syringe"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.POT_WITH_CAMONIA.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.pot_with_cammonia"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.DIFFUSION_SYRINGE.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.diffusion_syringe"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.IRIDIUM.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.iridium_use"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.INFORMANT_BLOCK.get()), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.informant_block"));
            registration.addIngredientInfo(new ItemStack(ChangedAddonItems.LUNAR_ROSE.get()), VanillaTypes.ITEM_STACK, Component.literal(Component.translatable("jei_descriptions.changed_addon.lunar_rose").getString().replace("#", "\n")));

            ItemStack stack = new ItemStack(ChangedItems.LATEX_SYRINGE.get());
            Syringe.setVariant(stack, ChangedAddonTransfurVariants.LUMINARA_FLOWER_BEAST.get().getFormId());
            registration.addIngredientInfo(stack, VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.luminara.riddle"));

            addSharedDescriptions(registration, List.of(
                    ChangedAddonItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get(),
                    ChangedAddonItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get(),
                    ChangedAddonItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get(),
                    ChangedAddonItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get()
            ), "item.changed_addon.colorful_wolf_crystal_fragment_desc");

            // Enchant Information
            registerLatexSolventDescriptions(registration);
            registerChangedLureDescriptions(registration);
            registerTransfurAspectDescriptions(registration);
            registration.addIngredientInfo(ChangedAddonItems.ALPHA_SERUM_SYRINGE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, Component.translatable("jei_descriptions.changed_addon.alpha_serum_syringe"));
        }

        private static void registerLatexSolventDescriptions(IRecipeRegistration registration) {
            ItemStack enchantedBookWithSolvent = new ItemStack(Items.ENCHANTED_BOOK);
            for (int i = 1; i < 6; i++) { // Começa em 1 para ignorar o nível 0
                float math = LatexSolventMath(i) * 100;
                EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.LATEX_SOLVENT.get(), i), enchantedBookWithSolvent);
                String text = Component.translatable("enchantment.changed_addon.latex_solvent.jei_desc", Math.round(math)).getString().replace(" T ", "% ");
                registration.addIngredientInfo(enchantedBookWithSolvent, VanillaTypes.ITEM_STACK, Component.literal(text));
            }
        }

        private static void registerTransfurAspectDescriptions(IRecipeRegistration registration) {
            ItemStack enchantedBookWithEnchantment = new ItemStack(Items.ENCHANTED_BOOK);
            for (int i = 1; i < 6; i++) { // Começa em 1 para ignorar o nível 0
                float math = TransfurAspectMath(i);
                EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.TRANSFUR_ASPECT.get(), i), enchantedBookWithEnchantment);
                registration.addIngredientInfo(enchantedBookWithEnchantment, VanillaTypes.ITEM_STACK, Component.translatable("enchantment.changed_addon.transfur_aspect.jei_desc", math));
            }
        }

        private static void registerChangedLureDescriptions(IRecipeRegistration registration) {
            ItemStack enchantedBookWithChangedLure = new ItemStack(Items.ENCHANTED_BOOK);
            for (int i = 1; i < 6; i++) { // Começa em 1 para ignorar o nível 0
                EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.CHANGED_LURE.get(), i), enchantedBookWithChangedLure);
                registration.addIngredientInfo(enchantedBookWithChangedLure, VanillaTypes.ITEM_STACK, Component.translatable("enchantment.changed_addon.changed_lure.desc"));
            }
        }

        private static float LatexSolventMath(int EnchantLevel) {
            return (EnchantLevel) * 0.20f;
        }

        private static float TransfurAspectMath(int EnchantLevel) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                return TransfurAspectEnchantment.getTransfurDamage(player, null, EnchantLevel);
            }
            return 0.0f;
        }

        private static void addSharedDescriptions(IRecipeRegistration registration, List<Item> items, String translationKey) {
            items.forEach(item ->
                    registration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK, Component.translatable(translationKey))
            );
        }
    }
}


