package net.foxyas.changedaddon.entity.ai.goals.generic.attacks;


import net.foxyas.changedaddon.mixins.entity.CombatTrackerAccessor;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;

public class ComboBurstGoal extends Goal {

    private final Mob mob;
    private final double mobDamageThreshold;
    private final double targetDamageThreshold;
    private final int checkInterval = 20; // 1 segundo
    private final float knockbackForce = 2.5f;
    private final int cooldown = 100; // 5 segundos
    private int ticks;
    private int cooldownTimer;

    public ComboBurstGoal(Mob mob, double mobDamageThreshold, double targetDamageThreshold) {
        this.mob = mob;
        this.mobDamageThreshold = mobDamageThreshold;
        this.targetDamageThreshold = targetDamageThreshold;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {

        if (cooldownTimer > 0) {
            cooldownTimer--;
            return false;
        }

        ticks++;
        if (ticks < checkInterval) return false;
        ticks = 0; // reset do timer

        List<CombatEntry> entries = ((CombatTrackerAccessor) mob.getCombatTracker()).getEntries();
        CombatEntry mobEntry = !entries.isEmpty() ? entries.get(entries.size() - 1) : null;
        if (mobEntry == null) return false;

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) return false;

        entries = ((CombatTrackerAccessor) target.getCombatTracker()).getEntries();
        CombatEntry targetEntry = !entries.isEmpty() ? entries.get(entries.size() - 1) : null;
        if (targetEntry == null) return false;

        float mobRecentDamage = mobEntry.damage();
        float targetRecentDamage = targetEntry.damage();

        Entity lastMobAttacker = mobEntry.source().getEntity();
        Entity lastTargetAttacker = targetEntry.source().getEntity();

        // Caso 1 → Mob foi danificado por OUTRO que não é o target
        if (lastMobAttacker != null && lastMobAttacker != target) {
            return mobRecentDamage >= mobDamageThreshold;
        }

        // Caso 2 → O alvo foi danificado por alguém que não é o mob
        if (lastTargetAttacker != null && lastTargetAttacker != mob) {
            return true;
        }

        // Caso 3 → Troca de dano, mas o target tomou pouco dano enquanto mob tomou bastante
        return mobRecentDamage >= mobDamageThreshold &&
                targetRecentDamage <= targetDamageThreshold &&
                lastTargetAttacker == mob;
    }

    @Override
    public void start() {

        Level level = mob.level;
        AABB area = mob.getBoundingBox().inflate(4.0);

        List<LivingEntity> entities = level.getEntitiesOfClass(
                LivingEntity.class,
                area,
                e -> e != mob && e.isAlive()
        );

        boolean playedSound = false;

        for (LivingEntity livingEntity : entities) {
            double dx = livingEntity.getX() - mob.getX();
            double dz = livingEntity.getZ() - mob.getZ();
            double dist = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));

            livingEntity.push((dx / dist) * knockbackForce, 0.5, (dz / dist) * knockbackForce);
            livingEntity.hurtMarked = true;

            if (!playedSound) {
                level.playSound(null, mob, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1, 1);
                playedSound = true;
            }

            if (livingEntity instanceof Player player) {
                player.displayClientMessage(
                        Component.literal("That's ENOUGH").withStyle(style -> style
                                .withBold(true)
                                .withItalic(true)
                                .withColor(new Color(0, 0, 0).getRGB())),
                        true
                );
            }
        }

        cooldownTimer = cooldown;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
