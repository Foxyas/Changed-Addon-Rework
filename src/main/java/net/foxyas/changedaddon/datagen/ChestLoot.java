package net.foxyas.changedaddon.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class ChestLoot extends net.minecraft.data.loot.ChestLoot {
    @Override
    public void accept(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> pOutput) {
        super.accept(pOutput);
    }
}
