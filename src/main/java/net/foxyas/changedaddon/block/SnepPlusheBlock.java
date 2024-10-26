
package net.foxyas.changedaddon.block;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

import net.foxyas.changedaddon.procedures.InteractPlushesProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;

import java.util.List;
import java.util.Collections;

public class SnepPlusheBlock extends Block implements SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public SnepPlusheBlock() {
		super(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL).strength(0.5f, 5f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return state.getFluidState().isEmpty();
	}

	@Override
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	public VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			default -> Shapes.or(box(5, 12, 5, 11, 18, 11), box(4.75, 11.55, 4.75, 11.25, 18.05, 11.25), box(4.7, 13.4, 4.7, 11.3, 18, 11.3), box(6.6, 12.1, 10.6, 9.4, 12.9, 11.4), box(6.1, 12.6, 10.35, 9.9, 14.4, 12.15),
					box(7.6, 16.225, 0.5, 8.4, 17.025, 1.3), box(4.21434, 16.91144, 6.4, 5.41434, 20.11144, 8.6), box(10.58566, 16.91144, 6.4, 11.78566, 20.11144, 8.6), box(4.95, 11.7, 5.2, 5.55, 13.3, 10.8),
					box(5.03934, 12.55235, 5.2, 5.63934, 14.15235, 10.8), box(10.45, 11.7, 5.2, 11.05, 13.3, 10.8), box(10.36066, 12.55235, 5.2, 10.96066, 14.15235, 10.8), box(4.9, 1.9, 6.9, 11.1, 12.1, 9.1),
					box(4.85, 1.85, 6.85, 11.15, 12.15, 9.15), box(6.2, -0.69821, 4.56624, 9.8, 2.90179, 9.16624), box(5.8, -3.31697, 2.71005, 10.2, 1.08303, 8.11005), box(5.5, 10.2863, -0.58216, 10.5, 18.2863, 4.41784),
					box(5.85, 12.8907, -4.11926, 10.15, 16.1907, 0.18074), box(5.05, 9.65235, 8.8, 10.95, 11.55235, 9.7), box(5.15, 9.25235, 9.4, 10.85, 10.95235, 10.1), box(5.05, 9.65235, 6.3, 10.95, 11.55235, 7.2),
					box(5.15, 9.25235, 5.9, 10.85, 10.95235, 6.6), box(2, 8.5, 6.5, 5, 11.5, 15.5), box(2.75, 8.625, 15.05, 4.25, 10.125, 15.55), box(2.05, 10.35, 14.75, 2.85, 11.15, 15.55), box(3.05, 10.55, 14.75, 3.85, 11.35, 15.55),
					box(4.05, 10.35, 14.75, 4.85, 11.15, 15.55), box(11, 8.5, 6.5, 14, 11.5, 15.5), box(11.75, 8.625, 15.05, 13.25, 10.125, 15.55), box(11.05, 10.35, 14.75, 11.85, 11.15, 15.55), box(12.05, 10.55, 14.75, 12.85, 11.35, 15.55),
					box(13.05, 10.35, 14.75, 13.85, 11.15, 15.55), box(4.25, 0.5, 8, 7.25, 3.5, 14.75), box(5, 0.67682, 14.333, 6.5, 2.17682, 14.833), box(4.3, 2.27682, 14.05, 5.1, 3.07682, 14.85), box(5.35, 2.52682, 14.05, 6.15, 3.32682, 14.85),
					box(6.375, 2.27682, 14.05, 7.175, 3.07682, 14.85), box(9, 0.5, 8, 12, 3.5, 14.75), box(9.75, 0.71235, 14.333, 11.25, 2.21235, 14.833), box(9.05, 2.31235, 14.05, 9.85, 3.11235, 14.85),
					box(10.1, 2.56235, 14.05, 10.9, 3.36235, 14.85), box(11.125, 2.31235, 14.05, 11.925, 3.11235, 14.85));
			case NORTH -> Shapes.or(box(5, 12, 5, 11, 18, 11), box(4.75, 11.55, 4.75, 11.25, 18.05, 11.25), box(4.7, 13.4, 4.7, 11.3, 18, 11.3), box(6.6, 12.1, 4.6, 9.4, 12.9, 5.4), box(6.1, 12.6, 3.85, 9.9, 14.4, 5.65),
					box(7.6, 16.225, 14.7, 8.4, 17.025, 15.5), box(10.58566, 16.91144, 7.4, 11.78566, 20.11144, 9.6), box(4.21434, 16.91144, 7.4, 5.41434, 20.11144, 9.6), box(10.45, 11.7, 5.2, 11.05, 13.3, 10.8),
					box(10.36066, 12.55235, 5.2, 10.96066, 14.15235, 10.8), box(4.95, 11.7, 5.2, 5.55, 13.3, 10.8), box(5.03934, 12.55235, 5.2, 5.63934, 14.15235, 10.8), box(4.9, 1.9, 6.9, 11.1, 12.1, 9.1), box(4.85, 1.85, 6.85, 11.15, 12.15, 9.15),
					box(6.2, -0.69821, 6.83376, 9.8, 2.90179, 11.43376), box(5.8, -3.31697, 7.88995, 10.2, 1.08303, 13.28995), box(5.5, 10.2863, 11.58216, 10.5, 18.2863, 16.58216), box(5.85, 12.8907, 15.81926, 10.15, 16.1907, 20.11926),
					box(5.05, 9.65235, 6.3, 10.95, 11.55235, 7.2), box(5.15, 9.25235, 5.9, 10.85, 10.95235, 6.6), box(5.05, 9.65235, 8.8, 10.95, 11.55235, 9.7), box(5.15, 9.25235, 9.4, 10.85, 10.95235, 10.1), box(11, 8.5, 0.5, 14, 11.5, 9.5),
					box(11.75, 8.625, 0.45, 13.25, 10.125, 0.95), box(13.15, 10.35, 0.45, 13.95, 11.15, 1.25), box(12.15, 10.55, 0.45, 12.95, 11.35, 1.25), box(11.15, 10.35, 0.45, 11.95, 11.15, 1.25), box(2, 8.5, 0.5, 5, 11.5, 9.5),
					box(2.75, 8.625, 0.45, 4.25, 10.125, 0.95), box(4.15, 10.35, 0.45, 4.95, 11.15, 1.25), box(3.15, 10.55, 0.45, 3.95, 11.35, 1.25), box(2.15, 10.35, 0.45, 2.95, 11.15, 1.25), box(8.75, 0.5, 1.25, 11.75, 3.5, 8),
					box(9.5, 0.67682, 1.167, 11, 2.17682, 1.667), box(10.9, 2.27682, 1.15, 11.7, 3.07682, 1.95), box(9.85, 2.52682, 1.15, 10.65, 3.32682, 1.95), box(8.825, 2.27682, 1.15, 9.625, 3.07682, 1.95), box(4, 0.5, 1.25, 7, 3.5, 8),
					box(4.75, 0.71235, 1.167, 6.25, 2.21235, 1.667), box(6.15, 2.31235, 1.15, 6.95, 3.11235, 1.95), box(5.1, 2.56235, 1.15, 5.9, 3.36235, 1.95), box(4.075, 2.31235, 1.15, 4.875, 3.11235, 1.95));
			case EAST -> Shapes.or(box(5, 12, 5, 11, 18, 11), box(4.75, 11.55, 4.75, 11.25, 18.05, 11.25), box(4.7, 13.4, 4.7, 11.3, 18, 11.3), box(10.6, 12.1, 6.6, 11.4, 12.9, 9.4), box(10.35, 12.6, 6.1, 12.15, 14.4, 9.9),
					box(0.5, 16.225, 7.6, 1.3, 17.025, 8.4), box(6.4, 16.91144, 10.58566, 8.6, 20.11144, 11.78566), box(6.4, 16.91144, 4.21434, 8.6, 20.11144, 5.41434), box(5.2, 11.7, 10.45, 10.8, 13.3, 11.05),
					box(5.2, 12.55235, 10.36066, 10.8, 14.15235, 10.96066), box(5.2, 11.7, 4.95, 10.8, 13.3, 5.55), box(5.2, 12.55235, 5.03934, 10.8, 14.15235, 5.63934), box(6.9, 1.9, 4.9, 9.1, 12.1, 11.1), box(6.85, 1.85, 4.85, 9.15, 12.15, 11.15),
					box(4.56624, -0.69821, 6.2, 9.16624, 2.90179, 9.8), box(2.71005, -3.31697, 5.8, 8.11005, 1.08303, 10.2), box(-0.58216, 10.2863, 5.5, 4.41784, 18.2863, 10.5), box(-4.11926, 12.8907, 5.85, 0.18074, 16.1907, 10.15),
					box(8.8, 9.65235, 5.05, 9.7, 11.55235, 10.95), box(9.4, 9.25235, 5.15, 10.1, 10.95235, 10.85), box(6.3, 9.65235, 5.05, 7.2, 11.55235, 10.95), box(5.9, 9.25235, 5.15, 6.6, 10.95235, 10.85), box(6.5, 8.5, 11, 15.5, 11.5, 14),
					box(15.05, 8.625, 11.75, 15.55, 10.125, 13.25), box(14.75, 10.35, 13.15, 15.55, 11.15, 13.95), box(14.75, 10.55, 12.15, 15.55, 11.35, 12.95), box(14.75, 10.35, 11.15, 15.55, 11.15, 11.95), box(6.5, 8.5, 2, 15.5, 11.5, 5),
					box(15.05, 8.625, 2.75, 15.55, 10.125, 4.25), box(14.75, 10.35, 4.15, 15.55, 11.15, 4.95), box(14.75, 10.55, 3.15, 15.55, 11.35, 3.95), box(14.75, 10.35, 2.15, 15.55, 11.15, 2.95), box(8, 0.5, 8.75, 14.75, 3.5, 11.75),
					box(14.333, 0.67682, 9.5, 14.833, 2.17682, 11), box(14.05, 2.27682, 10.9, 14.85, 3.07682, 11.7), box(14.05, 2.52682, 9.85, 14.85, 3.32682, 10.65), box(14.05, 2.27682, 8.825, 14.85, 3.07682, 9.625), box(8, 0.5, 4, 14.75, 3.5, 7),
					box(14.333, 0.71235, 4.75, 14.833, 2.21235, 6.25), box(14.05, 2.31235, 6.15, 14.85, 3.11235, 6.95), box(14.05, 2.56235, 5.1, 14.85, 3.36235, 5.9), box(14.05, 2.31235, 4.075, 14.85, 3.11235, 4.875));
			case WEST -> Shapes.or(box(5, 12, 5, 11, 18, 11), box(4.75, 11.55, 4.75, 11.25, 18.05, 11.25), box(4.7, 13.4, 4.7, 11.3, 18, 11.3), box(4.6, 12.1, 6.6, 5.4, 12.9, 9.4), box(3.85, 12.6, 6.1, 5.65, 14.4, 9.9),
					box(14.7, 16.225, 7.6, 15.5, 17.025, 8.4), box(7.4, 16.91144, 4.21434, 9.6, 20.11144, 5.41434), box(7.4, 16.91144, 10.58566, 9.6, 20.11144, 11.78566), box(5.2, 11.7, 4.95, 10.8, 13.3, 5.55),
					box(5.2, 12.55235, 5.03934, 10.8, 14.15235, 5.63934), box(5.2, 11.7, 10.45, 10.8, 13.3, 11.05), box(5.2, 12.55235, 10.36066, 10.8, 14.15235, 10.96066), box(6.9, 1.9, 4.9, 9.1, 12.1, 11.1),
					box(6.85, 1.85, 4.85, 9.15, 12.15, 11.15), box(6.83376, -0.69821, 6.2, 11.43376, 2.90179, 9.8), box(7.88995, -3.31697, 5.8, 13.28995, 1.08303, 10.2), box(11.58216, 10.2863, 5.5, 16.58216, 18.2863, 10.5),
					box(15.81926, 12.8907, 5.85, 20.11926, 16.1907, 10.15), box(6.3, 9.65235, 5.05, 7.2, 11.55235, 10.95), box(5.9, 9.25235, 5.15, 6.6, 10.95235, 10.85), box(8.8, 9.65235, 5.05, 9.7, 11.55235, 10.95),
					box(9.4, 9.25235, 5.15, 10.1, 10.95235, 10.85), box(0.5, 8.5, 2, 9.5, 11.5, 5), box(0.45, 8.625, 2.75, 0.95, 10.125, 4.25), box(0.45, 10.35, 2.05, 1.25, 11.15, 2.85), box(0.45, 10.55, 3.05, 1.25, 11.35, 3.85),
					box(0.45, 10.35, 4.05, 1.25, 11.15, 4.85), box(0.5, 8.5, 11, 9.5, 11.5, 14), box(0.45, 8.625, 11.75, 0.95, 10.125, 13.25), box(0.45, 10.35, 11.05, 1.25, 11.15, 11.85), box(0.45, 10.55, 12.05, 1.25, 11.35, 12.85),
					box(0.45, 10.35, 13.05, 1.25, 11.15, 13.85), box(1.25, 0.5, 4.25, 8, 3.5, 7.25), box(1.167, 0.67682, 5, 1.667, 2.17682, 6.5), box(1.15, 2.27682, 4.3, 1.95, 3.07682, 5.1), box(1.15, 2.52682, 5.35, 1.95, 3.32682, 6.15),
					box(1.15, 2.27682, 6.375, 1.95, 3.07682, 7.175), box(1.25, 0.5, 9, 8, 3.5, 12), box(1.167, 0.71235, 9.75, 1.667, 2.21235, 11.25), box(1.15, 2.31235, 9.05, 1.95, 3.11235, 9.85), box(1.15, 2.56235, 10.1, 1.95, 3.36235, 10.9),
					box(1.15, 2.31235, 11.125, 1.95, 3.11235, 11.925));
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, flag);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 20;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}

	@Override
	public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
		super.use(blockstate, world, pos, entity, hand, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double hitX = hit.getLocation().x;
		double hitY = hit.getLocation().y;
		double hitZ = hit.getLocation().z;
		Direction direction = hit.getDirection();
		InteractPlushesProcedure.execute(world, x, y, z);
		return InteractionResult.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayer() {
		ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.SNEP_PLUSHE.get(), renderType -> renderType == RenderType.cutoutMipped());
	}
}
