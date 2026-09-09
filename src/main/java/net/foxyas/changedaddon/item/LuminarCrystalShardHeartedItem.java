package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class LuminarCrystalShardHeartedItem extends Item {
    public LuminarCrystalShardHeartedItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.RARE));
    }
}
