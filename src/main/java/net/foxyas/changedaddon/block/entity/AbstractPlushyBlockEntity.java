package net.foxyas.changedaddon.block.entity;

import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPlushyBlockEntity extends BlockEntity {

    public static final int SQUEEZE_EVENT = 1;
    private static final String SQUEEZE_COOLDOWN_TAG = "squeezeCooldown";
    private final PlushySqueakController squeakController = new PlushySqueakController();
    public int squeezeCooldown = 0;

    public AbstractPlushyBlockEntity(BlockEntityType<?> type, BlockPos position, BlockState state) {
        super(type, position, state);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, AbstractPlushyBlockEntity blockEntity) {
        if (level.isClientSide()) clientTick(level, pos, state, blockEntity);
        if (!level.isClientSide()) serverTick(level, pos, state, blockEntity);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AbstractPlushyBlockEntity blockEntity) {
        blockEntity.squeakController.tickController();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AbstractPlushyBlockEntity blockEntity) {
        blockEntity.squeakController.tickController();
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if (compound.contains(SQUEEZE_COOLDOWN_TAG)) {
            this.squeezeCooldown = compound.getInt(SQUEEZE_COOLDOWN_TAG);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt(SQUEEZE_COOLDOWN_TAG, this.squeezeCooldown);
    }

    public PlushySqueakController getSqueakController() {
        return squeakController;
    }

    /**
     * Override this in specific plushy block entities to change their squeeze speed.
     * Higher = faster animation, Lower = slower/squishier animation.
     */
    public float getSqueezeSpeed() {
        return 0.2F; // Default speed
    }

    @Override
    public boolean triggerEvent(int id, int param) {
        if (id == SQUEEZE_EVENT) {
            if (this.level != null && this.level.isClientSide) {
                // Decode speed from eventParam (param / 1000f to preserve float precision in an int)
                float speed = param > 0 ? (param / 1000.0F) : getSqueezeSpeed();
                squeakController.triggerSqueak(speed);
            }
            return true;
        }
        return super.triggerEvent(id, param);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public boolean hasSqueezeCooldown() {
        return this.squeezeCooldown > 0;
    }

    public void subSqueezedTicks(int i) {
        this.squeezeCooldown = this.squeezeCooldown - i;
    }

    public void squeeze(BlockState state, Level world, BlockPos pos, BlockHitResult hit) {
        if (!world.isClientSide()) {
            Vec3 loc = hit.getLocation();
            world.playSound(null, loc.x, loc.y, loc.z, ChangedAddonSoundEvents.PLUSHY_SOUND.get(), SoundSource.BLOCKS, 1f, 1);

            // Encode the speed into an integer event parameter (e.g., 0.25f -> 250)
            int encodedSpeed = (int) (getSqueezeSpeed() * 1000.0F);
            world.blockEvent(pos, state.getBlock(), AbstractPlushyBlockEntity.SQUEEZE_EVENT, encodedSpeed);
        }
    }
}