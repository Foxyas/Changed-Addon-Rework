package net.foxyas.changedaddon.extension.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.comparison.EntryComparator;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.impl.Internals;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.CatalyzerGuiScreen;
import net.foxyas.changedaddon.client.gui.UnifuserGuiScreen;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonRecipeTypes;
import net.foxyas.changedaddon.menu.CatalyzerGuiMenu;
import net.foxyas.changedaddon.menu.UnifuserGuiMenu;
import net.foxyas.changedaddon.recipe.CatalyzerRecipe;
import net.foxyas.changedaddon.recipe.UnifuserRecipe;
import net.ltxprogrammer.changed.client.gui.InfuserScreen;
import net.ltxprogrammer.changed.client.gui.PurifierScreen;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedRecipeTypes;
import net.ltxprogrammer.changed.recipe.InfuserRecipe;
import net.ltxprogrammer.changed.recipe.PurifierRecipe;
import net.ltxprogrammer.changed.world.inventory.InfuserMenu;
import net.ltxprogrammer.changed.world.inventory.PurifierMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

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
        registry.register(SimpleTransferHandler.create(UnifuserGuiMenu.class, UNIFUSER,
                new SimpleTransferHandler.IntRange(36, 39)));
        registry.register(SimpleTransferHandler.create(CatalyzerGuiMenu.class, CATALYZER,
                new SimpleTransferHandler.IntRange(36, 36)));
    }
}