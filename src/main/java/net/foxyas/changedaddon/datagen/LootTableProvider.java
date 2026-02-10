package net.foxyas.changedaddon.datagen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableProvider extends net.minecraft.data.loot.LootTableProvider {

    public LootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker) {
        map.forEach((p_218436_2_, p_218436_3_) ->
                LootTables.validate(validationtracker, p_218436_2_, p_218436_3_));
    }

    @Override
    protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return List.of(
                Pair.of(BlockLoot::new, LootContextParamSets.BLOCK),
                Pair.of(EntityLoot::new, LootContextParamSets.ENTITY),
                Pair.of(ChestLoot::new, LootContextParamSets.CHEST)
        );
    }
}
