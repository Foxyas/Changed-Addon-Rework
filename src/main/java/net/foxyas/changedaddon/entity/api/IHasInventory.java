package net.foxyas.changedaddon.entity.api;

import net.minecraft.world.Container;

public interface IHasInventory<T extends Container> {

    T getInventory();
}
