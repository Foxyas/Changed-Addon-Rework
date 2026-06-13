package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.item.api.IDynamicCreativeTab;
import net.ltxprogrammer.changed.init.ChangedTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static net.foxyas.changedaddon.init.ChangedAddonItems.REGISTRY;
import static net.foxyas.changedaddon.init.ChangedAddonItems.THE_DECIMATOR;

public class ChangedAddonCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ChangedAddonMod.MODID);

    public static final RegistryObject<CreativeModeTab> CHANGED_ADDON_MAIN_TAB = TABS.register("main_tab", () -> CreativeModeTab.builder()
            .icon(() -> ChangedAddonItems.CHANGED_BOOK.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.changed_addon_main_tab"))
            .displayItems((params, items) -> {
                for (RegistryObject<Item> itemRegistryObject : REGISTRY.getEntries()) {
                    if (ChangedAddonItems.getNoTabItems().contains(itemRegistryObject)) continue;
                    if (itemRegistryObject.get() instanceof IDynamicCreativeTab IDynamicCreativeTab) {
                        IDynamicCreativeTab.fillItemCategory(items);
                        continue;
                    }

                    if (!ChangedAddonCreativeTabs.CHANGED_ADDON_OPTIONAL_COMBAT_TAB.get().contains(new ItemStack(itemRegistryObject.get()))) {
                        items.accept(itemRegistryObject.get().getDefaultInstance());
                    }
                }

            }).withTabsBefore(ChangedTabs.TAB_CHANGED_MUSIC.getKey()).withTabsAfter(ChangedAddonCreativeTabs.CHANGED_ADDON_OPTIONAL_COMBAT_TAB.getKey())
            .build());


    public static final RegistryObject<CreativeModeTab> CHANGED_ADDON_OPTIONAL_COMBAT_TAB = TABS.register("optional_combat_tab", () -> CreativeModeTab.builder()
            .icon(() -> ChangedAddonItems.ELECTRIC_KATANA.get().getDefaultInstance())
            .title(Component.translatable("itemGroup.changed_addon_optional_combat_tab"))
            .displayItems((params, items) -> {
                items.accept(THE_DECIMATOR.get());
            }).withTabsBefore(ChangedAddonCreativeTabs.CHANGED_ADDON_MAIN_TAB.getKey()).withTabsBefore(ChangedTabs.TAB_CHANGED_MUSIC.getKey()).build());
}
