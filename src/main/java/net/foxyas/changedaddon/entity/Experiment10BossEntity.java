
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.entity.CustomHandle.BossAbilitiesHandle;
import net.foxyas.changedaddon.entity.CustomHandle.BossMusicTheme;
import net.foxyas.changedaddon.entity.CustomHandle.BossWithMusic;
import net.foxyas.changedaddon.entity.CustomHandle.CustomPatReaction;
import net.foxyas.changedaddon.entity.goals.DashPunchGoal;
import net.foxyas.changedaddon.entity.goals.LeapSmashGoal;
import net.foxyas.changedaddon.entity.goals.ThrowBlockAtTargetGoal;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment10BossEntity extends ChangedEntity implements GenderedEntity, BossWithMusic, CustomPatReaction {
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private float TpCooldown;

    public Experiment10BossEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonModEntities.EXPERIMENT_10_BOSS.get(), world);
    }

    public Experiment10BossEntity(EntityType<Experiment10BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        maxUpStep = 0.6f;
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
    }

    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(Experiment10BossEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE2, false);
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((325));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(12);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(10);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(6);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.8);
    }

    @Override
    public boolean ShouldPlayMusic() {
        return this.isAlive();
    }

    @Override
    public @NotNull BossMusicTheme BossMusicTheme() {
        return BossMusicTheme.EXP10;
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

    @Override
    public Color3 getHairColor(int i) {
        return Color3.getColor("#1f1f1f");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")));
    }

    @Override
    public void checkDespawn() {
        if (true) {
            return;
        }
        super.checkDespawn();
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.NEUTRAL;
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

    @Override
    public Color3 getDripColor() {
        return Color3.getColor("#181818");
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.DARK;
    }


    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(10, new LeapSmashGoal(this));
        this.goalSelector.addGoal(15, new DashPunchGoal(this));
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
                maybeSendReactionToPlayer(source);
                return super.hurt(source, amount * 0.5f);
            }
        }

        if (source.isProjectile()) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0.5f);
        }

        return super.hurt(source, amount);
    }

    private void maybeSendReactionToPlayer(DamageSource source) {
        if (this.getLevel().random.nextFloat() <= 0.25f && source.getEntity() instanceof Player player) {
            player.displayClientMessage(new TranslatableComponent("changed_addon.entity_dialogues.exp10.reaction.range_attacks"), true);
        }
    }


    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance.getEffect() == MobEffects.WITHER) {
            return false;
        }

        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.getBasicPlayerInfo().setSize(1f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        this.getBasicPlayerInfo().setRightIrisColor(Color3.parseHex("#edbd25"));
        this.getBasicPlayerInfo().setLeftIrisColor(Color3.parseHex("#edbd25"));
        this.getBasicPlayerInfo().setScleraColor(Color3.parseHex("#edd725"));
        return retval;
    }


    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public static void init() {
    }


    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 300);
        builder = builder.add(Attributes.ARMOR, 20);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    public void setPhase2(boolean set) {
        this.entityData.set(PHASE2, set);
    }

    public boolean isPhase2() {
        return this.entityData.get(PHASE2);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Tp_Cooldown"))
            TpCooldown = tag.getFloat("Tp_Cooldown");
        if (tag.contains("Phase2")) {
            setPhase2(tag.getBoolean("Phase2"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Tp_Cooldown", TpCooldown);
        tag.putBoolean("Phase2", isPhase2());
    }

    @Override
    public void visualTick(Level level) {
        super.visualTick(level);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        updateSwimmingMovement();
        SetDefense(this);
        SetAttack(this);
        SetSpeed(this);
        TpEntity(this);
        CrawlSystem(this.getTarget());
        thisBurstAttack();
    }

    private void thisBurstAttack() {
        if (TpCooldown <= 0) {
            BossAbilitiesHandle.BurstAttack(this);
            this.TpCooldown = 50;
        }
    }


    public void CrawlSystem(LivingEntity target) {
        if (target != null) {
            setCrawlingPoseIfNeeded(target);
            crawlToTarget(target);
        } else {
            if (!this.isSwimming() && !this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ())).isAir()) {
                this.setPose(Pose.SWIMMING);
            }
        }
    }

    public void setCrawlingPoseIfNeeded(LivingEntity target) {
        double targetEyeY = target.getEyeY();
        double entityEyeY = this.getEyeY();

        if (target.getPose() == Pose.SWIMMING && !(this.getPose() == Pose.SWIMMING)) {
            if (target.getY() < entityEyeY && !(target.level.getBlockState(new BlockPos(target.getX(), target.getEyeY(), target.getZ()).above()).isAir())) {
                this.setPose(Pose.SWIMMING);
            }
        } else {
            if (!this.isSwimming() && this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
                this.setPose(Pose.STANDING);
            }
        }
    }

    public void crawlToTarget(LivingEntity target) {
        if (target.getPose() == Pose.SWIMMING && this.getPose() == Pose.SWIMMING) {
            Vec3 delta = target.position().subtract(this.position());
            double distance = delta.length();

            if (distance > 1.0) {
                Vec3 motion = delta.normalize().scale(0.00015);
                this.setDeltaMovement(this.getDeltaMovement().add(motion));
            }
        }
    }


    public void updateSwimmingMovement() {
        if (!this.isInWater()) {
            if (this.getPose() == Pose.SWIMMING && level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
                this.setPose(Pose.STANDING);
                this.setSwimming(false);
            }
            return;
        }

        LivingEntity target = this.getTarget();
        if (target != null) {
            Vec3 delta = target.position().subtract(this.position());
            double distance = delta.length();
            if (distance > 0) {
                Vec3 motion = delta.normalize().scale(0.07);
                this.setDeltaMovement(this.getDeltaMovement().add(motion));
            }
        }

        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setPose(Pose.SWIMMING);
            this.setSwimming(true);
        }
    }


    public void SetDefense(Experiment10BossEntity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "ArmorChange", 20, AttributeModifier.Operation.ADDITION);
        AttributeModifier AttibuteDefenseChange = new AttributeModifier(UUID.fromString("10-10-0-0-0"), "ArmorChange", 0.7, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.isPhase2()) {
            if (!((entity.getAttribute(Attributes.ARMOR).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.ARMOR).addTransientModifier(AttibuteChange);
            }

            if (!((entity.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(AttibuteDefenseChange)))) {
                entity.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(AttibuteDefenseChange);
            }

        } else {
            entity.getAttribute(Attributes.ARMOR).removeModifier(AttibuteChange);
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(AttibuteDefenseChange);
        }
    }

    public void SetAttack(Experiment10BossEntity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Attack", 0.6667, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.isPhase2()) {
            if (!((entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(AttibuteChange);
            }
        } else {
            entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(AttibuteChange);
        }
    }

    public void SetSpeed(Experiment10BossEntity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.getPose() == Pose.SWIMMING) {
            if (!((entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttibuteChange);
            }
        } else {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttibuteChange);
        }
    }

    public void TpEntity(Experiment10BossEntity entity) {
        double deltaZ;
        double distance;
        double deltaX;
        double deltaY;
        if (entity.getTarget() == null) {
            return; //stop if target = @null
        }


        Entity Target = entity.getTarget();
        LivingEntity Targets = entity.getLastHurtByMob();
        deltaX = Target.getX() - entity.getX();
        deltaY = Target.getY() - entity.getY();
        deltaZ = Target.getZ() - entity.getZ();
        distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        if (TpCooldown == 0) {
            if (distance > 3) {
                if (entity.getLastHurtByMob() == Target) {
                    entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
                    this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ChangedSounds.BOW2, SoundSource.HOSTILE, 10, 1, true);
                    TpCooldown = 40;
                } else {
                    if (Targets != null && !(Targets instanceof ServerPlayer)) {
                        entity.setTarget(Targets);
                    } else if (Targets != null && Targets instanceof ServerPlayer serverPlayer) {
                        if (serverPlayer.gameMode.getGameModeForPlayer() != GameType.CREATIVE && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                            entity.setTarget(Targets);
                        }
                    }// Check if the entity in not null and is instance of server player if is will check if the gametype and if is not Creative and Spectator return true
                    entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
                    this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ChangedSounds.BOW2, SoundSource.HOSTILE, 10, 1, true);
                    TpCooldown = 40;
                }

				/*if((TpCooldown != 0)){
					TpCooldown -= 0.5f;
				}*/
            }
        } else {
            TpCooldown -= 0.5f;
        }
    }

    @Override
    public void WhenPattedReaction(Player player) {
        if (!player.getLevel().isClientSide) {
            return;
        }
        List<TranslatableComponent> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp10.pat.type_0"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp10.pat.type_1"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp10.pat.type_2"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp10.pat.type_3"));
        player.getLevel().addParticle(
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double) this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f
        );
        player.displayClientMessage(translatableComponentList.get(this.getRandom().nextInt(translatableComponentList.size())), false);
    }
}
