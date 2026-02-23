package net.foxyas.changedaddon.entity.api;

import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.client.gui.AbstractRadialScreen;
import net.ltxprogrammer.changed.entity.ChangedEntity;

import java.awt.*;

public interface IDynamicPawColor {

    default Color getPawBeansColor() {
        if (this instanceof ChangedEntity changedEntity) {
            return new Color(AbstractRadialScreen.getColors(IAbstractChangedEntity.forEntity(changedEntity).getTransfurVariantInstance()).setForegroundToBright().foreground().toInt());
        }
        return Color.GRAY;
    }

    default Color getPawColor() {
        if (this instanceof ChangedEntity changedEntity) {
            return new Color(AbstractRadialScreen.getColors(IAbstractChangedEntity.forEntity(changedEntity).getTransfurVariantInstance()).setForegroundToBright().background().toInt());
        }
        return Color.WHITE;
    }

    default IDynamicPawColor.PawStyle getPawStyle() {
        if (this instanceof ChangedEntity changedEntity) {
            return switch (changedEntity.getEntityShape()) {
                case ANTHRO -> IDynamicPawColor.PawStyle.ANTHRO;
                case FERAL -> IDynamicPawColor.PawStyle.FERAL;
                default -> IDynamicPawColor.PawStyle.DEFAULT;
            };
        }
        return IDynamicPawColor.PawStyle.DEFAULT;
    }

    enum PawStyle {
        DEFAULT,
        ANTHRO,
        FERAL
    }
}
