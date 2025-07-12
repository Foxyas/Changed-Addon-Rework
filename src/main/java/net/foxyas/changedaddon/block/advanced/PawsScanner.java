package net.foxyas.changedaddon.block.advanced;

import com.google.common.collect.ImmutableMap;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

@SuppressWarnings("deprecation")
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

    // Shapes por direção
    protected static final VoxelShape PAWS_SCANNER_SHAPE_NORTH = Shapes.or(
            Block.box(2.5, 0.75, 7.7, 13.5, 1, 12.95)
    );

    protected static final VoxelShape PAWS_SCANNER_SHAPE_SOUTH = Shapes.or(
            Block.box(2.5, 0.75, 3.05, 13.5, 1, 8.3)
    );

    protected static final VoxelShape PAWS_SCANNER_SHAPE_EAST = Shapes.or(
            Block.box(3.05, 0.75, 2.5, 8.3, 1, 13.5)
    );

    protected static final VoxelShape PAWS_SCANNER_SHAPE_WEST = Shapes.or(
            Block.box(7.7, 0.75, 2.5, 12.95, 1, 13.5)
    );


    public @NotNull VoxelShape getShapeOfPawsScanner(BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> PAWS_SCANNER_SHAPE_SOUTH;
            case WEST -> PAWS_SCANNER_SHAPE_WEST;
            case EAST -> PAWS_SCANNER_SHAPE_EAST;
            default -> PAWS_SCANNER_SHAPE_NORTH;
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
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof ChangedEntity || entity instanceof Player) {
            if (!level.isClientSide && !state.getValue(POWERED)) {
                if (shouldActivate(state, level, pos, entity)) {
                    level.setBlock(pos, state.setValue(POWERED, true), 3);
                    updateRedstoneSignal(level, pos);
                    ChangedSounds.broadcastSound(Objects.requireNonNull(level.getServer()), ChangedSounds.CHIME2, pos, 1.0F, 1.0F);
                    level.scheduleTick(pos, this, 10); // Delay para reavaliar
                }
            }
        }
    }

    public void updateRedstoneSignal(Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        for (Direction direction : Direction.values()) {
            level.updateNeighborsAt(pos.relative(direction), this);
        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Random random) {
        boolean hasEntity = shouldActivate(state, level, pos);
        boolean isPowered = state.getValue(POWERED);

        if (hasEntity && !isPowered) {
            level.setBlock(pos, state.setValue(POWERED, true), 3);
            updateRedstoneSignal(level, pos);
            ChangedSounds.broadcastSound(level.getServer(), ChangedSounds.CHIME2, pos, 1.0F, 1.0F);
        } else if (!hasEntity && isPowered) {
            level.setBlock(pos, state.setValue(POWERED, false), 3);
            updateRedstoneSignal(level, pos);
            ChangedSounds.broadcastSound(level.getServer(), ChangedSounds.KEY, pos, 0.6F, 1.0F);
        }

        if (hasEntity) {
            level.scheduleTick(pos, this, 10);
        }
    }


    private boolean shouldActivate(BlockState state, Level level, BlockPos pos) {
        VoxelShape shape = getShapeOfPawsScanner(state, level, pos, CollisionContext.empty());
        AABB aabb = shape.bounds().move(new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(0, 0.25f, 0));
        //debugSpawnParticlesInShape(level, aabb, pos);

        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb,
                living -> living instanceof ChangedEntity changedEntity && changedEntity.getType() != ChangedEntities.LATEX_HUMAN.get() ||
                        living instanceof Player player && !player.isSpectator() && ProcessTransfur.getPlayerTransfurVariant(player) != null && !ProcessTransfur.getPlayerTransfurVariant(player).is(ChangedTransfurVariants.LATEX_HUMAN));

        // Permite ChangedEntity ou Player com variant
        return list.stream().anyMatch((living -> living.getItemBySlot(EquipmentSlot.FEET).isEmpty()));
    }

    private boolean shouldActivate(BlockState state, Level level, BlockPos pos, Entity stepEntity) {
        VoxelShape shape = getShapeOfPawsScanner(state, level, pos, CollisionContext.empty());
        AABB aabb = shape.bounds().move(new Vec3(pos.getX(), pos.getY(), pos.getZ()).add(0, 0.25f, 0));
        //debugSpawnParticlesInShape(stepEntity.level, aabb, pos);

        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb,
                living -> living instanceof ChangedEntity changedEntity && changedEntity.getType() != ChangedEntities.LATEX_HUMAN.get() ||
                        living instanceof Player player && !player.isSpectator() && ProcessTransfur.getPlayerTransfurVariant(player) != null && !ProcessTransfur.getPlayerTransfurVariant(player).is(ChangedTransfurVariants.LATEX_HUMAN));

        // Permite ChangedEntity ou Player com variant
        return list.stream().anyMatch((living -> living.getItemBySlot(EquipmentSlot.FEET).isEmpty()));
    }

    private void debugSpawnParticlesInShape(Level level, AABB box, BlockPos pos) {
        double step = 0.2;  // granularidade do grid de pontos para as partículas

        for (double x = box.minX; x <= box.maxX; x += step) {
            for (double y = box.minY; y <= box.maxY; y += step) {
                for (double z = box.minZ; z <= box.maxZ; z += step) {
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, true, x, y, z, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, BlockPos pos) {
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
