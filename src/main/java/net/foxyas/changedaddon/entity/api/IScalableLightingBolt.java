package net.foxyas.changedaddon.entity.api;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LightningBolt;

public interface IScalableLightingBolt {

    EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(LightningBolt.class, EntityDataSerializers.FLOAT);

    default float getScale() {
        return 1;
    }

    void setScale(float scale);
}
