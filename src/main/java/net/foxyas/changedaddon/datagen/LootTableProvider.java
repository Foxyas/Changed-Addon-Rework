package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.datagen.loot.BlockLoot;
import net.foxyas.changedaddon.datagen.loot.ChestLoot;
import net.foxyas.changedaddon.datagen.loot.EntityLoot;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class LootTableProvider extends net.minecraft.data.loot.LootTableProvider {

    public LootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(EntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(ChestLoot::new, LootContextParamSets.CHEST)
        ));
    }
}
