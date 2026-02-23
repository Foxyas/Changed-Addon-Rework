package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.foxyas.changedaddon.entity.api.CustomMerchant;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class LookAndFollowTradingPlayerSink extends Goal {

    private final PathfinderMob mob;
    private final float speedModifier;

    public LookAndFollowTradingPlayerSink(PathfinderMob mob, float speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!(mob instanceof CustomMerchant merchant)) return false;

        Player tradingPlayer = merchant.getTradingPlayer();
        return tradingPlayer != null && tradingPlayer.distanceTo(mob) <= 16;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    @Override
    public boolean isInterruptable() {
        return super.isInterruptable();
    }

    @Override
    public void start() {
        if (!(mob instanceof CustomMerchant merchant)) return;

        Player tradingPlayer = merchant.getTradingPlayer();
        if (tradingPlayer != null) {
            this.mob.getLookControl().setLookAt(tradingPlayer);
            if (tradingPlayer.distanceTo(mob) <= 16 && mob.distanceTo(tradingPlayer) >= 1) {
                this.mob.getNavigation().moveTo(tradingPlayer, speedModifier);
            }
        }
    }

    @Override
    public void tick() {
        if (!(mob instanceof CustomMerchant merchant)) return;

        Player tradingPlayer = merchant.getTradingPlayer();
        if (tradingPlayer != null) {
            this.mob.getLookControl().setLookAt(tradingPlayer);
            if (tradingPlayer.distanceTo(mob) <= 16 && mob.distanceTo(tradingPlayer) >= 1) {
                this.mob.getNavigation().moveTo(tradingPlayer, speedModifier);
            }
        }
    }
}