package net.foxyas.changedaddon.entity.goals;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class UseGrabAbilityGoal extends Goal {
    private final Mob mob;
    private final IAbstractChangedEntity changedEntity;
    private final GrabEntityAbilityInstance grabAbility;
    private LivingEntity target;
    private int ticks;

    public UseGrabAbilityGoal(Mob mob, GrabEntityAbilityInstance grabAbility) {
        this.mob = mob;
        this.changedEntity = grabAbility.entity;
        this.grabAbility = grabAbility;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity potentialTarget = this.mob.getTarget();
        if (potentialTarget == null || !potentialTarget.isAlive())
            return false;

        double distanceSq = this.mob.distanceToSqr(potentialTarget);
        if (distanceSq > 4.0D) // só tenta agarrar se estiver bem perto (2 blocos de distância)
            return false;

        // Não tenta usar se já está usando
        if (!grabAbility.canUse())
            return false;

        this.target = potentialTarget;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return grabAbility.canKeepUsing()
                && grabAbility.grabbedEntity != null
                && grabAbility.grabbedEntity.isAlive();
    }

    @Override
    public void start() {
        AbstractAbility.Controller controller = grabAbility.getController();
        grabAbility.getUseType().check(true, controller.exchangeKeyState(true), controller);
        grabAbility.startUsing(); // Inicia a habilidade (não inicia a grab ainda)
        grabAbility.tick();
        ticks = 0;
    }

    @Override
    public void tick() {
        if (grabAbility.grabbedEntity == null && target != null && mob.distanceToSqr(target) < 2.25D) {
            grabAbility.grabEntity(target); // Força o grab
        }


        grabAbility.tick(); // Avança a lógica da habilidade
        grabAbility.tickIdle(); // Avança a lógica da habilidade

        if (grabAbility.grabbedEntity != null) {
            if (grabAbility.attackDown) {
                ticks ++;
                if (ticks >= 20) {
                    grabAbility.attackDown = false;
                    ticks = 0;
                }
            }
        }

        if (!grabAbility.attackDown && changedEntity.getLevel().random.nextFloat() < 0.35f) {
            grabAbility.attackDown = true;
        }
    }

    @Override
    public void stop() {
        grabAbility.stopUsing();
    }
}
