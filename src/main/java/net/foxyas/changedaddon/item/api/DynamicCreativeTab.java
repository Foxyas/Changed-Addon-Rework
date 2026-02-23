package net.foxyas.changedaddon.item.api;

import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.NotNull;

public interface DynamicCreativeTab {

    default void fillItemCategory(@NotNull CreativeModeTab.Output tab) {
    }

}
