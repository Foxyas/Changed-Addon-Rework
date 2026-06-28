package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber() // ou Dist.DEDICATED_SERVER se for lógico
public class PlayerLavaSwimHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        if (player.level().isClientSide || !(player instanceof ServerPlayer serverPlayer)) return;

        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (instance == null || !ChangedAddonTransfurVariants.isAquatic(instance)) return;

        if (player.isEyeInFluid(FluidTags.LAVA) && player.hasEffect(MobEffects.FIRE_RESISTANCE) && (player.isSwimming() || player.isVisuallySwimming())) {
            ChangedAddonCriteriaTriggers.LAVA_SWIMMING_TRIGGER.trigger(serverPlayer);
        }
    }
}
