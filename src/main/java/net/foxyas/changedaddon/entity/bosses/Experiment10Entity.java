package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.entity.api.IDynamicPawColor;
import net.foxyas.changedaddon.entity.customHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.util.ColorUtil;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment10Entity extends ChangedEntity implements GenderedEntity, IDynamicPawColor, PowderSnowWalkable {
    //private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private float TpCooldown;
    private boolean Phase2;

    public Experiment10Entity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_10.get(), world);
    }

    public Experiment10Entity(EntityType<Experiment10Entity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 160;
        setNoAi(false);
        setPersistenceRequired();
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 300);
        builder = builder.add(Attributes.ARMOR, 20);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.MAX_HEALTH) + 16));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_DAMAGE) + 6.5);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ARMOR));
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ARMOR_TOUGHNESS));
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.KNOCKBACK_RESISTANCE));
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK));
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.4f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    public boolean startRiding(@NotNull Entity EntityIn, boolean force) {
        if (EntityIn instanceof Boat || EntityIn instanceof Minecart) {
            return false;
        }
        return super.startRiding(EntityIn, force);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity target) {
        if (target.getEyeY() > this.getEyeY() + 1) {
            return super.getMeleeAttackRangeSqr(target) * 1.5D;
        }
        return super.getMeleeAttackRangeSqr(target);
    }

    public Color3 getHairColor(int i) {
        return Color3.getColor("#1f1f1f");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(ChangedTags.EntityTypes.HUMANOIDS);
    }

    @Override
    public void checkDespawn() {
        super.checkDespawn();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance.getEffect() == MobEffects.WITHER) {
            return false;
        }

        return super.canBeAffected(mobEffectInstance);
    }


    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        return BALD.get();
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.FEMALE.getStyles();
    }

    public Color3 getDripColor() {
        return Color3.getColor("#181818");
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.getColor("#181818");
        Color3 secondColor = Color3.getColor("#ed1c24");
        return ColorUtil.lerpTFColor(firstColor, secondColor, this.getUnderlyingPlayer());
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        String id = source.getMsgId();

        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;

        switch (id) {
            case "fall", "cactus", "drown", "lightningBolt", "anvil", "dragonBreath", "wither", "witherSkull" -> {
                return false;
            }
            case "trident" -> {
                return super.hurt(source, amount * 0.5f);
            }
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            return super.hurt(source, amount * 0.5f);
        }

        return super.hurt(source, amount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.getBasicPlayerInfo().setSize(1f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        this.getBasicPlayerInfo().setRightIrisColor(Color3.getColor("#edbd25"));
        this.getBasicPlayerInfo().setLeftIrisColor(Color3.getColor("#edbd25"));
        this.getBasicPlayerInfo().setScleraColor(Color3.getColor("#edd725"));
        return retval;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        //this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        //this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        //this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    public boolean isPhase2() {
        return this.Phase2;
    }

    public void setPhase2(boolean set) {
        this.Phase2 = set;
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Tp_Cooldown"))
            TpCooldown = tag.getFloat("Tp_Cooldown");
        if (tag.contains("Phase2")) {
            Phase2 = tag.getBoolean("Phase2");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Tp_Cooldown", TpCooldown);
        tag.putBoolean("Phase2", Phase2);
    }


    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    public Color getPawBeansColor() {
        return Color.decode("#070607");
    }
}
