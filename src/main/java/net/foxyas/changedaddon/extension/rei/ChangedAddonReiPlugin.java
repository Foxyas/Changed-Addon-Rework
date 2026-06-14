package net.foxyas.changedaddon.extension.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.CatalyzerGuiScreen;
import net.foxyas.changedaddon.client.gui.UnifuserGuiScreen;
import net.foxyas.changedaddon.enchantment.TransfurAspectEnchantment;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.foxyas.changedaddon.extension.rei.DefaultInformationDisplay.INFO;

@REIPluginClient
public class ChangedAddonReiPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<UnifuserRecipeDisplay> UNIFUSER = CategoryIdentifier.of(ChangedAddonMod.MODID, "plugins/unifuser");
    public static final CategoryIdentifier<CatalyzerRecipeDisplay> CATALYZER = CategoryIdentifier.of(ChangedAddonMod.MODID, "plugins/catalyzer");

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
//        EntryComparator<Tag> nbtHasher = EntryComparator.nbt();
//        Function<ItemStack, Tag> ownerTag = stack -> {
//            CompoundTag tag = stack.getTag();
//            if (tag == null) return null;
//            if (tag.contains("owner")) tag.remove("owner");
//            return tag;
//        };
//        registry.register((context, itemStack) -> nbtHasher.hash(context, ownerTag.apply(itemStack)),
//                ChangedItems.LATEX_SYRINGE.get(),
//                ChangedItems.LATEX_FLASK.get(),
//                ChangedItems.BLOOD_SYRINGE.get());
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(75, 34, 29, 18), UnifuserGuiScreen.class, UNIFUSER);
        registry.registerContainerClickArea(new Rectangle(75, 43, 29, 18), CatalyzerGuiScreen.class, CATALYZER);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new UnifuserRecipeCategory());
        registry.add(new CatalyzerRecipeCategory());
        registry.add(new DefaultInformationCategory());
        registry.addWorkstations(UNIFUSER, EntryStacks.of(ChangedAddonBlocks.UNIFUSER.get()));
        registry.addWorkstations(CATALYZER, EntryStacks.of(ChangedAddonBlocks.CATALYZER.get()));
        registry.addWorkstations(UNIFUSER, EntryStacks.of(ChangedAddonBlocks.ADVANCED_UNIFUSER.get()));
        registry.addWorkstations(CATALYZER, EntryStacks.of(ChangedAddonBlocks.ADVANCED_CATALYZER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        // Filtra apenas as receitas que NÃO estão escondidas para o Unifuser
        registry.registerFiller(UnifuserRecipe.class,
                recipe -> !recipe.isHidden(), // Predicado de filtro
                UnifuserRecipeDisplay::new);

        // Filtra apenas as receitas que NÃO estão escondidas para o Catalyzer
        registry.registerFiller(CatalyzerRecipe.class,
                recipe -> !recipe.isHidden(), // Predicado de filtro
                CatalyzerRecipeDisplay::new);

        ChangedAddonREIDescriptionHandler.registerDescriptions(registry);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        REIClientPlugin.super.registerDisplaySerializer(registry);
        registry.register(INFO, DefaultInformationDisplay.serializer());
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(getUnifuserHandle());
        registry.register(getCatalyzerHandle());
//        registry.register(SimpleTransferHandler.create(
//                UnifuserGuiMenu.class,
//                UNIFUSER,
//                new SimpleTransferHandler.IntRange(36, 3) // Input: Início no 36, tamanho 3
//        ));
//        registry.register(SimpleTransferHandler.create(
//                CatalyzerGuiMenu.class,
//                CATALYZER,
//                new SimpleTransferHandler.IntRange(36, 1) // Input: Início no 36, tamanho 1
//        ));
    }

    private @NotNull SimpleTransferHandler getCatalyzerHandle() {
        return new SimpleTransferHandler() {
            @Override
            public ApplicabilityResult checkApplicable(Context context) {
                return context.getMenu() instanceof CatalyzerGuiMenu && context.getDisplay().getCategoryIdentifier().equals(CATALYZER) && context.getContainerScreen() != null ? ApplicabilityResult.createApplicable() : ApplicabilityResult.createNotApplicable();
            }

            @Override
            public Iterable<SlotAccessor> getInputSlots(Context context) {
                if (context.getMenu() instanceof CatalyzerGuiMenu menu) {
                    return List.of(SlotAccessor.fromSlot(menu.getLeftSlot()));
                }
                return List.of();
            }

            @Override
            public Iterable<SlotAccessor> getInventorySlots(Context context) {
                if (context.getMenu() instanceof CatalyzerGuiMenu menu) {
                    int maxInv = menu.getLeftSlot().index - 1;
                    return IntStream.range(0, maxInv)
                            .mapToObj(index -> SlotAccessor.fromSlot(menu.getSlot(index)))
                            .collect(Collectors.toList());
                }
                return List.of();
            }
        };
    }

    private @NotNull SimpleTransferHandler getUnifuserHandle() {
        return new SimpleTransferHandler() {
            @Override
            public ApplicabilityResult checkApplicable(Context context) {
                return context.getMenu() instanceof UnifuserGuiMenu && context.getDisplay().getCategoryIdentifier().equals(UNIFUSER) && context.getContainerScreen() != null ? ApplicabilityResult.createApplicable() : ApplicabilityResult.createNotApplicable();
            }

            @Override
            public Iterable<SlotAccessor> getInputSlots(Context context) {
                if (context.getMenu() instanceof UnifuserGuiMenu menu) {
                    int minInput = menu.getTopSlot().index;
                    int maxInput = menu.getOutputSlot().index - 1;
                    return IntStream.range(minInput, maxInput)
                            .mapToObj(index -> SlotAccessor.fromSlot(menu.getSlot(index)))
                            .collect(Collectors.toList());
//                    return List.of(
//                            new VanillaSlotAccessor(menu.getTopSlot()),
//                            new VanillaSlotAccessor(menu.getBottomSlot()),
//                            new VanillaSlotAccessor(menu.getSyringeSlot())
//                    );
                }
                return List.of();
            }

            @Override
            public Iterable<SlotAccessor> getInventorySlots(Context context) {
                if (context.getMenu() instanceof UnifuserGuiMenu menu) {
                    int maxInv = menu.getTopSlot().index - 1;
                    return IntStream.range(0, maxInv)
                            .mapToObj(index -> SlotAccessor.fromSlot(menu.getSlot(index)))
                            .collect(Collectors.toList());
                }
                return List.of();
            }
        };
    }

    public static class ChangedAddonREIDescriptionHandler {

        public static void registerDescriptions(DisplayRegistry registration) {
            // Informações de Itens Comuns
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.TRANSFUR_TOTEM.get()), Component.translatable("jei_descriptions.changed_addon.latex_totem"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.EXPERIMENT_009_DNA.get()), Component.translatable("jei_descriptions.changed_addon.exp9_dna"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get()), Component.translatable("jei_descriptions.changed_addon.litix_cammonia_syringe"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.LAETHIN_SYRINGE.get()), Component.translatable("jei_descriptions.changed_addon.laethin_syringe"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.POT_WITH_CAMONIA.get()), Component.translatable("jei_descriptions.changed_addon.pot_with_cammonia"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.DIFFUSION_SYRINGE.get()), Component.translatable("jei_descriptions.changed_addon.diffusion_syringe"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.IRIDIUM.get()), Component.translatable("jei_descriptions.changed_addon.iridium_use"));
            addIngredientInfo(registration, new ItemStack(ChangedAddonItems.INFORMANT_BLOCK.get()), Component.translatable("jei_descriptions.changed_addon.informant_block"));
            addIngredientInfo(registration, ChangedAddonItems.ALPHA_SERUM_SYRINGE.get().getDefaultInstance(), Component.translatable("jei_descriptions.changed_addon.alpha_serum_syringe"));

            // Tratamento especial para quebra de linhas da Lunar Rose baseada no caractere '#'
            String rawLunarRoseText = Component.translatable("jei_descriptions.changed_addon.lunar_rose").getString().replace("#", "\n");
            List<MutableComponent> lunarRoseLines = Arrays.stream(rawLunarRoseText.split("\n")).map(Component::literal).toList();
            DefaultInformationDisplay lunarDisplay = DefaultInformationDisplay.createFromEntry(EntryStacks.of(ChangedAddonItems.LUNAR_ROSE.get()), Component.translatable("item.changed_addon.lunar_rose"));
            lunarDisplay.lines(lunarRoseLines);
            registration.add(lunarDisplay);

            // Seringa com NBT específica
            ItemStack syringeStack = new ItemStack(ChangedItems.LATEX_SYRINGE.get());
            Syringe.setVariant(syringeStack, ChangedAddonTransfurVariants.LUMINARA_FLOWER_BEAST.get().getFormId());
            addIngredientInfo(registration, syringeStack, Component.translatable("jei_descriptions.changed_addon.luminara.riddle"));

            addSharedDescriptions(registration, List.of(
                    ChangedAddonItems.BLUE_WOLF_CRYSTAL_FRAGMENT.get(),
                    ChangedAddonItems.ORANGE_WOLF_CRYSTAL_FRAGMENT.get(),
                    ChangedAddonItems.YELLOW_WOLF_CRYSTAL_FRAGMENT.get(),
                    ChangedAddonItems.WHITE_WOLF_CRYSTAL_FRAGMENT.get()
            ), "item.changed_addon.colorful_wolf_crystal_fragment", "item.changed_addon.colorful_wolf_crystal_fragment_desc");

            // Registro dos Encantamentos por Nível
            registerLatexSolventDescriptions(registration);
            registerChangedLureDescriptions(registration);
            registerTransfurAspectDescriptions(registration);
        }

        private static void addIngredientInfo(DisplayRegistry registration, ItemStack stack, Component description) {
            DefaultInformationDisplay display = DefaultInformationDisplay.createFromEntry(EntryStacks.of(stack), stack.getHoverName());
            display.line(description);
            registration.add(display);
        }

        private static void registerLatexSolventDescriptions(DisplayRegistry registration) {
            for (int i = 1; i < 6; i++) {
                ItemStack enchantedBookWithSolvent = new ItemStack(Items.ENCHANTED_BOOK);
                float math = LatexSolventMath(i) * 100;
                EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.LATEX_SOLVENT.get(), i), enchantedBookWithSolvent);
                String text = Component.translatable("enchantment.changed_addon.latex_solvent.jei_desc", Math.round(math)).getString().replace(" T ", "% ");

                addIngredientInfo(registration, enchantedBookWithSolvent, Component.literal(text));
            }
        }

        private static void registerTransfurAspectDescriptions(DisplayRegistry registration) {
            for (int i = 1; i < 6; i++) {
                ItemStack enchantedBookWithEnchantment = new ItemStack(Items.ENCHANTED_BOOK);
                float math = TransfurAspectMath(i);
                EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.TRANSFUR_ASPECT.get(), i), enchantedBookWithEnchantment);

                addIngredientInfo(registration, enchantedBookWithEnchantment, Component.translatable("enchantment.changed_addon.transfur_aspect.jei_desc", math));
            }
        }

        private static void registerChangedLureDescriptions(DisplayRegistry registration) {
            for (int i = 1; i < 6; i++) {
                ItemStack enchantedBookWithChangedLure = new ItemStack(Items.ENCHANTED_BOOK);
                EnchantmentHelper.setEnchantments(Map.of(ChangedAddonEnchantments.CHANGED_LURE.get(), i), enchantedBookWithChangedLure);

                addIngredientInfo(registration, enchantedBookWithChangedLure, Component.translatable("enchantment.changed_addon.changed_lure.desc"));
            }
        }

        private static float LatexSolventMath(int EnchantLevel) {
            return EnchantLevel * 0.20f;
        }

        private static float TransfurAspectMath(int EnchantLevel) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                return TransfurAspectEnchantment.getTransfurDamage(player, null, EnchantLevel);
            }
            return 0.0f;
        }

        private static void addSharedDescriptions(DisplayRegistry registration, List<Item> items, String titleTranslationKey, String translationKey) {
            List<ItemStack> stacks = items.stream().map(ItemStack::new).toList();
            EntryIngredient ingredient = EntryIngredient.of(stacks.stream().map(EntryStacks::of).toList());

            // Título genérico para a aba de informações compartilhada dos fragmentos
            Component title = Component.translatable(titleTranslationKey);
            DefaultInformationDisplay display = DefaultInformationDisplay.createFromEntries(ingredient, title);
            display.line(Component.translatable(translationKey));
            registration.add(display);
        }
    }
}