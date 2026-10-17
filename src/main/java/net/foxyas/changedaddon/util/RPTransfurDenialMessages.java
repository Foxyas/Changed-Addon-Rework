package net.foxyas.changedaddon.util;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Random;

public class RPTransfurDenialMessages {
    private static final List<String> GUN_DENIAL_KEYS = List.of(
        "text.changed_addon.rp.guns_compatibility.paws_too_big",
        "text.changed_addon.rp.guns_compatibility.no_fingers_for_trigger",
        "text.changed_addon.rp.guns_compatibility.claws_stuck",
        "text.changed_addon.rp.guns_compatibility.anatomical_error"
    );

    private static final List<String> BOW_DENIAL_KEYS = List.of(
        "text.changed_addon.rp.bow_and_crossbow_stop.claws_rip_string",
        "text.changed_addon.rp.bow_and_crossbow_stop.no_grip_strength",
        "text.changed_addon.rp.bow_and_crossbow_stop.crossbow_safety",
        "text.changed_addon.rp.bow_and_crossbow_stop.clumsy_paws"
    );

    public static Component getRandomGunDenial(LivingEntity shooter) {
        RandomSource random = shooter.getRandom();
        return Component.translatable(GUN_DENIAL_KEYS.get(random.nextInt(GUN_DENIAL_KEYS.size())));
    }

    public static Component getRandomBowDenial(LivingEntity shooter) {
        RandomSource random = shooter.getRandom();
        return Component.translatable(BOW_DENIAL_KEYS.get(random.nextInt(BOW_DENIAL_KEYS.size())));
    }
}