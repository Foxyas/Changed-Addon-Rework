package net.foxyas.changedaddon.mixins.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.foxyas.changedaddon.util.LevelUtil;
import net.ltxprogrammer.changed.block.AbstractPuddle;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractPuddle.class)
public abstract class AbstractPuddleMixin {

    @WrapOperation(
            method = "entityInside",
            at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;progressTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/ai/LatexAssimilationDecision;)Z", remap = false)
    )
    private boolean isEntityTouchingCheck(LivingEntity entity, LatexAssimilationDecision<?> decision, Operation<Boolean> original, BlockState state, net.minecraft.world.level.Level level, BlockPos pos) {
        boolean isTouching = LevelUtil.isTouchingBlockCollision(level, pos, state, entity);

        if (isTouching) {
            return original.call(entity, decision);
        }

        return false;
    }
}
