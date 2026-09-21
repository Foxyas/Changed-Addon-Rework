package net.foxyas.changedaddon.entity.api;

import net.minecraft.world.entity.Entity;

public interface IDynamicCamera {

    boolean shouldResetCameraOnShift();
    boolean shouldSoftSetCameraByDefault();
    void setResetCameraOnShift(boolean resetCameraOnShift);
    void setSoftSetCameraByDefault(boolean softSetCameraByDefault);
    void softSetCamera(Entity entity);
}
