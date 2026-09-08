package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class RawIridiumItem extends Item {
    public RawIridiumItem() {
        super(new Item.Properties()
                .stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON));
    }
}
