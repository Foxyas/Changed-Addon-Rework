package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AmmoniaCompressedItem extends Item {

    public AmmoniaCompressedItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.COMMON));
    }
}
