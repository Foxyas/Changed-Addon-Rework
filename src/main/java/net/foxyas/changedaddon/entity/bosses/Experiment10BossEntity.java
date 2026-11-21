package net.foxyas.changedaddon.entity.bosses;

import com.google.common.annotations.VisibleForTesting;
import net.foxyas.changedaddon.compatibility.ChangedAddonModCompatEvents;
import net.foxyas.changedaddon.entity.ai.goals.exp10.CircleShootWitherProjectileGoal;
import net.foxyas.changedaddon.entity.ai.goals.exp10.ClawsComboAttackGoal;
import net.foxyas.changedaddon.entity.ai.goals.exp10.WitherWave;
import net.foxyas.changedaddon.entity.ai.goals.generic.LatexPullEntityGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.DashPunchGoal;
import net.foxyas.changedaddon.entity.ai.goals.exp10.Exp10LeapSmashGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.SimpleAntiFlyingAttack;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.entity.customHandle.BurstAbilityHandle;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class Experiment10BossEntity extends Experiment10Entity implements IExp10Logic {

    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(Experiment10BossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL =
            SynchedEntityData.defineId(Experiment10BossEntity.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public final BurstAbilityHandle<Experiment10BossEntity> burstAbilityHandle;
    private final PatDynamicAngerManagement angerManagement;

    protected int lastHurtTime = tickCount;

    public Experiment10BossEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), world);
    }

    public Experiment10BossEntity(EntityType<Experiment10BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
        this.angerManagement = new PatDynamicAngerManagement(this::targetSelectorTest, new ArrayList<>());
        burstAbilityHandle = new BurstAbilityHandle.Builder<>(this)
                .setEvasive()
                .setDestructive()
                .canDestroyBlock((state, pos) -> !state.isAir() && state.getDestroySpeed(this.level, pos) >= 0.0F)
                .maxProgress(this::getMaxBurstProgress)
                .onEvasiveBurst(this::onEvasiveBurst)
                .build();
    }

    public void setDashing(boolean dashing) {
        if (dashing) {
            this.overridePose = Pose.SPIN_ATTACK;
        } else {
            this.overridePose = null;
        }
        setLivingEntityFlag(LIVING_ENTITY_FLAG_SPIN_ATTACK, dashing);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 300);
        builder = builder.add(Attributes.ARMOR, 10);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 256f);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    public BurstAbilityHandle<Experiment10BossEntity> getBurstAbilityHandle() {
        return burstAbilityHandle;
    }

    public float getMaxBurstProgress(Experiment10BossEntity exp10, BurstAbilityHandle<Experiment10BossEntity> burst) {
        return exp10.isPhase2() ? 75f : 150f;
    }

    public void onEvasiveBurst(Experiment10BossEntity exp10, BurstAbilityHandle<Experiment10BossEntity> burst) {
        burst.applyEvasiveKnockback(5, 1.25D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIENT_ANGER_LEVEL, 0);
    }

    public int getClientAngerLevel() {
        return this.entityData.get(CLIENT_ANGER_LEVEL);
    }

    private void syncClientAngerLevel() {
        this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
    }

    public AngerLevel getAngerLevel() {
        return AngerLevel.byAnger(this.getActiveAnger());
    }

    private int getActiveAnger() {
        return this.angerManagement.getActiveAnger(this.getTarget());
    }

    private int getActiveAnger(boolean fromPats) {
        return this.angerManagement.getAngerFrom(this.getTarget(), fromPats ? PatDynamicAngerManagement.AngerSource.PATS : PatDynamicAngerManagement.AngerSource.OTHER);
    }

    public DynamicAngerManagement getAngerManagement() {
        return this.angerManagement;
    }

    public void clearAnger(LivingEntity pEntity) {
        this.angerManagement.clearAnger(pEntity);
    }

    public void increaseAngerAt(@Nullable LivingEntity pEntity) {
        this.increaseAngerAt(pEntity, 35);
    }

    public void increaseAngerAt(@Nullable LivingEntity pEntity, int pOffset) {
        this.increaseAngerAt(pEntity, pOffset, false);
    }

    public void increaseAngerAt(@Nullable LivingEntity pEntity, boolean isFromPats) {
        this.increaseAngerAt(pEntity, 35, isFromPats);
    }

    @VisibleForTesting
    public void increaseAngerAt(@Nullable LivingEntity pEntity, int pOffset, boolean isFromPats) {
        if (!this.isNoAi() && this.targetSelectorTest(pEntity)) {
            boolean flag = !(this.getTarget() instanceof Player);
            int i = this.angerManagement.increaseAnger(pEntity, pOffset, isFromPats ? PatDynamicAngerManagement.AngerSource.PATS : PatDynamicAngerManagement.AngerSource.OTHER);
            if (pEntity instanceof Player && flag && AngerLevel.byAnger(i).isAngry()) {
                this.setTarget(null);
            }
        }

    }

    public Optional<LivingEntity> getEntityAngryAt() {
        return this.getAngerLevel().isAngry() ? this.angerManagement.getActiveEntity() : Optional.empty();
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (getUnderlyingPlayer() == null) return;

        Player playerInControl = this.getUnderlyingPlayer();
        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(playerInControl);
        if (instance == null) return;

        if (playerInControl.level().getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR)) {
            if (!ChangedAddonVariables.ofPlayerSafe(playerInControl)
                    .map(playerVariables -> playerVariables.transfurPermissions.hasPermission(this.getSelfVariant()))
                    .orElse(false)) {
                ProcessTransfur.setPlayerTransfurVariant(playerInControl, ChangedAddonTransfurVariants.EXPERIMENT_10.get(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1, false);
            }
        }
    }

    @Override
    public @Nullable ResourceLocation getBossMusicId() {
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
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue(3);
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(325);
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(12);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(10);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(6);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.8);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.5f);
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

    protected void addAbilityGoals() {
        this.goalSelector.addGoal(5, new ClawsComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 40), //IntProvider -> castDuration,
                UniformFloat.of(6, 8))); //FloatProvider -> damage)
        this.goalSelector.addGoal(6, new WitherWave(this, UniformInt.of(60, 120)));
        this.goalSelector.addGoal(10, new Exp10LeapSmashGoal(this));
        this.goalSelector.addGoal(15, new DashPunchGoal(this));
        //this.goalSelector.addGoal(10, new BreakBlocksAroundGoal(this));
