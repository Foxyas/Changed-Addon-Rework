package net.foxyas.changedaddon.procedures;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class AmmoniaParticleItemIsCraftedSmeltedProcedure {
    public static void execute(Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof ServerPlayer _player) {
            Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:ammoniapoweradvancement"));
            assert _adv != null;
            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
            if (!_ap.isDone()) {
                for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
            }
        }
    }
}
