
package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.entity.SnepPlushBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.procedures.InteractPlushesProcedure;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SnepPlushBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

	public enum CansEnum implements StringRepresentable {
		NONE("none"),
		RIGHT("right"),
		LEFT("left"),
		HUG("hug"),
		BOTH("both");

		private final String name;

		CansEnum(String name) {
			this.name = name;
		}

		@Override
		public @NotNull String getSerializedName() {
			return this.name;
		}
	}
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final EnumProperty<CansEnum> CANS = EnumProperty.create("cans", CansEnum.class);

	public SnepPlushBlock() {
		super(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL).strength(0.5f, 5f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false).setValue(CANS, CansEnum.NONE));
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
		return state.getFluidState().isEmpty();
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
	public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			default -> box(4, 0, 4, 12, 18, 12.5);
			case NORTH -> box(4, 0, 3.5, 12, 18, 12);
			case EAST -> box(4, 0, 4, 12.5, 18, 12);
			case WEST -> box(3.5, 0, 4, 12, 18, 12);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, WATERLOGGED, CANS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, flag)
				.setValue(CANS, CansEnum.NONE); // NONE by Default
	}

	public @NotNull BlockState rotate(BlockState state, Rotation rot) {
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
	public BlockState updateShape(BlockState state, Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
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
	public InteractionResult use(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player entity, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		super.use(blockstate, world, pos, entity, hand, hit);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		double hitX = hit.getLocation().x;
		double hitY = hit.getLocation().y;
		double hitZ = hit.getLocation().z;
		Direction direction = hit.getDirection();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SnepPlushBlockEntity PlusheBlockEntity){
			if (!PlusheBlockEntity.isSqueezed()){
				InteractPlushesProcedure.execute(world, x, y, z);
				PlusheBlockEntity.squeezedTicks = 10;
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SnepPlushBlockEntity(pos, state);
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);

		// Chance muito pequena (ex: 1 em 100)
		Random random = new Random();
		if (random.nextInt(100) == 0) {  // 1% de chance
			// Gerar um valor aleatório para CANS, ignorando NONE
			CansEnum[] possibleValues = {CansEnum.RIGHT, CansEnum.LEFT, CansEnum.HUG, CansEnum.BOTH};
			CansEnum randomCans = possibleValues[random.nextInt(possibleValues.length)];

			// Criar um novo estado de bloco com o valor alterado de CANS
			BlockState newBlockState = blockstate.setValue(CANS, randomCans);

			// Atualizar o estado do bloco no mundo
			world.setBlock(pos, newBlockState, 3);  // Muda o estado do bloco
		}

		world.scheduleTick(pos, this, 10);  // Continua o tick
	}


	@Override
	public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, Random p_60465_) {
		super.tick(state, serverLevel, pos, p_60465_);
		BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
		if (blockEntity instanceof SnepPlushBlockEntity snepPlushBlockEntity && snepPlushBlockEntity.isSqueezed()){
			snepPlushBlockEntity.subSqueezedTicks(1);
		}
		serverLevel.scheduleTick(pos, this, 10);
	}

	@Override
	public boolean triggerEvent(BlockState state, Level world, BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayer() {
		ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.SNEP_PLUSH.get(), renderType -> renderType == RenderType.cutoutMipped());
	}
}
