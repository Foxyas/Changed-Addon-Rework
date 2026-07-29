package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.animal.FlyingAnimal;

public interface IFlyableChangedEntity extends FlyingAnimal {

    default void setFlyingMode(boolean value) {
        if (this instanceof ChangedEntity changedEntity) {
            changedEntity.setChangedEntityFlag(0, value);
        }
    }

    boolean isFlyingMode();

    @Override
    default boolean isFlying() {
        return this.isFlyingMode();
    }

    void updateNavigationAndControl(boolean flying);
}
