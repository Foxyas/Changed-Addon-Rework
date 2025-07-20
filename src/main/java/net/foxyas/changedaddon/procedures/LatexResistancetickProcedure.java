package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonAttributes;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class LatexResistancetickProcedure {
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
        double TransfurProgress = 0;
        double TransfurProgress_local_var = 0;
        if (((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get()).getValue() > 0) {
            if (!(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
                TransfurProgress = (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).Progress_Transfur_Number;
                TransfurProgress_local_var = ((LivingEntity) entity).getAttribute(ChangedAddonAttributes.LATEXRESISTANCE.get()).getValue();
                if (TransfurProgress > 0) {
                    AddTransfurProgressProcedure.setminus(entity, 0.5f * TransfurProgress_local_var);
                }
            }
        }
    }
}
