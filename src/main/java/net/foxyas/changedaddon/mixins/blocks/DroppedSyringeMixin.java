package net.foxyas.changedaddon.mixins.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.util.LevelUtil;
import net.ltxprogrammer.changed.block.DroppedSyringe;
import net.ltxprogrammer.changed.block.entity.DroppedSyringeBlockEntity;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DroppedSyringe.class)
public abstract class DroppedSyringeMixin {

    @WrapOperation(
            method = "lambda$entityInside$0",
            at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;progressTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/ai/LatexAssimilationDecision;)Z", remap = false)
    )
    private boolean isEntityTouchingCheck(LivingEntity entity, LatexAssimilationDecision<?> decision, Operation<Boolean> original, @Local(argsOnly = true) DroppedSyringeBlockEntity blockEntity) {
        boolean isTouching = LevelUtil.isTouchingBlockCollision(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), entity);

        if (isTouching) {
            return original.call(entity, decision);
        }

        return false;
    }
}
