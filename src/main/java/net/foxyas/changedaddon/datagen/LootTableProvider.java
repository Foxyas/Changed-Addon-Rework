package net.foxyas.changedaddon.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableProvider extends net.minecraft.data.loot.LootTableProvider {

    public LootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of();
    }
}
