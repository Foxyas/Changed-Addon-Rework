package net.foxyas.changedaddon.datagen.patchouli;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemStack;
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
                "Changed Addon Guide",
                "Welcome to the Changed Addon guidebook!"
        );
        book.setUseResourcePack(true);
        book.setI18n(true);

        ItemStack lunarRose = ChangedAddonItems.LUNAR_ROSE.get().getDefaultInstance();
        CategoryBuilder mainCategory = book.addCategory(
                "main",
                "Main",
                "General information about the mod.",
                lunarRose
        );

        mainCategory.addEntry(
                "lunar_rose_poem",
                "Lunar Rose Poem",
                lunarRose
        ).addSimpleTextPage("jei_descriptions.changed_addon.lunar_rose");

        consumer.accept(book);
    }
}