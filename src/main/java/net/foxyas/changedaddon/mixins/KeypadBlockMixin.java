package net.foxyas.changedaddon.mixins;

import net.foxyas.changedaddon.block.advanced.TimedKeypadBlockEntity;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.block.AbstractCustomShapeEntityBlock;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.ltxprogrammer.changed.block.entity.KeypadBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeypadBlock.class)
public class KeypadBlockMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void useInjector(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir){
        if (player.getUseItem().is(Items.CLOCK) && player.isShiftKeyDown()) {
            BlockState copy = ChangedAddonRegisters.ChangedAddonBlocks.TIMED_KEYPAD.get().defaultBlockState();
            copy.setValue(KeypadBlock.FACING, state.getValue(KeypadBlock.FACING));
            copy.setValue(KeypadBlock.POWERED, state.getValue(KeypadBlock.POWERED));
            BlockEntity originalBlockEntity = level.getBlockEntity(pos);
            level.setBlockAndUpdate(pos, copy);
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (originalBlockEntity instanceof KeypadBlockEntity keypadBlockEntity) {
                if (blockEntity instanceof TimedKeypadBlockEntity timedKeypadBlockEntity) {
                    timedKeypadBlockEntity.code = keypadBlockEntity.code;
                    level.sendBlockUpdated(pos, copy, copy, 3);
                }
            }
            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
        }
    }
}
