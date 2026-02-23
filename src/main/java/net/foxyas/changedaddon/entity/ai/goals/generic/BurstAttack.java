package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.foxyas.changedaddon.entity.customHandle.BossAbilitiesHandle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class BurstAttack extends Goal {

    private final Mob boss;

    public BurstAttack(Mob boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = boss.getTarget();
        if (target == null) return false;

        boolean isClose = boss.distanceTo(target) <= 3;
        boolean attackedByAnother = boss.getLastHurtByMob() != null && boss.getLastHurtByMob() != target;

        return isClose && (attackedByAnother);
    }


    @Override
    public void start() {
        BossAbilitiesHandle.ExplosionBurst(this.boss);
    }
}