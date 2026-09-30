package net.foxyas.changedaddon.ability.handle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.api.AbilityInstanceExtension;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
public class EventHandlers {


    @SubscribeEvent
    public static void onEntityJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        ProcessTransfur.getPlayerTransfurVariantSafe(EntityUtil.playerOrNull(entity)).ifPresent(transfurVariantInstance -> {
            transfurVariantInstance.abilityInstances.forEach((ability, instance) -> {
                if (instance instanceof AbilityInstanceExtension abilityInstanceExtension) {
                    abilityInstanceExtension.onEntityJump();
                }
            });
        });
    }

    @SubscribeEvent
    public static void onEntitySwing(PlayerInteractEvent.LeftClickEmpty event) {
        LivingEntity entity = event.getEntity();
        ProcessTransfur.getPlayerTransfurVariantSafe(EntityUtil.playerOrNull(entity)).ifPresent(transfurVariantInstance -> {
            transfurVariantInstance.abilityInstances.forEach((ability, instance) -> {
                if (instance instanceof AbilityInstanceExtension abilityInstanceExtension) {
                    abilityInstanceExtension.onEntitySwing();
                }
            });
        });
    }
}
