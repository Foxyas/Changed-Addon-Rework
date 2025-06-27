package net.foxyas.changedaddon.block.advanced;

import net.foxyas.changedaddon.registers.ChangedAddonRegisters;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.ltxprogrammer.changed.block.entity.KeypadBlockEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimedKeypadBlockEntity extends KeypadBlockEntity {
    private int timer = 0;
    private boolean canTick = false;
    private int ticks = 0;

    public TimedKeypadBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ChangedAddonRegisters.ChangedAddonBlockEntities.TIMED_KEYPAD_BLOCK_ENTITY.get();
    }

    public void setCanTick(boolean canTick) {
        this.canTick = canTick;
    }

    public boolean canTick() {
        return canTick;
    }

    public void addTimer(int timer) {
        this.timer = Math.max(0, Math.min(this.timer + timer, 1000));
    }

    public void setTimer(int timer) {
        this.timer = Math.max(0, Math.min(timer, 1000));
    }

    public int getTimer() {
        return timer;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("timer", timer);
        tag.putBoolean("canTick", canTick);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("timer")) timer = tag.getInt("timer");
        if (tag.contains("canTick")) canTick = tag.getBoolean("canTick");
    }

    private void playSound(SoundEvent event, float volume, float pitch) {
        if (this.level.getServer() != null) {
            ChangedSounds.broadcastSound(this.level.getServer(), event, this.worldPosition, volume, pitch);
        }

    }

    public void playUnlockSuccess() {
        this.playSound(ChangedSounds.CHIME2, 1.0F, 1.0F);
    }

    public void playUnlockFail() {
        this.playSound(ChangedSounds.BUZZER1, 1.0F, 1.0F);
    }

    public void playLock() {
        this.playSound(ChangedSounds.KEY, 1.0F, 1.0F);
    }

    public void playTimerAdjust(boolean isPositive) {
        this.playSound(ChangedSounds.KEY, 1.0F, isPositive ? 1f : 0.75f);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    public @NotNull CompoundTag getUpdatePacketTag() {
        return getUpdateTag();
    }

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public void tick(@NotNull Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }
        if (!canTick) {
            return;
        }
        if (!this.getBlockState().getValue(KeypadBlock.POWERED)) {
            return;
        }
        ticks++;

        if (ticks % 10 == 0) {
            if (timer > 0) {
                timer--;
            } else if (timer < 0) {
                timer = 0;
                lockKeypad(level, pos, getBlockState());
                this.playLock();
                canTick = false;
            } else {
                lockKeypad(level, pos, getBlockState());
                this.playLock();
                canTick = false;
            }
        }

        if (ticks >= 4096) {
            ticks = 0;
        }
    }

    public void lockKeypad(Level level, BlockPos blockPos, BlockState state) {
        level.setBlockAndUpdate(blockPos, this.getBlockState().setValue(KeypadBlock.POWERED, Boolean.FALSE));
        level.updateNeighborsAt(blockPos, state.getBlock());
        level.updateNeighborsAt(blockPos.relative(state.getValue(KeypadBlock.FACING).getOpposite()), state.getBlock());
    }

    @Override
    public void useCode(List<Byte> attemptedCode) {
        super.useCode(attemptedCode);
        if (this.level != null && this.code != null) {
            if (getTimer() > 0) {
                this.setCanTick(true);
            }
        }
    }
}
