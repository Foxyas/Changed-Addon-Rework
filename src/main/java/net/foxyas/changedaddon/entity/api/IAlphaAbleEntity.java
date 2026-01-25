package net.foxyas.changedaddon.entity.api;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.goals.AlphaSleepGoal;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.Set;
import java.util.UUID;

public interface IAlphaAbleEntity {

    EntityDataAccessor<Boolean> IS_ALPHA = SynchedEntityData.defineId(ChangedEntity.class, EntityDataSerializers.BOOLEAN);
    EntityDataAccessor<Float> ALPHA_SCALE = SynchedEntityData.defineId(ChangedEntity.class, EntityDataSerializers.FLOAT);

    UUID MAX_HEALTH = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a001");
    UUID ATTACK_DAMAGE = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a002");
    UUID ARMOR = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a003");
    UUID ARMOR_TOUGHNESS = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a004");
    UUID STEP_HEIGHT = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a005");
    UUID TRANSFUR_DAMAGE = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a006");
    UUID ATTACK_KNOCKBACK = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a007");
    UUID ATTACK_SPEED = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a008");
    UUID ATTACK_REACH = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a009");
    UUID BLOCK_REACH = UUID.fromString("8b8f5a1b-1c5c-4b9b-a001-01a01a01a010");

    static void applyOrRemoveAlphaModifiers(LivingEntity entity, boolean isAlpha, float alphaScale) {
        if (entity.isDeadOrDying()) return;
        if (entity.level.isClientSide) return;
        removeAlphaModifiers(entity);

        if (entity instanceof PathfinderMob mob) {
            Set<WrappedGoal> availableGoals = mob.goalSelector.getAvailableGoals();
            boolean flag = availableGoals.stream().map(WrappedGoal::getGoal).anyMatch(goal -> goal instanceof AlphaSleepGoal);
            if (flag && !isAlpha) {
                mob.goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).filter(goal -> goal instanceof AlphaSleepGoal).forEach(mob.goalSelector::removeGoal);
            } else if (!flag && isAlpha) {
                mob.goalSelector.addGoal(10, new AlphaSleepGoal(mob, 6, (inter) -> inter >= 6, 1.5f, UniformInt.of(400, 800)));
            }
        }

        if (!isAlpha) {
            entity.setHealth(entity.getMaxHealth());
            return;
        }

        float normalized = alphaScale / 0.75f;

