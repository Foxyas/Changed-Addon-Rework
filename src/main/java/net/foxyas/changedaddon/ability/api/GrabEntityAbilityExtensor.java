package net.foxyas.changedaddon.ability.api;

import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonDamageSources;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface GrabEntityAbilityExtensor {

    int SNUGGLED_COOLDOWN = 20;

    void setSafeModeAuthoritative(boolean safeMode);

    boolean isSafeMode();

    void setSafeMode(boolean safeMode);

    boolean isTransfurDamageMode();

    void setTransfurDamageMode(boolean normalDamageMode);

    boolean isGrabbedUnableToBreath();

    LivingEntity grabber();

    default void runHug(@NotNull LivingEntity livingEntity) {
        if (grabber() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.hugger", livingEntity.getDisplayName()), true);
                if (player instanceof ServerPlayer serverPlayer) {
                    ChangedAddonCriteriaTriggers.GRAB_ENTITY_TRIGGER.trigger(serverPlayer, ProcessTransfur.getPlayerTransfurVariant(serverPlayer), "hug");
                }
                player.level().playSound(null, player, ChangedAddonSoundEvents.PLUSHY_SOUND.get(), SoundSource.BLOCKS, 1, 1);
                setSnuggled(true);
                if (EntityUtil.maybeGetUnderlying(player) instanceof IVariantExtraStats iVariantExtraStats) {
                    iVariantExtraStats.onHugTarget(livingEntity, HugType.SNUGGLE);
                }
            }
            if (livingEntity instanceof Player grabbedPlayer) {
                if (!grabbedPlayer.level().isClientSide())
                    grabbedPlayer.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.hugged", player.getDisplayName()), true);
            }
        }
    }

    boolean isAlreadySnuggled();

    void setSnuggled(boolean value);

    boolean isSnugglingTight();

    void setSnugglingTight(boolean value);

    default void runTightHug(@NotNull LivingEntity livingEntity) {
        if (grabber() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.hugger.tight", livingEntity.getDisplayName()), true);
                if (player instanceof ServerPlayer serverPlayer) {
                    ChangedAddonCriteriaTriggers.GRAB_ENTITY_TRIGGER.trigger(serverPlayer, ProcessTransfur.getPlayerTransfurVariant(serverPlayer), "hug");
                    ChangedAddonCriteriaTriggers.GRAB_ENTITY_TRIGGER.trigger(serverPlayer, ProcessTransfur.getPlayerTransfurVariant(serverPlayer), "hug_tight");
                }
                player.level().playSound(null, player, ChangedAddonSoundEvents.PLUSHY_SOUND.get(), SoundSource.BLOCKS, 1, 1);
                setSnugglingTight(true);
                if (EntityUtil.maybeGetUnderlying(player) instanceof IVariantExtraStats iVariantExtraStats) {
                    iVariantExtraStats.onHugTarget(livingEntity, HugType.TIGHT);
                }
            }
            if (livingEntity instanceof Player grabbedPlayer) {
                if (!grabbedPlayer.level().isClientSide()) {
                    grabbedPlayer.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.hugged.tight", player.getDisplayName()), true);
                }
            }
        }
    }

    void setAllowGrabTransfurred(boolean value);

    boolean allowGrabTransfurred();

    default boolean canGrabEntity(LivingEntity livingTarget) {
        GrabEntityAbilityInstance self = this instanceof GrabEntityAbilityInstance instance ? instance : null;
        if (self != null && self.entity.getChangedEntity() instanceof IOverrideGrabAbilityTargetConditions overrideGrabAbilityTargetConditions) {
            return overrideGrabAbilityTargetConditions.canGrabEntity(livingTarget, self); // For custom entities conditions
        }

        if (!this.isSafeMode()) return false;

        if (ProcessTransfur.isPlayerTransfurred(EntityUtil.playerOrNull(livingTarget)) && allowGrabTransfurred()) {
            return true;
        }
        return livingTarget instanceof ChangedEntity && allowGrabTransfurred();
    }

    default void tryCausingChokeDamage(LivingEntity grabber, float damageAmount) {
        if (!(this instanceof GrabEntityAbilityInstance instance)) {
            return;
        }

        LivingEntity grabbedEntity = instance.grabbedEntity;
        Consumer<LivingEntity> afterDamage = (livingEntity) -> {
            grabber.level().playSound(null, grabbedEntity.getX(), grabbedEntity.getY(), grabbedEntity.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 0.55f);
            if (grabbedEntity.isDeadOrDying()) {
                if (instance.suited) {
                    grabbedEntity.setInvisible(true);
                }
                instance.releaseEntity(false);
            }
        };

        if (grabbedEntity == null) return;
        DamageSource source = ChangedAddonDamageSources.CHOKE.source(grabber, net.foxyas.changedaddon.util.EntityUtil.getMouthPosition(grabbedEntity));

        if (instance.suited) {
            source = ChangedAddonDamageSources.CONSTRICTION.source(grabber);
        }

        if (grabber instanceof IGrabberEntity.ICanChokePlayers canChokePlayers) {
            source = canChokePlayers.getChokeDamageSource(grabber.level());

            if (canChokePlayers.doChokeDamage(grabbedEntity, source, damageAmount)) {
                afterDamage.accept(grabbedEntity);
            }
        } else {
            if (grabbedEntity.hurt(source, damageAmount)) {
                afterDamage.accept(grabbedEntity);
            }
        }
    }

    interface IOverrideGrabAbilityTargetConditions {
        default boolean canGrabEntity(LivingEntity livingTarget, GrabEntityAbilityInstance grabEntityAbilityInstance) {
            if (!(grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor grabEntityAbilityExtensor)) {
                return false;
            }

            // Uses the default behavior.
            if (!grabEntityAbilityExtensor.isSafeMode()) return false;
            boolean allowGrabTransfurred = grabEntityAbilityExtensor.allowGrabTransfurred();
            if (ProcessTransfur.isPlayerTransfurred(EntityUtil.playerOrNull(livingTarget)) && allowGrabTransfurred) {
                return true;
            }
            return livingTarget instanceof ChangedEntity && allowGrabTransfurred;
        }
    }

    enum HugType {
        SNUGGLE,
        TIGHT
    }
}
