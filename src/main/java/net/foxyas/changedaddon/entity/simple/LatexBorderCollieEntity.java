package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.defaults.AbstractBasicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class LatexBorderCollieEntity extends AbstractBasicChangedEntity {

    public LatexBorderCollieEntity(EntityType<? extends LatexBorderCollieEntity> type, Level level) {
        super(type, level);
    }

    public LatexBorderCollieEntity(PlayMessages.SpawnEntity ignoreMessage, Level level) {
        this(ChangedAddonEntities.LATEX_BORDER_COLLIE.get(), level);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.REPLICATION;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return ColorUtil.lerpTFColors(this.maybeGetUnderlying(),
                1,
                Color3.parseHex("#18181e"),
                Color3.parseHex("#ffffff")
        );
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        AttributePresets.wolfLike(attributes);
    }
}
