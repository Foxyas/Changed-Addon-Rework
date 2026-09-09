package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CatalyzedDNAItem extends Item {
    public CatalyzedDNAItem() {
        super(new Item.Properties()
                .stacksTo(1).rarity(Rarity.RARE));
    }
}
