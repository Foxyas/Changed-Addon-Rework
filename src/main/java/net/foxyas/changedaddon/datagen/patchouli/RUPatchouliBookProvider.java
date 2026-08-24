package net.foxyas.changedaddon.datagen.patchouli;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonCreativeTabs;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.patchouliprovider.BookBuilder;
import xyz.brassgoggledcoders.patchouliprovider.CategoryBuilder;
import xyz.brassgoggledcoders.patchouliprovider.PatchouliBookProvider;

import java.util.function.Consumer;

public class RUPatchouliBookProvider extends PatchouliBookProvider {

    protected final String locate;

    public RUPatchouliBookProvider(PackOutput packOutput) {
        super(packOutput, ChangedAddonMod.MODID, "ru_ru");
        this.locate = "ru_ru";
    }

    @Override
    public @NotNull String getName() {
        return super.getName() + ":" + locate;
    }

    @Override
    protected void addBooks(Consumer<BookBuilder> consumer) {

        // MAIN BOOK START \\
        BookBuilder book = createBookBuilder(
                "guide_book",
                "§bChanged Addon Guide",
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
        // MAIN BOOK END \\

        // RIDDLES CATEGORY START \\
        ItemStack lunarRose = ChangedAddonItems.LUNAR_ROSE.get().getDefaultInstance();
        ItemStack paper = Items.PAPER.getDefaultInstance();
        CategoryBuilder riddles = book.addCategory(
                "riddles",
                "patchouli.title.changed_addon.riddles",
                "patchouli.descriptions.changed_addon.riddles",
                paper
        );

        riddles.addEntry(
                        "lunar_rose_poem",
                        "patchouli.title.changed_addon.lunar_rose_poem",
                        lunarRose
                )
                .addSimpleTextPage("patchouli.descriptions.changed_addon.lunar_rose.page1")
                .addSimpleTextPage("patchouli.descriptions.changed_addon.lunar_rose.page2");
        // RIDDLES CATEGORY END \\

        consumer.accept(book);
    }
}