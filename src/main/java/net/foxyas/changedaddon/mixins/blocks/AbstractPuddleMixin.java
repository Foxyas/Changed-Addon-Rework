package net.foxyas.changedaddon.mixins.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

    @Shadow
    public abstract VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_);

    @WrapOperation(
            method = "entityInside",
            at = @At(value = "INVOKE", target = "Lnet/ltxprogrammer/changed/process/ProcessTransfur;progressTransfur(Lnet/minecraft/world/entity/LivingEntity;Lnet/ltxprogrammer/changed/entity/ai/LatexAssimilationDecision;)Z", remap = false)
    )
    private boolean isEntityTouchingCheck(LivingEntity entity, LatexAssimilationDecision<?> decision, Operation<Boolean> original, BlockState state, net.minecraft.world.level.Level level, BlockPos pos) {
        // 1. Pega a VoxelShape real do bloco (considerando o estado atual dele)
        VoxelShape blockShape = this.getShape(state, level, pos, CollisionContext.of(entity));

        // 2. Transfere a shape para a posição real no mundo (offset)
        // e verifica se a AABB da entidade intersecta com a shape real do bloco
        boolean isTouching = blockShape.toAabbs().stream()
                .anyMatch(aabb -> aabb.inflate(0.05f).move(pos).intersects(entity.getBoundingBox()));

        if (isTouching) {
            return original.call(entity, decision);
        }

        return false;
    }
}
