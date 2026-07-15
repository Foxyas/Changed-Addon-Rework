package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CounterDodgeType extends DodgeType {

    public static final CounterDodgeType COUNTER = new CounterDodgeType();

    public CounterDodgeType() {
        super();
    }

    @Override
    public void startUsing(DodgeAbilityInstance dodgeAbilityInstance) {
        super.startUsing(dodgeAbilityInstance);
        dodgeAbilityInstance.canDodgeTicks = 60;
    }

    @Override
    public void tickIdle(DodgeAbilityInstance dodgeAbilityInstance) {
        super.tickIdle(dodgeAbilityInstance);
        dodgeAbilityInstance.setDodgeActivate(dodgeAbilityInstance.getCanDodgeTicks() > 0);
        if (dodgeAbilityInstance.getCanDodgeTicks() > 0) {
            dodgeAbilityInstance.canDodgeTicks--;
            if (dodgeAbilityInstance.canDodgeTicks <= 0) {
                dodgeAbilityInstance.entity.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, true, true));
            }
        }
    }

    @Override
    public void applyDodgeMovement(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, LivingEntity dodger, Entity attacker, double distance, Vec3 dodgePosBehind, boolean causeExhaustion) {
        //dodgeAbilityInstance.dodgeAwayFromAttacker(dodger, attacker);
    }

    @Override
    public void applyDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, boolean causeExhaustion) {
        dodgeAbilityInstance.applyDodgeAnimations(dodger);
        dodgeAbilityInstance.subDodgeAmount();
        if (dodger != null) {
            dodger.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 2, true, true));
        }

        if (this.willApplyIFrames(dodgeAbilityInstance, levelAccessor, dodger, attacker, dodgeType, causeExhaustion) && dodger != null) {
            dodger.invulnerableTime = 10;
            dodger.hurtDuration = (int) (20 * 0.25);
            dodger.hurtTime = dodger.hurtDuration;
            dodger.hurtMarked = false;
            dodgeAbilityInstance.projectilesImmuneTicks = 30;
        }
    }

    @Override
    public boolean willPlayDodgeAnimation(LivingEntity dodger) {
        return true;
    }

    @Override
    public boolean willApplyIFrames(DodgeAbilityInstance dodgeAbilityInstance, LevelAccessor levelAccessor, @Nullable LivingEntity dodger, @Nullable Entity attacker, DodgeType dodgeType, boolean causeExhaustion) {
        return true;
    }

    @Override
    public boolean willDodge(DodgeAbilityInstance dodgeAbilityInstance, Entity entity) {
        return super.willDodge(dodgeAbilityInstance, entity);
    }
}