package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class SodaCanBlockAddProcedure {

    @SubscribeEvent
    public static void UseItem(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity().isCrouching()) {
            if (event.getItem().is(ChangedAddonItems.SNEPSI.get()) || event.getItem().is(ChangedAddonItems.FOXTA.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getPlayer().isCrouching()) {
            return;
        }

        Direction direction = event.getFace();
        DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
        BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

        Block block = event.getItemStack().is(ChangedAddonItems.SNEPSI.get()) ? ChangedAddonBlocks.SNEPSI_CAN.get() :
                event.getItemStack().is(ChangedAddonItems.FOXTA.get()) ? ChangedAddonBlocks.FOXTA_CAN.get() : Blocks.AIR;

        if (block == Blocks.AIR) {
            return;
        }

        BlockState blockState = block.defaultBlockState().setValue(FACING, event.getPlayer().getDirection().getOpposite());

        Level world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockPos targetPos;

        switch (Objects.requireNonNull(direction)) {
            case UP:
                targetPos = pos.above();
                break;
            case DOWN:
                targetPos = pos.below();
                break;
            case NORTH:
                targetPos = pos.north();
                break;
            case SOUTH:
                targetPos = pos.south();
                break;
            case WEST:
                targetPos = pos.west();
                break;
            case EAST:
                targetPos = pos.east();
                break;
            default:
                return;
        }

        if (!world.getBlockState(targetPos).isAir() && !world.getBlockState(targetPos).is(Blocks.WATER)) {
            return;
        }

        if (!world.getBlockState(targetPos.below()).isFaceSturdy(world, targetPos.below(), Direction.UP)) {
            return;
        }

        boolean isWater = world.getBlockState(targetPos).getFluidState().is(FluidTags.WATER) && world.getBlockState(targetPos).getFluidState().isSource();
        blockState = blockState.setValue(WATERLOGGED, isWater);

        if (world.setBlock(targetPos, blockState, 3)) {
            world.playSound(event.getPlayer(), targetPos, blockState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            event.getPlayer().swing(event.getHand());
            if (event.getPlayer() instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer().isSurvival()) {
                event.getItemStack().shrink(1);
            }
        }
    }
}
