package net.foxyas.changedaddon.mixins.items;

import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.block.interfaces.IBrushableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin {

    @Inject(
            method = "onUseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"
            ),
            cancellable = true)
    private void onCustomBrushTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration, CallbackInfo ci,
                                   @Local Player player,
                                   @Local BlockHitResult blockHitResult,
                                   @Local BlockState blockState,
                                   @Local BlockPos blockPos) {
        if (blockState.getBlock() instanceof IBrushableBlock brushable) {
            ci.cancel();
            pLevel.playSound(player, blockPos, brushable.getBrushSound(), SoundSource.BLOCKS);
            if (!pLevel.isClientSide()) {
                if (brushable.brush(pLevel, blockState, blockPos, player, blockHitResult.getDirection(), pStack)) {
                    EquipmentSlot equipmentslot = pStack.equals(player.getItemBySlot(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                    pStack.hurtAndBreak(1, pLivingEntity, (livingEntity) -> {
                        livingEntity.broadcastBreakEvent(equipmentslot);
                    });
                }
            }
        }
    }
}