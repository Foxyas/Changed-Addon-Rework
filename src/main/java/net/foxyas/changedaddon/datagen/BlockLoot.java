package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.block.LuminarCrystalLarge;
import net.foxyas.changedaddon.block.LuminarCrystalSmall;
import net.foxyas.changedaddon.block.MultifaceBlock;
import net.foxyas.changedaddon.block.StackableCanBlock;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static net.foxyas.changedaddon.init.ChangedAddonBlocks.*;

public class BlockLoot extends net.minecraft.data.loot.BlockLootSubProvider {

    public static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    protected static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();
    protected static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
    private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
    private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();
    public static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};

    public BlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(LATEX_INSULATOR.get());

        add(DEEPSLATE_IRIDIUM_ORE.get(), createOreDrop(DEEPSLATE_IRIDIUM_ORE.get(), ChangedAddonItems.RAW_IRIDIUM.get()));
        dropSelf(IRIDIUM_BLOCK.get());

        dropSelf(IRIDIUM_BLOCK.get());
        add(DEEPSLATE_PAINITE_ORE.get(), createOreDrop(DEEPSLATE_PAINITE_ORE.get(), ChangedAddonItems.PAINITE.get()));
        dropSelf(PAINITE_BLOCK.get());

        dropSelf(CATALYZER.get());
        dropSelf(ADVANCED_CATALYZER.get());

        dropSelf(UNIFUSER.get());
        dropSelf(ADVANCED_UNIFUSER.get());

        dropSelf(DARK_LATEX_PUDDLE.get());
        dropSelf(SIGNAL_BLOCK.get());
        dropSelf(INFORMANT_BLOCK.get());
        dropSelf(SNEP_PLUSHY.get());
        dropSelf(WOLF_PLUSHY.get());
        dropSelf(DARK_LATEX_WOLF_PLUSHY.get());
        dropSelf(CONTAINMENT_CONTAINER.get());
        dropSelf(REINFORCED_WALL.get());
        dropSelf(REINFORCED_WALL_SILVER_STRIPED.get());
        dropSelf(REINFORCED_WALL_SILVER_TILED.get());
        dropSelf(REINFORCED_WALL_CAUTION.get());
        dropSelf(REINFORCED_CROSS_BLOCK.get());
        dropSelf(WALL_WHITE_CRACKED.get());
        dropPottedContents(ChangedAddonBlocks.POTTED_LUMINARA_BLOOM.get());
        dropPottedContents(POTTED_LUMINARA_SAPLING.get());

        add(LUMINAR_CRYSTAL_BLOCK.get(), createSilkTouchDispatchTable(LUMINAR_CRYSTAL_BLOCK.get(), LootItem.lootTableItem(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get())
                .apply(ApplyExplosionDecay.explosionDecay())
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));

        add(LUMINAR_CRYSTAL_SMALL.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(ChangedAddonItems.LUMINAR_CRYSTAL_SMALL.get()).when(HAS_SILK_TOUCH),
                                LootItem.lootTableItem(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get())
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(LUMINAR_CRYSTAL_SMALL.get())
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LuminarCrystalSmall.HEARTED, true)))
                                        .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.PLAYER))))
                                        .apply(ApplyExplosionDecay.explosionDecay())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5)))
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)),
                                LootItem.lootTableItem(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get())
                                        .apply(ApplyExplosionDecay.explosionDecay())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        ))
                )
        );

        add(LUMINAR_CRYSTAL_LARGE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(AlternativesEntry.alternatives(
                                        LootItem.lootTableItem(ChangedAddonItems.LUMINAR_CRYSTAL_LARGE.get()).when(HAS_SILK_TOUCH),
                                        LootItem.lootTableItem(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get())
                                                .apply(ApplyExplosionDecay.explosionDecay())
                                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 6)))
                                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                ).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(LUMINAR_CRYSTAL_LARGE.get())
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(LuminarCrystalLarge.HALF, Half.BOTTOM)))
                        )
                )
        );

        add(GOO_CORE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ChangedAddonItems.GOO_CORE_FRAGMENT.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        )
                )
        );

        dropStackableCan(FOXTA_CAN, ChangedAddonItems.FOXTA);
        dropStackableCan(SNEPSI_CAN, ChangedAddonItems.SNEPSI);
        dropSelf(HAND_SCANNER.get());
        dropSelf(PAWS_SCANNER.get());

        dropSelf(LUMINARA_BLOOM.get());

        add(WOLF_CRYSTAL_PILLAR.get(), createSilkTouchOnlyTable(WOLF_CRYSTAL_PILLAR.get()));

        coverBlockDrop(COVER_BLOCK.get());
        coverBlockDropSelfOrOther(DARK_LATEX_COVER_BLOCK.get(), ChangedItems.DARK_LATEX_GOO.get());
        coverBlockDropSelfOrOther(WHITE_LATEX_COVER_BLOCK.get(), ChangedItems.WHITE_LATEX_GOO.get());

        add(LUMINARA_LEAVES.get(), (block) -> createLeavesWithFruitDrops(block, LUMINARA_SAPLING.get(), LUMINARA_BLOOM.get().asItem(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(LUMINARA_SAPLING.get());
        dropSelf(LUMINARA_LOG.get());
        dropSelf(STRIPPED_LUMINARA_LOG.get());
        dropSelf(LUMINARA_WOOD);
        dropSelf(STRIPPED_LUMINARA_WOOD);
        dropSelf(LUMINARA_PLANKS);
        dropSelf(LUMINARA_STAIRS);
        add(LUMINARA_SLAB.get(), this::createSlabItemTable);
        add(LUMINARA_DOOR.get(), this::createDoorTable);
        dropSelf(LUMINARA_TRAPDOOR);
        dropSelf(LUMINARA_FENCE);
        dropSelf(LUMINARA_FENCE_GATE);
        dropSelf(LUMINARA_SIGN);
        dropSelf(LUMINARA_WALL_SIGN);
        dropSelf(LUMINARA_HANGING_SIGN);
        dropSelf(LUMINARA_WALL_HANGING_SIGN);
        dropSelf(LUMINARA_BUTTON);
        dropSelf(LUMINARA_PRESSURE_PLATE);
    }

    protected void dropSelf(RegistryObject<? extends Block> block) {
        dropSelf(block.get());
    }

    /**
     * Used for oak and dark oak, same as droppingWithChancesAndSticks but adding in apples.
     */
    protected LootTable.Builder createLeavesWithFruitDrops(Block leaves, Block sapling, Item fruit, float... pChances) {
        return this.createLeavesDrops(leaves, sapling, pChances).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_NO_SHEARS_OR_SILK_TOUCH).add(this.applyExplosionCondition(leaves, LootItem.lootTableItem(fruit)).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
    }

    private void coverBlockDrop(MultifaceBlock cover) {
        LootTable.Builder table = LootTable.lootTable();
        for (Direction direction : Direction.values()) {
            table.withPool(LootPool.lootPool().add(LootItem.lootTableItem(cover))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(cover)
                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true))));
        }

        add(cover, table);
    }

    private void coverBlockDropOther(MultifaceBlock cover, ItemLike other) {
        LootTable.Builder table = LootTable.lootTable();
        for (Direction direction : Direction.values()) {
            table.withPool(LootPool.lootPool().add(LootItem.lootTableItem(other))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(cover)
                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true))));
        }

        add(cover, table);
    }

    private void coverBlockDropSelfOrOther(
            MultifaceBlock cover,
            ItemLike other
    ) {
        LootTable.Builder table = LootTable.lootTable();

        for (Direction direction : Direction.values()) {
            table.withPool(
                    faceDropPool(cover, cover.asItem(), direction, true)
            );

            table.withPool(
                    faceDropPool(cover, other, direction, false)
            );
        }

        add(cover, table);
    }

    private LootPool.Builder faceDropPool(
            MultifaceBlock cover,
            ItemLike drop,
            Direction direction,
            boolean silkTouch
    ) {
        LootPool.Builder pool = LootPool.lootPool()
                .add(LootItem.lootTableItem(drop))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(cover)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(PipeBlock.PROPERTY_BY_DIRECTION.get(direction), true)));

        if (silkTouch) {
            pool.when(MatchTool.toolMatches(
                    ItemPredicate.Builder.item()
                            .hasEnchantment(
                                    new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))
                            )
            ));
        } else {
            pool.when(MatchTool.toolMatches(
                    ItemPredicate.Builder.item()
                            .hasEnchantment(
                                    new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))
                            )
            ).invert());
        }

        return pool;
    }


    private void dropStackableCan(RegistryObject<? extends StackableCanBlock> canBlock, RegistryObject<? extends Item> canItem) {
        StackableCanBlock block = canBlock.get();
        LootTable.Builder table = LootTable.lootTable();
        LootPool.Builder pool = LootPool.lootPool();
        var item = LootItem.lootTableItem(canItem.get());

        for (int i = 1; i < 5; i++) {
            item.apply(SetItemCountFunction.setCount(ConstantValue.exactly(i))
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StackableCanBlock.CANS, i))));
        }

        pool.add(item);
        table.withPool(pool);
        add(block, table);
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return REGISTRY.getEntries().stream().map(RegistryObject::get).filter(block -> !(block instanceof LiquidBlock)).filter((block) -> block.getLootTable() != BuiltInLootTables.EMPTY).toList();
    }
}
