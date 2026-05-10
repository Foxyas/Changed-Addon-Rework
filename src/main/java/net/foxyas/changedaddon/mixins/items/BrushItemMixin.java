package net.foxyas.changedaddon.mixins.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.block.interfaces.IBrushableBlock;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(BrushItem.class)
public abstract class BrushItemMixin {

    @WrapOperation(method = "calculateHitResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getHitResultOnViewVector(Lnet/minecraft/world/entity/Entity;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/HitResult;"))
    private HitResult mayGetBrushAbleBlocks(Entity pProjectile, Predicate<Entity> pFilter, double pScale, Operation<HitResult> original) {
        HitResult call = original.call(pProjectile, pFilter, pScale);
        if (pProjectile instanceof LivingEntity livingEntity) {
            HitResult pick = livingEntity.pick(livingEntity instanceof Player player ? player.getBlockReach() : 4, 0, false);
            if (pick.getType() == HitResult.Type.MISS) {
                return call;
            }

            if (pick instanceof BlockHitResult blockHitResult) {
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState blockState = livingEntity.level().getBlockState(pos);
                if (blockState.getBlock() instanceof IBrushableBlock iBrushableBlock) {
                    return blockHitResult;
                }
            }
        }

        return call;
    }
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