package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.defaults.AbstractBasicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class LatexCalicoCatEntity extends AbstractBasicChangedEntity {
    public LatexCalicoCatEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    public LatexCalicoCatEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.LATEX_CALICO_CAT.get(), world);
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        AttributePresets.catLike(attributes);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    public Color3 getDripColor() {
        return this.random.nextBoolean() ? Color3.parseHex("#d67053") : Color3.parseHex("#67423f");
    }
}
