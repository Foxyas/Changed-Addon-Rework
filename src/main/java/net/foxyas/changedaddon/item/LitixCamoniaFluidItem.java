package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class LitixCamoniaFluidItem extends BucketItem {
    public LitixCamoniaFluidItem() {
        super(ChangedAddonFluids.LITIX_CAMONIA_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.COMMON)//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
        );
    }
}
