package net.foxyas.changedaddon.ability.handle;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record DamageSourceInstance(DamageSource damageSource, float amount) {


    @Nullable
    public Entity getDirectEntity() {
        return this.damageSource().getDirectEntity();
    }

    /**
     * Retrieves the true causer of the damage, e.g. the player who fired an arrow, the shulker who fired the bullet,
     * etc.
     */
    @Nullable
    public Entity getEntity() {
        return this.damageSource().getEntity();
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public Component getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        return this.damageSource().getLocalizedDeathMessage(pLivingEntity);
    }

    /**
     * Return the name of damage type.
     */
    public String getMsgId() {
        return this.damageSource().getMsgId();
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean scalesWithDifficulty() {
        return this.damageSource().scalesWithDifficulty();
    }

    public boolean isCreativePlayer() {
        return this.damageSource().isCreativePlayer();
    }

    /**
     * Gets the location from which the damage originates.
     */
    @Nullable
    public Vec3 getSourcePosition() {
        return this.damageSource().getSourcePosition();
    }

    @Nullable
    public Vec3 sourcePositionRaw() {
        return this.damageSource().sourcePositionRaw();
    }

    public boolean is(TagKey<DamageType> pDamageTypeKey) {
        return this.damageSource().is(pDamageTypeKey);
    }

    public boolean is(ResourceKey<DamageType> pDamageTypeKey) {
        return this.damageSource().is(pDamageTypeKey);
    }

    public DamageType type() {
        return this.damageSource().type();
    }

    public Holder<DamageType> typeHolder() {
        return this.damageSource().typeHolder();
    }
}
