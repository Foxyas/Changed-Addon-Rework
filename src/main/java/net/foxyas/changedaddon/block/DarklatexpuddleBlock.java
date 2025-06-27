package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.entity.DarklatexpuddleBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.procedures.DarklatexpuddleBlockAddedProcedure;
import net.foxyas.changedaddon.procedures.DarklatexpuddleFeatureProcedure;
import net.foxyas.changedaddon.procedures.DarklatexpuddleUpdateTickProcedure;
import net.ltxprogrammer.changed.block.NonLatexCoverableBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DarklatexpuddleBlock extends Block implements EntityBlock, NonLatexCoverableBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public DarklatexpuddleBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.SLIME_BLOCK).strength(1f, 10f).speedFactor(0.8f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @OnlyIn(Dist.CLIENT)
	@SuppressWarnings("removal")
    public static void registerRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.DARKLATEXPUDDLE.get(), renderType -> renderType == RenderType.cutout());
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 0;
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH ->
                    Shapes.or(box(6, 0.25, 1.5, 14.5, 1.5, 10), box(5.5, 0, 1, 15, 1, 10.5), box(0.75, 0, 10, 5.75, 1, 15), box(1.25, 0.25, 10.5, 5.25, 1.5, 14.5));
            case EAST ->
                    Shapes.or(box(6, 0.25, 6, 14.5, 1.5, 14.5), box(5.5, 0, 5.5, 15, 1, 15), box(1, 0, 0.75, 6, 1, 5.75), box(1.5, 0.25, 1.25, 5.5, 1.5, 5.25));
            case WEST ->
                    Shapes.or(box(1.5, 0.25, 1.5, 10, 1.5, 10), box(1, 0, 1, 10.5, 1, 10.5), box(10, 0, 10.25, 15, 1, 15.25), box(10.5, 0.25, 10.75, 14.5, 1.5, 14.75));
            default ->
                    Shapes.or(box(1.5, 0.25, 6, 10, 1.5, 14.5), box(1, 0, 5.5, 10.5, 1, 15), box(10.25, 0, 1, 15.25, 1, 6), box(10.75, 0.25, 1.5, 14.75, 1.5, 5.5));
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootParams.@NotNull Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void onPlace(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        world.scheduleTick(pos, this, 1);
        DarklatexpuddleBlockAddedProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void tick(@NotNull BlockState blockstate, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.tick(blockstate, world, pos, random);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        DarklatexpuddleUpdateTickProcedure.execute(world, x, y, z);
        world.scheduleTick(pos, this, 1);
    }

    @Override
    public void attack(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player entity) {
        super.attack(blockstate, world, pos, entity);
        DarklatexpuddleFeatureProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
    }

    @Override
    public void entityInside(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull Entity entity) {
        super.entityInside(blockstate, world, pos, entity);
        DarklatexpuddleFeatureProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
    }

    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DarklatexpuddleBlockEntity(pos, state);
    }

    @Override
    public boolean triggerEvent(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, int eventID, int eventParam) {
        super.triggerEvent(state, world, pos, eventID, eventParam);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
    }
}
