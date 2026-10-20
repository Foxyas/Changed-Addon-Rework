package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;

// TODO Remove this class and replace everything with Changed IFlyingCapable
public interface IFlyingCapable extends FlyingAnimal {

    default void setFlying(boolean value) {
        if (this instanceof ChangedEntity changedEntity) {
            changedEntity.setChangedEntityFlag(0, value);
        }
    }

    boolean canSafelyLandHere();

    void updateNavigationAndControl(boolean flying);

    PathNavigation getNavigationForFlyingState(boolean flying);
}
