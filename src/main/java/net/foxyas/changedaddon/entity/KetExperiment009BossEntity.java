
package net.foxyas.changedaddon.entity;

import com.mojang.math.Vector3f;
import net.foxyas.changedaddon.effect.particles.ChangedAddonParticles;
import net.foxyas.changedaddon.entity.CustomHandle.BossMusicTheme;
import net.foxyas.changedaddon.entity.CustomHandle.BossWithMusic;
import net.foxyas.changedaddon.entity.CustomHandle.CustomPatReaction;
import net.foxyas.changedaddon.entity.CustomHandle.Exp9AttacksHandle;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class KetExperiment009BossEntity extends ChangedEntity implements BossWithMusic, CustomPatReaction {

    public final EntityDamageSource ThunderDmg = new EntityDamageSource(DamageSource.LIGHTNING_BOLT.getMsgId(), this);
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);
    private boolean Phase2;
    private int AttackCoolDown;
    private boolean shouldBleed;

    public void setAttackCoolDown(int attackCoolDown) {
        AttackCoolDown = attackCoolDown;
    }

    public int getAttackCoolDown() {
        return AttackCoolDown;
    }

    public KetExperiment009BossEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonModEntities.KET_EXPERIMENT_009_BOSS.get(), world);
    }

    public KetExperiment009BossEntity(EntityType<KetExperiment009BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        maxUpStep = 0.6f;
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((425));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1));
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(12.5);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(12.5);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(6);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.85);
    }

    @Override
    public boolean ShouldPlayMusic() {
        return this.isAlive();
    }

    @Override
    public @NotNull BossMusicTheme BossMusicTheme() {
        return BossMusicTheme.EXP9;
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
        return Color3.getColor("#F1F1F1");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 1000;
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")));
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

    @Override
    public Color3 getDripColor() {
        return Color3.getColor("#E2E2E2");
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.WHITE;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(9, new Exp9AttacksHandle.ThunderPathway(this));
        this.goalSelector.addGoal(8, new Exp9AttacksHandle.ThunderShock(this));
        this.goalSelector.addGoal(5, new Exp9AttacksHandle.ThunderSpeed(this));
        this.goalSelector.addGoal(5, new Exp9AttacksHandle.ThunderWave(this));
        this.goalSelector.addGoal(10, new Exp9AttacksHandle.ThunderStorm(this));
        this.goalSelector.addGoal(5, new Exp9AttacksHandle.TeleportAttack(this));
        this.goalSelector.addGoal(4, new Exp9AttacksHandle.TeleportComboGoal(this));
        this.goalSelector.addGoal(4, new Exp9AttacksHandle.TeleportAirComboGoal(this));
        this.goalSelector.addGoal(6, new Exp9AttacksHandle.BurstAttack(this));
        this.goalSelector.addGoal(8, new Exp9AttacksHandle.ThunderBoltImpactAttack(this));
        this.goalSelector.addGoal(7, new Exp9AttacksHandle.ThunderBoltAreaAttack(this));
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

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
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
        if (source == DamageSource.IN_WALL){
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
        return super.hurt(source, amount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        setEyeStyle(EyeStyle.TALL);
        CompoundTag dataIndex0 = new CompoundTag();
        this.saveWithoutId(dataIndex0);
        dataIndex0.getCompound("LocalVariantInfo").putFloat("scale", 1);
        this.load(dataIndex0);
        return retval;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
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

    public void setPhase2(boolean set) {
        this.Phase2 = set;
    }

    public boolean isPhase2() {
        return this.Phase2;
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Phase2"))
            Phase2 = tag.getBoolean("Phase2");
        if (tag.contains("AttackCoolDown"))
            AttackCoolDown = tag.getInt("AttackCoolDown");
        if (tag.contains("Bleeding"))
            shouldBleed = tag.getBoolean("Bleeding");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Phase2", Phase2);
        tag.putInt("AttackCoolDown", AttackCoolDown);
        tag.putBoolean("Bleeding", shouldBleed);
    }

    @Override
    public void visualTick(Level level) {
        super.visualTick(level);
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
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(
                            entityDamageSource.getDirectEntity().getLevel(),
                            ParticleTypes.ELECTRIC_SPARK,
                            pos,
                            0.1f, 0.1f, 0.1f,
                            5, 0.1f
                    );
                }
            }
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1, 1);
        }
        super.die(damageSource);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getUnderlyingPlayer() == null) {
            if (shouldBleed && this.computeHealthRatio() > 0.10 && this.tickCount % 4 == 0){
                this.setHealth(this.getHealth() - 0.05f);
            }
            if (this.AttackCoolDown < 100) {
                this.AttackCoolDown += this.isPhase2() ? 2 : 1;
            }
            if (this.isPhase2()) {
                if (this.shouldBleed) {
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, this.position().add(0, 1f, 0), 0.3f, 0.5f, 0.3f, 15, 0.2f);
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(this.getLevel(), ChangedAddonParticles.thunderSpark(1), this.getEyePosition(), 0.35f, 0.50f, 0.35f, 5, 1);
                }
                if (this.computeHealthRatio() <= 0.35f) {
                    removeStatModifiers();
                    applyStatModifierAllOutPhase();
                    this.shouldBleed = true;
                }
                else {
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(this.getLevel(), ChangedAddonParticles.thunderSpark(1), this.getEyePosition(), 0.35f, 0.50f, 0.35f, 5, 1);
                    applyStatModifier(this, 1.5);
                }
                /*
                Color[] colors = new Color[2];
                colors[0] = new Color(70, 199, 255);
                colors[1] = new Color(13, 160, 208);
                ParticleOptions dustColor = getParticleOptions(colors[0], colors[1]);
                PlayerUtilProcedure.ParticlesUtil.sendParticles(this.getLevel(), dustColor, this.position().add(0, 0.5, 0), 0.35f, 0.70f, 0.35f, 5, 0);
                */

            } else {
                removeStatModifiers();
            }
            updateSwimmingMovement();
            setSpeed(this);
            crawlingSystem(this.getTarget());
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



    @NotNull
    private static ParticleOptions getParticleOptions(Color StartColor, Color EndColor) {
        Vector3f startColor = new Vector3f((float) StartColor.getRed() / 255, (float) StartColor.getGreen() / 255, (float) StartColor.getBlue() / 255);
        Vector3f endColor = new Vector3f((float) EndColor.getRed() / 255, (float) EndColor.getGreen() / 255, (float) EndColor.getBlue() / 255);
        return new DustColorTransitionOptions(startColor, endColor, 1);
    }

    public void SpawnThunderBolt(BlockPos pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning != null) {
            lightning.moveTo(pos.getX(), pos.getY(), pos.getZ());
            lightning.setCause(null);
            lightning.setDamage(6f);
            this.level.addFreshEntity(lightning);
            PlayerUtilProcedure.ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
        }
    }

    public void SpawnThunderBolt(Vec3 pos) {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        if (lightning != null) {
            lightning.moveTo(pos.x(), pos.y(), pos.z());
            lightning.setCause(null);
            this.level.addFreshEntity(lightning);
            PlayerUtilProcedure.ParticlesUtil.sendParticles(this.getLevel(), ParticleTypes.ELECTRIC_SPARK, pos, 0.3f, 0.5f, 0.3f, 5, 1f);
        }
    }


    public void setSpeed(KetExperiment009BossEntity entity) {
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

    public void crawlingSystem(LivingEntity target) {
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
        if (target.getPose() == Pose.SWIMMING && this.getPose() != Pose.SWIMMING) {
            if (target.getY() < this.getEyeY() && !target.level.getBlockState(new BlockPos(target.getX(), target.getEyeY(), target.getZ()).above()).isAir()) {
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
            Vec3 direction = target.position().subtract(this.position()).normalize();
            this.setDeltaMovement(this.getDeltaMovement().add(direction.scale(0.05)));
        }
    }

    public void updateSwimmingMovement() {
        if (this.isInWater()) {
            if (this.getTarget() != null) {
                Vec3 direction = this.getTarget().position().subtract(this.position()).normalize();
                this.setDeltaMovement(this.getDeltaMovement().add(direction.scale(0.07)));
            }
            if (this.isEyeInFluid(FluidTags.WATER)) {
                this.setPose(Pose.SWIMMING);
                this.setSwimming(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSwimming(false);
            }
        } else if (this.getPose() == Pose.SWIMMING && !this.isInWater() && this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
            this.setPose(Pose.STANDING);
        }
    }

    @Override
    public void WhenPattedReaction(Player player) {
        List<TranslatableComponent> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp9.pat.type_1"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp9.pat.type_2"));
        translatableComponentList.add(new TranslatableComponent("changed_addon.entity_dialogues.exp9.pat.type_3"));

        PlayerUtilProcedure.ParticlesUtil.sendParticles(player.getLevel(),
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double)this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f, 1, 0f
        );
        player.displayClientMessage(translatableComponentList.get(this.getRandom().nextInt(translatableComponentList.size())), false);
    }
}
