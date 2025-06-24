package net.foxyas.changedaddon.block.advanced;

import net.foxyas.changedaddon.client.renderer.blockEntitys.TimedKeypadBlockEntityRenderer;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters.ChangedAddonBlocks;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TimedKeypad extends KeypadBlock {
    public TimedKeypad() {
        super();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TimedKeypadBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (player.isShiftKeyDown()) {
            player.displayClientMessage(new TextComponent("Pos:" + (hitResult.getLocation().subtract(hitResult.getBlockPos().getX(),
                            hitResult.getBlockPos().getY(),
                            hitResult.getBlockPos().getZ()))),
                    true);
            Vec3 relative = (hitResult.getLocation().subtract(hitResult.getBlockPos().getX(),
                    hitResult.getBlockPos().getY(),
                    hitResult.getBlockPos().getZ()));
            if (relative.y > 0.185f && relative.y < 0.25f) {
                Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
                if (direction == Direction.NORTH) {
                    if (relative.x == 0.0625f || (relative.x >= 0.0624 && relative.x < 0.0626) ){
                        if (relative.z > 0.75f && relative.z < 0.8125) {
                            BlockEntity blockEntity = level.getBlockEntity(pos);
                            if (blockEntity instanceof TimedKeypadBlockEntity timedKeypadBlockEntity) {
                                timedKeypadBlockEntity.setTimer(timedKeypadBlockEntity.getTimer() + 10);
                            }
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, Random random) {
        super.tick(blockState, level, blockPos, random);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type,
                ChangedAddonRegisters.ChangedAddonBlockEntities.TIMED_KEYPAD_BLOCK_ENTITY.get(), // Certifique-se que esse seja o registry correto
                (lvl, pos, blockState, be) -> {
                    be.tick(lvl, pos);
                    lvl.sendBlockUpdated(pos, blockState, blockState, 3);
                }
        );
    }

    @Override
    public boolean triggerEvent(BlockState p_49226_, Level p_49227_, BlockPos p_49228_, int p_49229_, int p_49230_) {
        return super.triggerEvent(p_49226_, p_49227_, p_49228_, p_49229_, p_49230_);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderLayer() {
        ItemBlockRenderTypes.setRenderLayer(ChangedAddonBlocks.TIMED_KEYPAD.get(), renderType -> renderType == RenderType.cutout());
    }
}
