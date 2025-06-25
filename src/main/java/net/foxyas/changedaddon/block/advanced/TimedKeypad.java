package net.foxyas.changedaddon.block.advanced;

import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.foxyas.changedaddon.registers.ChangedAddonRegisters.ChangedAddonBlocks;
import net.ltxprogrammer.changed.block.AbstractCustomShapeEntityBlock;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
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
            /*player.displayClientMessage(new TextComponent("Pos:" + (hitResult.getLocation().subtract(hitResult.getBlockPos().getX(),
                            hitResult.getBlockPos().getY(),
                            hitResult.getBlockPos().getZ()))),
                    true);*/
            Vec3 relative = (hitResult.getLocation().subtract(hitResult.getBlockPos().getX(),
                    hitResult.getBlockPos().getY(),
                    hitResult.getBlockPos().getZ()));

            Direction direction = state.getValue(KeypadBlock.FACING);
            if (direction == Direction.NORTH) {
                if (isInside(relative, 0.0624f, 0.0626f, 0.185f, 0.25f, 0.75f, 0.814f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() + 1);
                        keypad.playTimerAdjust(true);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInside(relative, 0.0624f, 0.0626f, 0.185f, 0.25f, 0.814f, 0.8772f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        if (keypad.getTimer() > 0) {
                            keypad.setTimer(0);
                            keypad.playTimerAdjust(true);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInside(relative, 0.0624f, 0.0626f, 0.185f, 0.25f, 0.8772f, 0.9404f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() - 1);
                        keypad.playTimerAdjust(false);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (direction == Direction.SOUTH) {
                if (isInside(relative, 0.9374f, 0.9376f, 0.185f, 0.25f, 0.186f, 0.25f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() + 1);
                        keypad.playTimerAdjust(true);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInside(relative, 0.9374f, 0.9376f, 0.185f, 0.25f, 0.1228f, 0.186f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        if (keypad.getTimer() > 0) {
                            keypad.setTimer(0);
                            keypad.playTimerAdjust(true);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInside(relative, 0.9374f, 0.9376f, 0.185f, 0.25f, 0.0596f, 0.1228f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() - 1);
                        keypad.playTimerAdjust(false);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (direction == Direction.WEST) {
                if (isInsideWithDirection(relative, direction, 0.9374f, 0.9376f, 0.185f, 0.25f, 0.75f, 0.814f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() + 1);
                        keypad.playTimerAdjust(true);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInsideWithDirection(relative, direction, 0.9374f, 0.9376f, 0.185f, 0.25f, 0.814f, 0.880f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        if (keypad.getTimer() > 0) {
                            keypad.setTimer(0);
                            keypad.playTimerAdjust(true);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInsideWithDirection(relative, direction, 0.9374f, 0.9376f, 0.185f, 0.25f, 0.880f, 0.9432f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() - 1);
                        keypad.playTimerAdjust(false);
                    }
                    return InteractionResult.SUCCESS;
                }
            } else if (direction == Direction.EAST) {
                if (isInsideWithDirection(relative, direction, 0.0624f, 0.0626f, 0.185f, 0.25f, 0.186f, 0.25f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() + 1);
                        keypad.playTimerAdjust(true);
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInsideWithDirection(relative, direction, 0.0624f, 0.0626f, 0.185f, 0.25f, 0.1228f, 0.186f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        if (keypad.getTimer() > 0) {
                            keypad.setTimer(0);
                            keypad.playTimerAdjust(true);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
                if (isInsideWithDirection(relative, direction, 0.0624f, 0.0626f, 0.185f, 0.25f, 0.0596f, 0.1228f)) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity instanceof TimedKeypadBlockEntity keypad) {
                        keypad.setTimer(keypad.getTimer() - 1);
                        keypad.playTimerAdjust(false);
                    }
                    return InteractionResult.SUCCESS;
                }
            }


            return InteractionResult.SUCCESS;
        }

        return super.use(state, level, pos, player, hand, hitResult);
    }

/*
    void oldLogic(){
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
    }
*/


    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos blockPos, Random random) {
        super.tick(blockState, level, blockPos, random);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && stack.hasTag() && stack.getTag().contains("TimerValue")) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TimedKeypadBlockEntity keypad) {
                keypad.setTimer(stack.getTag().getInt("TimerValue"));
                keypad.setCanTick(true);
            }
        }
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


    // pixelX, pixelY, pixelZ vão de 0 a 15 (inclusive)
    private boolean isInsidePixel(Vec3 relative, int px, int py, int pz) {
        final float pixelSize = 1.0f / 16.0f;
        float minX = px * pixelSize;
        float maxX = minX + pixelSize;

        float minY = py * pixelSize;
        float maxY = minY + pixelSize;

        float minZ = pz * pixelSize;
        float maxZ = minZ + pixelSize;

        return relative.x >= minX && relative.x < maxX &&
                relative.y >= minY && relative.y < maxY &&
                relative.z >= minZ && relative.z < maxZ;
    }


    private boolean isInside(Vec3 rel, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        return rel.x >= minX && rel.x < maxX &&
                rel.y >= minY && rel.y < maxY &&
                rel.z >= minZ && rel.z < maxZ;
    }

    private boolean isInsideWithDirection(Vec3 rel, Direction direction, double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            return isInside(rel, minX, maxX, minY, maxY, minZ, maxZ);
        } else if (direction == Direction.WEST || direction == Direction.EAST) {
            return isInside(rel, minZ, maxZ, minY, maxY, minX, maxX);
        }

        return isInside(rel, minX, maxX, minY, maxY, minZ, maxZ);
    }
}
