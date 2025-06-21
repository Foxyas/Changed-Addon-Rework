package net.foxyas.changedaddon.entity.goals;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;

public class KnockBackBurstGoal extends Goal {
    private final Mob mob;
    private final double damageThreshold;
    private final int checkInterval = 20; // ticks (1 segundo)
    private int ticks;
    private float recentDamage;
    private int recentHits;

    private final float knockbackForce = 2.5f;
    private final int cooldown = 100; // 5 segundos de cooldown
    private int cooldownTimer;

    public KnockBackBurstGoal(Mob mob, double damageThreshold) {
        this.mob = mob;
        this.damageThreshold = damageThreshold;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldownTimer > 0) {
            cooldownTimer--;
            return false;
        }

        ticks++;
        if (ticks >= checkInterval) {
            ticks = 0;

			
// reset
            recentDamage = 0;
            recentHits = 0;

            // Verifica se tomou muito dano e não revidou muito
            if (recentDamage >= damageThreshold && recentHits <= 1) {
                // reset
                recentDamage = 0;
                recentHits = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        // reset
        recentDamage = 0;
        recentHits = 0;

        Level level = mob.getLevel();
        AABB area = mob.getBoundingBox().inflate(4.0); // raio da explosão de knockback
        List<LivingEntity> players = level.getEntitiesOfClass(LivingEntity.class, area, (livingEntity -> livingEntity != mob));

        for (LivingEntity livingEntity : players) {
            if (livingEntity.isAlive()) {
                double dx = livingEntity.getX() - mob.getX();
                double dz = livingEntity.getZ() - mob.getZ();
                double dist = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));
                livingEntity.push((dx / dist) * knockbackForce, 0.5, (dz / dist) * knockbackForce);
                livingEntity.hurtMarked = true; // força sincronização de movimento no cliente
                livingEntity.getLevel().playSound(null, mob, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 1, 1);
                if (livingEntity instanceof Player player) {
                    player.displayClientMessage(new TextComponent("That's ENOUGH").withStyle((style) -> {
                        Style newStyle = style;
                        Color color = new Color(0, 0, 0);
                        newStyle = newStyle.withColor(color.getRGB()).withBold(true).withItalic(true);
                        return newStyle;
                    }), true);
                }
            }
        }

        cooldownTimer = cooldown;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Chame este método na entidade para registrar o dano recebido.
     */
    public void registerDamage(float amount) {
        this.recentDamage += amount;
    }

    /**
     * Chame este método na entidade quando ela acertar um ataque.
     */
    public void registerHit() {
        this.recentHits++;
    }
}
