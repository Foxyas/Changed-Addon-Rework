package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;

public class MeaninglessStrafeMusicDiscItem extends RecordItem {
    public MeaninglessStrafeMusicDiscItem() {
        super(15, ChangedAddonSoundEvents.EXP9_THEME, new Item.Properties()
                .stacksTo(1).rarity(Rarity.RARE), 20 * 120);
    }
}
