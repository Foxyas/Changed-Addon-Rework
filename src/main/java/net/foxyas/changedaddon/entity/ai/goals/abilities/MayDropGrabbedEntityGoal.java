package net.foxyas.changedaddon.entity.ai.goals.abilities;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.mixins.entity.CombatTrackerAccessor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.Optional;

public class MayDropGrabbedEntityGoal extends Goal {

    private final IGrabberEntity grabber;

    public MayDropGrabbedEntityGoal(IGrabberEntity grabber) {
        this.grabber = grabber;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
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
        GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
        PathfinderMob mob = grabber.asMob();
        return ((CombatTrackerAccessor) mob.getCombatTracker()).isTakingDamage() && mob.getCombatTracker().getCombatDuration() <= 20 && grabAbilityInstance != null && grabAbilityInstance.grabbedEntity != null;
    }

    @Override
    public void start() {
        super.start();
        CombatTrackerAccessor combatTracker = (CombatTrackerAccessor) grabber.asMob().getCombatTracker();
        Optional<CombatEntry> first = combatTracker.getEntries().stream().findFirst();
        first.ifPresent((combatEntry -> grabber.mayDropGrabbedEntity(combatEntry.source(), combatEntry.damage())));
    }
}
