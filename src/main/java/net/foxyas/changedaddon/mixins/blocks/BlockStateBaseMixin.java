package net.foxyas.changedaddon.mixins.blocks;

import net.foxyas.changedaddon.entity.advanced.LuminaraFlowerBeastEntity;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {

    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract boolean is(Block pBlock);

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
    private void getCollisionShapeHook(BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        if (!(pContext instanceof EntityCollisionContext collisionContext)) {
            return;
        }

        if (!(this.getBlock() instanceof AirBlock airBlock)) {
            return;
        }

        boolean above = collisionContext.isAbove(Shapes.block(), pPos, false);
        if (!above) return;

        Entity contextEntity = collisionContext.getEntity();
        if (!(contextEntity instanceof LivingEntity livingEntity)) return;

        Optional<IAbstractChangedEntity> iAbstractChangedEntity = IAbstractChangedEntity.forEitherSafe(livingEntity);

        iAbstractChangedEntity.ifPresent((iAbstractChanged) -> {
            if (iAbstractChanged.getChangedEntity() instanceof LuminaraFlowerBeastEntity luminaraFlowerBeast) {
                if (luminaraFlowerBeast.isHyperAwakened() && !luminaraFlowerBeast.isShiftKeyDown() && (!luminaraFlowerBeast.isFlying() || !luminaraFlowerBeast.isFallFlying())) {
                    if (pLevel.getBlockState(pPos).is(Blocks.VOID_AIR)) {
                        cir.setReturnValue(Shapes.block());
                    }
                }
            }
        });

    }

    @Inject(method = "entityCanStandOn", at = @At("RETURN"), cancellable = true)
    private void canEntityStandOnHook(BlockGetter pLevel, BlockPos pPos, Entity pEntity, CallbackInfoReturnable<Boolean> cir) {

        if (!(this.getBlock() instanceof AirBlock airBlock)) {
            return;
        }

        if (!(pEntity instanceof LivingEntity livingEntity)) return;

        Optional<IAbstractChangedEntity> iAbstractChangedEntity = IAbstractChangedEntity.forEitherSafe(livingEntity);

        iAbstractChangedEntity.ifPresent((iAbstractChanged) -> {
            if (iAbstractChanged.getChangedEntity() instanceof LuminaraFlowerBeastEntity luminaraFlowerBeast) {
                if (luminaraFlowerBeast.isHyperAwakened() && !luminaraFlowerBeast.isShiftKeyDown() && (!luminaraFlowerBeast.isFlying() && !luminaraFlowerBeast.isFallFlying())) {
                    if (pLevel.getBlockState(pPos).is(Blocks.VOID_AIR)) {
                        cir.setReturnValue(true);
                    }
                }
            }
        });

    }
}
