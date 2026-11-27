package net.foxyas.changedaddon.util;

import net.ltxprogrammer.changed.world.data.ActiveFacilityInstance;
import net.ltxprogrammer.changed.world.data.ChangedGameDataAccessor;
import net.minecraft.server.level.ServerLevel;

import java.util.List;

public class FacilityUtil {

    public static List<ActiveFacilityInstance> getActiveFacilityInstanceOnLevel(ServerLevel serverLevel) {
        return ((ChangedGameDataAccessor)serverLevel).getChangedGameData().facilities;
    }


}
