package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.registers.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
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
        if (player.getLevel().isClientSide) return;
        if (player instanceof ServerPlayer serverPlayer) {
            boolean isInLava = player.isEyeInFluid(FluidTags.LAVA);
            boolean hasFireResist = player.hasEffect(MobEffects.FIRE_RESISTANCE);
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfurVariantInstance != null && ChangedAddonTransfurVariants.TransfurVariantTags.isAquaticDiet(transfurVariantInstance)) {
                if (isInLava && hasFireResist) {
                    ChangedAddonCriteriaTriggers.LAVA_SWIMMING_TRIGGER.trigger(serverPlayer);
                }
            }

        }

    }
}
