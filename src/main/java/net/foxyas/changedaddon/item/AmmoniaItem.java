package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AmmoniaItem extends Item {
    public AmmoniaItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.COMMON));
    }
}
