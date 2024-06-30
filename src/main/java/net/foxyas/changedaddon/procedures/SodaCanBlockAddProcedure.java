package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.block.SnepsiCanBlock;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.ltxprogrammer.changed.block.AbstractPuddle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class SodaCanBlockAddProcedure {


	/*@SubscribeEvent
	public static void UseItem(LivingEntityUseItemEvent.Start event){
		if(event.getEntity().isCrouching()){
			if (event.getItem().is(ChangedAddonModItems.SNEPSI.get())) {
				event.setCanceled(true);
			} else if (event.getItem().is(ChangedAddonModItems.FOXTA.get())) {
				event.setCanceled(true);
			}
		}
	}*/


	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		//Only if the player is Crouching
		/*if(!event.getPlayer().isCrouching()){
			event.setCanceled(true); 
		}*/

		//Basic Variables
		Direction direction = event.getFace();
		DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
		BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

		//Blocks
		Block snepsi = ChangedAddonModBlocks.SNEPSI_CAN.get();
		Block foxta = ChangedAddonModBlocks.FOXTA_CAN.get();
		BlockState block; //Place Block

		//BlockStates
		BlockState SnepsiState = snepsi.defaultBlockState();
		SnepsiState.setValue(FACING, event.getPlayer().getDirection().getOpposite());
		BlockState FoxtaState = foxta.defaultBlockState();
		FoxtaState.setValue(FACING, event.getPlayer().getDirection().getOpposite());

		//Level Aka World
		Level world = event.getWorld();

		//Cords of the pos
		double x = event.getPos().getX();
		double y = event.getPos().getY();
		double z = event.getPos().getZ();

		//Item Handle to get which one is to be placed
		if (event.getItemStack().is(ChangedAddonModItems.SNEPSI_CAN.get())) {
			block = SnepsiState;
		} else if (event.getItemStack().is(ChangedAddonModItems.FOXTA_CAN.get())) {
			block = FoxtaState;
		} else {
			event.setCanceled(true); 
		}

		//BlockPos Handle
		BlockPos Up = new BlockPos(x, y + 1, z);
		BlockPos DownOfUp = new BlockPos(x, y, z);
		BlockPos Down = new BlockPos(x, y - 1, z);
		BlockPos DownOfDown = new BlockPos(x, y - 2, z);
		BlockPos NORTH = new BlockPos(x, y, z - 1);
		BlockPos DownOfNORTH = new BlockPos(x, y - 1, z - 1);
		BlockPos SOUTH = new BlockPos(x, y, z + 1);
		BlockPos DownOfSOUTH = new BlockPos(x, y -1, z + 1);
		BlockPos WEST = new BlockPos(x - 1, y, z);
		BlockPos DownOfWEST = new BlockPos(x - 1, y - 1, z);
		BlockPos EAST = new BlockPos(x + 1, y, z);
		BlockPos DownOfEAST = new BlockPos(x + 1, y - 1, z);
		
		boolean isWater = world.getBlockState(EAST).is(Blocks.WATER);
		
		
		//Main logic Handle
		if (direction == Direction.UP) {
			if (!world.getBlockState(DownOfUp).isCollisionShapeFullBlock(world,DownOfUp)){
				event.setCanceled(true); 
			}
			/*if (!world.getBlockState(Up).isAir() && !world.getBlockState(Up).is(Blocks.WATER)){
				event.setCanceled(true); 
			}
			/*block.setValue(WATERLOGGED, world.getBlockState(Up).is(Blocks.WATER));
			world.setBlock(Up, block,3);
			SetValues(Up,world,WATERLOGGED,isWater,FACING, event.getPlayer().getDirection().getOpposite());
			world.playSound(event.getPlayer(),Up,block.getSoundType().getPlaceSound(),SoundSource.BLOCKS,1,1);
			event.getPlayer().swing(event.getHand());
			if(event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()){
				event.getItemStack().shrink(1);
			}*/
		} else if (direction == Direction.DOWN) {
			if (!world.getBlockState(DownOfDown).isCollisionShapeFullBlock(world,DownOfDown)){
				event.setCanceled(true); 
			}
			/*if (!world.getBlockState(Down).isAir() && !world.getBlockState(Down).is(Blocks.WATER)){
				event.setCanceled(true); 
			}
			/*block.setValue(WATERLOGGED, world.getBlockState(Down).is(Blocks.WATER));
			world.setBlock(Down, block,3);
			SetValues(Down,world,WATERLOGGED,isWater,FACING, event.getPlayer().getDirection().getOpposite());
			world.playSound(event.getPlayer(),Down,block.getSoundType().getPlaceSound(),SoundSource.BLOCKS,1,1);
			event.getPlayer().swing(event.getHand());
			if(event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()){
				event.getItemStack().shrink(1);
			}*/
		}
		if (direction == Direction.NORTH) {
			if (!world.getBlockState(DownOfNORTH).isCollisionShapeFullBlock(world,DownOfNORTH)){
				event.setCanceled(true); 
			}
			/*if (!world.getBlockState(NORTH).isAir() && !world.getBlockState(NORTH).is(Blocks.WATER)){
				event.setCanceled(true); 
			}
			/*block.setValue(WATERLOGGED, world.getBlockState(NORTH).is(Blocks.WATER));
			world.setBlock(NORTH, block,3);
			SetValues(NORTH,world,WATERLOGGED,isWater,FACING, event.getPlayer().getDirection().getOpposite());
			world.playSound(event.getPlayer(),NORTH,block.getSoundType().getPlaceSound(),SoundSource.BLOCKS,1,1);
			event.getPlayer().swing(event.getHand());
			if(event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()){
				event.getItemStack().shrink(1);
			}*/
		} else if (direction == Direction.SOUTH) {
			if (!world.getBlockState(DownOfSOUTH).isCollisionShapeFullBlock(world,DownOfSOUTH)){
				event.setCanceled(true); 
			}
			/*if (!world.getBlockState(SOUTH).isAir() && !world.getBlockState(SOUTH).is(Blocks.WATER)){
				event.setCanceled(true); 
			}
			/*block.setValue(WATERLOGGED, world.getBlockState(SOUTH).is(Blocks.WATER));
			world.setBlock(SOUTH, block,3);
			SetValues(SOUTH,world,WATERLOGGED,isWater,FACING, event.getPlayer().getDirection().getOpposite());
			world.playSound(event.getPlayer(),SOUTH,block.getSoundType().getPlaceSound(),SoundSource.BLOCKS,1,1);
			event.getPlayer().swing(event.getHand());
			if(event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()){
				event.getItemStack().shrink(1);
			}*/
		}
		if (direction == Direction.WEST) {
			if (!world.getBlockState(DownOfWEST).isCollisionShapeFullBlock(world,DownOfWEST)){
				event.setCanceled(true); 
			}
			/*if (!world.getBlockState(WEST).isAir() && !world.getBlockState(WEST).is(Blocks.WATER)){
				event.setCanceled(true); 
			}
			/*block.setValue(WATERLOGGED, world.getBlockState(WEST).is(Blocks.WATER));
			world.setBlock(WEST, block,3);
			SetValues(WEST,world,WATERLOGGED,isWater,FACING, event.getPlayer().getDirection().getOpposite());
			world.playSound(event.getPlayer(),WEST,block.getSoundType().getPlaceSound(),SoundSource.BLOCKS,1,1);
			event.getPlayer().swing(event.getHand());
			if(event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()){
				event.getItemStack().shrink(1);
			}*/
		} else if (direction == Direction.EAST) {
			if (!world.getBlockState(DownOfEAST).isCollisionShapeFullBlock(world,DownOfEAST)){
				event.setCanceled(true); 
			}
			/*if (!world.getBlockState(EAST).isAir() && !world.getBlockState(EAST).is(Blocks.WATER)){
				event.setCanceled(true); 
			}
			/*block.setValue(WATERLOGGED, world.getBlockState(EAST).is(Blocks.WATER));
			world.setBlock(EAST, block,3);
			SetValues(EAST,world,WATERLOGGED,isWater,FACING, event.getPlayer().getDirection().getOpposite());
			world.playSound(event.getPlayer(),EAST,block.getSoundType().getPlaceSound(),SoundSource.BLOCKS,1,1);
			event.getPlayer().swing(event.getHand());
			if(event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()){
				event.getItemStack().shrink(1);
			}*/
		}
	}

	/*public static void SetValues(BlockPos Block, Level World,BooleanProperty property,boolean value,DirectionProperty property2,Direction value2){
		World.getBlockState(Block).setValue(property,value);
		World.getBlockState(Block).setValue(property2,value2);
	}*/
}