package net.foxyas.changedaddon.extension.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.CatalyzerGuiScreen;
import net.foxyas.changedaddon.client.gui.UnifuserGuiScreen;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@REIPluginClient
public class ChangedAddonReiPlugin implements REIClientPlugin {
    public static final CategoryIdentifier<UnifuserRecipeDisplay> UNIFUSER = CategoryIdentifier.of(ChangedAddonMod.MODID, "plugins/unifuser");
    public static final CategoryIdentifier<CatalyzerRecipeDisplay> CATALYZER = CategoryIdentifier.of(ChangedAddonMod.MODID, "plugins/catalyzer");

    @Override
    public void registerItemComparators(ItemComparatorRegistry registry) {
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(103, 33, 22, 15), UnifuserGuiScreen.class, UNIFUSER);
        registry.registerContainerClickArea(new Rectangle(103, 33, 22, 15), CatalyzerGuiScreen.class, CATALYZER);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new UnifuserRecipeCategory());
        registry.add(new CatalyzerRecipeCategory());
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
}