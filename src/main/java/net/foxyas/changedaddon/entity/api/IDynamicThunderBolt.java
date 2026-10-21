package net.foxyas.changedaddon.entity.api;

public interface IDynamicThunderBolt extends IScalableLightingBolt, IColorableLightingBolt {

    boolean maySpawnFire();
    void setMaySpawnFire(boolean maySpawnFire);
}
