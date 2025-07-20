package net.foxyas.changedaddon.procedures;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.Iterator;

public class FoxyasEntityDiesProcedure {
    public static void execute(Entity sourceentity) {
        if (sourceentity == null)
            return;
        if (sourceentity instanceof ServerPlayer _player) {
            Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:foxyas_advancement"));
            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
            if (!_ap.isDone()) {
                Iterator _iterator = _ap.getRemainingCriteria().iterator();
                while (_iterator.hasNext())
                    _player.getAdvancements().award(_adv, (String) _iterator.next());
            }
        }
    }
}
