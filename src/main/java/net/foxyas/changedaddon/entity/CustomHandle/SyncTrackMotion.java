package net.foxyas.changedaddon.entity.CustomHandle;

import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public interface SyncTrackMotion {

    void setIsMoving(boolean isMoving);

    boolean isMoving();

    @Nullable
    Vec3 getLastKnownMotion();

    void setLastKnownMotion(@Nullable Vec3 vec3);
}
