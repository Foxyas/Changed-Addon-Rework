package net.foxyas.changedaddon.mixins.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.block.interfaces.IBrushableBlock; // Certifique-se de importar sua interface
import net.minecraft.core.BlockPos;
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

@Mixin(BrushItem.class)
public abstract class BrushItemMixin {

    @WrapOperation(
            method = "onUseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BrushItem;calculateHitResult(Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/phys/HitResult;"
            )
    )
    private HitResult afterFlagCheck(BrushItem instance, LivingEntity entity, Operation<HitResult> original,
                                     Level level, LivingEntity pLivingEntity, ItemStack stack, int remainingUseDuration) {

        HitResult hitResult = original.call(instance, entity);

        if (hitResult instanceof BlockHitResult blockHitResult && hitResult.getType() == HitResult.Type.BLOCK) {
            int useDuration = instance.getUseDuration(stack) - remainingUseDuration + 1;
            boolean isBrushTick = useDuration % 10 == 5;

            if (isBrushTick && entity instanceof Player player) {
                BlockPos pos = blockHitResult.getBlockPos();
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof IBrushableBlock brushable) {
                    brushable.onBrush(level, state, pos, player, blockHitResult.getDirection(), stack);
                }
            }
        }

        return hitResult;
    }
}