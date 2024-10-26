
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

public class SnowFoxPlusheBlock extends Block implements SimpleWaterloggedBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public SnowFoxPlusheBlock() {
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
			default -> Shapes.or(box(5, 2, 7, 11, 12, 9), box(4.85, 1.85, 6.85, 11.15, 12.15, 9.15), box(6.2, -0.69821, 4.56624, 9.8, 2.90179, 9.16624), box(5.5, 9.51427, 1.97787, 10.5, 17.51427, 6.97787),
					box(5.85, 13.15713, -1.45865, 10.15, 16.45713, 2.84135), box(4.5, 12, 4.5, 11.5, 19, 11.5), box(6.6, 12.1, 11.35, 9.4, 12.9, 12.15), box(6.1, 12.85, 11.35, 9.9, 14.65, 13.15), box(7.6, 16.725, 1.55, 8.4, 17.525, 2.35),
					box(4.3, 11.6, 4.3, 11.7, 19, 11.7), box(4.2, 13.4, 4.2, 11.8, 19, 11.8), box(3.85, 11.85, 5.1, 4.65, 13.65, 10.9), box(3.58934, 13.10235, 5, 4.58934, 15.10235, 11), box(11.35, 11.85, 5.1, 12.15, 13.65, 10.9),
					box(11.41066, 13.10235, 5, 12.41066, 15.10235, 11), box(0.10711, 18.5, 6.4967, 3.00711, 21.4, 7.3967), box(1.01711, 18.42, 5.8067, 3.09711, 21.5, 6.8867), box(1.10711, 21.4, 6.4967, 3.00711, 22.3, 7.3967),
					box(2.10711, 22.3, 6.4967, 3.00711, 23.2, 7.3967), box(12.81612, 18.5, 6.67348, 15.71612, 21.4, 7.57348), box(12.72612, 18.42, 5.98348, 14.80612, 21.5, 7.06348), box(12.81612, 21.4, 6.67348, 14.71612, 22.3, 7.57348),
					box(12.81612, 22.3, 6.67348, 13.71612, 23.2, 7.57348), box(2, 8.5, 6.5, 5, 11.5, 15.5), box(2.75, 8.625, 15.05, 4.25, 10.125, 15.55), box(2.05, 10.35, 14.75, 2.85, 11.15, 15.55), box(3.05, 10.55, 14.75, 3.85, 11.35, 15.55),
					box(4.05, 10.35, 14.75, 4.85, 11.15, 15.55), box(11, 8.5, 6.5, 14, 11.5, 15.5), box(11.75, 8.625, 15.05, 13.25, 10.125, 15.55), box(11.05, 10.35, 14.75, 11.85, 11.15, 15.55), box(12.05, 10.55, 14.75, 12.85, 11.35, 15.55),
					box(13.05, 10.35, 14.75, 13.85, 11.15, 15.55), box(4.75, 0.5, 8, 7.75, 3.5, 14.75), box(5.5, 0.67682, 14.333, 7, 2.17682, 14.833), box(4.8, 2.27682, 14.05, 5.6, 3.07682, 14.85), box(5.85, 2.52682, 14.05, 6.65, 3.32682, 14.85),
					box(6.875, 2.27682, 14.05, 7.675, 3.07682, 14.85), box(8.25, 0.5, 8, 11.25, 3.5, 14.75), box(9, 0.71235, 14.333, 10.5, 2.21235, 14.833), box(8.3, 2.31235, 14.05, 9.1, 3.11235, 14.85),
					box(9.35, 2.56235, 14.05, 10.15, 3.36235, 14.85), box(10.375, 2.31235, 14.05, 11.175, 3.11235, 14.85));
			case NORTH -> Shapes.or(box(5, 2, 7, 11, 12, 9), box(4.85, 1.85, 6.85, 11.15, 12.15, 9.15), box(6.2, -0.69821, 6.83376, 9.8, 2.90179, 11.43376), box(5.5, 9.51427, 9.02213, 10.5, 17.51427, 14.02213),
					box(5.85, 13.15713, 13.15865, 10.15, 16.45713, 17.45865), box(4.5, 12, 4.5, 11.5, 19, 11.5), box(6.6, 12.1, 3.85, 9.4, 12.9, 4.65), box(6.1, 12.85, 2.85, 9.9, 14.65, 4.65), box(7.6, 16.725, 13.65, 8.4, 17.525, 14.45),
					box(4.3, 11.6, 4.3, 11.7, 19, 11.7), box(4.2, 13.4, 4.2, 11.8, 19, 11.8), box(11.35, 11.85, 5.1, 12.15, 13.65, 10.9), box(11.41066, 13.10235, 5, 12.41066, 15.10235, 11), box(3.85, 11.85, 5.1, 4.65, 13.65, 10.9),
					box(3.58934, 13.10235, 5, 4.58934, 15.10235, 11), box(12.99289, 18.5, 8.6033, 15.89289, 21.4, 9.5033), box(12.90289, 18.42, 9.1133, 14.98289, 21.5, 10.1933), box(12.99289, 21.4, 8.6033, 14.89289, 22.3, 9.5033),
					box(12.99289, 22.3, 8.6033, 13.89289, 23.2, 9.5033), box(0.28388, 18.5, 8.42652, 3.18388, 21.4, 9.32652), box(1.19388, 18.42, 8.93652, 3.27388, 21.5, 10.01652), box(1.28388, 21.4, 8.42652, 3.18388, 22.3, 9.32652),
					box(2.28388, 22.3, 8.42652, 3.18388, 23.2, 9.32652), box(11, 8.5, 0.5, 14, 11.5, 9.5), box(11.75, 8.625, 0.45, 13.25, 10.125, 0.95), box(13.15, 10.35, 0.45, 13.95, 11.15, 1.25), box(12.15, 10.55, 0.45, 12.95, 11.35, 1.25),
					box(11.15, 10.35, 0.45, 11.95, 11.15, 1.25), box(2, 8.5, 0.5, 5, 11.5, 9.5), box(2.75, 8.625, 0.45, 4.25, 10.125, 0.95), box(4.15, 10.35, 0.45, 4.95, 11.15, 1.25), box(3.15, 10.55, 0.45, 3.95, 11.35, 1.25),
					box(2.15, 10.35, 0.45, 2.95, 11.15, 1.25), box(8.25, 0.5, 1.25, 11.25, 3.5, 8), box(9, 0.67682, 1.167, 10.5, 2.17682, 1.667), box(10.4, 2.27682, 1.15, 11.2, 3.07682, 1.95), box(9.35, 2.52682, 1.15, 10.15, 3.32682, 1.95),
					box(8.325, 2.27682, 1.15, 9.125, 3.07682, 1.95), box(4.75, 0.5, 1.25, 7.75, 3.5, 8), box(5.5, 0.71235, 1.167, 7, 2.21235, 1.667), box(6.9, 2.31235, 1.15, 7.7, 3.11235, 1.95), box(5.85, 2.56235, 1.15, 6.65, 3.36235, 1.95),
					box(4.825, 2.31235, 1.15, 5.625, 3.11235, 1.95));
			case EAST -> Shapes.or(box(7, 2, 5, 9, 12, 11), box(6.85, 1.85, 4.85, 9.15, 12.15, 11.15), box(4.56624, -0.69821, 6.2, 9.16624, 2.90179, 9.8), box(1.97787, 9.51427, 5.5, 6.97787, 17.51427, 10.5),
					box(-1.45865, 13.15713, 5.85, 2.84135, 16.45713, 10.15), box(4.5, 12, 4.5, 11.5, 19, 11.5), box(11.35, 12.1, 6.6, 12.15, 12.9, 9.4), box(11.35, 12.85, 6.1, 13.15, 14.65, 9.9), box(1.55, 16.725, 7.6, 2.35, 17.525, 8.4),
					box(4.3, 11.6, 4.3, 11.7, 19, 11.7), box(4.2, 13.4, 4.2, 11.8, 19, 11.8), box(5.1, 11.85, 11.35, 10.9, 13.65, 12.15), box(5, 13.10235, 11.41066, 11, 15.10235, 12.41066), box(5.1, 11.85, 3.85, 10.9, 13.65, 4.65),
					box(5, 13.10235, 3.58934, 11, 15.10235, 4.58934), box(6.4967, 18.5, 12.99289, 7.3967, 21.4, 15.89289), box(5.8067, 18.42, 12.90289, 6.8867, 21.5, 14.98289), box(6.4967, 21.4, 12.99289, 7.3967, 22.3, 14.89289),
					box(6.4967, 22.3, 12.99289, 7.3967, 23.2, 13.89289), box(6.67348, 18.5, 0.28388, 7.57348, 21.4, 3.18388), box(5.98348, 18.42, 1.19388, 7.06348, 21.5, 3.27388), box(6.67348, 21.4, 1.28388, 7.57348, 22.3, 3.18388),
					box(6.67348, 22.3, 2.28388, 7.57348, 23.2, 3.18388), box(6.5, 8.5, 11, 15.5, 11.5, 14), box(15.05, 8.625, 11.75, 15.55, 10.125, 13.25), box(14.75, 10.35, 13.15, 15.55, 11.15, 13.95), box(14.75, 10.55, 12.15, 15.55, 11.35, 12.95),
					box(14.75, 10.35, 11.15, 15.55, 11.15, 11.95), box(6.5, 8.5, 2, 15.5, 11.5, 5), box(15.05, 8.625, 2.75, 15.55, 10.125, 4.25), box(14.75, 10.35, 4.15, 15.55, 11.15, 4.95), box(14.75, 10.55, 3.15, 15.55, 11.35, 3.95),
					box(14.75, 10.35, 2.15, 15.55, 11.15, 2.95), box(8, 0.5, 8.25, 14.75, 3.5, 11.25), box(14.333, 0.67682, 9, 14.833, 2.17682, 10.5), box(14.05, 2.27682, 10.4, 14.85, 3.07682, 11.2), box(14.05, 2.52682, 9.35, 14.85, 3.32682, 10.15),
					box(14.05, 2.27682, 8.325, 14.85, 3.07682, 9.125), box(8, 0.5, 4.75, 14.75, 3.5, 7.75), box(14.333, 0.71235, 5.5, 14.833, 2.21235, 7), box(14.05, 2.31235, 6.9, 14.85, 3.11235, 7.7), box(14.05, 2.56235, 5.85, 14.85, 3.36235, 6.65),
					box(14.05, 2.31235, 4.825, 14.85, 3.11235, 5.625));
			case WEST -> Shapes.or(box(7, 2, 5, 9, 12, 11), box(6.85, 1.85, 4.85, 9.15, 12.15, 11.15), box(6.83376, -0.69821, 6.2, 11.43376, 2.90179, 9.8), box(9.02213, 9.51427, 5.5, 14.02213, 17.51427, 10.5),
					box(13.15865, 13.15713, 5.85, 17.45865, 16.45713, 10.15), box(4.5, 12, 4.5, 11.5, 19, 11.5), box(3.85, 12.1, 6.6, 4.65, 12.9, 9.4), box(2.85, 12.85, 6.1, 4.65, 14.65, 9.9), box(13.65, 16.725, 7.6, 14.45, 17.525, 8.4),
					box(4.3, 11.6, 4.3, 11.7, 19, 11.7), box(4.2, 13.4, 4.2, 11.8, 19, 11.8), box(5.1, 11.85, 3.85, 10.9, 13.65, 4.65), box(5, 13.10235, 3.58934, 11, 15.10235, 4.58934), box(5.1, 11.85, 11.35, 10.9, 13.65, 12.15),
					box(5, 13.10235, 11.41066, 11, 15.10235, 12.41066), box(8.6033, 18.5, 0.10711, 9.5033, 21.4, 3.00711), box(9.1133, 18.42, 1.01711, 10.1933, 21.5, 3.09711), box(8.6033, 21.4, 1.10711, 9.5033, 22.3, 3.00711),
					box(8.6033, 22.3, 2.10711, 9.5033, 23.2, 3.00711), box(8.42652, 18.5, 12.81612, 9.32652, 21.4, 15.71612), box(8.93652, 18.42, 12.72612, 10.01652, 21.5, 14.80612), box(8.42652, 21.4, 12.81612, 9.32652, 22.3, 14.71612),
					box(8.42652, 22.3, 12.81612, 9.32652, 23.2, 13.71612), box(0.5, 8.5, 2, 9.5, 11.5, 5), box(0.45, 8.625, 2.75, 0.95, 10.125, 4.25), box(0.45, 10.35, 2.05, 1.25, 11.15, 2.85), box(0.45, 10.55, 3.05, 1.25, 11.35, 3.85),
					box(0.45, 10.35, 4.05, 1.25, 11.15, 4.85), box(0.5, 8.5, 11, 9.5, 11.5, 14), box(0.45, 8.625, 11.75, 0.95, 10.125, 13.25), box(0.45, 10.35, 11.05, 1.25, 11.15, 11.85), box(0.45, 10.55, 12.05, 1.25, 11.35, 12.85),
					box(0.45, 10.35, 13.05, 1.25, 11.15, 13.85), box(1.25, 0.5, 4.75, 8, 3.5, 7.75), box(1.167, 0.67682, 5.5, 1.667, 2.17682, 7), box(1.15, 2.27682, 4.8, 1.95, 3.07682, 5.6), box(1.15, 2.52682, 5.85, 1.95, 3.32682, 6.65),
					box(1.15, 2.27682, 6.875, 1.95, 3.07682, 7.675), box(1.25, 0.5, 8.25, 8, 3.5, 11.25), box(1.167, 0.71235, 9, 1.667, 2.21235, 10.5), box(1.15, 2.31235, 8.3, 1.95, 3.11235, 9.1), box(1.15, 2.56235, 9.35, 1.95, 3.36235, 10.15),
					box(1.15, 2.31235, 10.375, 1.95, 3.11235, 11.175));
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
		ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.SNOW_FOX_PLUSHE.get(), renderType -> renderType == RenderType.cutoutMipped());
	}
}
