
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.entity.CustomHandle.CrawlFeature;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class VoidFoxEntity extends ChangedEntity implements CrawlFeature {
    private static final int MAX_DODGING_TICKS = 20;

    public VoidFoxEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonModEntities.VOID_FOX.get(), world);
    }

    public VoidFoxEntity(EntityType<VoidFoxEntity> type, Level world) {
        super(type, world);
        maxUpStep = 0.6f;
        xpReward = 1000;
        setNoAi(false);
        setPersistenceRequired();
    }

    public final ServerBossEvent bossBar = getBossBar();
    public ServerBossEvent getBossBar(){
        var bossBar = new ServerBossEvent(
                this.getDisplayName(), // Nome exibido na boss bar
                BossEvent.BossBarColor.WHITE, // Cor da barra
                BossEvent.BossBarOverlay.NOTCHED_10 // Estilo da barra
        );
        bossBar.setCreateWorldFog(true);
        bossBar.setDarkenScreen(true);
        return bossBar;
    }

    private static final EntityDataAccessor<Integer> DODGE_ANIM_TICKS =
            SynchedEntityData.defineId(VoidFoxEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DODGE_ANIM_TICKS, 0);
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.NEUTRAL;
    }

    @Override
    public TransfurMode getTransfurMode() {
        if (this.getTarget() != null) {
            return TransfurMode.NONE;
        }

        return TransfurMode.ABSORPTION;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public Color3 getDripColor() {
        return this.getRandom().nextBoolean() ? Color3.BLACK : Color3.WHITE;
    }

    @Override
    public MobType getMobType() {
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

    public double getTorsoYOffset(ChangedEntity self) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
        return (double) (Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
    }

    public double getTorsoYOffsetForFallFly(ChangedEntity self) {
        float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
        return 0.375 + bpiSize;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(this);
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("dodgeTicks")) {
            this.setDodgingTicks(tag.getInt("dodgeTicks"));
        }
        //if (tag.contains("DEVATTACKTESTTICK")) {
        //	this.DEVATTACKTESTTICK = tag.getInt("DEVATTACKTESTTICK");
        //}
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("dodgeTicks", this.getDodgingTicks());
        //tag.putInt("DEVATTACKTESTTICK", DEVATTACKTESTTICK);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        float randomValue = this.getRandom().nextFloat();
        boolean willHit = randomValue > 0.25f;

        if (source.getEntity() != null && !willHit) {
            this.setDodging(source.getEntity(), true);
            return false;
        }

        this.setDodging(source.getEntity(), false);
        return super.hurt(source, amount);
    }

    private void setDodging(Entity entity, boolean b) {
        if (b) {
            this.setDodgingTicks(getLevel().random.nextBoolean() ? MAX_DODGING_TICKS : -MAX_DODGING_TICKS);

            if (entity != null) {
                this.lookAt(entity, 1, 1);
            }
            this.getNavigation().stop();
        } else {

            if (entity != null) {
                this.lookAt(entity, 1, 1);
            }
            this.setDodgingTicks(0);
        }
    }

    public void setDodgingTicks(int dodgingTicks) {
        this.entityData.set(DODGE_ANIM_TICKS, dodgingTicks);
    }

    public boolean isDodging() {
        return getDodgingTicks() > 0;
    }

    public int getDodgingTicks() {
        return this.entityData.get(DODGE_ANIM_TICKS);
    }

    public static int getMaxDodgingTicks() {
        return MAX_DODGING_TICKS;
    }

    public void tickDodgeTicks() {
        if (!this.isNoAi()) {
            int ticks = this.getDodgingTicks();
            if (ticks > 0) {
                this.setDodgingTicks(ticks - 2);
            } else if (ticks < 0) {
                this.setDodgingTicks(ticks + 2);
            }
        }
    }

    @Override
    public @NotNull SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    public static void init() {
    }

    @Override
    public void visualTick(Level level) {
        super.visualTick(level);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        crawlingSystem(this, this.getTarget());
        tickDodgeTicks();
        if (!this.level.isClientSide) {
            this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder = builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 3f);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 2);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((7.5));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((500));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1);
        attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(10);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(20);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(2);
    }
}
