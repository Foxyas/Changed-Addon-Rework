package net.foxyas.changedaddon.entity.ai.goals.generic;

import net.foxyas.changedaddon.entity.api.CustomMerchant;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;

import java.util.EnumSet;

public class TradeWithPlayerGoal extends Goal {

    private final PathfinderMob mob;

    public TradeWithPlayerGoal(PathfinderMob pMob) {
        this.mob = pMob;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (!this.mob.isAlive()) {
            return false;
        } else if (this.mob.isInWater()) {
            return false;
        } else if (!this.mob.onGround()) {
            return false;
        } else if (this.mob.hurtMarked) {
            return false;
        } else {
            if (this.mob instanceof Merchant merchant) {
                Player player = merchant.getTradingPlayer();
                if (player == null) {
                    return false;
                } else return !(this.mob.distanceToSqr(player) > 16.0D);
            }
            if (this.mob instanceof CustomMerchant customMerchant) {
                Player player = customMerchant.getTradingPlayer();
                if (player == null) {
                    return false;
                } else if (this.mob.distanceToSqr(player) > 16.0D) {
                    return false;
                } else {
                    return player.containerMenu != null;
                }
            }

            return false;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.mob.getNavigation().stop();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        if (this.mob instanceof Merchant merchant) {
            merchant.setTradingPlayer(null);
        } else if (this.mob instanceof CustomMerchant merchant) {
            merchant.setTradingPlayer(null);
        }
    }
}