package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.block.AbstractWolfCrystalExtender;
import net.ltxprogrammer.changed.block.SmallWolfCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SmallWolfCrystal.class,remap = false)
public class WolfCrystalSmallMixin {

    @Inject(method = "mayPlaceOn",at = @At("HEAD"),cancellable = true)
    private void SurviveMixin(BlockState blockState, BlockGetter p_51043_, BlockPos p_51044_, CallbackInfoReturnable<Boolean> cir){
        if (blockState.getBlock() instanceof AbstractWolfCrystalExtender.AbstractWolfCrystalSmall){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canSurvive",at = @At("TAIL"),cancellable = true)
    private void CanSurviveMixin(BlockState blockState, LevelReader level, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir){
        BlockState blockStateOn = level.getBlockState(blockPos.below());
        if (blockStateOn.getBlock() instanceof AbstractWolfCrystalExtender.AbstractWolfCrystalSmall){
            cir.setReturnValue(true);
        }
    }
}
