package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class LuminarCrystalShardItem extends Item {
    public LuminarCrystalShardItem() {
        super(new Item.Properties()
                .stacksTo(64).fireResistant().rarity(Rarity.RARE));
    }
}
