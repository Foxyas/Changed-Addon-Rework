package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import com.mojang.datafixers.util.Either;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

public abstract class DodgeType {
    public DodgeType() {
        super();
    }

    public void applyDodgeMovement(DodgeAbilityInstance dodgeAbilityInstance, LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
    }

    public void applyDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        if (this.willApplyIFrames(dodgeAbilityInstance, dodger, sourceProjectileEither, causeExhaustion) && dodger != null) {
            dodger.invulnerableTime = 20 * 3;
            dodger.hurtDuration = 20 * 3;
            dodger.hurtTime = dodger.hurtDuration;
            dodger.hurtMarked = false;
        }

        if (dodger instanceof Player player) {
            player.displayClientMessage(Component.translatable("ability.changed_addon.dodge.dodge_amount_left", dodgeAbilityInstance.getDodgeStaminaRatio()), false);
            if (causeExhaustion) {
                player.causeFoodExhaustion(this.getFoodExhaustionCaused(dodgeAbilityInstance, dodger, sourceProjectileEither));
            }
        }

        dodgeAbilityInstance.subDodgeStamina();
    }

    public float getFoodExhaustionCaused(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither) {
        return 8f;
    }

    public void startUsing(DodgeAbilityInstance dodgeAbilityInstance) {
    }

    public void tickIdle(DodgeAbilityInstance dodgeAbilityInstance) {
    }

    public boolean willApplyIFrames(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        return false;
    }

    public float getDodgeUsage(DodgeAbilityInstance dodgeAbilityInstance, LivingEntity dodger, @Nullable Either<DamageSource, Projectile> sourceProjectileEither, boolean causeExhaustion) {
        return getDodgeUsageWithoutContext(dodgeAbilityInstance);
    }

    public float getDodgeUsageWithoutContext(DodgeAbilityInstance dodgeAbilityInstance) {
        return dodgeAbilityInstance.isUltraInstinct() ? 0 : 1;
    }

    public boolean willPlayDodgeAnimation(LivingEntity dodger) {
        return false;
    }

    public boolean willDodge(DodgeAbilityInstance dodgeAbilityInstance, Either<DamageSource, Projectile> sourceProjectileEither) {
        if (sourceProjectileEither.left().map(damageSource -> damageSource.is(ChangedAddonTags.DamageTypes.BYPASSES_DODGE)).orElse(false))
            return false;

        return dodgeAbilityInstance.canUse() && dodgeAbilityInstance.canKeepUsing() && dodgeAbilityInstance.isDodgeActive();
    }

    public boolean isSingleDodge(DodgeAbilityInstance dodgeAbilityInstance) {
        return this.getDodgeUsageWithoutContext(dodgeAbilityInstance) >= dodgeAbilityInstance.getMaxDodgeStamina();
    }

    public boolean shouldDisplayDodgeAmount(DodgeAbilityInstance dodgeAbilityInstance) {
        return !isSingleDodge(dodgeAbilityInstance);
    }

}