//        this.goalSelector.addGoal(10, new ThrowWitherProjectileGoal(this, UniformInt.of(60, 120), UniformInt.of(1, 8), 36));
        this.goalSelector.addGoal(10, new CircleShootWitherProjectileGoal(this, UniformInt.of(60, 120), UniformInt.of(1, 8), 36));
    }

    protected void addPassivesGoals() {
        this.passivesSelector.addGoal(20, new SimpleAntiFlyingAttack(this,
                UniformInt.of(60, 100),
                3,
                32,
                8f,
                10));
        this.passivesSelector.addGoal(10, new LatexPullEntityGoal(this, 32, 1));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        String id = source.getMsgId();

        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;

        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            amount *= 0.25f;
        }

        switch (id) {
            case "fall", "cactus", "drown", "lightningBolt", "anvil", "dragonBreath", "wither", "witherSkull" -> {
                return false;
            }
            case "trident" -> amount *= 0.5f;
        }

        if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            amount *= 0.25f;
        }

        if (source.is(DamageTypeTags.IS_FIRE)) {
            amount = 0;
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 0.5f;
        }

        if (ChangedAddonModCompatEvents.isDamageTypeBullet(source)) {
            amount *= 0.5f;
        }

        maybeSendDamageReactionToPlayer(source);

        if (burstAbilityHandle != null) {
            burstAbilityHandle.onDamageTaken(source, amount);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        super.setTarget(entity);
        if (burstAbilityHandle != null) {
            burstAbilityHandle.setTarget(entity);
        }
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource pDamageSource, float pDamageAmount) {
        super.actuallyHurt(pDamageSource, pDamageAmount);

        float currentHealth = this.getHealth();
        float maxHealth = this.getMaxHealth();

        if (getUnderlyingPlayer() == null) lastHurtTime = tickCount;

        if (pDamageSource.getEntity() instanceof LivingEntity livingEntity) {
            increaseAngerAt(livingEntity);
        }

        if (this.getUnderlyingPlayer() == null && currentHealth <= maxHealth * 0.75f && !isPhase2()) {
            this.setPhase2(true);
            level.playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 0);
        }
    }

    private void maybeSendDamageReactionToPlayer(DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return;

        if (this.random.nextFloat() <= 0.25f) {
            if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                player.displayClientMessage(getEntityChat(Component.translatable("entity_dialogues.changed_addon.exp10.reaction.range_attacks")), false);
            } else if (source.is(DamageTypeTags.IS_FIRE)) {
                player.displayClientMessage(getEntityChat(Component.translatable("entity_dialogues.changed_addon.exp10.reaction.fire_damage")), false);
            }
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

        float maxHealth = this.getMaxHealth();
        float currentHealth = this.getHealth();
        float healthRatio = currentHealth / maxHealth;

        // Se estiver com menos de 50% da vida, simula que 50% é o "cheio" da barra
        if (healthRatio <= 0.75f) {
            this.bossInfo.setProgress(healthRatio / 0.75f);
            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_10) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
            }
            this.bossInfo.setName(bossInfo.getName().copy().withStyle(style -> style.withColor(ChatFormatting.DARK_RED)));
        } else {
            this.bossInfo.setProgress(healthRatio);
            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_6) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_6);
            }
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.angerManagement.load(tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.angerManagement.save(tag);
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    @Override
    protected EntityDataAccessor<Boolean> getPhase2DataAccessor() {
        return PHASE2;
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (getUnderlyingPlayer() == null
                && lastHurt > 0 && tickCount - lastHurtTime > 200 && tickCount % 10 == 0) heal(lastHurt * 0.05f);

        updateDefense();
        updateAttack();
        updateSpeed();

        this.crawlingSystem((float) this.getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 0.35f);
        this.burstAbilityHandle.tick();
    }

    @Override
    public boolean speak(Component component, @Nullable LivingEntity hearTarget) {
        MutableComponent entityChat = getEntityChat(component);
        boolean spoke = false;
        if (hearTarget instanceof Player player) {
            player.displayClientMessage(entityChat, false);
            spoke = true;
        } else {
            sout(component);
        }
        return spoke;
    }

    @Override
    public void sout(Component component) {
        MutableComponent entityChat = getEntityChat(component);
        List<Player> nearbyPlayers = this.level.getNearbyPlayers(TargetingConditions.DEFAULT.ignoreLineOfSight().ignoreInvisibilityTesting(), this, this.getBoundingBox().inflate(this.speakRange()));
        for (Player player : nearbyPlayers) {
            player.displayClientMessage(entityChat, false);
        }
    }

    public void updateDefense() {
        AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "ArmorChange", 1, AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeModifier attributeModifier2 = new AttributeModifier(UUID.fromString("10-10-0-0-0"), "ArmorChange", 0.7, AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeInstance armorAttributeInstance = this.getAttribute(Attributes.ARMOR);
        AttributeInstance armorThoughnessAttributeInstance = this.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (this.isPhase2()) {
            if (armorAttributeInstance != null && !armorAttributeInstance.hasModifier(attributeModifier)) {
                armorAttributeInstance.addTransientModifier(attributeModifier);
            }

            if (armorThoughnessAttributeInstance != null && !armorThoughnessAttributeInstance.hasModifier(attributeModifier2)) {
                armorThoughnessAttributeInstance.addTransientModifier(attributeModifier2);
            }

        } else {
            if (armorAttributeInstance != null) {
                armorAttributeInstance.removeModifier(attributeModifier);
            }
            if (armorThoughnessAttributeInstance != null) {
                armorThoughnessAttributeInstance.removeModifier(attributeModifier2);
            }
        }
    }

    public void updateAttack() {
        AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Attack", 0.65, AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeInstance attributeInstance = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attributeInstance == null) return;
        if (this.isPhase2() && !attributeInstance.hasModifier(attributeModifier)) {
            attributeInstance.addTransientModifier(attributeModifier);
        } else {
            attributeInstance.removeModifier(attributeModifier);
        }
    }

    public void updateSpeed() {
        AttributeModifier attributeModifier = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeInstance == null) return;
        if (this.getPose() == Pose.SWIMMING) {
            if (!attributeInstance.hasModifier(attributeModifier)) {
                attributeInstance.addTransientModifier(attributeModifier);
            }
        } else {
            attributeInstance.removeModifier(attributeModifier);
        }
    }

    @Override
    public boolean onPattedBy(LivingEntity patter, InteractionHand hand) {
        if (!(patter.level() instanceof ServerLevel)) return false;
        if (patter instanceof ServerPlayer serverPlayer) {
            ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.trigger(serverPlayer, this, "pats_on_the_beast");
        }
        if (!(patter instanceof Player player)) {
            return false;
        }

        List<Component> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_0"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_1"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_2"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_3"));

        ParticlesUtil.sendParticles(player.level(),
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double) this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f, 1, 0f
        );

        int index = this.getRandom().nextInt(translatableComponentList.size());
        Component translatableComponent = translatableComponentList.get(index);

        increaseAngerAt(player, 30, true);

        int activeAngerFromPats = getActiveAnger(true);
        if (activeAngerFromPats >= 60) {
            player.hurt(this.damageSources().mobAttack(this), (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2f));
            translatableComponent = Component.translatable("entity_dialogues.changed_addon.exp10.pat.damage_after_warn");
            increaseAngerAt(player, -activeAngerFromPats, true);
        }
        MutableComponent entityChat = Component.translatable("chat.type.text", this.getDisplayName(), translatableComponent);
        player.displayClientMessage(entityChat, false);
        applyRageBuff();
        return true;
    }

    private void applyRageBuff() {
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

    @Override
    public boolean canCauseGrabDamage() {
        return true;
    }

    @Override
    public void applyAlphaAttributesModifiers(LivingEntity entity, float normalized) {
        IAlphaAbleEntity.apply(entity, Attributes.MAX_HEALTH, IAlphaAbleEntity.MAX_HEALTH, "Alpha Max Health", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_DAMAGE, IAlphaAbleEntity.ATTACK_DAMAGE, "Alpha Attack Damage", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ARMOR, IAlphaAbleEntity.ARMOR, "Alpha Armor", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ARMOR_TOUGHNESS, IAlphaAbleEntity.ARMOR_TOUGHNESS, "Alpha Armor Toughness", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.STEP_HEIGHT_ADDITION.get(), IAlphaAbleEntity.STEP_HEIGHT, "Alpha Step Height", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ChangedAttributes.TRANSFUR_DAMAGE.get(), IAlphaAbleEntity.TRANSFUR_DAMAGE, "Alpha Transfur Damage", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_KNOCKBACK, IAlphaAbleEntity.ATTACK_KNOCKBACK, "Alpha Knockback", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_SPEED, IAlphaAbleEntity.ATTACK_SPEED, "Alpha Attack Speed", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.ENTITY_REACH.get(), IAlphaAbleEntity.ENTITY_REACH, "Alpha Attack Reach", normalized * 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.BLOCK_REACH.get(), IAlphaAbleEntity.BLOCK_REACH, "Alpha Block Reach", normalized * 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ChangedAttributes.JUMP_STRENGTH.get(), IAlphaAbleEntity.JUMP_STRENGTH, "Alpha Jump Strength", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}
