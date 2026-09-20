package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.process.GrabPersistenceMode;
import net.foxyas.changedaddon.process.GrabberAttachment;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class GrabReattachHandlerEvent {

    @SubscribeEvent
    public static void onPlayerLoggedInAttachGrab(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (!(ChangedAddonServerConfiguration.SHOULD_CHANGED_ENTITY_GRABBING_BE_PERSISTENT.get())) {
            GrabberAttachment attachment = ChangedAddonVariables.ofOrDefault(player).getGrabberAttachment();
            attachment.setStoredGrabberData(null);
            return;
        }

        if (player.level().isClientSide() || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        GrabberAttachment attachment = ChangedAddonVariables.ofOrDefault(player).getGrabberAttachment();
        CompoundTag storedTag = attachment.getStoredGrabberData();
        if (storedTag == null) {
            return; // player wasn't grabbed when they logged out
        }

        // Claim it immediately so a re-fired event or a failed spawn can't double-consume it.
        attachment.setStoredGrabberData(null);

        // Reconstruct the grabber (and any non-player passengers it still had) at the
        // player's current position — not wherever it was standing when they logged out.
        Entity spawnedRaw = EntityType.loadEntityRecursive(storedTag, serverLevel, entity -> {
            entity.moveTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), 0F);
            return !serverLevel.addWithUUID(entity) ? null : entity;
        });

        if (!(spawnedRaw instanceof LivingEntity freshGrabber)) {
            return; // couldn't reconstruct (e.g. entity type missing) — data is lost, log if needed
        }

        addRecursively(freshGrabber, serverLevel);

        // The real ServerPlayer object couldn't be part of the saved NBT (it didn't exist
        // yet, and it's a live connection, not data) — attach it manually now.

        IAbstractChangedEntity.forEitherSafe(freshGrabber).ifPresent(freshChanged -> {
            GrabEntityAbilityInstance freshAbility =
                    freshChanged.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
            if (freshAbility instanceof GrabEntityAbilityExtensor freshExtensor) {
                freshAbility.grabbedEntity = player; // live reference, must be set post-spawn
                freshExtensor.markNeedToSyncGrabber();
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerLoggedOutAttachGrab(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();

        if (!(ChangedAddonServerConfiguration.SHOULD_CHANGED_ENTITY_GRABBING_BE_PERSISTENT.get())) {
            GrabberAttachment attachment = ChangedAddonVariables.ofOrDefault(player).getGrabberAttachment();
            attachment.setStoredGrabberData(null);
            return;
        }

        if (player.level().isClientSide() || !(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        for (Entity entity : serverLevel.getAllEntities()) {
            if (!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }

            if (entity instanceof Player){
                continue;
            }

            Optional<IAbstractChangedEntity> abstractChangedEntity = IAbstractChangedEntity.forEitherSafe(livingEntity);
            if (abstractChangedEntity.isEmpty()) {
                continue;
            }

            IAbstractChangedEntity iAbstractChangedEntity = abstractChangedEntity.get();

            if (iAbstractChangedEntity.isPlayer()) {
                return;
            }

            GrabEntityAbilityInstance grabEntityAbilityInstance =
                    iAbstractChangedEntity.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());

            if (!(grabEntityAbilityInstance instanceof GrabEntityAbilityExtensor extensor)) {
                continue;
            }

            // Match on who this grabber is CURRENTLY holding — not on entityUUIDToTryAttachTo,
            // which is pending-reattach bookkeeping and may not be populated at logout time.
            // grabbedEntity is the live source of truth for "is this the player leaving?"
            Entity grabbedEntity = grabEntityAbilityInstance.grabbedEntity;
            if (grabbedEntity == null || !grabbedEntity.getUUID().equals(player.getUUID())) {
                continue;
            }

            if (!isGrabEligibleForPersistence(livingEntity, player)) {
                continue; // this grab type is excluded by GRAB_PERSISTENCE_MODE — let it end normally
            }

            CompoundTag tag = new CompoundTag();
            boolean saved = livingEntity.saveAsPassenger(tag);
            if (!saved) {
                // save() returns false if the entity is itself a passenger of something else,
                // or its RemovalReason doesn't allow saving. Bail out rather than store junk.
                continue;
            }

            ChangedAddonVariables.ofOrDefault(player).getGrabberAttachment().setStoredGrabberData(tag);

            // The grabber now exists ONLY as data on the player. Remove it from the world —
            // this is the literal "unload" half of unload/reload.
            livingEntity.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);


            break; // a player can only be grabbed by one entity at a time; stop scanning
        }
    }

    /**
     * Checks whether this specific grab qualifies for persistence under the current
     * GRAB_PERSISTENCE_MODE config setting.
     *
     * ADAPT: replace the body of isSafeGrab() below with however "owner" is actually
     * exposed in your codebase (e.g. a UUID field on the transfur/latex variant, a
     * method on IAbstractChangedEntity, etc). This is currently a guess.
     */
    public static boolean isGrabEligibleForPersistence(LivingEntity grabber, Player grabbedPlayer) {
        GrabPersistenceMode mode = ChangedAddonServerConfiguration.GRAB_PERSISTENCE_MODE.get(); // ADAPT: your config accessor path

        if (mode == GrabPersistenceMode.BOTH) {
            return true;
        }

        boolean safe;
        if (grabber instanceof TamableLatexEntity entity && entity.getOwner() != null && entity.getOwner().is(grabbedPlayer)) {
            safe = true;
        } else {
            safe = false;
        }
        return mode == GrabPersistenceMode.SAFE ? safe : !safe; // THREAT mode wants the inverse
    }

    /** Recursively registers a reconstructed entity and all its passengers into the level. */
    private static void addRecursively(Entity entity, ServerLevel serverLevel) {
        serverLevel.addFreshEntity(entity);
        for (Entity passenger : entity.getPassengers()) {
            if (passenger instanceof Player) {
                continue; // should never be present — players are stripped before saving
            }
            addRecursively(passenger, serverLevel);
        }
    }
}