package net.foxyas.changedaddon.ability.handle.dodgeTypes;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.ability.handle.DamageContext;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class DodgeType {
    public DodgeType() {
        super();
    }

    public void applyDodgeMovement(DodgeAbilityInstance dodgeAbilityInstance, LivingEntity dodger, DamageContext sourceProjectileEither, boolean causeExhaustion) {
        Vec3 backWards = FoxyasUtil.getRelativePosition(dodger, 0, 0, -1, true).normalize();

        Vec3 multiply = backWards.multiply(0.85f, 0.25f, 0.85f);
        if (dodger instanceof ServerPlayer serverPlayer) {
            serverPlayer.setDeltaMovement(dodger.getDeltaMovement().add(multiply));
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer.getId(), serverPlayer.getDeltaMovement()));
            serverPlayer.hasImpulse = true;
        } else {
            dodger.setDeltaMovement(dodger.getDeltaMovement().add(multiply));
            dodger.hasImpulse = true;
        }
    }

    public void applyDodgeEffects(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, DamageContext sourceProjectileEither, boolean causeExhaustion) {
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

        sourceProjectileEither.ifDamageSource(damageSourceInstance -> dodgeAbilityInstance.subDodgeStamina(damageSourceInstance.amount() * 0.15f)).ifProjectile(p -> dodgeAbilityInstance.subDodgeStamina());
    }

    public float getFoodExhaustionCaused(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, DamageContext sourceProjectileEither) {
        return 8f;
    }

    public void startUsing(DodgeAbilityInstance dodgeAbilityInstance) {
    }

    public void tickIdle(DodgeAbilityInstance dodgeAbilityInstance) {
    }

    public boolean willApplyIFrames(DodgeAbilityInstance dodgeAbilityInstance, @Nullable LivingEntity dodger, DamageContext sourceProjectileEither, boolean causeExhaustion) {
        return false;
    }

    public float getDodgeStaminaUsage(DodgeAbilityInstance dodgeAbilityInstance, LivingEntity dodger, @Nullable DamageContext sourceProjectileEither, boolean causeExhaustion) {
        return getDodgeUsageWithoutContext(dodgeAbilityInstance);
    }

    public float getDodgeUsageWithoutContext(DodgeAbilityInstance dodgeAbilityInstance) {
        return dodgeAbilityInstance.isUltraInstinct() ? 0 : 1;
    }

    public boolean willPlayDodgeAnimation(LivingEntity dodger) {
        return false;
    }

    public boolean willDodge(DodgeAbilityInstance dodgeAbilityInstance, DamageContext sourceProjectileEither) {
        if (sourceProjectileEither.getDamage().map(damageSourceInstance -> damageSourceInstance.damageSource().is(ChangedAddonTags.DamageTypes.BYPASSES_DODGE)).orElse(false))
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