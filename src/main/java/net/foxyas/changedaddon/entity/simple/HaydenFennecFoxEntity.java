package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HaydenFennecFoxEntity extends AbstractBasicOrganicChangedEntity {
    public HaydenFennecFoxEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.HAYDEN_FENNEC_FOX.get(), world);
    }

    public HaydenFennecFoxEntity(EntityType<HaydenFennecFoxEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setPersistenceRequired();
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((15));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(50.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.5f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.00f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.1f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    public boolean causeFallDamage(float p_148859_, float p_148860_, @NotNull DamageSource p_148861_) {
        return false;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof LivingEntity living) {
            if (source.getMsgId().contains("mob")) {
                return super.hurt(source, amount * 0.9f);
            }
        }

        return super.hurt(source, amount);
    }

    public Color3 getDripColor() {
        return this.random.nextBoolean() ? Color3.getColor("F6DC70") : Color3.getColor("F0E4B9");
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.35D;
    }

}
