package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.npc.AbstractVillager;

public interface ISafeChangedEntity {

    default boolean shouldScareVillagers(ChangedEntity entity, AbstractVillager villager) {
        return false;
    }
}
