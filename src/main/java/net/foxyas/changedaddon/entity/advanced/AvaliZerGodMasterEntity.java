package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class AvaliZerGodMasterEntity extends AvaliEntity {
    public AvaliZerGodMasterEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        super(ChangedAddonEntities.AVALI_ZERGODMASTER.get(), world);
    }

    public AvaliZerGodMasterEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isColorful() {
        return false;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }
}