        apply(entity, Attributes.MAX_HEALTH, MAX_HEALTH, "Alpha Max Health", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE, "Alpha Attack Damage", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, Attributes.ARMOR, ARMOR, "Alpha Armor", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS, "Alpha Armor Toughness", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, ForgeMod.STEP_HEIGHT_ADDITION.get(), STEP_HEIGHT, "Alpha Step Height", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, ChangedAttributes.TRANSFUR_DAMAGE.get(), TRANSFUR_DAMAGE, "Alpha Transfur Damage", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK, "Alpha Knockback", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED, "Alpha Attack Speed", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, ForgeMod.ATTACK_RANGE.get(), ATTACK_REACH, "Alpha Attack Rang", normalized * 0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        apply(entity, ForgeMod.REACH_DISTANCE.get(), BLOCK_REACH, "Alpha Reach Distance", normalized * 0.5f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        entity.setHealth(entity.getMaxHealth());
    }

    private static void apply(LivingEntity entity, Attribute attribute, UUID uuid, String name, double value, AttributeModifier.Operation op) {
        AttributeInstance inst = entity.getAttribute(attribute);
        if (inst == null || value == 0) return;

        inst.addPermanentModifier(new AttributeModifier(uuid, name, value, op));
    }

    static void removeAlphaModifiers(LivingEntity entity) {
        remove(entity, Attributes.MAX_HEALTH, MAX_HEALTH);
        remove(entity, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
        remove(entity, Attributes.ARMOR, ARMOR);
        remove(entity, Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS);
        remove(entity, ForgeMod.STEP_HEIGHT_ADDITION.get(), STEP_HEIGHT);
        remove(entity, ChangedAttributes.TRANSFUR_DAMAGE.get(), TRANSFUR_DAMAGE);
        remove(entity, Attributes.ATTACK_KNOCKBACK, ATTACK_KNOCKBACK);
        remove(entity, Attributes.ATTACK_SPEED, ATTACK_SPEED);
        remove(entity, ForgeMod.ATTACK_RANGE.get(), ATTACK_REACH);
        remove(entity, ForgeMod.REACH_DISTANCE.get(), BLOCK_REACH);
    }

    private static void remove(LivingEntity entity, Attribute attr, UUID uuid) {
        AttributeInstance inst = entity.getAttribute(attr);
        if (inst != null) inst.removeModifier(uuid);
    }

    static boolean isEntityAlpha(Entity entity) {
        return entity instanceof IAlphaAbleEntity iAlphaAbleEntity && iAlphaAbleEntity.isAlpha();
    }

    static float getEntityAlphaScale(Entity entity) {
        return entity instanceof IAlphaAbleEntity iAlphaAbleEntity ? iAlphaAbleEntity.alphaAdditionalScale() : 0;
    }

    void setAlpha(boolean alphaGene);

    boolean isAlpha();

    void setAlphaScale(float scale);

    default void refreshAttributes(ChangedEntity self) {
        if (self.isDeadOrDying()) return;
        SynchedEntityData entityData = self.getEntityData();
        IAlphaAbleEntity.applyOrRemoveAlphaModifiers(self, entityData.get(IS_ALPHA), entityData.get(ALPHA_SCALE));
        IAbstractChangedEntity.forEitherSafe(self.maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    default void refreshAttributesForHost(ChangedEntity creature) {
        if (!(creature.maybeGetUnderlying() instanceof Player host)) return;
        if (host.isDeadOrDying()) return;

        SynchedEntityData entityData = creature.getEntityData();
        IAlphaAbleEntity.applyOrRemoveAlphaModifiers(host, entityData.get(IS_ALPHA), entityData.get(ALPHA_SCALE));
        IAbstractChangedEntity.forEitherSafe(host).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    default void cleanAlphaAttributesFromHost(ChangedEntity creature) {
        if (!(creature.maybeGetUnderlying() instanceof Player host)) return;
        if (host.isDeadOrDying()) return;

        IAlphaAbleEntity.applyOrRemoveAlphaModifiers(host, false, 0);
        IAbstractChangedEntity.forEitherSafe(host).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    default float chanceToSpawnAsAlpha() {
        if (this instanceof ChangedEntity changedEntity) {
            boolean cantSpawn = changedEntity.getType().is(ChangedAddonTags.EntityTypes.CANT_SPAWN_AS_ALPHA_ENTITY);
            if (cantSpawn) return 0f;

            Level level = changedEntity.level;
            Difficulty difficulty = level.getDifficulty();
            if (level.getLevelData().isHardcore()) return ChangedAddonServerConfiguration.ALPHA_SPAWN_HARDCORE.get().floatValue();

            return switch (difficulty) {
                case PEACEFUL -> ChangedAddonServerConfiguration.ALPHA_SPAWN_PEACEFUL.get().floatValue();
                case EASY -> ChangedAddonServerConfiguration.ALPHA_SPAWN_EASY.get().floatValue();
                case NORMAL -> ChangedAddonServerConfiguration.ALPHA_SPAWN_NORMAL.get().floatValue();
                case HARD -> ChangedAddonServerConfiguration.ALPHA_SPAWN_HARD.get().floatValue();
            };
        }

        return 0.025f; //Fail Safe
    }

    default float alphaCameraOffset() {
        if (isAlpha()) return alphaAdditionalScale() / 1.5f;
        return 0;
    }

    default float alphaScaleForRender() {
        if (this instanceof ChangedEntity changedEntity) {
            return 1 + alphaAdditionalScale(); // For future changes
        }
        return 1f;
    }

    default float alphaAdditionalScale() {
        if (this instanceof ChangedEntity changedEntity) {
            SynchedEntityData entityData = changedEntity.getEntityData();
            float scale;
            try {
                scale = entityData.get(ALPHA_SCALE);
            } catch (Exception e) {
                scale = 0.75f;
            }
            return scale; // For future changes
        }
        return 0f;
    }
}
