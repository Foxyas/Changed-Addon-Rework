package net.foxyas.changedaddon.entity.ai.goals.exp9;

import net.foxyas.changedaddon.entity.bosses.Experiment009BossEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

public class ThunderStorm extends Goal {

    public final Experiment009BossEntity boss;

    protected final IntProvider cooldownProvider;
    public int cooldown = 0;

    public ThunderStorm(Experiment009BossEntity boss, IntProvider cooldownProvider) {
        super();
        this.boss = boss;
        this.cooldownProvider = cooldownProvider;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        if (!boss.getCombatTracker().takingDamage) {
            return boss.getRandom().nextFloat() <= 0.05f;
        }

        LivingEntity target = this.getTarget();
        if (target != null) {
            double distance = this.boss.distanceTo(target);
            return distance <= 6;
        }
        return boss.getRandom().nextFloat() >= 0.6f;
    }


    public LivingEntity getTarget() {
        return this.boss.getTarget();
    }

    @Override
    public void start() {
        run();
    }

    public void run() {
        thunderStorm();
    }

    @Override
    public void stop() {
        super.stop();
        cooldown = (int) (cooldownProvider.sample(this.boss.getRandom()) * boss.getPhase().getDamageModifier(boss.getTarget()));
    }

    private void thunderStorm() {
        Level level = this.boss.level;
        if (level instanceof ServerLevel) {
            if (this.boss.getTarget() == null) {
                for (int i = 0; i < 7; i++) {
                    double offsetX = boss.getRandom().nextGaussian() * 20;
                    double offsetZ = boss.getRandom().nextGaussian() * 20;
                    BlockPos pos = new BlockPos((int) (this.boss.getX() + offsetX), (int) this.boss.getY(), (int) (this.boss.getZ() + offsetZ));
                    if (level.getBlockState(pos.below()).isAir()) return;
                    this.boss.spawnThunderBolt(pos);
                }
            } else {
                for (int i = 0; i < 12; i++) {
                    double offsetX = boss.getRandom().nextGaussian() * 10;
                    double offsetZ = boss.getRandom().nextGaussian() * 10;
                    BlockPos pos = new BlockPos((int) (this.boss.getX() + offsetX), (int) this.boss.getY(), (int) (this.boss.getZ() + offsetZ));
                    if (level.getBlockState(pos.below()).isAir()) return;
                    this.boss.spawnThunderBolt(pos);
                }
            }
        }
    }

}