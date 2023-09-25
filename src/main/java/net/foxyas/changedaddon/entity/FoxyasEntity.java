
package net.foxyas.changedaddon.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

import net.foxyas.changedaddon.procedures.IfnotInWaterProcedure;
import net.foxyas.changedaddon.procedures.IfInWaterProcedure;
import net.foxyas.changedaddon.procedures.FoxyasRightClickedOnEntityProcedure;
import net.foxyas.changedaddon.procedures.FoxyasOnEntityTickUpdateProcedure;
import net.foxyas.changedaddon.procedures.FoxyasEntityDiesProcedure;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;

public class FoxyasEntity extends Monster {
	public FoxyasEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.FOXYAS.get(), world);
	}

	public FoxyasEntity(EntityType<FoxyasEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 10;
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
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, ServerPlayer.class, (float) 6));
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, (float) 6));
		this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1) {
			@Override
			public boolean canUse() {
				double x = FoxyasEntity.this.getX();
				double y = FoxyasEntity.this.getY();
				double z = FoxyasEntity.this.getZ();
				Entity entity = FoxyasEntity.this;
				Level world = FoxyasEntity.this.level;
				return super.canUse() && IfnotInWaterProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = FoxyasEntity.this.getX();
				double y = FoxyasEntity.this.getY();
				double z = FoxyasEntity.this.getZ();
				Entity entity = FoxyasEntity.this;
				Level world = FoxyasEntity.this.level;
				return super.canContinueToUse() && IfnotInWaterProcedure.execute(entity);
			}
		});
		this.goalSelector.addGoal(7, new RandomSwimmingGoal(this, 1, 40) {
			@Override
			public boolean canUse() {
				double x = FoxyasEntity.this.getX();
				double y = FoxyasEntity.this.getY();
				double z = FoxyasEntity.this.getZ();
				Entity entity = FoxyasEntity.this;
				Level world = FoxyasEntity.this.level;
				return super.canUse() && IfInWaterProcedure.execute(entity);
			}

			@Override
			public boolean canContinueToUse() {
				double x = FoxyasEntity.this.getX();
				double y = FoxyasEntity.this.getY();
				double z = FoxyasEntity.this.getZ();
				Entity entity = FoxyasEntity.this;
				Level world = FoxyasEntity.this.level;
				return super.canContinueToUse() && IfInWaterProcedure.execute(entity);
			}
		});
		this.targetSelector.addGoal(8, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(10, new FloatGoal(this));
		this.goalSelector.addGoal(11, new OpenDoorGoal(this, true));
		this.goalSelector.addGoal(12, new OpenDoorGoal(this, false));
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
	public void die(DamageSource source) {
		super.die(source);
		FoxyasEntityDiesProcedure.execute(source.getEntity());
	}

	@Override
	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
		ItemStack itemstack = sourceentity.getItemInHand(hand);
		InteractionResult retval = InteractionResult.sidedSuccess(this.level.isClientSide());
		super.mobInteract(sourceentity, hand);
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		Entity entity = this;
		Level world = this.level;

		FoxyasRightClickedOnEntityProcedure.execute(world, x, y, z, entity, sourceentity);
		return retval;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		FoxyasOnEntityTickUpdateProcedure.execute(this.level, this);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}
