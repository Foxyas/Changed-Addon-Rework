package net.foxyas.changedaddon.compatibility;

import com.tacz.guns.api.event.common.GunShootEvent;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.util.RPTransfurDenialMessages;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TACZEvents {
    
    public static void register() {
        // Registra esta própria classe no barramento de eventos
        MinecraftForge.EVENT_BUS.register(new TACZEvents());
    }

//    @SubscribeEvent
//    public void onAttemptToFireGun(GunFireEvent event) {
//        LivingEntity entity = event.getShooter();
//        if (!(entity instanceof Player shooter)) return;
//
//        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(shooter);
//        if (transfurVariantInstance != null) {
//            boolean isRestricted = false;
//            ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();
//
//            if (changedEntity instanceof VariantExtraStats variantExtraStats && !variantExtraStats.canFireGuns()) {
//                isRestricted = true;
//            } else if (ChangedAddonServerConfiguration.STOP_TRANSFURRED_PLAYERS_USE_GUNS.get()) {
//                isRestricted = true;
//            }
//
//            if (isRestricted) {
//                event.setCanceled(true);
//                // Envia a mensagem na barra de ação (acima do inventário) para não poluir o chat
//                shooter.displayClientMessage(RPTransfurDenialMessages.getRandomGunDenial(), true);
//                SoundEvent changeSound = ChangedSounds.EXOSKELETON_CHIME.get();
//                shooter.playSound(changeSound, 1.0F, 0.5F);
//                ItemCooldowns cooldowns = shooter.getCooldowns();
//                cooldowns.addCooldown(event.getGunItemStack().getItem(), 10);
//            }
//        }
//    }

    @SubscribeEvent
    public void onAttemptToFireGun(GunShootEvent event) {
        LivingEntity entity = event.getShooter();
        if (!(entity instanceof Player shooter)) return;

        TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(shooter);
        if (transfurVariantInstance != null) {
            boolean isRestricted = false;
            ChangedEntity changedEntity = transfurVariantInstance.getChangedEntity();

            if (changedEntity instanceof IVariantExtraStats IVariantExtraStats && !IVariantExtraStats.canFireGuns()) {
                isRestricted = true;
            } else if (ChangedAddonServerConfiguration.STOP_TRANSFURRED_PLAYERS_USE_GUNS.get()) {
                isRestricted = true;
            }

            if (isRestricted) {
                event.setCanceled(true);
                // Envia a mensagem na barra de ação (acima do inventário) para não poluir o chat
                ItemCooldowns cooldowns = shooter.getCooldowns();
                if (!cooldowns.isOnCooldown(event.getGunItemStack().getItem())) {
                    shooter.displayClientMessage(RPTransfurDenialMessages.getRandomGunDenial(), true);
                    SoundEvent changeSound = ChangedSounds.EXOSKELETON_CHIME.get();
                    shooter.playSound(changeSound, 1.0F, 0.5F);
                    cooldowns.addCooldown(event.getGunItemStack().getItem(), 10);
                }
            }
        }
    }
}