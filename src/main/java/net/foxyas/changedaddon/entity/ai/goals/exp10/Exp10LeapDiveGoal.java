package net.foxyas.changedaddon.entity.ai.goals.exp10;

import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.LeapDiveGoal;
import net.foxyas.changedaddon.util.DelayedTask;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

public class Exp10LeapDiveGoal extends LeapDiveGoal {

    public Exp10LeapDiveGoal(PathfinderMob mob,
                             IntProvider cooldownProvider,
                             Vec3 followAscendMultiplier,
                             double ascendSpeed,
                             double ascendInitialBoost,
                             double ascendHoldY,
                             Vec3 diveSpeedMultiplier,
                             float ringRadius,
                             int failSafeTicks) {
        super(mob,
                cooldownProvider,
                followAscendMultiplier,
                ascendSpeed,
                ascendInitialBoost,
                ascendHoldY,
                diveSpeedMultiplier,
                ringRadius,
                failSafeTicks);
    }

    @Override
    public void stop() {
        // aterrissou
        mob.setNoGravity(false);
        mob.removeEffect(MobEffects.SLOW_FALLING);
        phase = null;

        if (!(mob.level() instanceof ServerLevel serverLevel)) return;

        BlockPos center = mob.blockPosition();

        // Anel of effects em 4 ondas (outline em XZ)
        applyKnockBack(center);
        spawnBlockBreakParticleCircle(serverLevel, center, ringRadius, 12, 5);
        DelayedTask.schedule(2, () -> spawnBlockBreakParticleCircle(serverLevel, center, ringRadius * 1.4, 14, 4));
        DelayedTask.schedule(5, () -> spawnBlockBreakParticleCircle(serverLevel, center, ringRadius * 1.8, 16, 3));
        DelayedTask.schedule(8, () -> spawnBlockBreakParticleCircle(serverLevel, center, ringRadius * 2.2, 20, 2));

        // efeito visual simples no chão
        // serverLevel.levelEvent(2001, center, Block.getId(Blocks.LIGHTNING_ROD.defaultBlockState()));

        cooldown = cooldownProvider.sample(this.mob.getRandom());
    }
}
