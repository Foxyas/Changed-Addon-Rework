package net.foxyas.changedaddon.entity.goals.exp9;

import com.google.common.collect.Iterables;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.ComponentUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.pathfinder.Path;

import java.util.Random;

public class InductionCoilGoal extends Goal {

    protected final PathfinderMob holder;
    protected final IntProvider cooldownProvider;
    protected final float tooFarSqr;
    protected final IntProvider durationProvider;
    protected final FloatProvider damageProvider;

    protected LivingEntity target;
    protected int cooldown;
    protected int duration;
    protected Iterable<ItemStack> items;

    public InductionCoilGoal(PathfinderMob holder, IntProvider cooldown, float tooFar, IntProvider duration, FloatProvider damage) {
        this.holder = holder;
        cooldownProvider = cooldown;
        tooFarSqr = tooFar * tooFar;
        durationProvider = duration;
        damageProvider = damage;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public boolean canUse() {
        target = holder.getTarget();
        if (target == null || !target.isAlive()) return false;

        Path path = holder.getNavigation().getPath();
        boolean stuck = (path != null && !path.canReach()) || holder.distanceToSqr(target) > tooFarSqr;

        if (cooldown > 0) {
            cooldown--;
            if (stuck) cooldown--;
            return false;
        }

        return stuck;
    }

    @Override
    public boolean canContinueToUse() {
        return target.isAlive() && duration > 0;
    }

    @Override
    public void start() {
        duration = durationProvider.sample(holder.getRandom());

        if (target instanceof Player player) {
            items = Iterables.concat(player.getInventory().offhand, target.getArmorSlots(), Iterables.limit(player.getInventory().items, 9));
        } else items = Iterables.concat(target.getHandSlots(), target.getArmorSlots());

        holder.level.playSound(null, holder, SoundEvents.TRIDENT_THUNDER, SoundSource.MASTER, 10000, 0.8f + holder.getRandom().nextFloat(0.2f));
    }

    @Override
    public void tick() {
        if (duration <= 0) return;
        Path path = holder.getNavigation().getPath();
        if ((path == null || path.canReach()) && holder.distanceToSqr(target) < tooFarSqr) duration--;

        spawnParticles();

        if (holder.tickCount % 20 != 0) return;

        Random random = holder.getRandom();
        int metal = 0, slots = 0;
        for (ItemStack stack : items) {
            slots++;
            if (stack.is(ChangedAddonTags.Items.METAL)) {
                hurtAndBreak(stack, (int) Math.max(2, stack.getMaxDamage() * 0.02f), random);
                metal++;
            } else if (stack.is(ChangedAddonTags.Items.PARTIAL_METAL)) {
                hurtAndBreak(stack, (int) Math.max(1, stack.getMaxDamage() * 0.01f), random);
                metal++;
            }
        }

        if (metal == 0) return;

        if (target instanceof Player player) player.displayClientMessage(ComponentUtil.translatable("message.changed_addon.induction_coil_melt"), true);

        float metalPercentage = (float) metal / slots;
        if (metalPercentage <= 0.1f) return;

        if (target.hurt(DamageSource.IN_FIRE, damageProvider.sample(random) * metalPercentage)) {
            target.setSecondsOnFire(5);
        }
    }

    protected void spawnParticles() {
        final float radius = 2;
        float rad;
        float x, z;
        float ringHeight = target.getBbHeight() / 4 + 0.1f;
        for (int ring = 0; ring < 4; ring++) {
            for (int i = 0; i < 10; i++) {
                rad = i * 36 * Mth.DEG_TO_RAD;
                x = Mth.cos(rad) * radius;
                z = Mth.sin(rad) * radius;

                ((ServerLevel) holder.level).sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        target.getX() + x - 0.05, target.getY() + ringHeight * ring - 0.05, target.getZ() + z - 0.05,
                        5, 0.1, 0.1, 0.1, 0);
            }
        }
    }

    protected void hurtAndBreak(ItemStack stack, int damage, Random random) {
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) return;
        if (!stack.isDamageableItem() || !stack.hurt(damage, random, null)) return;

        stack.shrink(1);
        stack.setDamageValue(0);

        if (target instanceof Player player) {
            player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
        }
    }

    @Override
    public void stop() {
        target = null;
        cooldown = cooldownProvider.sample(holder.getRandom());
        duration = 0;
        items = null;
    }
}
