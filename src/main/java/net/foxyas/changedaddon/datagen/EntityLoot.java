package net.foxyas.changedaddon.datagen;

import net.foxyas.changedaddon.init.ChangedAddonItems; // Update with your actual registry classes
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonTransfurDiets;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.item.loot.RandomVariantFunction;
import net.ltxprogrammer.changed.item.loot.SetVariantFunction;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static net.foxyas.changedaddon.init.ChangedAddonEntities.EntitiesWithLoot;
import static net.minecraft.world.level.storage.loot.LootPool.lootPool;

public class EntityLoot extends EntityLootSubProvider {

    public EntityLoot() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE.get(), createLuminarcticLeopardTable(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_FEMALE));
        this.add(ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE.get(), createLuminarcticLeopardTable(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_MALE));

        EntitiesWithLoot.forEach((supplierBuilderPair -> add(supplierBuilderPair.getFirst().get(), supplierBuilderPair.getSecond().get())));
    }

    /**
     * Builds the full LootTable matching your luminarctic leopard JSON.
     */
    public <T extends ChangedEntity> LootTable.Builder createLuminarcticLeopardTable(RegistryObject<TransfurVariant<T>> variant) {
        CompoundTag isBossTag = new CompoundTag();
        isBossTag.putBoolean("isBoss", true);

        isBossTag.putBoolean("isBoss", false);

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
                .withPool(pool(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD_HEARTED.get(), 0, 1, 1, 3).when(
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
        List<EntityType<?>> list = new ArrayList<>();
        EntitiesWithLoot.forEach((supplierBuilderPair) -> list.add(supplierBuilderPair.getFirst().get()));
        return list.stream();
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