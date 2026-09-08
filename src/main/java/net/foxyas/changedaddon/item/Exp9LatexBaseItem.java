package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class Exp9LatexBaseItem extends Item {
    public Exp9LatexBaseItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.RARE));
    }
}
