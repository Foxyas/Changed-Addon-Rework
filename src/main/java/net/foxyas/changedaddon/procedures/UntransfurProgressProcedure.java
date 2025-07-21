package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class UntransfurProgressProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player);
        }
    }

    public static void execute(Entity entity) {
        execute(null, entity);
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        if (entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get())) {
            if (!(entity instanceof Player player)) {
                return;
            }
            boolean transfur = ProcessTransfur.isPlayerTransfurred(player);
            boolean organic_transfur = ProcessTransfur.isPlayerNotLatex(player);
            if (transfur) {
            String transfurId = ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString();
                if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UntransfurProgress < 0) {
                    entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.UntransfurProgress = 0;
                        capability.syncPlayerVariables(entity);
                    });
                } else {
                    entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                        capability.UntransfurProgress += (organic_transfur ? 0.1 : 0.2);
                        capability.syncPlayerVariables(entity);
                    });

                    if (_livEnt.isSleeping()) {
                        entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                            capability.UntransfurProgress += 0.5;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                }
            }
        } else {
            if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).UntransfurProgress > 0) {
                entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
                    capability.UntransfurProgress -= 0.1;
                    capability.syncPlayerVariables(entity);
                });
            }
        }
    }
}
