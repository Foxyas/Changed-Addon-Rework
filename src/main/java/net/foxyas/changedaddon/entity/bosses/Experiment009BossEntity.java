package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.entity.api.CustomPatReaction;
import net.foxyas.changedaddon.entity.api.ICrawlAbleEntity;
import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.foxyas.changedaddon.entity.customHandle.Exp9AttacksHandle;
import net.foxyas.changedaddon.entity.goals.exp9.*;
import net.foxyas.changedaddon.entity.goals.generic.BreakBlocksAroundGoal;
import net.foxyas.changedaddon.entity.goals.generic.attacks.SimpleAntiFlyingAttack;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.util.ColorUtil;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedDamageSources;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.foxyas.changedaddon.event.TransfurEvents.getPlayerVars;
import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class Experiment009BossEntity extends ChangedEntity implements CustomPatReaction, PowderSnowWalkable, IHasBossMusic, ICrawlAbleEntity {

    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(Experiment009BossEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PHASE3 =
            SynchedEntityData.defineId(Experiment009BossEntity.class, EntityDataSerializers.BOOLEAN);
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private boolean shouldBleed;

    public Experiment009BossEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_009_BOSS.get(), world);
    }

    public Experiment009BossEntity(EntityType<Experiment009BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static void init() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
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

    public EntityDamageSource getThunderDmg() {
        return new EntityDamageSource(DamageSource.LIGHTNING_BOLT.getMsgId(), this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE2, false);
        this.entityData.define(PHASE3, false);
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((425));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(8);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(12.5);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(6);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.85);
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
        return Color3.getColor("#F1F1F1");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, ResourceLocation.parse("changed:humanoids")));
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
        return HairStyle.Collection.MALE.getStyles();
    }

    public Color3 getDripColor() {
        return Color3.getColor("#E2E2E2");
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.WHITE;
        Color3 secondColor = Color3.getColor("#E9E9E9");
        return ColorUtil.lerpTFColor(firstColor, secondColor, this.getUnderlyingPlayer());
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(20, new SimpleAntiFlyingAttack(this,
                UniformInt.of(60, 100),
                3,
                8,
                8f,
                10));
        this.goalSelector.addGoal(6, new Exp9AttacksHandle.BurstAttack(this));
        this.goalSelector.addGoal(10, new Exp9AttacksHandle.ThunderStorm(this, UniformInt.of(60, 100)));

        //New AI
        this.goalSelector.addGoal(5, new ThunderStrikeGoal(
                this,
                UniformInt.of(80, 120), //IntProvider -> cooldownProvider
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
                UniformInt.of(90, 150), //IntProvider -> cooldown,
                UniformInt.of(2, 4), //IntProvider -> lightningCount,
                UniformInt.of(60, 100), //IntProvider -> castDuration,
                UniformInt.of(80, 100), //IntProvider -> lightningDelay,
                ConstantFloat.of(10))); //FloatProvider -> damage

        this.goalSelector.addGoal(5, new StaticDischargeGoal(this,//PathfinderMob holder,
                UniformInt.of(75, 125), //IntProvider -> cooldown,
                4,
                UniformInt.of(30, 50), //IntProvider -> castDuration,
                8,
                UniformFloat.of(8, 12))); //FloatProvider -> damage

        this.goalSelector.addGoal(1, new InductionCoilGoal(this, //PathfinderMob -> holder
                UniformInt.of(100, 150), //IntProvider -> cooldown
                20,
                UniformInt.of(60, 80), //IntProvider -> duration
                UniformFloat.of(3, 5))); //FloatProvider -> damage

        this.goalSelector.addGoal(5, new LightningComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 40), //IntProvider -> castDuration,
                UniformFloat.of(6, 8))); //FloatProvider -> damage)

        this.goalSelector.addGoal(10, new BreakBlocksAroundGoal(this));
    }

    //private void addOldAI() {
    //    this.targetSelector.addGoal(9, new Exp9AttacksHandle.ThunderPathway(this));
    //    this.targetSelector.addGoal(8, new Exp9AttacksHandle.ThunderShock(this));
    //    this.targetSelector.addGoal(5, new Exp9AttacksHandle.ThunderSpeed(this));
    //    this.targetSelector.addGoal(5, new Exp9AttacksHandle.ThunderWave(this));
    //    this.targetSelector.addGoal(5, new Exp9AttacksHandle.TeleportAttack(this));
    //    this.targetSelector.addGoal(20, new Exp9AttacksHandle.RandomTeleportAttack(this));
    //    this.targetSelector.addGoal(4, new Exp9AttacksHandle.TeleportComboGoal(this));
    //    this.targetSelector.addGoal(4, new Exp9AttacksHandle.TeleportAirComboGoal(this));
    //    this.targetSelector.addGoal(8, new Exp9AttacksHandle.ThunderBoltImpactAttack(this));
    //    this.targetSelector.addGoal(7, new Exp9AttacksHandle.ThunderBoltAreaAttack(this));
    //}

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (this.getUnderlyingPlayer() != null) {
            Player playerInControl = this.getUnderlyingPlayer();
            TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(playerInControl);
            if (transfurVariantInstance != null) {
                if (playerInControl.getLevel().getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR)) {
                    if (!getPlayerVars(playerInControl).Exp009TransfurAllowed) {
                        ProcessTransfur.setPlayerTransfurVariant(playerInControl, ChangedAddonTransfurVariants.EXPERIMENT_009.get(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1, false);
                    }
                }
                DodgeAbilityInstance dodgeAbilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAddonAbilities.DODGE.get());
                if (dodgeAbilityInstance != null && dodgeAbilityInstance.getMaxDodgeAmount() < 10) {
                    dodgeAbilityInstance.setMaxDodgeAmount(10);
                    dodgeAbilityInstance.setDodgeAmount(10);
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

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {
        @SubscribeEvent
        public static void WhenAttack(LivingAttackEvent event) {
            LivingEntity target = event.getEntityLiving();
            Entity source = event.getSource().getEntity();
            if (source instanceof Experiment009BossEntity experiment009BossEntity) {
                if (experiment009BossEntity.isPhase3()) {
                    experiment009BossEntity.heal(0.5f);
                }
            }
        }
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
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source == DamageSource.FALL)
            return false;
        if (source == DamageSource.CACTUS)
            return false;
        if (source == DamageSource.DROWN)
            return false;
        if (source == DamageSource.LIGHTNING_BOLT)
            return false;
        if (source.getMsgId().equals("trident")) {
            if (this.getLevel().random.nextFloat() <= 0.25f) {
                if (source.getEntity() instanceof Player player) {
                    player.displayClientMessage(new TranslatableComponent("changed_addon.entity_dialogues.exp9.reaction.range_attacks"), true);
                }
            }
            return super.hurt(source, amount * 0.5f);
        }
        if (source == DamageSource.ANVIL)
            return false;
        if (source == DamageSource.DRAGON_BREATH)
            return false;
        if (source == DamageSource.WITHER)
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;
        if (source == DamageSource.IN_WALL) {
            Exp9AttacksHandle.TeleportAttack.Teleport(this, this.getTarget() == null
                    ? this.getLevel().getNearestPlayer(this.getX(), this.getY(), this.getZ(), 32d, true)
                    : this.getTarget());
            return false;
        }
        if (source.isProjectile()) {
            if (this.getLevel().random.nextFloat() <= 0.25f) {
                if (source.getEntity() instanceof Player player) {
                    player.displayClientMessage(new TranslatableComponent("changed_addon.entity_dialogues.exp9.reaction.range_attacks"), true);
                }
            }
            return super.hurt(source, amount * 0.5f);
        }

        if (source instanceof EntityDamageSource entityDamageSource) {
            if (entityDamageSource.isThorns()) {
                return super.hurt(source, 0);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean isDamageSourceBlocked(@NotNull DamageSource pDamageSource) {
        if (pDamageSource == ChangedDamageSources.ELECTROCUTION) {
            return true;
        }
        return super.isDamageSourceBlocked(pDamageSource);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        this.getBasicPlayerInfo().setSize(1f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
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

        float maxHealth = this.getMaxHealth();
        float currentHealth = this.getHealth();
        float healthRatio = currentHealth / maxHealth;

        // Se estiver com menos de 40% da vida, simula que 40% é o "cheio" da barra
        if (healthRatio <= 0.4f) {
            this.bossInfo.setProgress(healthRatio / 0.4f); // estica a barra
            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_10) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
            }
        } else {
            this.bossInfo.setProgress(healthRatio);
            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_6) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_6);
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
        if (tag.contains("Phase2"))
            setPhase2(tag.getBoolean("Phase2"));
        if (tag.contains("Phase3"))
            setPhase3(tag.getBoolean("Phase3"));
        if (tag.contains("Bleeding"))
            shouldBleed = tag.getBoolean("Bleeding");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Phase2", isPhase2());
        tag.putBoolean("Phase3", isPhase3());
        tag.putBoolean("Bleeding", shouldBleed);
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
        if (damageSource instanceof EntityDamageSource entityDamageSource && entityDamageSource.getDirectEntity() != null) {
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
        }

        super.die(damageSource);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getUnderlyingPlayer() == null) {
            if (firstTick) {
                this.getBasicPlayerInfo().setSize(1f);
                this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
            }

            if (shouldBleed && (this.computeHealthRatio() / 0.4f) > 0.25f && this.tickCount % 4 == 0) {
                this.setHealth(this.getHealth() - 0.25f);
            }

            if (this.getRandom().nextFloat() < 1 - Math.min(0.95, computeHealthRatio())) {
                if (this.isPhase2()) {
                    if (this.shouldBleed) {
                        ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, this.getEyePosition().subtract(0, this.getRandom().nextFloat(this.getEyeHeight()), 0), 0.3f, 0.25f, 0.3f, 15, 0.01f);
                        ParticlesUtil.sendParticles(this.getLevel(), ChangedAddonParticleTypes.thunderSpark(1), this.getEyePosition().subtract(0, this.getRandom().nextFloat(this.getEyeHeight()), 0), 0.3f, 0.25f, 0.3f, 15, 0.05f);
                    } else {
                        if (this.getRandom().nextFloat() > 0.95) {
                            ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, this.getEyePosition().subtract(0, this.getRandom().nextFloat(this.getEyeHeight()), 0), 0.3f, 0.25f, 0.3f, 10, 0.01f);
                        }
                        ParticlesUtil.sendParticles(this.getLevel(), ChangedAddonParticleTypes.thunderSpark(1), this.getEyePosition().subtract(0, this.getRandom().nextFloat(this.getEyeHeight()), 0), 0.25f, 0.25f, 0.25f, 10, 1);
                    }
                } else {
                    ParticlesUtil.sendParticles(this.getLevel(), ChangedAddonParticleTypes.thunderSpark(1), this.getEyePosition().subtract(0, this.getRandom().nextFloat(this.getEyeHeight()), 0), 0.25f, 0.25f, 0.25f, 5, 1);
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
            this.crawlingSystem(0.025f);
        }
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
            ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
        }
    }

    public void SpawnThunderBolt(Vec3 pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning != null) {
            lightning.moveTo(pos.x(), pos.y(), pos.z());
            lightning.setCause(null);
            this.level.addFreshEntity(lightning);
            ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
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
        if (!(player.getLevel() instanceof ServerLevel serverLevel)) return;
        if (player instanceof ServerPlayer serverPlayer) {
            ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.Trigger(serverPlayer, this, "pats_on_the_beast");
        }


        List<TranslatableComponent> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp9.pat.type_1"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp9.pat.type_2"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp9.pat.type_3"));

        ParticlesUtil.sendParticles(player.getLevel(),
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double) this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f, 1, 0f
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
