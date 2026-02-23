package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.network.packet.S2CCheckGrabberEntity;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

public interface IGrabberEntity {

    EntityDataAccessor<Boolean> CAN_USE_GRAB = SynchedEntityData.defineId(ChangedEntity.class, EntityDataSerializers.BOOLEAN);
    EntityDataAccessor<Integer> GRAB_COOLDOWN = SynchedEntityData.defineId(ChangedEntity.class, EntityDataSerializers.INT);

    PathfinderMob asMob();

    LivingEntity getGrabbedEntity();

    GrabEntityAbilityInstance getGrabAbilityInstance();

    default GrabEntityAbilityInstance createGrabAbility() {
        if (this instanceof ChangedEntity changedEntity) {
            return new GrabEntityAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get(), IAbstractChangedEntity.forEntity(changedEntity));
        } else return null;
    }

    default boolean canUseGrab() {
        return isAbleToGrab();
    }

    default void setCanUseGrab(boolean value) {
    }

    default void mayTickGrabAbility() {
        GrabEntityAbilityInstance grabAbilityInstance = this.getGrabAbilityInstance();
        if (grabAbilityInstance != null) {
            grabAbilityInstance.tickIdle();

            if (grabAbilityInstance.getController().getHoldTicks() > 0 && (grabAbilityInstance.canUse() && grabAbilityInstance.canKeepUsing())) {
                grabAbilityInstance.tick();
            }

            LivingEntity grabbed = grabAbilityInstance.grabbedEntity;
            if (grabbed != null) {
                IAbstractChangedEntity entity = grabAbilityInstance.entity;
                int grabberId = entity.getEntity().getId();
                if (!grabbed.level().isClientSide()) {
                    ChangedAddonMod.PACKET_HANDLER.send(
                            PacketDistributor.TRACKING_ENTITY.with(entity::getEntity),
                            new S2CCheckGrabberEntity(grabberId, grabbed.getId())
                    );
                }
            }

        }
    }

    default void saveGrabAbilityInTag(CompoundTag tag) {
        CompoundTag grabInstanceTag = new CompoundTag();
        tag.putBoolean("canUseGrab", canUseGrab());

        GrabEntityAbilityInstance grabAbilityInstance = this.getGrabAbilityInstance();
        if (grabAbilityInstance != null) {
            grabAbilityInstance.saveData(grabInstanceTag);
            tag.putInt("grabCooldown", this.getGrabCooldown());
            tag.put("grabAbility", grabInstanceTag);
        }
    }

    default void readGrabAbilityInTag(CompoundTag tag) {
        if (tag.contains("canUseGrab")) this.setCanUseGrab(tag.getBoolean("canUseGrab"));

        CompoundTag grabAbilityTag = tag.getCompound("grabAbility");
        GrabEntityAbilityInstance grabAbilityInstance = this.getGrabAbilityInstance();
        if (grabAbilityInstance != null) {
            grabAbilityInstance.readData(grabAbilityTag);
            if (tag.contains("grabCooldown")) this.setGrabCooldown(tag.getInt("grabCooldown"));
        }
    }

    default void mayDropGrabbedEntity(DamageSource pDamageSource, float pDamageAmount) {
        if (!this.asMob().level().isClientSide()) {
            GrabEntityAbilityInstance grabAbilityInstance = getGrabAbilityInstance();
            if (grabAbilityInstance != null) {
                LivingEntity grabbedEntity = grabAbilityInstance.grabbedEntity;
                if (grabbedEntity != null) {
                    grabAbilityInstance.releaseEntity(false);
                    // manda packet de GRAB (tipo ARMS)
                    Changed.PACKET_HANDLER.send(
                            PacketDistributor.TRACKING_ENTITY.with(this::asMob),
                            new GrabEntityPacket(this.asMob(), grabbedEntity, GrabEntityPacket.GrabType.RELEASE)
                    );

                    setGrabCooldown(120);
                }
            }
        }
    }

    int getGrabCooldown();

    void setGrabCooldown(int i);

    default void applyGrabCooldown(int extraTime) {
        this.setGrabCooldown(getGrabMaxCooldown() + extraTime);
    }

    default int getGrabMaxCooldown() {
        return 120;
    }

    default boolean shouldRespectGrab(PathfinderMob entitiesTryingToTarget) {
        return entitiesTryingToTarget.getType().is(ChangedAddonTags.EntityTypes.IGNORE_GRABBED_TARGETS);
    }

    default int getGrabDamageCooldown() {
        if (this instanceof LivingEntity living) {
            Level level = living.level();
            Difficulty difficulty = level.getDifficulty();
            return switch (difficulty) {
                case HARD -> 5;
                case NORMAL -> 10;
                case EASY -> 20;
                case PEACEFUL -> 20 * 2;
            };
        }
        return 20 * 2;
    }

    default void setCausingGrabDamage(boolean value) {
        GrabEntityAbilityInstance grabAbilityInstance = this.getGrabAbilityInstance();
        if (grabAbilityInstance != null) {
            grabAbilityInstance.attackDown = value;
        }
    }

    default boolean canCauseGrabDamage() {
        if (!(this instanceof LivingEntity living)) return false;
        GrabEntityAbilityInstance grabAbilityInstance = getGrabAbilityInstance();
        if (grabAbilityInstance == null) return false;
        if (living instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha()) return true;

        return living.level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.forNonCombat()
                        .range(16)
                        .ignoreLineOfSight()
                        .ignoreInvisibilityTesting(),
                living,
                living.getBoundingBox().inflate(16)
        ).stream().noneMatch((e) -> {
            // 1. If X is the grabber, it does not block the damage
            if (e == living) return false;

            // 2. If X is the grabbed entity, it does not block the damage
            if (e == grabAbilityInstance.grabbedEntity) return false;

            // 3. Armor stands are considered inanimate/empty and do not block damage
            if (e instanceof ArmorStand) return false;

            // 4. Skip entities that are tagged to NOT target grabbed entities.
            // Since they won't interfere, they don't block they ability to cause damage.
            return !e.getType().is(ChangedAddonTags.EntityTypes.IGNORE_GRABBED_TARGETS);
        });
    }

    boolean isAbleToGrab();

    default boolean canEntityGrab(EntityType<?> type, Level level) {
        if (type.is(ChangedAddonTags.EntityTypes.CANT_USE_GRAB)) {
            return false;
        }
        if (this instanceof ChangedEntity changedEntity) {
            if (changedEntity instanceof IAlphaAbleEntity alphaAbleEntity) return alphaAbleEntity.isAlpha();
        }
        return type.is(ChangedAddonTags.EntityTypes.CAN_GRAB) || isAbleToGrab();
    }
}
