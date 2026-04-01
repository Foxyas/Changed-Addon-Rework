package net.foxyas.changedaddon.entity.bosses;

import com.google.common.collect.Iterables;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.entity.ai.goals.exp9.*;
import net.foxyas.changedaddon.entity.ai.goals.generic.BreakBlocksAroundGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.LatexPullEntityGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.SimpleAntiFlyingAttack;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.animation.StunAnimationParameters;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.*;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
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
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static net.foxyas.changedaddon.event.TransfurEvents.getPlayerVars;

public class Experiment009BossEntity extends Experiment009Entity implements IExp9Logic {

    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(Experiment009BossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PHASE3 =
            SynchedEntityData.defineId(Experiment009BossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CASTING_ATTACK =
            SynchedEntityData.defineId(Experiment009BossEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private boolean shouldBleed;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation groundNavigation;

    public Experiment009BossEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_009_BOSS.get(), world);
    }

    public Experiment009BossEntity(EntityType<Experiment009BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
        applyDefaultBasicPlayerInfo();
        this.waterNavigation = new WaterBoundPathNavigation(this, world);
        this.groundNavigation = new GroundPathNavigation(this, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 425);
        builder = builder.add(Attributes.ARMOR, 12.5);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 15);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    private static GearTier getGearTier(LivingEntity entity) {

        double armor = entity.getAttributeValue(Attributes.ARMOR);
        double toughness = entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        double score = armor + (toughness * 2);

        if (score >= 40) return GearTier.HIGH;
        if (score >= 15) return GearTier.MID;
        return GearTier.LOW;
    }

    protected void applyDefaultBasicPlayerInfo() {
        super.applyDefaultBasicPlayerInfo();
    }

    public DamageSource getThunderDmg() {
        DamageSource damageSource = this.level().damageSources().lightningBolt();
        Holder<DamageType> pType = damageSource.typeHolder();
        return new DamageSource(pType, this);
    }

    public DamageSource getShockDmg() {
        DamageSource damageSource = this.level().damageSources().lightningBolt();
        Holder<DamageType> pType = damageSource.typeHolder();
        return new DamageSource(pType, this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE3, false);
        this.entityData.define(CASTING_ATTACK, false);
    }

    @Override
    protected EntityDataAccessor<Boolean> getPhase2DataAccessor() {
        return PHASE2;
    }

    public boolean isCastingAttack() {
        return this.entityData.get(CASTING_ATTACK);
    }

    public void setCastingAttack(boolean value) {
        this.entityData.set(CASTING_ATTACK, value);
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6f));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((425));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(256f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1f));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(10f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(11f);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(5f);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.05f);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.85f);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.5f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
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
        return super.getHairColor(i);
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return super.getTicksRequiredToFreeze();
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return super.targetSelectorTest(livingEntity);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return super.getTransfurMode();
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        return super.getDefaultHairStyle();
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.MALE.getStyles();
    }

    public Color3 getDripColor() {
        return super.getDripColor();
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return super.getTransfurColor(cause);
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
    protected void addAbilitiesGoals() {
        this.goalSelector.addGoal(15, new ElectrifyNearbyWaterGoal(this, UniformFloat.of(2, 6)));
        this.goalSelector.addGoal(20, new SimpleAntiFlyingAttack(this,
                UniformInt.of(60, 100),
                3,
                32,
                8f,
                10));
        this.goalSelector.addGoal(10, new ThunderStorm(this, UniformInt.of(60, 100)));

        //New AI
        this.goalSelector.addGoal(5, new ThunderStrikeGoal(
                this,
                UniformInt.of(80, 120), //IntProvider -> cooldownProvider
                UniformInt.of(4, 8), //IntProvider -> damageProvider
                1.5f,
                200));
        this.goalSelector.addGoal(10, new ThunderDiveGoal(this,
                UniformInt.of(60, 100), //IntProvider -> cooldownProvider
                1.5f,
                6f,
                1f,
                0.5f,
                4));

        //Basically perfect, damn... well done 0senia0
        this.goalSelector.addGoal(5, new SummonLightningGoal(this, //PathfinderMob -> holder,
                UniformInt.of(120, 240), //IntProvider -> cooldown,
                UniformInt.of(2, 4), //IntProvider -> lightningCount,
                UniformInt.of(80, 160), //IntProvider -> castDuration,
                UniformInt.of(80, 100), //IntProvider -> lightningDelay,
                UniformFloat.of(5, 12))); //FloatProvider -> damage

        this.goalSelector.addGoal(5, new StaticDischargeGoal(this,//PathfinderMob holder,
                UniformInt.of(75, 125), //IntProvider -> cooldown,
                4,
                UniformInt.of(40, 80), //IntProvider -> castDuration,
                8,
                UniformFloat.of(4, 8))); //FloatProvider -> damage

//        this.goalSelector.addGoal(1, new InductionCoilGoal(this, //PathfinderMob -> holder
//                UniformInt.of(100, 150), //IntProvider -> cooldown
//                20,
//                UniformInt.of(60, 80), //IntProvider -> duration
//                UniformFloat.of(3, 5))); //FloatProvider -> damage

        this.goalSelector.addGoal(5, new LightningComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 60), //IntProvider -> castDuration,
                UniformFloat.of(6, 8))); //FloatProvider -> damage)

        this.goalSelector.addGoal(10, new BreakBlocksAroundGoal(this));
        this.goalSelector.addGoal(10, new LatexPullEntityGoal(this, 32, 1));
    }

    public enum Exp9Phase {
        PHASE1(1f, 1f),
        PHASE2(2.5f, 0.5f),

        PHASE3(1.5f, 0.20f);

        private final float damageModifier;
        private final float castModifier;

        Exp9Phase(float damageModifier, float castModifier) {
            this.damageModifier = damageModifier;
            this.castModifier = castModifier;
        }

        public float getDamageModifier(LivingEntity target) {
            return damageModifier; //Todo tweak this damage modifier to be less or more based on the player "metalic points". (Being a prototype/Protogen,using metal armor, etc).
        }

        public float getCastModifier() {
            return castModifier;
        }
    }

    public Exp9Phase getPhase() {
        if (isPhase2()) return Exp9Phase.PHASE2;
        if (isPhase3()) return Exp9Phase.PHASE3;
        return Exp9Phase.PHASE1;
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (this.getUnderlyingPlayer() != null) {
            Player playerInControl = this.getUnderlyingPlayer();
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(playerInControl);
            if (transfurVariantInstance != null) {
                if (playerInControl.level().getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR)) {
                    if (!getPlayerVars(playerInControl).Exp009TransfurAllowed) {
                        ProcessTransfur.setPlayerTransfurVariant(playerInControl, ChangedAddonTransfurVariants.EXPERIMENT_009.get(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1, false);
                    }
                }
            }
        }
    }

    @Override
    public @Nullable ResourceLocation getBossMusic() {
        return ChangedAddonSoundEvents.EXP9_THEME.get().getLocation();
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }

    @Override
    public float getMusicVolume() {
        return 0.5f;
    }

    @Override
    public @NotNull MobType getMobType() {
        return super.getMobType();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return super.removeWhenFarAway(distanceToClosestPlayer);
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return super.getHurtSound(ds);
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource pSource) {
        if (pSource.is(DamageTypes.LIGHTNING_BOLT))
            return true;
        if (pSource.is(ChangedDamageSources.ELECTROCUTION.key())) {
            return true;
        }

        return super.isInvulnerableTo(pSource);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypes.OUTSIDE_BORDER) || source.is(DamageTypes.GENERIC_KILL))
            return super.hurt(source, amount);
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source.is(DamageTypeTags.IS_FALL))
            return false;
        if (source.is(DamageTypes.CACTUS))
            return false;
        if (source.is(DamageTypes.DROWN))
            return false;
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.getMsgId().equals("trident")) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0.5f);
        }
        if (source.is(DamageTypes.FALLING_ANVIL))
            return false;
        if (source.is(DamageTypes.DRAGON_BREATH))
            return false;
        if (source.is(DamageTypes.WITHER))
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;
        if (source.is(DamageTypes.IN_WALL)) {
            teleportToNearLivingEntity();
            return false;
        }
        if (source.getEntity() instanceof Warden warden) {
            DodgeAbilityInstance.executeRandomDodgeAnimation(this);
            this.navigation.stop();
            return false;
        }

        if (source.getEntity() == null || source.getDirectEntity() == null) {
            if (this.getTarget() == null) {
                teleportToNearLivingEntity();
                return false;
            }
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0.5f);
        }

        if (source.getMsgId().contains("bullet") || source.getMsgId().contains("gun")) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0.45f);
        }

        if (source.is(DamageTypeTags.IS_FIRE)) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0f);
        }
        if (source.is(DamageTypes.THORNS)) {
            return super.hurt(source, 0);
        }

        return super.hurt(source, amount);
    }

    private void teleportToNearLivingEntity() {
        List<LivingEntity> entitiesOfClass = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(64f), (target) -> !target.is(this) && this.canAttack(target)).stream().sorted((Comparator.comparing((target) -> target.distanceTo(this)))).toList();
        if (!entitiesOfClass.isEmpty()) {
            teleport(this.getTarget() == null
                    ? entitiesOfClass.get(0)
                    : this.getTarget());
        }
    }

    private void maybeSendReactionToPlayer(DamageSource source) {
        if (source.getEntity() instanceof Player player) {
            if (this.random.nextFloat() <= 0.25f) {
                if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                    player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.reaction.range_attacks"), true);
                } else if (source.is(DamageTypeTags.IS_FIRE)) {
                    player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp9.reaction.fire_damage"), true);
                }
            }
        }
    }

    public void teleport(LivingEntity target) {
        if (target == null || this.level().isClientSide) {
            return;
        }
        Vec3 targetPos = target.position().add(0, target.getEyeHeight() * 0.5, 0);
        this.teleportTo(targetPos.x, targetPos.y, targetPos.z);
        this.getLookControl().setLookAt(target, 180, 180);
        target.hurt(this.getThunderDmg(), 2);
    }

    @Override
    public boolean isDamageSourceBlocked(@NotNull DamageSource pDamageSource) {
        if (pDamageSource.is(ChangedDamageSources.ELECTROCUTION.key())) {
            return true;
        }
        return super.isDamageSourceBlocked(pDamageSource);
    }

    @Override
    public boolean canChangeDimensions() {
        return super.canChangeDimensions();
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

        if (healthRatio <= 0.4f) {

            float progress = healthRatio / 0.4f;
            this.bossInfo.setProgress(progress);

            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_10) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
            }
            this.bossInfo.setName(bossInfo.getName().copy().withStyle(style -> style.withColor(ChatFormatting.AQUA)));
        } else if (healthRatio <= 0.75f) {

            float progress = (healthRatio - 0.4f) / (0.75f - 0.4f);
            this.bossInfo.setProgress(progress);

            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_6) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_6);
            }
            this.bossInfo.setName(bossInfo.getName().copy().withStyle(style -> style.withColor(ChatFormatting.DARK_AQUA)));
        } else {

            float progress = (healthRatio - 0.75f) / (1.0f - 0.75f);
            this.bossInfo.setProgress(progress);

            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.PROGRESS);
            }
            this.bossInfo.setName(this.getDisplayName());
        }

        // Formula used: progress = (value - minValueOfPhase) / (maxValueOfPhase - minValueOfPhase)
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource pDamageSource, float pDamageAmount) {
        super.actuallyHurt(pDamageSource, pDamageAmount);

        float currentHealth = this.getHealth();
        float maxHealth = this.getMaxHealth();


        if (this.isPhase2()) {
            float ratio = this.computeHealthRatio();
            if (currentHealth <= maxHealth * 0.4f && ratio >= 0.4f && !this.isPhase3()) {
                this.setPhase3(true);
                this.knockbackNearbyEntities(this);
                level.playSound(null, this.blockPosition().above(), SoundEvents.BEACON_POWER_SELECT, SoundSource.HOSTILE, 500, 0);
            }
        } else if (this.getUnderlyingPlayer() == null && currentHealth <= maxHealth * 0.75f) {
            this.setPhase2(true);
            this.SpawnThunderBolt(this.position());
            level.playSound(null, this.blockPosition().above(), SoundEvents.BEACON_POWER_SELECT, SoundSource.HOSTILE, 500, 0);
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return this.getTarget() != null ? 0.98f : super.getWaterSlowDown();
    }

    private void knockbackNearbyEntities(LivingEntity source) {
        AABB attackArea = source.getBoundingBox().inflate(6);
        List<LivingEntity> nearby = source.level.getEntitiesOfClass(LivingEntity.class, attackArea);


        for (LivingEntity target : nearby) {
            if (target != source && source.canAttack(target)) {
                double xForce = Mth.sin(source.getYRot() * ((float) Math.PI / 180F));
                double zForce = -Mth.cos(source.getYRot() * ((float) Math.PI / 180F));
                target.knockback(5, xForce, zForce);

                if (target instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(
                            serverPlayer.getId(),
                            serverPlayer.getDeltaMovement())
                    );
                }
            }
        }
    }

    public boolean isPhase3() {
        return this.entityData.get(PHASE3);
    }

    public void setPhase3(boolean set) {
        this.entityData.set(PHASE3, set);
    }

    public boolean isPhase2() {
        return this.entityData.get(PHASE2);
    }

    public void setPhase2(boolean set) {
        this.entityData.set(PHASE2, set);
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("isPhase3"))
            setPhase3(tag.getBoolean("isPhase3"));
        if (tag.contains("Bleeding"))
            shouldBleed = tag.getBoolean("Bleeding");
        if (tag.contains("casting"))
            this.setCastingAttack(tag.getBoolean("casting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isPhase3", isPhase3());
        tag.putBoolean("Bleeding", shouldBleed);
        tag.putBoolean("casting", this.isCastingAttack());
    }

    @Override
    public CompoundTag savePlayerVariantData() {
        CompoundTag tag = super.savePlayerVariantData();
        tag.putBoolean("isPhase2", isPhase2());
        tag.putBoolean("isPhase3", isPhase3());
        return tag;
    }

    @Override
    public void readPlayerVariantData(CompoundTag tag) {
        super.readPlayerVariantData(tag);
        if (tag.contains("isPhase2"))
            setPhase2(tag.getBoolean("isPhase2"));
        if (tag.contains("isPhase3"))
            setPhase3(tag.getBoolean("isPhase3"));
    }

    @Override
    public boolean shouldShowGlow() {
        return isPhase2() || isPhase3();
    }

    public boolean isBleeding() {
        return shouldBleed;
    }

    @Override
    protected void onEffectAdded(@NotNull MobEffectInstance mobEffectInstance, @Nullable Entity entity) {
        super.onEffectAdded(mobEffectInstance, entity);
        if (this.getUnderlyingPlayer() == null && mobEffectInstance.getEffect() == MobEffects.HEAL && this.isBleeding()) {
            this.shouldBleed = false;
        }
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        if (damageSource.getDirectEntity() != null) {
            this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1, 1);
            for (int theta = 0; theta < 360; theta += 25) { // Ângulo horizontal
                double angleTheta = Math.toRadians(theta);
                for (int phi = 0; phi <= 180; phi += 25) { // Ângulo vertical
                    double anglePhi = Math.toRadians(phi);
                    double x = this.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
                    double y = this.getY() + Math.cos(anglePhi) * 4.0;
                    double z = this.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
                    Vec3 pos = new Vec3(x, y, z);
                    ParticlesUtil.sendParticlesWithMotion(
                            this,
                            ParticleTypes.ELECTRIC_SPARK,
                            new Vec3(0, 0, 0),
                            this.position().subtract(pos),
                            5, 0.025f
                    );
                }
            }
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1, 1);
            for (BlockPos pos : FoxyasUtils.betweenClosedStreamSphere(blockPosition(), 16, 16, 1).toList()) {
                BlockState state = level.getBlockState(pos);

                if (state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)) {
                    level.removeBlock(pos, false);
                    level.levelEvent(1009, pos, 0); // Partículas e som de "extinguir fogo"
                }
            }

            if (damageSource.getEntity() instanceof LivingEntity living) {
                FoxyasUtils.repairAllItems(living, 1000);
            }
        }

        super.die(damageSource);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getUnderlyingPlayer() == null) {
            if (firstTick) {
                applyDefaultBasicPlayerInfo();
            }

            if (shouldBleed && (this.computeHealthRatio() / 0.4f) > 0.25f && this.tickCount % 4 == 0) {
                this.setHealth(this.getHealth() - 0.25f);
            }

            Random randomSource = new Random();
            if (randomSource.nextFloat() < 1 - Math.min(0.95, computeHealthRatio())) {
                if (this.isPhase2()) {
                    if (this.shouldBleed) {
                        ParticlesUtil.sendParticles(this.level(), ParticleTypes.ELECTRIC_SPARK, this.getEyePosition().subtract(0, randomSource.nextFloat(this.getEyeHeight()), 0), 0.3f, 0.25f, 0.3f, 15, 0.01f);
                        ParticlesUtil.sendParticles(this.level(), ChangedAddonParticleTypes.thunderSpark(1), this.getEyePosition().subtract(0, randomSource.nextFloat(this.getEyeHeight()), 0), 0.3f, 0.25f, 0.3f, 15, 0.05f);
                    } else {
                        if (randomSource.nextFloat() > 0.95) {
                            ParticlesUtil.sendParticles(this.level(), ParticleTypes.ELECTRIC_SPARK, this.getEyePosition().subtract(0, randomSource.nextFloat(this.getEyeHeight()), 0), 0.3f, 0.25f, 0.3f, 10, 0.01f);
                        }
                        ParticlesUtil.sendParticles(this.level(), ChangedAddonParticleTypes.thunderSpark(1), this.getEyePosition().subtract(0, randomSource.nextFloat(this.getEyeHeight()), 0), 0.25f, 0.25f, 0.25f, 10, 1);
                    }
                } else {
                    ParticlesUtil.sendParticles(this.level(), ChangedAddonParticleTypes.thunderSpark(1), this.getEyePosition().subtract(0, randomSource.nextFloat(this.getEyeHeight()), 0), 0.25f, 0.25f, 0.25f, 5, 1);
                }
            }


            if (this.isPhase2()) {
                if (this.computeHealthRatio() <= 0.4f) {
                    removeStatModifiers();
                    applyStatModifierAllOutPhase();
                    this.shouldBleed = true;
                    setPhase3(true);
                } else {
                    applyStatModifier(this, 1.5);
                }
            } else {
                removeStatModifiers();
            }
            setSpeed(this);
            float speed = (float) this.getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 0.35f;
            this.crawlingSystem(speed);
        }
    }

    @Override
    public void updateStepSizeBasedInSwimState(boolean updateSwimmingMovement) {
        this.setMaxUpStep(updateSwimmingMovement ? 1f : 0.7f);
    }

    public void removeStatModifiers() {
        removeModifierUUID(this, Attributes.ATTACK_DAMAGE, "a06083b0-291d-4a72-85de-73bd93ffb736");
        removeModifierUUID(this, Attributes.ARMOR, "a06083b0-291d-4a72-85de-73bd93ffb737");
        removeModifierUUID(this, Attributes.ARMOR_TOUGHNESS, "a06083b0-291d-4a72-85de-73bd93ffb738");
        removeModifierUUID(this, Attributes.KNOCKBACK_RESISTANCE, "a06083b0-291d-4a72-85de-73bd93ffb739");
        //removeModifierUUID(this, Attributes.MOVEMENT_SPEED, "a06083b0-291d-4a72-85de-73bd93ffb710");
    }

    public void removeStatModifiers(LivingEntity entity) {
        removeModifierUUID(entity, Attributes.ATTACK_DAMAGE, "AttackMultiplier");
        removeModifierUUID(entity, Attributes.ARMOR, "ArmorMultiplier");
        removeModifierUUID(entity, Attributes.ARMOR_TOUGHNESS, "ArmorToughnessMultiplier");
        removeModifierUUID(entity, Attributes.KNOCKBACK_RESISTANCE, "KnockbackResistanceMultiplier");
        //removeModifierUUID(entity, Attributes.MOVEMENT_SPEED, "SpeedMultiplier");
    }

    private void removeModifier(LivingEntity entity, Attribute attribute, String modifierName) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            for (AttributeModifier modifier : instance.getModifiers()) {
                if (modifier.getName().equals(modifierName)) {
                    instance.removeModifier(modifier);
                    break; // Remove apenas um, caso haja múltiplos com o mesmo nome
                }
            }
        }
    }

    private void removeModifierUUID(LivingEntity entity, Attribute attribute, String uuid) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            for (AttributeModifier modifier : instance.getModifiers()) {
                if (modifier.getId().equals(UUID.fromString(uuid))) {
                    instance.removeModifier(modifier);
                    break; // Remove apenas um, caso haja múltiplos com o mesmo nome
                }
            }
        }
    }

    public void applyStatModifier(LivingEntity entity, double multiplier) {
        applyModifierIfAbsent(entity, Attributes.ATTACK_DAMAGE, "a06083b0-291d-4a72-85de-73bd93ffb736", "AttackMultiplier", multiplier - 1);
        applyModifierIfAbsent(entity, Attributes.ARMOR, "a06083b0-291d-4a72-85de-73bd93ffb737", "ArmorMultiplier", multiplier - 1);
        applyModifierIfAbsent(entity, Attributes.ARMOR_TOUGHNESS, "a06083b0-291d-4a72-85de-73bd93ffb738", "ArmorToughnessMultiplier", multiplier - 1);
        applyModifierIfAbsent(entity, Attributes.KNOCKBACK_RESISTANCE, "a06083b0-291d-4a72-85de-73bd93ffb739", "KnockbackResistanceMultiplier", multiplier - 1);
        //applyModifierIfAbsent(entity, Attributes.MOVEMENT_SPEED, "a06083b0-291d-4a72-85de-73bd93ffb710", "SpeedMultiplier", (multiplier - 1) * 0.5);
    }

    public void applyStatModifierAllOutPhase() {
        applyModifierIfAbsent(this, Attributes.ATTACK_DAMAGE, "a06083b0-291d-4a72-85de-73bd93ffb736", "AttackMultiplier", 0.25f);
        applyModifierIfAbsent(this, Attributes.ARMOR, "a06083b0-291d-4a72-85de-73bd93ffb737", "ArmorMultiplier", 1.25f);
        applyModifierIfAbsent(this, Attributes.ARMOR_TOUGHNESS, "a06083b0-291d-4a72-85de-73bd93ffb738", "ArmorToughnessMultiplier", 1.25f);
        applyModifierIfAbsent(this, Attributes.KNOCKBACK_RESISTANCE, "a06083b0-291d-4a72-85de-73bd93ffb739", "KnockbackResistanceMultiplier", 0.5f);
        //applyModifierIfAbsent(entity, Attributes.MOVEMENT_SPEED, "a06083b0-291d-4a72-85de-73bd93ffb710", "SpeedMultiplier", (multiplier - 1) * 0.5);
    }

    private void applyModifierIfAbsent(LivingEntity entity, Attribute attribute, String uuid, String name, double value) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) return;

        UUID modifierUUID = UUID.fromString(uuid);
        if (attributeInstance.getModifier(modifierUUID) == null) { // Verifica se o modificador já existe
            attributeInstance.addTransientModifier(new AttributeModifier(modifierUUID, name, value, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    public void SpawnThunderBolt(BlockPos pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning != null) {
            lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
            lightning.setCause(null);
            lightning.setDamage(6f);
            this.level.addFreshEntity(lightning);
            ParticlesUtil.sendParticles(this.level(), ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
        }
    }

    public void SpawnThunderBolt(Vec3 pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning != null) {
            lightning.moveTo(pos.x(), pos.y(), pos.z());
            lightning.setCause(null);
            this.level.addFreshEntity(lightning);
            ParticlesUtil.sendParticles(this.level(), ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
        }
    }

    public void setSpeed(Experiment009BossEntity entity) {
        AttributeModifier speedModifier = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.getPose() == Pose.SWIMMING) {
            if (!entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(speedModifier)) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(speedModifier);
            }
        } else {
            if (entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(speedModifier)) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(speedModifier);
            }
        }
    }

    @Override
    public void WhenPattedReaction(Player player, InteractionHand hand) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return;
        if (player instanceof ServerPlayer serverPlayer) {
            ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.Trigger(serverPlayer, this, "pats_on_the_beast");
        }


        List<Component> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp9.pat.type_1"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp9.pat.type_2"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp9.pat.type_3"));

        ParticlesUtil.sendParticles(player.level(),
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double) this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f, 1, 0f
        );

        Component translatableComponent = translatableComponentList.get(this.getRandom().nextInt(translatableComponentList.size()));
        MutableComponent entityChat = Component.translatable("chat.type.text", this.getDisplayName(), translatableComponent);

        player.displayClientMessage(entityChat, false);
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

    @Override
    public boolean canCauseGrabDamage() {
        return true;
    }

    @Override
    public EntityType<?> getReferencedEntityType() {
        return this.getType();
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

    private enum GearTier {
        LOW,
        MID,
        HIGH
    }

    public static float getMetalPercentage(LivingEntity target) {
        float metalScore = 0f;
        int totalSlots = 0;

        Iterable<ItemStack> items = Iterables.concat(target.getHandSlots(), target.getArmorSlots());

        for (ItemStack stack : items) {
            totalSlots++;
            if (stack.is(ChangedAddonTags.Items.METAL)) {
                metalScore += 1.0f;
            } else if (stack.is(ChangedAddonTags.Items.PARTIAL_METAL)) {
                metalScore += 0.5f;
            }
        }

        if (totalSlots == 0) return 0;
        return metalScore / totalSlots;
    }

    public static boolean shouldAlwaysDamageEntity(LivingEntity target) {
        return !(target instanceof Player player);
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {

        @SubscribeEvent
        public static void onBossHurtEntity(LivingHurtEvent event) {
            LivingEntity target = event.getEntity();
            DamageSource damageSource = event.getSource();
            Entity source = damageSource.getEntity();

            if (source instanceof Experiment009BossEntity boss) {
                if (boss.isPhase3()) {
                    boss.heal(0.5f);
                }

                float metalPercentage = getMetalPercentage(target);
                if (metalPercentage == 0) return;

                if (metalPercentage > 0.1f) {
                    float extraDamage = 5.0f * metalPercentage;
                    event.setAmount(event.getAmount() + extraDamage);

                    if (!target.level.isClientSide) {
                        ChangedAnimationEvents.broadcastEntityAnimation(target, ChangedAnimationEvents.SHOCK_STUN.get(), StunAnimationParameters.INSTANCE);
                        target.level.playSound(null, target.getX(), target.getY(), target.getZ(),
                                ChangedSounds.TSC_WEAPON_SHOCK.get(), SoundSource.HOSTILE, 1.0f, 1.0f);
                    }

                    target.hurtTime = 10;
                    target.hurtDuration = 10;
                }
            }
        }

        @SubscribeEvent
        public static void onBossHurtPlayer(LivingHurtEvent event) {
            if (!(event.getSource().getEntity() instanceof Experiment009BossEntity source)) return;
            if (!(event.getEntity() instanceof Player target)) return;

            GearTier tier = getGearTier(target);

            switch (tier) {
                case LOW -> event.setAmount(event.getAmount() * 0.75F);
                case MID -> event.setAmount(event.getAmount());
                case HIGH -> event.setAmount(event.getAmount() * 1.25F);
            }
        }

        @SubscribeEvent
        public static void onPlayerHurtBoss(LivingHurtEvent event) {
            if (!(event.getSource().getEntity() instanceof Player source)) return;
            if (!(event.getEntity() instanceof Experiment009BossEntity target)) return;

            GearTier tier = getGearTier(source);

            switch (tier) {
                case LOW -> event.setAmount(event.getAmount() * 2.5F);
                case MID, HIGH -> event.setAmount(event.getAmount());
            }
        }
    }
}
