package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.entity.LuminarCrystalHeartedBlockEntity;
import net.foxyas.changedaddon.block.interfaces.IBrushableBlock;
import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

import static net.foxyas.changedaddon.block.LuminarCrystalBlock.moveOrTarget;
import static net.foxyas.changedaddon.block.LuminarCrystalBlock.spawnParticleOnFace;

public class LuminarCrystalLarge extends BushBlock implements SimpleWaterloggedBlock, EntityBlock, IBrushableBlock {

    public static final VoxelShape SHAPE_UP = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);
    public static final VoxelShape SHAPE_DOWN = SHAPE_UP;
    public static final VoxelShape SHAPE_NORTH = Block.box(1, 1, 0, 15, 15, 16);
    public static final VoxelShape SHAPE_SOUTH = Block.box(1, 1, 0, 15, 15, 16);
    public static final VoxelShape SHAPE_WEST = Block.box(0, 1, 1, 16, 15, 15);
    public static final VoxelShape SHAPE_EAST = Block.box(0, 1, 1, 16, 15, 15);

    public static final BooleanProperty HEARTED = LuminarCrystalSmall.HEARTED;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public LuminarCrystalLarge(Properties properties) {
        super(properties.sound(SoundType.AMETHYST_CLUSTER)
                .noOcclusion()
                .dynamicShape()
                .randomTicks()
                .strength(1.7F, 2.5F)
                .hasPostProcess((blockState, blockGetter, blockPos) -> true)
                .emissiveRendering((blockState, blockGetter, blockPos) -> true)
                .noOcclusion());
        registerDefaultState(getStateDefinition().any().setValue(HEARTED, false).setValue(HALF, Half.BOTTOM).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state,
                                        @NotNull BlockGetter level,
                                        @NotNull BlockPos pos,
                                        @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case DOWN -> SHAPE_DOWN;
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_UP;
        };
    }


    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 0;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 4;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        Direction dir = pContext.getClickedFace();
        if ((dir == Direction.UP && pos.getY() >= level.getMaxBuildHeight() - 1)
                || (dir == Direction.DOWN && pos.getY() <= level.getMinBuildHeight() + 1)) return null;

        BlockState state = defaultBlockState().setValue(FACING, dir).setValue(WATERLOGGED, level.getFluidState(pos).getType() == Fluids.WATER);
        if (!canSurvive(state, level, pos)) return null;

        BlockState relative = level.getBlockState(pos.relative(dir));
        return relative.isAir() || relative.canBeReplaced() ? state : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        BlockPos relative = pos.relative(state.getValue(FACING));
        level.setBlockAndUpdate(relative, state.setValue(HALF, Half.TOP).setValue(WATERLOGGED, level.getFluidState(relative).getType() == Fluids.WATER));
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos blockPos) {
        return blockState.getBlock() == ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get();
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        Direction oppositeDirection = blockState.getValue(FACING).getOpposite();
        BlockState blockStateOn = level.getBlockState(blockPos.relative(oppositeDirection));
        if (blockState.getValue(HALF) == Half.TOP) {
            return blockStateOn.is(this) && blockStateOn.getValue(HALF) == Half.BOTTOM;
        }

        if (!canSupportRigidBlock(level, blockPos.relative(oppositeDirection)))
            return false;
        return blockStateOn.is(ChangedAddonTags.Blocks.CAN_LUMINAR_CRYSTAL_SURVIVE);
    }

    @Override
    public void randomTick(@NotNull BlockState thisState, @NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (random.nextFloat() < 0.99) return;

        for (Direction direction : Direction.values()) {
            spawnParticleOnFace(serverLevel, pos, direction, 2, 0.01f);
        }
    }


    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        if (pState.getValue(HEARTED)) {
            return createTickerHelper(pBlockEntityType, ChangedAddonBlockEntities.LUMINAR_CRYSTAL_HEARTED.get(), (level, pPos, state, blockEntity) -> {
                blockEntity.tick(level, pPos, state);
            });
        } else {
            return null;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        if (pState.getValue(HEARTED) && pState.getValue(HALF) == Half.BOTTOM) {
            return new LuminarCrystalHeartedBlockEntity(pPos, pState);
        } else {
            return null;
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if (!pState.getValue(HEARTED)) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof LuminarCrystalHeartedBlockEntity luminarCrystalHeartedBlock) {
                luminarCrystalHeartedBlock.setRemoved();
                pLevel.removeBlockEntity(pPos);
            }
        }
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof AbstractLuminarcticLeopard) return;

        entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
        if (!(entity instanceof LivingEntity livingEntity) || livingEntity.isSpectator()) return;

        if (livingEntity instanceof Player player
                && (player.isCreative() || ProcessTransfur.getPlayerTransfurVariantSafe(player)
                .map(var -> var.is(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_MALE)
                        || var.is(ChangedAddonTransfurVariants.LUMINARCTIC_LEOPARD_FEMALE)).orElse(false))) {
            return;
        }

        MobEffectInstance EffectInstance = new MobEffectInstance(MobEffects.WITHER, 20 * 20, 1, false, true, true);
        if (livingEntity.canBeAffected(EffectInstance) && !livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.addEffect(EffectInstance);
        }

        int pTicksFrozen = livingEntity.getTicksFrozen() + 5;
        int frozenTicks = Math.min(livingEntity.getTicksRequiredToFreeze(), pTicksFrozen);
        livingEntity.setTicksFrozen(frozenTicks);
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.isClientSide || state.getValue(HALF) == Half.BOTTOM)
            return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

        BlockPos below = pos.relative(state.getValue(FACING), -1);
        BlockState bottom = level.getBlockState(below);

        if (!super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) return false;

        if (willHarvest && bottom.is(this)) {
            Block.dropResources(bottom, level, below, null, player, player.getMainHandItem());
        }

        return true;
    }

    @Override
    public void onRemove(@NotNull BlockState oldState, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (level.isClientSide || oldState.is(newState.getBlock())) {
            super.onRemove(oldState, level, pos, newState, isMoving);
            return;
        }

        if (oldState.getValue(HALF) == Half.TOP) {
            BlockPos below = pos.relative(oldState.getValue(FACING), -1);
            BlockState bottom = level.getBlockState(below);
            if (bottom.is(this))
                level.setBlockAndUpdate(below, bottom.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
            return;
        }

        if (!(level instanceof ServerLevel serverLevel)) return;

        // Procura a entidade viva mais próxima (excluindo leopardos)
        LivingEntity closestEntity = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(8),
                        entity -> {
                            if (entity instanceof Player player) {
                                return !player.isSpectator() && !player.isCreative();
                            }
                            return !(entity instanceof AbstractLuminarcticLeopard);
                        }).stream()
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ())))
                .orElse(null);

        // Leopardo já existente
        List<AbstractLuminarcticLeopard> nearbyLeopards = level.getEntitiesOfClass(AbstractLuminarcticLeopard.class, new AABB(pos).inflate(10));
        if (!nearbyLeopards.isEmpty()) {
            if (closestEntity != null) {
                for (AbstractLuminarcticLeopard leopard : nearbyLeopards) {
                    if (!leopard.canAttack(closestEntity) || !leopard.hasLineOfSight(closestEntity)) continue;

                    LuminarCrystalBlock.moveOrTarget(closestEntity, leopard);
                    level.playSound(null, pos, SoundEvents.ENDERMAN_SCREAM, SoundSource.MASTER, 1, 0);
                }
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
            if (!oldState.getValue(HEARTED)) {
                return;
            }
        }

        Direction oppositeDirection = oldState.getValue(FACING).getOpposite();
        BlockState blockStateOn = level.getBlockState(pos.relative(oppositeDirection));
        if (blockStateOn.is(ChangedAddonTags.Blocks.CAN_SPAWN_LUMINARCTIC_LEOPARDS_ON_CRYSTAL_BREAK)) {
            EntityType<? extends AbstractLuminarcticLeopard> leopardType = level.random.nextBoolean()
                    ? ChangedAddonEntities.LUMINARCTIC_LEOPARD_FEMALE.get()
                    : ChangedAddonEntities.LUMINARCTIC_LEOPARD_MALE.get();

            AbstractLuminarcticLeopard newLeopard = leopardType.create(serverLevel);
            if (newLeopard == null) {
                super.onRemove(oldState, level, pos, newState, isMoving);
                return;
            }

            if (oldState.getValue(HEARTED)) {
                newLeopard.setBoss(true);
            }

            BlockPos.MutableBlockPos spawnPos = pos.mutable();
            Vec3 spawnVec = Vec3.atCenterOf(spawnPos);
            newLeopard.setPos(spawnVec.x, spawnVec.y, spawnVec.z);
            newLeopard.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
            if (closestEntity != null) {
                LuminarCrystalBlock.moveOrTarget(closestEntity, newLeopard);

                boolean placed = false;
                Vec3 backward, behind;
                for (double dist = 0.5; dist <= 1.5; dist += 0.5) {
                    backward = Vec3.directionFromRotation(0, closestEntity.getYRot());
                    behind = closestEntity.position().subtract(backward.scale(dist));

                    BlockPos.MutableBlockPos candidate = BlockPos.containing(behind).mutable();

                    newLeopard.setPos(Vec3.atCenterOf(candidate));

                    if (level.noCollision(newLeopard)
                            && level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP)
                            && newLeopard.hasLineOfSight(closestEntity)) {

                        spawnPos.set(candidate);
                        placed = true;
                        break;
                    }

                    BlockPos.MutableBlockPos moved = candidate.above().mutable();
                    if (level.noCollision(newLeopard)
                            && level.getBlockState(moved.below()).isFaceSturdy(level, moved.below(), Direction.UP)
                            && newLeopard.hasLineOfSight(closestEntity)) {

                        spawnPos.set(moved);
                        placed = true;
                        break;
                    }
                }

                // fallback final
                if (!placed) {
                    spawnPos.set(pos);
                    newLeopard.setPos(Vec3.atCenterOf(spawnPos));
                }
            }

            level.addFreshEntity(newLeopard);
            newLeopard.playSound(SoundEvents.ENDERMAN_SCREAM, 1, 0);

        }
        super.onRemove(oldState, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(HEARTED, HALF, FACING, WATERLOGGED));
    }

    @Override
    public @Nullable BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
        if (entity instanceof AbstractLuminarcticLeopard) {
            return BlockPathTypes.WALKABLE;
        }
        return BlockPathTypes.DANGER_OTHER;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean brush(Level level, BlockState state, BlockPos pos, Player player, Direction side, ItemStack brushStack) {
        if (!level.isClientSide()) {
            RandomSource randomSource = player.getRandom();

            // 1. Loot Level Logic (Makes it easier to get ANY crystal)
            // Gets the Fortune level from the brush (you can change it to another enchantment if needed)
            int lootLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, brushStack);

            // Base Chance: 5% (0.05f). Each Loot level increases it by 2% (0.02f).
            // Ex: Fortune 3 = 0.05 + (3 * 0.02) = 0.11 (11%)
            float crystalChance = 0.05f + (lootLevel * 0.02f);

            if (randomSource.nextFloat() <= crystalChance) {

                // 2. Player Luck Logic (Makes it easier to get the Hearted Crystal)
                float playerLuck = player.getLuck();

                // Base Chance: 0.01% (0.0001f). Each point of luck adds 0.05% (0.0005f).
                // We use Mth.clamp to ensure the chance is never less than 0.0f or greater than 1.0f.
                float heartedChance = Mth.clamp(0.0001f + (playerLuck * 0.0005f), 0f, 1f);

                if (randomSource.nextFloat() <= heartedChance) {
                    Block.popResource(level, pos, ChangedAddonItems.LUMINAR_CRYSTAL_SHARD_HEARTED.get().getDefaultInstance());
                } else {
                    Block.popResource(level, pos, ChangedAddonItems.LUMINAR_CRYSTAL_SHARD.get().getDefaultInstance());
                }

                level.playSound(null,
                        pos,
                        this.soundType.getBreakSound(),
                        SoundSource.BLOCKS,
                        1,
                        1);
                return true;
            }
        }

        return false;
    }
}
