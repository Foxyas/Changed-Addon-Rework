package net.foxyas.changedaddon.ability.api;

import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public interface GrabEntityAbilityExtensor {

    int SNUGGLED_COOLDOWN = 20;

    void setSafeModeAuthoritative(boolean safeMode);

    boolean isSafeMode();

    void setSafeMode(boolean safeMode);

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
            }
            if (livingEntity instanceof Player grabbedPlayer) {
                if (!grabbedPlayer.level().isClientSide())
                    grabbedPlayer.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.hugged", player.getDisplayName()), true);
            }
        }
    }

    boolean isAlreadySnuggled();

    void setSnuggled(boolean value);

    boolean isAlreadySnuggledTight();

    void setSnuggledTight(boolean value);

    default void runTightHug(@NotNull LivingEntity livingEntity) {
        if (grabber() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable("ability.changed_addon.grab_entity.extender.hugger.tight", livingEntity.getDisplayName()), true);
                if (player instanceof ServerPlayer serverPlayer) {
                    ChangedAddonCriteriaTriggers.GRAB_ENTITY_TRIGGER.trigger(serverPlayer, ProcessTransfur.getPlayerTransfurVariant(serverPlayer), "hug");
                    ChangedAddonCriteriaTriggers.GRAB_ENTITY_TRIGGER.trigger(serverPlayer, ProcessTransfur.getPlayerTransfurVariant(serverPlayer), "hug_tight");
                }
                player.level().playSound(null, player, ChangedAddonSoundEvents.PLUSHY_SOUND.get(), SoundSource.BLOCKS, 1, 1);
                setSnuggledTight(true);
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
}
