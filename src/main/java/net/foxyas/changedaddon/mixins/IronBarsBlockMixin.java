package net.foxyas.changedaddon.mixins;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossCollisionBlock.class)
public abstract class IronBarsBlockMixin{

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void getCollisionModShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityCollisionContext entityContext) {
            Entity collidingEntity = entityContext.getEntity();

            if (collidingEntity != null && state.getBlock() == Blocks.IRON_BARS) {
                // Verifica se a entidade é um jogador
                if (collidingEntity instanceof Player player) {
                    // Verifica uma condição específica do jogador (no caso, ProcessTransfur)
                    if (ProcessTransfur.isPlayerLatex(player) && !player.isShiftKeyDown()) {
                        // Se for um jogador Latex, permite que ele atravesse a barra de ferro (forma vazia)
                        cir.setReturnValue(Shapes.empty()); // Colisão desativada para jogadores Latex
                    }
                }
            }
        }
    }
}
