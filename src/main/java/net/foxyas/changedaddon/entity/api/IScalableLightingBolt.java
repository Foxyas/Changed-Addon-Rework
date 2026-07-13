package net.foxyas.changedaddon.entity.api;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LightningBolt;
import org.joml.Vector3f;

public interface IScalableLightingBolt {

    EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(LightningBolt.class, EntityDataSerializers.FLOAT);
    EntityDataAccessor<Vector3f> VISUAL_SCALE = SynchedEntityData.defineId(LightningBolt.class, EntityDataSerializers.VECTOR3);

    default float getScale() {
        return 1;
    }

    void setScale(float scale);

    default Vector3f getRenderScale() {
        return new Vector3f(getScale(), getScale(), getScale());
    }

    void setRenderScale(Vector3f renderScale);
}
