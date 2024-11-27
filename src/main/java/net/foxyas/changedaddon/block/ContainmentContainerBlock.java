
package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.block.entity.ContainmentContainerBlockEntity;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.block.CustomFallable;
import net.ltxprogrammer.changed.block.NonLatexCoverableBlock;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.item.LatexFlask;
import net.ltxprogrammer.changed.item.LatexSyringe;
import net.ltxprogrammer.changed.item.Syringe;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ContainmentContainerBlock extends Block implements SimpleWaterloggedBlock, EntityBlock, NonLatexCoverableBlock , CustomFallable {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final VoxelShape SHAPE_WHOLE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

	public ContainmentContainerBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.GLASS).strength(3f, 5f).noOcclusion().isRedstoneConductor((bs, br, bp) -> false).requiresCorrectToolForDrops());
		this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
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
	public VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return SHAPE_WHOLE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return SHAPE_WHOLE;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
		return SHAPE_WHOLE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
		return this.defaultBlockState().setValue(WATERLOGGED, flag);
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
		world.scheduleTick(currentPos, this, this.getDelayAfterPlace());
		return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> dropsOriginal = super.getDrops(state, builder);
		if (!dropsOriginal.isEmpty())
			return dropsOriginal;
		return Collections.singletonList(new ItemStack(this, 1));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ContainmentContainerBlockEntity(pos, state);
	}

	public ContainmentContainerBlockEntity getBlockEntity(BlockState state, BlockGetter level, BlockPos pos){
		if (level.getBlockEntity(pos) instanceof ContainmentContainerBlockEntity blockEntity){
			return blockEntity;
		}
		return null;
	}

	@Override
	public boolean triggerEvent(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity == null ? false : blockEntity.triggerEvent(eventID, eventParam);
	}

	protected int getDelayAfterPlace() {
		return 2;
	}

	@Override
	public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState p_53236_, boolean p_53237_) {
		level.scheduleTick(pos, this, this.getDelayAfterPlace());
		super.onPlace(state, level, pos, p_53236_, p_53237_);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random p_60465_) {
		level.scheduleTick(pos, this, this.getDelayAfterPlace());
		if (pos.getY() >= level.getMinBuildHeight() && !this.canSurvive(state,level,pos)) {
			Optional<ContainmentContainerBlockEntity> blockEntity = level.getBlockEntity(pos, ChangedAddonRegisters.ChangedAddonBlockEntitys.CONTAINMENT_CONTAINER.get());
			CompoundTag blockData = blockEntity.map(BlockEntity::saveWithFullMetadata).orElse(null);
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), 67);
			FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
			fallingBlockEntity.blockData = blockData;
			this.falling(fallingBlockEntity);
		}
		super.tick(state, level, pos, p_60465_);
	}

	protected void falling(FallingBlockEntity blockEntity) {
		Changed.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY.with(() -> {
			return blockEntity;
		}), CustomFallable.updateBlockData(blockEntity));
	}

	@Override
	public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newState, boolean noSimulate) {
		super.onRemove(state, level, blockPos, newState, noSimulate);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos blockPos, Player player, boolean willHarvest, FluidState fluid) {
		var blockEntity = this.getBlockEntity(state,level,blockPos);
		if (blockEntity.getTransfurVariant() != null){
			if (blockEntity.getTransfurVariant().getEntityType().is(ChangedTags.EntityTypes.LATEX)) {
				ChangedEntity changedEntity = blockEntity.getTransfurVariant().getEntityType().create(level);
				assert changedEntity != null;
				changedEntity.moveTo(blockPos,changedEntity.getYRot(),changedEntity.getXRot());
				level.addFreshEntity(changedEntity);
			} else {
				if (blockEntity.getTransfurVariant().is(ChangedTransfurVariants.GAS_WOLF.get())
						|| blockEntity.getTransfurVariant().getRegistryName().toString().contains("gas")){
					if (level instanceof ServerLevel serverLevel){
						serverLevel.sendParticles(ChangedParticles.gas(blockEntity.getTransfurVariant().getColors().getFirst()),blockPos.getX() + 0.5f,blockPos.getY() + 0.5f,blockPos.getZ() + 0.5f,5,0.5,0.5,0.5,0);
						serverLevel.sendParticles(ChangedParticles.gas(blockEntity.getTransfurVariant().getColors().getSecond()),blockPos.getX() + 0.5f,blockPos.getY() + 0.5f,blockPos.getZ() + 0.5f,5,0.5,0.5,0.5,0);
					}
				}
			}
		}
		return super.onDestroyedByPlayer(state, level, blockPos, player, willHarvest, fluid);
	}

	@Override
	public void wasExploded(Level level, BlockPos blockPos, Explosion explosion) {
		var blockEntity = this.getBlockEntity(level.getBlockState(blockPos),level,blockPos);
		if (blockEntity != null && blockEntity.getTransfurVariant() != null){
			if (blockEntity.getTransfurVariant().getEntityType().is(ChangedTags.EntityTypes.LATEX)) {
				ChangedEntity changedEntity = blockEntity.getTransfurVariant().getEntityType().create(level);
				assert changedEntity != null;
				changedEntity.moveTo(blockPos,changedEntity.getYRot(),changedEntity.getXRot());
				level.addFreshEntity(changedEntity);
			} else {
				if (blockEntity.getTransfurVariant().is(ChangedTransfurVariants.GAS_WOLF.get())
						|| blockEntity.getTransfurVariant().getRegistryName().toString().contains("gas")){
					if (level instanceof ServerLevel serverLevel){
						serverLevel.sendParticles(ChangedParticles.gas(blockEntity.getTransfurVariant().getColors().getFirst()),blockPos.getX() + 0.5f,blockPos.getY() + 0.5f,blockPos.getZ() + 0.5f,5,0.5,0.5,0.5,0);
						serverLevel.sendParticles(ChangedParticles.gas(blockEntity.getTransfurVariant().getColors().getSecond()),blockPos.getX() + 0.5f,blockPos.getY() + 0.5f,blockPos.getZ() + 0.5f,5,0.5,0.5,0.5,0);
					}
				}
			}
		}
		super.wasExploded(level, blockPos, explosion);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		var blockEntity = this.getBlockEntity(state,level,pos);
		ItemStack selectedItem = player.getItemInHand(interactionHand);
		TransfurVariant<?> variant = Syringe.getVariant(selectedItem);


		if (variant != null && blockEntity.getTransfurVariant() == null){
			if (selectedItem.getItem() instanceof LatexSyringe
					|| selectedItem.getItem() instanceof LatexFlask
					&& !variant.getEntityType().is(ChangedTags.EntityTypes.PARTIAL_LATEX)){
				blockEntity.setTransfurVariant(variant);
				level.blockUpdated(pos,this);
				level.updateNeighborsAt(pos,this);
				ItemStack normalSyringe = new ItemStack(ChangedItems.SYRINGE.get());
				ItemStack glassFlask = new ItemStack(ChangedItems.getBlockItem(ChangedBlocks.ERLENMEYER_FLASK.get()));
				if (selectedItem.getItem() instanceof LatexSyringe){
					selectedItem.shrink(1);
					if (!player.addItem(normalSyringe)){
						player.drop(normalSyringe,false);
					}
				} else if (selectedItem.getItem() instanceof LatexFlask) {
					selectedItem.shrink(1);
					if (!player.addItem(glassFlask)){
						player.drop(glassFlask,false);
					}
				}
				
			}
		} else if (selectedItem.getItem() instanceof Syringe
				&& blockEntity.getTransfurVariant() != null) {
			ItemStack latexSyringe = selectedItem.getItem() instanceof LatexSyringe ? new ItemStack(ChangedItems.LATEX_SYRINGE.get())
					: selectedItem.getItem() instanceof LatexFlask ? new ItemStack(ChangedItems.LATEX_FLASK.get())
						: new ItemStack(ChangedItems.LATEX_SYRINGE.get());
			Syringe.setVariant(latexSyringe,blockEntity.getTransfurVariant().getFormId());
			blockEntity.setTransfurVariant(null);
			level.blockUpdated(pos,this);
			level.updateNeighborsAt(pos,this);
			selectedItem.shrink(1);
			if (!player.addItem(latexSyringe)){
				player.drop(latexSyringe,false);
			}
		}

		return super.use(state, level, pos, player, interactionHand, blockHitResult);
	}

	public double getFallDistance(FallingBlockEntity falling) {
		return (double)falling.getStartPos().getY() - falling.getY();
	}

	public void onLand(Level level, BlockPos pos, BlockState state, BlockState fellOn, FallingBlockEntity falling) {
		Optional<ContainmentContainerBlockEntity> blockEntity = level.getBlockEntity(pos, ChangedAddonRegisters.ChangedAddonBlockEntitys.CONTAINMENT_CONTAINER.get());
		blockEntity.ifPresent((container) -> {
			if (falling.blockData != null) {
				container.load(falling.blockData);
			}

		});
		if (this.getFallDistance(falling) >= 3.0 && level.getFluidState(pos).isEmpty()) {
			level.playSound(null, pos, ChangedSounds.CRASH, SoundSource.BLOCKS, 1.0F, 1.0F);
			blockEntity.ifPresent((containerBlock) -> {
				if (containerBlock.getTransfurVariant() != null) {
					if (!containerBlock.isTransfurNotLatex(containerBlock.getTransfurVariant())) {
					ChangedEntity changedEntity = containerBlock.getTransfurVariant().getEntityType().create(level);
					assert changedEntity != null;
					changedEntity.moveTo(containerBlock.getBlockPos(),changedEntity.getYRot(),changedEntity.getXRot());
					level.addFreshEntity(changedEntity);
					} else if (containerBlock.isTransfurGasLike(containerBlock.getTransfurVariant())){
						if (level instanceof ServerLevel serverLevel){
							serverLevel.sendParticles(ChangedParticles.gas(containerBlock.getTransfurVariant().getColors().getFirst()),
									containerBlock.getBlockPos().getX() + 0.5f,
									containerBlock.getBlockPos().getY() + 0.5f,
									containerBlock.getBlockPos().getZ() + 0.5f,5,0.5,0.5,0.5,0);
							serverLevel.sendParticles(ChangedParticles.gas(containerBlock.getTransfurVariant().getColors().getSecond()),
									containerBlock.getBlockPos().getX() + 0.5f,
									containerBlock.getBlockPos().getY() + 0.5f,
									containerBlock.getBlockPos().getZ() + 0.5f,5,0.5,0.5,0.5,0);
						}
					}
				}
			});
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void registerRenderLayer() {
		ItemBlockRenderTypes.setRenderLayer(ChangedAddonModBlocks.CONTAINMENT_CONTAINER.get(), renderType -> renderType == RenderType.cutout());
	}

	@Override
	public ResourceLocation getModelName() {
		return new ModelResourceLocation(new ResourceLocation("changed_addon:containment_container"), "inventory");
	}

	/*@Override
	public boolean stateHasBlockEntity(BlockState blockState) {
		return blockState.getValue(HALF) == DoubleBlockHalf.LOWER;
	}*/
}
