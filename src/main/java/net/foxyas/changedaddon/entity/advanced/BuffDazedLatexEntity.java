package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedBlocks;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuffDazedLatexEntity extends AbstractDazedEntity {

    public BuffDazedLatexEntity(EntityType<BuffDazedLatexEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        this.setAttributes(this.getAttributes());
        setPersistenceRequired();
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void addLivingEntityToBiomes(SpawnPlacementRegisterEvent event) {
        event.register(ChangedAddonEntities.BUFF_DAZED_LATEX.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                BuffDazedLatexEntity::canSpawnNear,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public static LootTable.Builder getLoot() {
        return LootTable.lootTable()

                // white_latex_goo
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ChangedItems.WHITE_LATEX_GOO.get())
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(2.0F, 4.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(1, 3)))
                        )
                )

                // white_latex_block
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ChangedBlocks.WHITE_LATEX_BLOCK.get())
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0F, 2.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(1, 3)))
                        )
                )

                // white_dye
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.WHITE_DYE)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0F, 2.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(1, 3)))
                        )
                )

                // latex_base
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ChangedItems.LATEX_BASE.get())
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(1.0F, 3.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(1, 3)))
                        )
                )

                // white_latex_puddle_male
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ChangedBlocks.WHITE_LATEX_PUDDLE_MALE.get())
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0F, 2.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(1, 3)))
                        )
                )

                // white_latex_puddle_female
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ChangedBlocks.WHITE_LATEX_PUDDLE_FEMALE.get())
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(0.0F, 2.0F)))
                                .apply(LootingEnchantFunction.lootingMultiplier(
                                        UniformGenerator.between(1, 3)))
                        )
                );
    }

    private static boolean canSpawnNear(EntityType<BuffDazedLatexEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        if (!isDarkEnoughToSpawn(world, pos, random)) {
            //ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n isn't dark enough");
            return false;
        }

        if (!world.getBiome(pos).is(Tags.Biomes.IS_PLAINS)) {
            //ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n isn't plains");
            return false;
        }

        // Certifica-se de que o bloco abaixo não é ar e é sólido
        BlockState blockBelow = world.getBlockState(pos.below());
        if (!blockBelow.isSolidRender(world, pos.below()) || !blockBelow.isFaceSturdy(world, pos.below(), Direction.UP)) {
            //ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n isn't a good block");
            return false;
        }

        // Defina uma AABB (Área de Checagem) ao redor do spawn para verificar se há Oak Log por perto.
        AABB checkArea = new AABB(pos).inflate(32); // Raio de 32 blocos ao redor

        //ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n" + nearSpawnBlock);

        return world.getBlockStatesIfLoaded(checkArea)
                .anyMatch(state -> state.is(ChangedAddonBlocks.GOO_CORE.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    public float getScale() {
        return super.getScale() * 1.08f;
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 5f);
        safeSetBaseValue(attributes.getInstance(Attributes.MAX_HEALTH), 40f);
        safeSetBaseValue(attributes.getInstance(Attributes.FOLLOW_RANGE), 40.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.MOVEMENT_SPEED), 1.05F);
        safeSetBaseValue(attributes.getInstance(ForgeMod.SWIM_SPEED.get()), 1.025F);
        safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 5.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 4);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 1);
        safeSetBaseValue(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE), 0.25f);
    }
}