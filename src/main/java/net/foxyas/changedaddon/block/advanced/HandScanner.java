package net.foxyas.changedaddon.block.advanced;

import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class HandScanner extends HorizontalDirectionalBlock {

    public static final BooleanProperty POWERED = LeverBlock.POWERED;
    public static final EnumProperty<LockType> LOCK_TYPE = EnumProperty.create("lock_type", LockType.class);
    // Shapes por direção
    protected static final VoxelShape SHAPE_NORTH = Block.box(5, 3.5, 14, 11, 12.5, 16);
    protected static final VoxelShape SHAPE_SOUTH = Block.box(5, 3.5, 0, 11, 12.5, 2);
    protected static final VoxelShape SHAPE_WEST = Block.box(14, 3.5, 5, 16, 12.5, 11);
    protected static final VoxelShape SHAPE_EAST = Block.box(0, 3.5, 5, 2, 12.5, 11);


    public HandScanner() {
        super(Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK).isRedstoneConductor((state, blockGetter, blockPos) -> state.getValue(POWERED)).sound(SoundType.METAL).strength(3.0F, 3.0F).dynamicShape());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE).setValue(LOCK_TYPE, LockType.TRANSFURRED));
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED, FACING, LOCK_TYPE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull VoxelShape getVisualShape(BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    public void onPlace(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        world.scheduleTick(pos, this, 5);
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        TransfurVariantInstance<?> playerTransfur = ProcessTransfur.getPlayerTransfurVariant(player);
        boolean isTransfurred = playerTransfur != null;

        LockType lockType = state.getValue(LOCK_TYPE);
        boolean allow = (lockType == LockType.HUMAN && (!isTransfurred || playerTransfur.is(ChangedTransfurVariants.LATEX_HUMAN))) || (lockType == LockType.TRANSFURRED && (isTransfurred && !playerTransfur.is(ChangedTransfurVariants.LATEX_HUMAN)));

        if (player.isShiftKeyDown()) {
            boolean canChange = false;
            for (Direction direction : Direction.values()) {
                if (!level.getBlockState(pos.relative(direction)).is(BlockTags.BUTTONS) && !level.getBlockState(pos.relative(direction)).is(Blocks.LEVER)) {
                    continue;
                }
                if (level.getSignal(pos, direction) > 0 && (level.getBlockState(pos.relative(direction)).getValue(POWERED) || level.getBlockState(pos.relative(direction)).getValue(BlockStateProperties.POWERED))) {
                    canChange = true;
                    break;
                }
            }
            if (canChange) {
                if (level instanceof ServerLevel serverLevel) {
                    ChangedSounds.broadcastSound(serverLevel, ChangedSounds.TRANSFUR_HURT, pos, 0.25F, 2.0F);
                    ChangedSounds.broadcastSound(serverLevel, ChangedSounds.KEYPAD_LOCK, pos, 0.5F, 1.0F);
                }
                if (state.getValue(LOCK_TYPE) == LockType.HUMAN) {
                    level.setBlock(pos, state.setValue(LOCK_TYPE, LockType.TRANSFURRED), 3);
                } else if (state.getValue(LOCK_TYPE) == LockType.TRANSFURRED) {
                    level.setBlock(pos, state.setValue(LOCK_TYPE, LockType.HUMAN), 3);
                }
                level.scheduleTick(pos, this, 5);
                return InteractionResult.SUCCESS;
            }
        }

        if (!allow) {
            if (level instanceof ServerLevel serverLevel) {
                ChangedSounds.broadcastSound(serverLevel, ChangedSounds.KEYPAD_UNLOCK_FAIL, pos, 1.0F, 1.0F);
            }
            player.displayClientMessage(Component.literal("You cannot use this lock in your current form!"), true);
            return InteractionResult.CONSUME;
        }

        if (!state.getValue(POWERED)) {
            if (level instanceof ServerLevel serverLevel) {
                ChangedSounds.broadcastSound(serverLevel, ChangedSounds.CARDBOARD_BOX_OPEN, pos, 0.25F, 2.0F);
            }
            level.setBlock(pos, state.setValue(POWERED, true), 3);
        } else {
            if (level instanceof ServerLevel serverLevel) {
                ChangedSounds.broadcastSound(serverLevel, ChangedSounds.TRANSFUR_HURT, pos, 0.25F, 2.0F);
                ChangedSounds.broadcastSound(serverLevel, ChangedSounds.KEYPAD_LOCK, pos, 0.5F, 1.0F);
            }
            level.setBlock(pos, state.setValue(POWERED, false), 3);
        }
        updateRedstoneSignal(level, pos, state.getBlock());
        level.scheduleTick(pos, this, 5);

        return InteractionResult.SUCCESS;
    }

    public void updateRedstoneSignal(Level level, BlockPos pos, Block block) {
        level.updateNeighborsAt(pos, this);
        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), block);
        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(state, world, pos, random);
        if (state.getValue(POWERED)) {
            ChangedSounds.broadcastSound(world, ChangedSounds.KEYPAD_UNLOCK_SUCCESS, pos, 1.0F, 1.0F);
        }
    }

    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        return level.getBlockState(blockPos.relative(blockState.getValue(FACING).getOpposite())).isFaceSturdy(level, blockPos.relative(blockState.getValue(FACING).getOpposite()), blockState.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LOCK_TYPE, LockType.getValueByPlayer(context.getPlayer()));
        }

        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public enum LockType implements StringRepresentable {
        HUMAN(),
        TRANSFURRED();

        public static LockType getValueByPlayer(@NotNull Player player) {
            return ProcessTransfur.isPlayerTransfurred(player) ? TRANSFURRED : HUMAN;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }
}
