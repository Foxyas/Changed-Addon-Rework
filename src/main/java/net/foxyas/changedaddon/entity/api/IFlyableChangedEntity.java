package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;

public interface IFlyableChangedEntity {

    default void setFlyingMode(boolean value) {
        if (this instanceof ChangedEntity changedEntity) {
            changedEntity.setChangedEntityFlag(0, value);
        }
    }

    boolean isFlyingMode();

    void updateNavigationAndControl(boolean flying);
}
