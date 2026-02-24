package net.foxyas.changedaddon.item.api;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

public interface IBestiaryItemData {
    EntityType<?> getEntityTypeReference();

    default boolean shouldBeConsidered(ItemStack itemStack) {
        return true;
    }
}
