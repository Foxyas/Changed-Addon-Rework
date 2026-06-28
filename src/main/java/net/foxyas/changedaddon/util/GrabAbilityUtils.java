package net.foxyas.changedaddon.util;

import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

public class GrabAbilityUtils {

    public static boolean grabEntity(LivingEntity target,
                                     LivingEntity grabber,
                                     GrabEntityAbilityInstance grabAbilityInstance) {
        if (grabAbilityInstance.grabEntity(target)) {
            Changed.PACKET_HANDLER.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> grabber),
                    new GrabEntityPacket(grabber, target, GrabEntityPacket.GrabType.ARMS)
            );
            ProcessTransfur.forceNearbyToRetarget(target.level(), target);
            return true;
        }
        return false;
    }

    public static boolean suitEntity(LivingEntity target,
                                     LivingEntity grabber,
                                     GrabEntityAbilityInstance grabAbilityInstance) {
        if (grabAbilityInstance.suitEntity(target)) {
            Changed.PACKET_HANDLER.send(
                    PacketDistributor.TRACKING_ENTITY.with(() -> grabber),
                    new GrabEntityPacket(grabber, target, GrabEntityPacket.GrabType.SUIT)
            );
            ProcessTransfur.forceNearbyToRetarget(target.level(), target);
            return true;
        }
        return false;
    }

    public static void releaseEntity(LivingEntity target,
                                     LivingEntity grabber,
                                     GrabEntityAbilityInstance grabAbilityInstance,
                                     boolean applyDebuffs) {
        grabAbilityInstance.releaseEntity(applyDebuffs);
        // manda packet de GRAB (tipo ARMS)
        Changed.PACKET_HANDLER.send(
                PacketDistributor.TRACKING_ENTITY.with(() -> grabber),
                new GrabEntityPacket(grabber, target, GrabEntityPacket.GrabType.RELEASE)
        );
    }
}
