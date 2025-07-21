package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Iterator;

@Mod.EventBusSubscriber
public class SwimRegretTriggerProcedure {
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
        if (!(entity instanceof Player player)) {
            return;
        }
        String type_form = "";
        if (entity.isSwimming() && entity.isInWaterOrBubble()) {
            if (ProcessTransfur.isPlayerTransfurred(player)) {
                type_form = ProcessTransfur.getPlayerTransfurVariant(player).getFormId().toString();
                if (VariantUtilProcedure.GetSwimSpeed(type_form, player) <= 0.95) {
                    entity.getPersistentData().putDouble("TransfurData.SlowSwimInWaterTicks", (entity.getPersistentData().getDouble("TransfurData.SlowSwimInWaterTicks") + 1));
                }
            }
        }
        if (ProcessTransfur.isPlayerTransfurred(player)) {
            if (VariantUtilProcedure.GetSwimSpeed(type_form, player) <= 0.95) {
                if (entity.getPersistentData().getDouble("TransfurData.SlowSwimInWaterTicks") >= 600) {
                    if (entity instanceof ServerPlayer _player) {
                        Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:swim_regret"));
                        AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
                        if (!_ap.isDone()) {
                            for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
                        }
                    }
                }
            } else {
                if (entity.getPersistentData().getDouble("TransfurData.SlowSwimInWaterTicks") > 1) {
                    entity.getPersistentData().putDouble("TransfurData.SlowSwimInWaterTicks", 0);
                }
            }
        }
    }
}
