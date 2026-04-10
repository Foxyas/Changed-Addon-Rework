package net.foxyas.changedaddon.compatibility;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.util.RPTransfurDenialMessages;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ttv.migami.jeg.event.GunFireEvent;

public class JEGSEvents {
    
    public static void register() {
        // Registra esta própria classe no barramento de eventos
        MinecraftForge.EVENT_BUS.register(new JEGSEvents());
    }

    @SubscribeEvent
    public void onAttemptToFireGun(GunFireEvent event) {
        Player shooter = event.getEntity();
        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(shooter);
        if (transfurVariantInstance != null) {
            boolean isRestricted = false;
            ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();

            if (changedEntity instanceof VariantExtraStats variantExtraStats && !variantExtraStats.canFireGuns()) {
                isRestricted = true;
            } else if (ChangedAddonServerConfiguration.STOP_TRANSFURRED_PLAYERS_USE_GUNS.get()) {
                isRestricted = true;
            }

            if (isRestricted) {
                event.setCanceled(true);
                // Envia a mensagem na barra de ação (acima do inventário) para não poluir o chat
                shooter.displayClientMessage(RPTransfurDenialMessages.getRandomGunDenial(), true);
                SoundEvent changeSound = ChangedSounds.EXOSKELETON_CHIME.get();
                shooter.playSound(changeSound, 1.0F, 0.5F);
                ItemCooldowns cooldowns = shooter.getCooldowns();
                cooldowns.addCooldown(event.getStack().getItem(), 10);
            }
        }
    }
}