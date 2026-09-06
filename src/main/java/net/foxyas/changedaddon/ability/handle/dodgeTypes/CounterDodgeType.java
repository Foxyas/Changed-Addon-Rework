package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
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
    public void applyDodgeMovement(DodgeAbilityInstance dodgeAbilityInstance, LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        //dodgeAbilityInstance.dodgeAwayFromAttacker(dodger, attacker);
    }

    @Override
    public void applyDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        dodgeAbilityInstance.applyDodgeAnimations(sourceProjectileEither);
        dodgeAbilityInstance.subDodgeStamina();
        if (dodger != null) {
            dodger.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 2, true, true));
        }

        if (this.willApplyIFrames(dodgeAbilityInstance, dodger, sourceProjectileEither, causeExhaustion) && dodger != null) {
            dodger.invulnerableTime = 10;
            dodger.hurtDuration = (int) (20 * 0.25);
            dodger.hurtTime = dodger.hurtDuration;
            dodger.hurtMarked = false;
            dodgeAbilityInstance.projectilesImmuneTicks = 30;
            if (dodger instanceof Player player) {
                if (causeExhaustion) {
                    player.causeFoodExhaustion(8f);
                }
            }
        }
    }

    @Override
    public boolean willPlayDodgeAnimation(LivingEntity dodger) {
        return true;
    }

    @Override
    public boolean willApplyIFrames(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        return true;
    }
}