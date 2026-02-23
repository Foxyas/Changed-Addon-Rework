package net.foxyas.changedaddon.block;

import net.foxyas.changedaddon.event.TransfurEvents;
import net.ltxprogrammer.changed.block.WhiteLatexTransportInterface;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WhiteLatexCoverBlock extends LatexCoverBlock implements WhiteLatexTransportInterface {

    public WhiteLatexCoverBlock(Properties pProperties) {
        super(pProperties, ChangedLatexTypes.WHITE_LATEX::get);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (LatexType.getEntityLatexType(player) == ChangedLatexTypes.WHITE_LATEX.get() && player.getItemInHand(player.getUsedItemHand()).isEmpty() && !WhiteLatexTransportInterface.isEntityInWhiteLatex(player)) {
            if (pos.distSqr(new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ())) > (double) 4.0F) {
                return super.use(state, level, pos, player, hand, hitResult);
            } else {
                WhiteLatexTransportInterface.entityEnterLatex(player, pos);
                return InteractionResult.CONSUME;
            }
        } else {
            return super.use(state, level, pos, player, hand, hitResult);
        }
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        if (pContext instanceof EntityCollisionContext entityCollisionContext) {
            Entity entity = TransfurEvents.resolveChangedEntity(entityCollisionContext.getEntity());
            if (entity instanceof LivingEntity living) {
                boolean flag = LatexType.getEntityLatexType(living) == ChangedLatexTypes.WHITE_LATEX.get() && !WhiteLatexTransportInterface.isEntityInWhiteLatex(living);
                if (flag) {
                    return this.getShape(pState, pLevel, pPos, pContext);
                }
            }
        }

        return super.getCollisionShape(pState, pLevel, pPos, pContext);
    }
}
