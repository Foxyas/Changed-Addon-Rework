package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class KillExperiment009ProcedureProcedure {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Experiment009BossEntity)) return;

        Entity sourceentity = event.getSource().getEntity();
        if (!(sourceentity instanceof ServerPlayer player)) return;

        Advancement _adv = player.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:kill_experiment_009"));
        AdvancementProgress _ap = player.getAdvancements().getOrStartProgress(_adv);
        if (!_ap.isDone()) {
            for (String s : _ap.getRemainingCriteria()) player.getAdvancements().award(_adv, s);
        }
    }
}
