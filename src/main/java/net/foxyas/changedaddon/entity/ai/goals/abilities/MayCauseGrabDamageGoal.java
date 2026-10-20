package net.foxyas.changedaddon.entity.ai.goals.abilities;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.util.GrabAbilityUtil;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class MayCauseGrabDamageGoal extends Goal {

    private final PathfinderMob mob;
    private final IGrabberEntity grabber;

    private int cooldown;

    public MayCauseGrabDamageGoal(IGrabberEntity grabber) {
        this.grabber = grabber;
        this.mob = grabber.asMob();
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        if (!mob.isAlive()) return false;

        if (grabber.getGrabbedEntity() == null || grabber.getGrabAbilityInstance() == null) return false;

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

        if (!mob.level().isClientSide()) {
            GrabEntityAbilityInstance grabAbilityInstance = grabber.getGrabAbilityInstance();
            if (grabAbilityInstance == null) {
                return;
            }
            LivingEntity grabbedEntity = grabAbilityInstance.grabbedEntity;
            grabAbilityInstance.useDown = this.grabber.getGrabStrategy(grabbedEntity).shouldSuit() && !grabAbilityInstance.suited;


            if (grabbedEntity instanceof Player player) {
                TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (variantInstance != null && variantInstance.isTransfurring()) {
                    GrabAbilityUtil.releaseEntity(grabbedEntity, grabAbilityInstance.entity.getEntity(), grabAbilityInstance, false);
                }
            }
        }
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
