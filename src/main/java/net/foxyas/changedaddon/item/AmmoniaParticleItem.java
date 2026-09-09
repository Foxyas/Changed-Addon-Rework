package net.foxyas.changedaddon.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class AmmoniaParticleItem extends Item {
    public AmmoniaParticleItem() {
        super(new Item.Properties()
                .stacksTo(64).rarity(Rarity.COMMON));
    }
}
