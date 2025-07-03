package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.registers.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class KitsuneEntity extends AbstractKitsuneEntity {
    public KitsuneEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public KitsuneEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.KITSUNE.get(), world);
    }

}
