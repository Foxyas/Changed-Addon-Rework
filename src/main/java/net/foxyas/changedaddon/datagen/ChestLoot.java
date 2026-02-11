package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class ChestLoot implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> pOutput) {

        pOutput.accept(
                ChangedAddonMod.resourceLoc("chests/low_tier_archives"),
                lowBooksLoot()
        );

        pOutput.accept(
                ChangedAddonMod.resourceLoc("chests/mid_tier_archives"),
                midBooksLoot()
        );

        pOutput.accept(
                ChangedAddonMod.resourceLoc("chests/high_tier_archives"),
                highBooksLoot()
        );
    }

    private static LootTable.Builder lowBooksLoot() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(3))

                        // Papel (comum)
                        .add(LootItem.lootTableItem(Items.PAPER)
                                .setWeight(8)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(2, 6))))

                        // Livro normal
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(5)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1, 3))))

                        // Livro encantado fraco (raro)
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(2)
                                .apply(EnchantWithLevelsFunction.enchantWithLevels(
                                        UniformGenerator.between(5, 10)
                                ).allowTreasure()))
                );
    }

    private static LootTable.Builder midBooksLoot() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(3))

                        // Papel (menos)
                        .add(LootItem.lootTableItem(Items.PAPER)
                                .setWeight(4)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1, 4))))

                        // Livro normal
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(3)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1, 2))))

                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(6)
                                .apply(EnchantWithLevelsFunction.enchantWithLevels(
                                        UniformGenerator.between(15, 25)
                                ).allowTreasure()))
                );
    }

    private static LootTable.Builder highBooksLoot() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(4))

                        // Papel (bem raro)
                        .add(LootItem.lootTableItem(Items.PAPER)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1, 2))))

                        // Livro normal
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(2)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1, 2))))

                        // Livro encantado forte
                        .add(LootItem.lootTableItem(Items.BOOK)
                                .setWeight(7)
                                .apply(EnchantWithLevelsFunction.enchantWithLevels(
                                        UniformGenerator.between(30, 40)
                                ).allowTreasure()))

                        // Alpha Gene Syringe (chance média)
                        .add(LootItem.lootTableItem(ChangedAddonItems.ALPHA_SERUM_SYRINGE.get())
                                .setWeight(3).apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0, 1))
                                )
                        )
                );
    }
}
