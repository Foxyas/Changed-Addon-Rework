package net.foxyas.changedaddon.item;

import net.minecraft.world.food.Foods;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GoldenOrange extends Item {
    public GoldenOrange() {
        super(new Item.Properties()
                .tab(CreativeModeTab.TAB_FOOD)
                .rarity(Rarity.RARE)
                .food(Foods.GOLDEN_APPLE)
        );
    }
}
