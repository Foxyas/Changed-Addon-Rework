package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.ltxprogrammer.changed.entity.animation.StunAnimationParameters;
import net.ltxprogrammer.changed.init.ChangedAnimationEvents;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.List;

public class ElectrifyNearbyWaterGoal extends Goal {

    protected final Experiment009BossEntity boss;
    protected final UniformFloat damageProvider;

    public ElectrifyNearbyWaterGoal(Experiment009BossEntity experiment009BossEntity, UniformFloat damageProvider) {
        this.boss = experiment009BossEntity;
        this.damageProvider = damageProvider;
    }

    @Override
    public boolean canUse() {
        return boss.isInWater() && (boss.isPhase2() || boss.isPhase3());
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        Level level = boss.level;
        List<BlockPos> connectedFluids = FoxyasUtil.getConnectedFluids(level, boss.blockPosition(), 128).stream().map(BlockPos::immutable).toList();
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(32), EntitySelector.NO_CREATIVE_OR_SPECTATOR.and((entity) -> {
            if (entity.is(boss)) return false;

            DamageSource shockDmg = boss.getShockDmg();
            DamageSource thunderDmg = boss.getThunderDmg();
            return !(entity.isInvulnerableTo(shockDmg) && entity.isInvulnerableTo(thunderDmg));
        }));

        for (LivingEntity nearbyEntity : nearbyEntities) {
            if (connectedFluids.contains(nearbyEntity.blockPosition())) {
                if (nearbyEntity.hurt(boss.getShockDmg(), damageProvider.sample(boss.getRandom()) * boss.getPhase().getDamageModifier(nearbyEntity))) {
                    ChangedAnimationEvents.broadcastEntityAnimation(nearbyEntity, ChangedAnimationEvents.SHOCK_STUN.get(), StunAnimationParameters.INSTANCE);
                    level.playSound(null, nearbyEntity, ChangedSounds.TSC_WEAPON_SHOCK.get(), SoundSource.HOSTILE, 1, 1);
                    nearbyEntity.invulnerableTime = 40;
                    nearbyEntity.hurtDuration = 10;
                    nearbyEntity.hurtTime = nearbyEntity.hurtDuration;
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
