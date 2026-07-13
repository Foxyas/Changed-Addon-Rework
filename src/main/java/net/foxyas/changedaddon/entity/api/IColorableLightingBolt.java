package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LightningBolt;

import java.awt.*;

public interface IColorableLightingBolt {

    EntityDataAccessor<Integer> COLOR_INT = SynchedEntityData.defineId(LightningBolt.class, EntityDataSerializers.INT);
    Color DEFAULT_COLOR = new Color(0.45f, 0.45F, 0.5F);

    default Color getThunderColor() {
        return new Color(0.45f, 0.45F, 0.5F);
    }

    default void setThunderColor(Color color) {

    }

    default void setThunderColor(Color3 color) {
        this.setThunderColor(ColorUtil.getColorFromColor3(color));
    }
}
