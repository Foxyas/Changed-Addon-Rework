package net.foxyas.changedaddon.entity.ai.goals.abilities;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MayCauseGrabDamageGoal extends Goal {

    private final PathfinderMob mob;
    private final IGrabberEntity grabber;

    private int cooldown;

    public MayCauseGrabDamageGoal(IGrabberEntity grabber) {
        this.grabber = grabber;
        this.mob = grabber.asMob();
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        if (!mob.isAlive()) return false;

        if (grabber.getGrabbedEntity() == null) return false;

        return grabber.canCauseGrabDamage();
    }

    @Override
    public void start() {
        // Update the "causing dmg" flag of the grab ability instance
        grabber.setCausingGrabDamage(true);
    }

    @Override
    public void tick() {
        // force a tick of the grab ability instances to avoid issues.
        grabber.mayTickGrabAbility();
    }

    @Override
    public boolean canContinueToUse() {
        // A Single Tick is enough
        return false;
    }

    @Override
    public void stop() {
        grabber.setCausingGrabDamage(false);

        cooldown = grabber.getGrabDamageCooldown();
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
    protected int adjustedTickDelay(int pAdjustment) {
        return super.adjustedTickDelay(pAdjustment);
    }
}
