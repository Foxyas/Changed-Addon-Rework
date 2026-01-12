package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.entity.api.ICrawlAbleEntity;
import net.foxyas.changedaddon.entity.api.CustomPatReaction;
import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.foxyas.changedaddon.entity.customHandle.BossAbilitiesHandle;
import net.foxyas.changedaddon.entity.goals.exp10.ClawsComboAttackGoal;
import net.foxyas.changedaddon.entity.goals.exp10.ThrowWitherProjectileGoal;
import net.foxyas.changedaddon.entity.goals.exp10.WitherWave;
import net.foxyas.changedaddon.entity.goals.generic.BreakBlocksAroundGoal;
import net.foxyas.changedaddon.entity.goals.generic.BurstAttack;
import net.foxyas.changedaddon.entity.goals.generic.attacks.DashPunchGoal;
import net.foxyas.changedaddon.entity.goals.generic.attacks.LeapSmashGoal;
import net.foxyas.changedaddon.entity.goals.generic.attacks.SimpleAntiFlyingAttack;
import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.util.ColorUtil;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.foxyas.changedaddon.event.TransfurEvents.getPlayerVars;
import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment10BossEntity extends ChangedEntity implements GenderedEntity, CustomPatReaction, PowderSnowWalkable, IHasBossMusic, ICrawlAbleEntity {

    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(Experiment10BossEntity.class, EntityDataSerializers.BOOLEAN);
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private float TpCooldown;

    public Experiment10BossEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), world);
    }

    public Experiment10BossEntity(EntityType<Experiment10BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE2, false);
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (this.getUnderlyingPlayer() != null) {
            Player playerInControl = this.getUnderlyingPlayer();
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(playerInControl);
            if (transfurVariantInstance != null) {
                if (playerInControl.level().getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR)) {
                    if (!getPlayerVars(playerInControl).Exp10TransfurAllowed) {
                        ProcessTransfur.setPlayerTransfurVariant(playerInControl, ChangedAddonTransfurVariants.EXPERIMENT_10.get(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1, false);
                    }
                }
            }
        }
    }

    @Override
    public @Nullable ResourceLocation getBossMusic() {
        return ChangedAddonSoundEvents.EXP10_THEME.get().getLocation();
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }

    @Override
    public float getMusicVolume() {
        return 0.5f;
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
    protected boolean shouldDespawnInPeaceful() {
        return false;
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

        this.goalSelector.addGoal(5, new ClawsComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 40), //IntProvider -> castDuration,
                UniformFloat.of(6, 8))); //FloatProvider -> damage)

        this.goalSelector.addGoal(6, new BurstAttack(this));
        this.goalSelector.addGoal(6, new WitherWave(this, UniformInt.of(60, 120)));
        this.goalSelector.addGoal(20, new SimpleAntiFlyingAttack(this,
                UniformInt.of(60, 100),
                3,
                8,
                8f,
                10));
        this.goalSelector.addGoal(10, new LeapSmashGoal(this));
        this.goalSelector.addGoal(15, new DashPunchGoal(this));
        this.goalSelector.addGoal(10, new BreakBlocksAroundGoal(this));
        this.goalSelector.addGoal(10, new ThrowWitherProjectileGoal(this, UniformInt.of(60, 120), UniformInt.of(1, 8), 36));
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

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0.5f);
        }

        return super.hurt(source, amount);
    }

    private void maybeSendReactionToPlayer(DamageSource source) {
        if (this.level().random.nextFloat() <= 0.25f && source.getEntity() instanceof Player player) {
            player.displayClientMessage(Component.translatable("changed_addon.entity_dialogues.exp10.reaction.range_attacks"), true);
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

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    public boolean isPhase2() {
        return this.entityData.get(PHASE2);
    }

    public void setPhase2(boolean set) {
        this.entityData.set(PHASE2, set);
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
    public void baseTick() {
        super.baseTick();

        if (firstTick) {
            this.getBasicPlayerInfo().setSize(1f);
            this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
            this.getBasicPlayerInfo().setRightIrisColor(Color3.getColor("#880015"));
            this.getBasicPlayerInfo().setLeftIrisColor(Color3.getColor("#880015"));
            this.getBasicPlayerInfo().setScleraColor(Color3.getColor("#edd725"));
        }

        SetDefense(this);
        SetAttack(this);
        SetSpeed(this);
        TpEntity(this);
        this.crawlingSystem(this.getTarget());
        thisBurstAttack();
    }

    private void thisBurstAttack() {
        if (TpCooldown <= 0) {
            BossAbilitiesHandle.BurstAttack(this);
            this.TpCooldown = 50;
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
                    this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ChangedSounds.CARDBOARD_BOX_OPEN.get(), SoundSource.HOSTILE, 10, 1, true);
                    TpCooldown = 40;
                } else {
                    if (Targets != null && !(Targets instanceof ServerPlayer)) {
                        entity.setTarget(Targets);
                    } else if (Targets instanceof ServerPlayer serverPlayer) {
                        if (serverPlayer.gameMode.getGameModeForPlayer() != GameType.CREATIVE && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                            entity.setTarget(Targets);
                        }
                    }// Check if the entity in not null and is instance of server player if is will check if the gametype and if is not Creative and Spectator return true
                    entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
                    this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), ChangedSounds.CARDBOARD_BOX_OPEN.get(), SoundSource.HOSTILE, 10, 1, true);
                    TpCooldown = 40;
                }

            }
        } else {
            TpCooldown -= 0.5f;
        }
    }

    @Override
    public void WhenPattedReaction(Player player, InteractionHand hand) {
        if (!(player.level() instanceof ServerLevel)) return;
        if (player instanceof ServerPlayer serverPlayer) {
            ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.Trigger(serverPlayer, this, "pats_on_the_beast");
        }

        List<Component> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(Component.translatable("changed_addon.entity_dialogues.exp10.pat.type_0"));
        translatableComponentList.add(Component.translatable("changed_addon.entity_dialogues.exp10.pat.type_1"));
        translatableComponentList.add(Component.translatable("changed_addon.entity_dialogues.exp10.pat.type_2"));
        translatableComponentList.add(Component.translatable("changed_addon.entity_dialogues.exp10.pat.type_3"));
        player.level().addParticle(
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double) this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f
        );
        player.displayClientMessage(translatableComponentList.get(this.getRandom().nextInt(translatableComponentList.size())), false);
        applyRampage();
    }

    private void applyRampage() {
        MobEffectInstance thisEffect = this.getEffect(MobEffects.DAMAGE_BOOST);
        MobEffectInstance mobEffectInstance;
        if (thisEffect != null) {
            int pDuration = thisEffect.getDuration() + 10;
            int pAmplifier = Mth.clamp(thisEffect.getAmplifier() + 1, 0, 5);
            mobEffectInstance = new MobEffectInstance(MobEffects.DAMAGE_BOOST, pDuration, pAmplifier, thisEffect.isAmbient(), thisEffect.isVisible(), thisEffect.showIcon());
        } else {
            mobEffectInstance = new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 0, true, true, true);
        }
        this.addEffect(mobEffectInstance);
    }
}
