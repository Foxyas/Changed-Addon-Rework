package net.foxyas.changedaddon.block.advanced;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class PawsScanner extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POWERED = LeverBlock.POWERED;


    public enum LockType implements StringRepresentable {
        HUMAN(),
        TRANSFURRED();

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }
    }

    public PawsScanner() {
        super(Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).sound(SoundType.METAL).strength(3.0F, 3.0F).dynamicShape());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.FALSE));
    }

    @Override
    protected @NotNull ImmutableMap<BlockState, VoxelShape> getShapeForEachState(@NotNull Function<BlockState, VoxelShape> p_152459_) {
        return super.getShapeForEachState(p_152459_);
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED, FACING);
    }

    protected static VoxelShape rotateShape(VoxelShape shape, Direction direction) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = switch (direction) {
            case SOUTH -> 2;
            case WEST -> 3;
            case EAST -> 1;
            default -> 0;
        };

        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                buffer[1] = Shapes.or(buffer[1], Shapes.box(16 - maxZ, minY, minX, 16 - minZ, maxY, maxX));
            });
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }


    // Shapes por direção
    protected static final VoxelShape SHAPE_NORTH = Shapes.or(
            Block.box(1, 0, 3, 15, 0.75, 14),
            Block.box(2.5, 0.75, 7.7, 13.5, 1, 12.95)
    );

    protected static final VoxelShape SHAPE_SOUTH = Shapes.or(
            Block.box(1, 0, 2, 15, 0.75, 13),
            Block.box(2.5, 0.75, 3.05, 13.5, 1, 8.3)
    );

    protected static final VoxelShape SHAPE_EAST = Shapes.or(
            Block.box(2, 0, 1, 13, 0.75, 15),
            Block.box(3.05, 0.75, 2.5, 8.3, 1, 13.5)
    );

    protected static final VoxelShape SHAPE_WEST = Shapes.or(
            Block.box(3, 0, 1, 14, 0.75, 15),
            Block.box(7.7, 0.75, 2.5, 12.95, 1, 13.5)
    );


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
    public boolean isSignalSource(BlockState state) {
        return state.getValue(POWERED);
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        super.entityInside(state, level, pos, entity);
        if (!level.isClientSide) {
            if (entity instanceof ChangedEntity changedEntity) {
                if (!state.getValue(POWERED)) {
                    ChangedSounds.broadcastSound(Objects.requireNonNull(level.getServer()), ChangedSounds.CHIME2, pos, 1.0F, 1.0F);
                    level.setBlock(pos, state.setValue(POWERED, true), 3);
                    level.updateNeighborsAt(pos, state.getBlock());
                    level.scheduleTick(pos, this, 40);
                }
            } else if (entity instanceof Player player) {
                TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
                if (!state.getValue(POWERED)) {
                    ChangedSounds.broadcastSound(Objects.requireNonNull(level.getServer()), ChangedSounds.CHIME2, pos, 1.0F, 1.0F);
                    level.setBlock(pos, state.setValue(POWERED, true), 3);
                    level.updateNeighborsAt(pos, state.getBlock());
                    level.scheduleTick(pos, this, 40);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.tick(state, world, pos, random);
        if (state.getValue(POWERED)) {
            world.setBlock(pos, state.setValue(POWERED, false), 3);
            world.updateNeighborsAt(pos, state.getBlock());
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(ChangedAddonRegisters.ChangedAddonBlocks.HAND_SCANNER.get(), renderType -> renderType == RenderType.cutout());
    }
}
