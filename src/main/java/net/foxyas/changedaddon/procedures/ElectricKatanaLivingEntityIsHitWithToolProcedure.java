package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.init.ChangedEffects;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ElectricKatanaLivingEntityIsHitWithToolProcedure {
    public static void execute(Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null) {
            return;
        }
        LivingEntity enemy = (LivingEntity) entity;
        ChangedSounds.broadcastSound(enemy, ChangedSounds.PARALYZE1, 1.0F, 1.0F);
        enemy.addEffect(new MobEffectInstance(ChangedEffects.SHOCK, 6, 0, false, false, true));
    }

}

