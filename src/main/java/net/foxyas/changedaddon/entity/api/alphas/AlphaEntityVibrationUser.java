package net.foxyas.changedaddon.entity.api.alphas;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AlphaEntityVibrationUser implements VibrationSystem.User {

    public final PositionSource positionSource;
    private final PathfinderMob alpha;

    public AlphaEntityVibrationUser(PathfinderMob alpha) {
        this.alpha = alpha;
        positionSource = new EntityPositionSource(alpha, alpha.getEyeHeight());
    }

    @Override
    public int getListenerRadius() {
        return 16;
    }

    @Override
    public @NotNull PositionSource getPositionSource() {
        return positionSource;
    }

    @Override
    public boolean canReceiveVibration(@NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull GameEvent pGameEvent, GameEvent.@NotNull Context pContext) {
        // Só escuta se estiver dormindo
        if (!alpha.isSleeping()) return false;

        // Eventos que acordam
        return pGameEvent == GameEvent.STEP || pGameEvent == GameEvent.HIT_GROUND || pGameEvent == GameEvent.ENTITY_DAMAGE;
    }

    @Override
    public void onReceiveVibration(@NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull GameEvent pGameEvent, @Nullable Entity pEntity, @Nullable Entity pPlayerEntity, float pDistance) {
        if (alpha instanceof IHearingSystem iHearingSystem) {
            iHearingSystem.setHeardCooldown(20);
        }
    }

}