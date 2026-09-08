package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class PainiteGemItem extends Item {
    public PainiteGemItem() {
        super(new Item.Properties()
                .stacksTo(64).fireResistant().rarity(Rarity.RARE));
    }
}
