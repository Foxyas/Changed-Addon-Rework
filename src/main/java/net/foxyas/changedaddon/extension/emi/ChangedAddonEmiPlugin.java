package net.foxyas.changedaddon.extension.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonMenus;
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
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static net.foxyas.changedaddon.extension.emi.CARecipesCategories.*;

@EmiEntrypoint
public class ChangedAddonEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        // 1. Registrar Categorias
        registry.addCategory(CATALYZER_CATEGORY);
        registry.addCategory(UNIFUSER_CATEGORY);
        registry.addCategory(KEYCARD_COLOR_CATEGORY);

        // 2. Registrar Workstations (Catalisadores)
        registry.addWorkstation(CATALYZER_CATEGORY, EmiStack.of(ChangedAddonBlocks.CATALYZER.get()));
        registry.addWorkstation(CATALYZER_CATEGORY, EmiStack.of(ChangedAddonBlocks.ADVANCED_CATALYZER.get()));
        registry.addWorkstation(UNIFUSER_CATEGORY, EmiStack.of(ChangedAddonBlocks.UNIFUSER.get()));
        registry.addWorkstation(UNIFUSER_CATEGORY, EmiStack.of(ChangedAddonBlocks.ADVANCED_UNIFUSER.get()));

        // 3. Registrar as Receitas customizadas
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        // Catalyzer
        recipeManager.getAllRecipesFor(CatalyzerRecipe.Type.INSTANCE).stream()
                .filter(recipe -> !recipe.isHidden())
                .forEach(recipe -> registry.addRecipe(new EmiCatalyzerRecipe(recipe)));

        // Unifuser
        recipeManager.getAllRecipesFor(UnifuserRecipe.Type.INSTANCE).stream()
                .filter(recipe -> !recipe.isHidden())
                .forEach(recipe -> registry.addRecipe(new EmiUnifuserRecipe(recipe)));

        // Keycard Color
        List<CraftingRecipe> craftingRecipes = recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING);
        for (CraftingRecipe recipe : craftingRecipes) {
            if (recipe instanceof KeycardColorRecipe colorRecipe) {
                registry.addRecipe(new EmiKeycardColorRecipe(colorRecipe));
            }
        }

        registry.addRecipeHandler(ChangedAddonMenus.UNIFUSER_MENU.get(), new UnifuserRecipeHandler());
        registry.addRecipeHandler(ChangedAddonMenus.CATALYZER_MENU.get(), new CatalyzerRecipeHandler());

        // 4. Registrar Receitas de Brewing (Poções)
        registerBrewingRecipes(registry);

        // 5. Adicionar Descrições de Itens (Information)
        registerDescriptions(registry);
    }

    private void registerBrewingRecipes(EmiRegistry registry) {
        // O EMI lida com poções de forma nativa ou você pode criar EmiBrewingRecipe se necessário.
        // Como o vanilla do EMI já puxa do BrewingRecipeRegistry automaticamente, receitas que usam
        // o PotionBrewing.addMix padrão do vanilla costumam aparecer sozinhas.
        // Se precisar forçar manualmente via EMI, avise que criamos uma classe simples pra isso!
    }

    private void registerDescriptions(EmiRegistry registry) {
        addItemDesc(registry, ChangedAddonItems.TRANSFUR_TOTEM.get(), "jei_descriptions.changed_addon.latex_totem");
        addItemDesc(registry, ChangedAddonItems.EXPERIMENT_009_DNA.get(), "jei_descriptions.changed_addon.exp9_dna");
        addItemDesc(registry, ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get(), "jei_descriptions.changed_addon.litix_cammonia_syringe");
        addItemDesc(registry, ChangedAddonItems.LAETHIN_SYRINGE.get(), "jei_descriptions.changed_addon.laethin_syringe");
        addItemDesc(registry, ChangedAddonItems.POT_WITH_CAMONIA.get(), "jei_descriptions.changed_addon.pot_with_cammonia");
        addItemDesc(registry, ChangedAddonItems.DIFFUSION_SYRINGE.get(), "jei_descriptions.changed_addon.diffusion_syringe");
        addItemDesc(registry, ChangedAddonItems.IRIDIUM.get(), "jei_descriptions.changed_addon.iridium_use");
        addItemDesc(registry, ChangedAddonItems.INFORMANT_BLOCK.get(), "jei_descriptions.changed_addon.informant_block");

        // Lunar Rose substituição de caractere
        String rawLunarRoseText = Component.translatable("jei_descriptions.changed_addon.lunar_rose").getString().replace("#", "\n");
        List<Component> lunarRoseLines = Arrays.stream(rawLunarRoseText.split("\n")).map(Component::literal).map(Component.class::cast).toList();
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(ChangedAddonItems.LUNAR_ROSE.get())), lunarRoseLines, getInfoSyntheticIdFromItem(ChangedAddonItems.LUNAR_ROSE.get())));

        // Seringa com Variant específica
        ItemStack syringeStack = new ItemStack(ChangedItems.LATEX_SYRINGE.get());
        Syringe.setVariant(syringeStack, ChangedAddonTransfurVariants.LUMINARA_FLOWER_BEAST.get().getFormId());
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(syringeStack)), List.of(Component.translatable("jei_descriptions.changed_addon.luminara.riddle")), ChangedAddonMod.resourceLoc("info/luminara_beast_awakening")));

        // Fragmentos compartilhados
        List<Item> fragments = List.of(
                ChangedAddonItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get(),
                ChangedAddonItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get(),
                ChangedAddonItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get(),
                ChangedAddonItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get()
        );
        fragments.forEach(item -> addItemDesc(registry, item, "item.changed_addon.colorful_wolf_crystal_fragment_desc"));

        // Encantamentos
        registerEnchantmentDescriptions(registry);

        addItemDesc(registry, ChangedAddonItems.ALPHA_SERUM_SYRINGE.get(), "jei_descriptions.changed_addon.alpha_serum_syringe");
    }


    public static ResourceLocation getItemIdFromStack(Item stack) {
        return ForgeRegistries.ITEMS.getKey(stack);
    }

    private static @NotNull ResourceLocation getInfoIdFromItem(Item item) {
        return ResourceLocation.fromNamespaceAndPath(getItemIdFromStack(item).getNamespace(), "info/" + getItemIdFromStack(item).getPath());
    }

    private static @NotNull ResourceLocation getInfoSyntheticIdFromItem(Item item) {
        return ResourceLocation.fromNamespaceAndPath(getItemIdFromStack(item).getNamespace(), "/info/" + getItemIdFromStack(item).getPath());
    }

    private void addItemDesc(EmiRegistry registry, Item item, String translationKey) {
        ResourceLocation id = getInfoSyntheticIdFromItem(item);
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(item)), List.of(Component.translatable(translationKey)), id));
    }


    private void addItemDesc(EmiRegistry registry, Item item, List<Component> decs) {
        ResourceLocation id = getInfoSyntheticIdFromItem(item);
        registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(item)), decs, id));
    }

    private void registerEnchantmentDescriptions(EmiRegistry registry) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);

        // Latex Solvent
        for (int i = 1; i <= 5; i++) {
            ItemStack currentBook = book.copy();
            float math = i * 0.20f * 100;
            EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.LATEX_SOLVENT.get(), i), currentBook);
            String text = Component.translatable("enchantment.changed_addon.latex_solvent.jei_desc", Math.round(math)).getString().replace(" T ", "% ");
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(currentBook)), List.of(Component.literal(text)), getInfoSyntheticIdFromItem(currentBook.getItem())));
        }

        // Transfur Aspect
        for (int i = 1; i <= 5; i++) {
            ItemStack currentBook = book.copy();
            EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.TRANSFUR_ASPECT.get(), i), currentBook);
            LocalPlayer player = Minecraft.getInstance().player;
            float math = player != null ? net.foxyas.changedaddon.enchantment.TransfurAspectEnchantment.getTransfurDamage(player, null, i) : 0.0f;
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(currentBook)), List.of(Component.translatable("enchantment.changed_addon.transfur_aspect.jei_desc", math)), getInfoSyntheticIdFromItem(currentBook.getItem())));
        }

        // Changed Lure
        for (int i = 1; i <= 5; i++) {
            ItemStack currentBook = book.copy();
            EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.CHANGED_LURE.get(), i), currentBook);
            registry.addRecipe(new EmiInfoRecipe(List.of(EmiStack.of(currentBook)), List.of(Component.translatable("enchantment.changed_addon.changed_lure.desc")), getInfoSyntheticIdFromItem(currentBook.getItem())));
        }
    }
}