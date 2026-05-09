package net.foxyas.changedaddon.util;

import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;

public class ChangedEntityUtils {

    public int getCutenessLevelOfEntity(ChangedEntity changedEntity) {
        int cuteLevel = 0;
        if (changedEntity instanceof VariantExtraStats variantExtraStats) {
            cuteLevel = variantExtraStats.cuteLevel();
        }

        return cuteLevel;
    }
}
