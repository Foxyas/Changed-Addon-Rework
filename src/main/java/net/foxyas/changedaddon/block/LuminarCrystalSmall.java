package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.entity.defaults.AbstractLuminarcticLeopard;
import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.block.TransfurCrystalBlock;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.*;
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

public class LuminarCrystalSmall extends TransfurCrystalBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty HEARTED = BooleanProperty.create("hearted");
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape NORTH_AABB = Block.box(2, 2, 2, 14, 14, 16.0);
    protected static final VoxelShape SOUTH_AABB = Block.box(2, 2, 0, 14, 14, 14.0); // Corrigido
    protected static final VoxelShape EAST_AABB = Block.box(0, 2, 2, 14, 14, 14); // Corrigido
    protected static final VoxelShape WEST_AABB = Block.box(2, 2, 2, 16.0, 14, 14);
    protected static final VoxelShape UP_AABB = Block.box(2, 0, 2, 14, 14.0, 14);
    protected static final VoxelShape DOWN_AABB = Block.box(2, 2, 2, 14, 16.0, 14);

    public LuminarCrystalSmall() {
        super(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD,
                Properties.of() //FIXME : ChangedMaterials.LATEX_CRYSTAL
                        .sound(SoundType.AMETHYST_CLUSTER)
                        .noOcclusion()
                        .dynamicShape()
                        .randomTicks()
                        .strength(1.7F, 2.5F)
                        .hasPostProcess((blockState, blockGetter, blockPos) -> true)
                        .emissiveRendering((blockState, blockGetter, blockPos) -> true)
                        .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(HEARTED, false).setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));

    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case EAST -> EAST_AABB;
            case WEST -> WEST_AABB;
            case UP -> UP_AABB;
            case DOWN -> DOWN_AABB;
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
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
    public boolean canBeReplaced(@NotNull BlockState thisState, @NotNull Fluid fluid) {
        if (fluid instanceof LavaFluid || (fluid instanceof WaterFluid)) {
            return !thisState.getValue(HEARTED);
        }
        return super.canBeReplaced(thisState, fluid);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEARTED);
        builder.add(FACING);
        builder.add(WATERLOGGED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public void randomTick(@NotNull BlockState thisState, @NotNull ServerLevel serverLevel, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.randomTick(thisState, serverLevel, pos, random);
        if (random.nextFloat() < 0.99) return;

        for (Direction direction : Direction.values()) {
            LuminarCrystalBlock.spawnParticleOnFace(serverLevel, pos, direction, 2, 0.01f);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof AbstractLuminarcticLeopard) return;

        super.entityInside(state, level, pos, entity);
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
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter level, BlockPos blockPos) {
        return blockState.getBlock() == ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get();
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_152019_) {
        LevelAccessor levelaccessor = p_152019_.getLevel();
        BlockPos blockpos = p_152019_.getClickedPos();
        return this.defaultBlockState().setValue(WATERLOGGED, levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER).setValue(FACING, p_152019_.getClickedFace());
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        Direction oppositeDirection = blockState.getValue(FACING).getOpposite();
        BlockState blockStateOn = level.getBlockState(blockPos.relative(oppositeDirection));
        if (!canSupportRigidBlock(level, blockPos.relative(oppositeDirection)))
            return false;
        return blockStateOn.getBlock() == ChangedAddonBlocks.LUMINAR_CRYSTAL_BLOCK.get();
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
    public void onRemove(@NotNull BlockState oldState, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (level.isClientSide || oldState.is(newState.getBlock())) {
            super.onRemove(oldState, level, pos, newState, isMoving);
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
            return;
        }

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


        super.onRemove(oldState, level, pos, newState, isMoving);
    }
}
