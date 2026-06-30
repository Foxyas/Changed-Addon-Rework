package net.foxyas.changedaddon.datagen.patchouli;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonCreativeTabs;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class ModPatchouliBookProvider extends PatchouliBookProvider {

    public ModPatchouliBookProvider(PackOutput packOutput) {
        super(packOutput, ChangedAddonMod.MODID, "en_us");
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {
        BookBuilder book = createBookBuilder(
                "guidebook",
                "§3Changed Addon Guide",
                "Welcome to the Changed Addon guidebook!"
        );
        book.setCustomBookItem(ChangedAddonItems.CHANGED_BOOK.get().getDefaultInstance());
        book.setUseResourcePack(true);
        book.setI18n(true);
        book.setDontGenerateBook(false);
        book.setShowProgress(false);
        book.setShowToasts(true);
        book.setCreativeTab(ChangedAddonCreativeTabs.CHANGED_ADDON_MAIN_TAB.getId().toString());
        book.setModel(ChangedAddonItems.CHANGED_BOOK.getId());
        book.setShowToasts(true);

        ItemStack lunarRose = ChangedAddonItems.LUNAR_ROSE.get().getDefaultInstance();
        ItemStack paper = Items.PAPER.getDefaultInstance();
        CategoryBuilder riddles = book.addCategory(
                "riddles",
                "Changed Addon Riddles",
                "patchouli_descriptions.changed_addon.riddles",
                paper
        );

        riddles.addEntry(
                        "lunar_rose_poem",
                        "Lunar Rose Poem",
                        lunarRose
                )
                .addSimpleTextPage("patchouli_descriptions.changed_addon.lunar_rose");

        consumer.accept(book);
    }
}