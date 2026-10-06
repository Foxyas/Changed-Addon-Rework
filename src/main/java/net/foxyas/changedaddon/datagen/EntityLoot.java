package net.foxyas.changedaddon.datagen;

import com.mojang.datafixers.util.Pair;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems; // Update with your actual registry classes
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.item.loot.SetVariantFunction;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.foxyas.changedaddon.init.ChangedAddonEntities.EntitiesWithLoot;
import static net.minecraft.world.level.storage.loot.LootPool.lootPool;

public class EntityLoot extends EntityLootSubProvider {

    public final Set<EntityType<?>> entityTypes = new HashSet<>();

    public EntityLoot() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        EntitiesWithLoot.forEach((supplierBuilderPair -> add(supplierBuilderPair.getFirst().get(), supplierBuilderPair.getSecond().get())));

        this.add(ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE.get(), createLuminarcticLeopardTable(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_FEMALE));
        this.add(ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE.get(), createLuminarcticLeopardTable(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_MALE));
        this.add(ChangedAddonEntities.EXPERIMENT_009_BOSS.get(), createExperiment009BossLootTable());
        this.add(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), createExperiment10BossLootTable());
    }

    @Override
    protected void add(@NotNull EntityType<?> pEntityType, @NotNull ResourceLocation pLootTableLocation, LootTable.@NotNull Builder pBuilder) {
        boolean containsInRegistry = EntitiesWithLoot.stream().map(Pair::getFirst).map(Supplier::get).toList().contains(pEntityType);
        if (!containsInRegistry) this.entityTypes.add(pEntityType);
        super.add(pEntityType, pLootTableLocation, pBuilder);
    }

    public static LootTable.Builder createExperiment009BossLootTable() {
        SetVariantFunction.Builder setFormVariantFunction = new SetVariantFunction.Builder().withVariant(ChangedAddonTransfurVariants.EXPERIMENT_009.get());
        return LootTable.lootTable()
                // Pool 1: Disc
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ChangedAddonItems.MEANINGLESS_STRAFE_MUSIC_DISC.get())
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())))

                // Pool 2: Transfur Totem
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ChangedAddonItems.TRANSFUR_TOTEM.get())
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())))

                // Pool 3: Experiment 009 DNA
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ChangedAddonItems.EXPERIMENT_009_DNA.get())
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05f, 0.25f))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                        )
                        .add(LootItem.lootTableItem(ChangedAddonItems.BLUE_LATEX_GOO.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 2f)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1f, 2f)))
                        )
                )

                // Pool 4: Variable Drop Pool
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(5f, 15f))
                        .add(LootItem.lootTableItem(ChangedItems.WHITE_LATEX_GOO.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 12.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                        )

                        .add(LootItem.lootTableItem(ChangedItems.LATEX_BASE.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))

                        .add(LootItem.lootTableItem(ChangedAddonItems.LUMINARA_BASE.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))

                        .add(LootItem.lootTableItem(Items.DIAMOND)
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(5.0F, 10.0F))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceCondition.randomChance(0.75F))))

                        .add(LootItem.lootTableItem(ChangedAddonItems.PAINITE.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0f))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.05F))))

                        .add(LootItem.lootTableItem(ChangedItems.LATEX_SYRINGE.get())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.02F, 0.05F))
                                .apply(setFormVariantFunction)
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))

                        .add(LootItem.lootTableItem(ChangedItems.LATEX_FLASK.get())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.02F, 0.05F))
                                .apply(setFormVariantFunction)
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))));
    }

    public static LootTable.Builder createExperiment10BossLootTable() {
        SetVariantFunction.Builder setFormVariantFunction = new SetVariantFunction.Builder().withVariant(ChangedAddonTransfurVariants.EXPERIMENT_10.get());
        return LootTable.lootTable()
                // Pool 1: Experiment 10 DNA
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ChangedAddonItems.EXPERIMENT_10_DNA.get())
                                .setWeight(1)
                                .setQuality(1)
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.05f, 0.25f))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0f, 1.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))

                // Pool 2: Red Latex Goo
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ChangedAddonItems.RED_LATEX_GOO.get())
                                .setWeight(1)
                                .setQuality(1)
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))

                // Pool 3: Variable Drop Pool
                .withPool(LootPool.lootPool()
                        .setRolls(UniformGenerator.between(5f, 15f))
                        .add(LootItem.lootTableItem(ChangedItems.WHITE_LATEX_GOO.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 12.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                        )

                        .add(LootItem.lootTableItem(ChangedItems.LATEX_BASE.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 9.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))

                        .add(LootItem.lootTableItem(ChangedAddonItems.LUMINARA_BASE.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))

                        .add(LootItem.lootTableItem(Items.NETHERITE_SCRAP)
                                .setWeight(1)
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.001F, 0.05F))
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F))))

                        .add(LootItem.lootTableItem(Items.NETHERITE_INGOT)
                                .setWeight(1)
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.0001F, 0.01F))
                                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))

                        .add(LootItem.lootTableItem(ChangedAddonItems.PAINITE.get())
                                .setWeight(1)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 6.0F))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.05F))))

                        .add(LootItem.lootTableItem(ChangedItems.LATEX_SYRINGE.get())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.02F, 0.025F))
                                .apply(setFormVariantFunction)
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))

                        .add(LootItem.lootTableItem(ChangedItems.LATEX_FLASK.get())
                                .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.02F, 0.05F))
                                .apply(setFormVariantFunction)
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                );
    }

    /**
     * Builds the full LootTable matching your luminarctic leopard JSON.
     */
    public <T extends ChangedEntity> LootTable.Builder createLuminarcticLeopardTable(RegistryObject<TransfurVariant<T>> variant) {
        CompoundTag isBossTag = new CompoundTag();
        isBossTag.putBoolean("isBoss", true);

        SetVariantFunction.Builder variantBuilder = new SetVariantFunction.Builder();
        return LootTable.lootTable()
                // Pool 1: Salmon (Smelted if on fire)
                .withPool(lootPool()
                        .setRolls(UniformGenerator.between(1, 1))
                        .add(LootItem.lootTableItem(Items.SALMON)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, 3)))
                                .apply(SmeltItemFunction.smelted().when(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS,
                                                EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build())
                                        )
                                ))
                        )
                )
                // Pool 2: Snowball
                .withPool(pool(Items.SNOWBALL, 0, 2, 1, 3))
                // Pool 3: Glow Lichen
                .withPool(pool(Items.GLOW_LICHEN, 0, 3, 1, 3))
                // Pool 4: Glowstone Dust
                .withPool(pool(Items.GLOWSTONE_DUST, 1, 3, 1, 3))
                // Pool 5: String
                .withPool(pool(Items.STRING, 0, 3, 1, 3))
                // Pool 6: White Dye
                .withPool(pool(Items.WHITE_DYE, 0, 1, 1, 3))
                // Pool 7: Latex Base
                .withPool(pool(ChangedItems.LATEX_BASE.get(), 1, 1, 1, 3))
                // Pool 8: Syringe
                .withPool(pool(ChangedItems.SYRINGE.get(), 1, 1, 1, 3))
                // Pool 9: Luminar Crystal Shard
                .withPool(pool(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get(), 1, 9, 1, 3))
                // Pool 10: Hearted Crystal
                .withPool(pool(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD_HEARTED.get(), 0, 1, 1, 1).when(
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity().nbt(new NbtPredicate(isBossTag)).build())
                        )
                )
                .withPool(lootPool()
                        .setRolls(UniformGenerator.between(1, 1))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer()
                                .and(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.01f, 0.05f))
                        )
                        .add(LootItem.lootTableItem(ChangedItems.LATEX_SYRINGE.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)))
                                .apply(variantBuilder.withVariant(variant.get()))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0f, 1.0f)))
                        )
                );
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        Set<EntityType<?>> set = entityTypes;
        EntitiesWithLoot.forEach((supplierBuilderPair) -> set.add(supplierBuilderPair.getFirst().get()));
        return set.stream();
    }

    /**
     * 🔧 Util Method matching standard loot entry structure[cite: 1]
     */
    private LootPool.Builder pool(ItemLike item, float min, float max, float lootingMin, float lootingMax) {
        return lootPool()
                .setRolls(UniformGenerator.between(1, 1))
                .add(LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(lootingMin, lootingMax)))
                );
    }
}