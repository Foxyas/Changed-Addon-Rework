package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Objects;

@Mod.EventBusSubscriber
public class LatexResistanceTickProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player);
        }
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null)
            return;
        double TransfurProgress;
        double TransfurProgress_local_var;
        if (((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get()).getValue() > 0) {
            if (entity instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (instance == null) {
                    TransfurProgress = ProcessTransfur.getPlayerTransfurProgress(player);
                    TransfurProgress_local_var = Objects.requireNonNull(((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get())).getValue();
                    if (TransfurProgress > 0) {
                        AddTransfurProgressProcedure.setminus(entity, 0.5f * TransfurProgress_local_var);
                    }
                }
            }
        }
    }
}
