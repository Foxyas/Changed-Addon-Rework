package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class SyringeItem extends Item {
    public SyringeItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.COMMON));
    }
}
