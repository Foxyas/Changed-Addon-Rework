package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.ltxprogrammer.changed.block.SmallWolfCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = SmallWolfCrystal.class,remap = true)
public class WolfCrystalSmallMixin {

    @Inject(method = "mayPlaceOn",at = @At("HEAD"),cancellable = true)
    private void SurviveMixin(BlockState blockState, BlockGetter p_51043_, BlockPos p_51044_, CallbackInfoReturnable<Boolean> cir){
        List<Block> blockList = List.of(
                ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_BLOCK.get(),
                ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_BLOCK.get(),
                ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_BLOCK.get(),
                ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_BLOCK.get()
        );
        if (blockList.contains(blockState.getBlock())){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canSurvive",at = @At("HEAD"),cancellable = true)
    private void CanSurviveMixin(BlockState blockState, LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir){
        BlockState blockStateOn = level.getBlockState(blockPos.below());
        List<Block> blockList = List.of(
                ChangedAddonBlocks.WHITE_WOLF_CRYSTAL_BLOCK.get(),
                ChangedAddonBlocks.BLUE_WOLF_CRYSTAL_BLOCK.get(),
                ChangedAddonBlocks.ORANGE_WOLF_CRYSTAL_BLOCK.get(),
                ChangedAddonBlocks.YELLOW_WOLF_CRYSTAL_BLOCK.get()
        );
        if (blockList.contains(blockStateOn.getBlock())){
            cir.setReturnValue(true);
        }
    }
}
