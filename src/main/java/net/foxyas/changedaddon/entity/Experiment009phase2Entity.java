
package net.foxyas.changedaddon.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;

import net.foxyas.changedaddon.procedures.IfplayerishighofentityProcedure;
import net.foxyas.changedaddon.procedures.IfplayerareinwaterProcedure;
import net.foxyas.changedaddon.procedures.IfnotInWaterProcedure;
import net.foxyas.changedaddon.procedures.IfInWaterProcedure;
import net.foxyas.changedaddon.procedures.Experiment009phase2OnInitialEntitySpawnProcedure;
import net.foxyas.changedaddon.procedures.Experiment009phase2OnEntityTickUpdateProcedure;
import net.foxyas.changedaddon.procedures.Experiment009phase2EntityDiesProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;

import javax.annotation.Nullable;

public class Experiment009phase2Entity extends Monster {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);

	public Experiment009phase2Entity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.EXPERIMENT_009_PHASE_2.get(), world);
	}

	public Experiment009phase2Entity(EntityType<Experiment009phase2Entity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 2800;
		setNoAi(false);
		setPersistenceRequired();
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.getNavigation().getNodeEvaluator().setCanOpenDoors(true);
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false, false));
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.7, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, (float) 0.5) {
			@Override
			public boolean canUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canUse() && IfplayerishighofentityProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canContinueToUse() && IfplayerishighofentityProcedure.execute(entity);
			}
		});
		this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1, 40) {
			@Override
			public boolean canUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canUse() && IfInWaterProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canContinueToUse() && IfInWaterProcedure.execute(entity);
			}
		});
		this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1) {
			@Override
			public boolean canUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canUse() && IfnotInWaterProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canContinueToUse() && IfnotInWaterProcedure.execute(entity);
			}
		});
		this.goalSelector.addGoal(7, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(8, new OpenDoorGoal(this, false));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(10, new FloatGoal(this) {
			@Override
			public boolean canUse() {
				double x = Experiment009phase2Entity.this.getX();
				double y = Experiment009phase2Entity.this.getY();
				double z = Experiment009phase2Entity.this.getZ();
				Entity entity = Experiment009phase2Entity.this;
				Level world = Experiment009phase2Entity.this.level;
				return super.canUse() && IfplayerareinwaterProcedure.execute(entity);
			}
		});
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
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.getDirectEntity() instanceof AbstractArrow)
			return false;
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
		if (source.isExplosion())
			return false;
		if (source.getMsgId().equals("trident"))
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);
		Experiment009phase2EntityDiesProcedure.execute(source.getEntity());
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
		Experiment009phase2OnInitialEntitySpawnProcedure.execute(world, this);
		return retval;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		Experiment009phase2OnEntityTickUpdateProcedure.execute(this.level, this.getX(), this.getY(), this.getZ(), this);
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
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 250);
		builder = builder.add(Attributes.ARMOR, 20);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 15);
		builder = builder.add(Attributes.FOLLOW_RANGE, 64);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1.5);
		return builder;
	}
}
