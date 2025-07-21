package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SleepWithUntransfurEffectProcedure {
    @SubscribeEvent
    public static void onEntityEndSleep(PlayerWakeUpEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level;

        if(!level.isDay() || !(entity instanceof Player player)
                || !player.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())) return;

        new DelayedTask(5, () -> {
            player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(cap -> {
                if (ProcessTransfur.isPlayerTransfurred(player) && player.isSleepingLongEnough()) {
                    cap.UntransfurProgress += 50;
                    cap.syncPlayerVariables(player);
                }
            });
        });
    }
}
