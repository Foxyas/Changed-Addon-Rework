
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.procedures.Exp009IAProcedure;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.beast.AquaticEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class KetExperiment009Entity extends LatexEntity {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);
	private boolean Phase2;

	public KetExperiment009Entity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.KET_EXPERIMENT_009.get(), world);
	}

	public KetExperiment009Entity(EntityType<KetExperiment009Entity> type, Level world) {
		super(type, world);
		this.setAttributes(getAttributes());
		maxUpStep = 0.6f;
		xpReward = 3000;
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((425));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1));
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(15);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(20);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
		attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.85);
	}

	@Override
	public boolean startRiding(Entity EntityIn, boolean force) {
		if (EntityIn instanceof Boat){
			return false;
		}
		return super.startRiding(EntityIn, force);
	}

	@Override
	public double getMeleeAttackRangeSqr(LivingEntity target) {
		if (target.getEyeY() > this.getEyeY() + 1){
			return 6 * 6;
		}
		return super.getMeleeAttackRangeSqr(target);
	}



	@Override
	public Color3 getHairColor(int i) {
		return Color3.getColor("#F1F1F1");
	}

	@Override
	public int getTicksRequiredToFreeze() { return 1000; }
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


	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();

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
		return -0.35D;
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
		if (source.getMsgId().equals("trident"))
			return false;
		if (source == DamageSource.ANVIL)
			return false;
		if (source == DamageSource.DRAGON_BREATH)
			return false;
		if (source == DamageSource.WITHER)
			return false;
		if (source.getMsgId().equals("witherSkull"))
			return false;
		if (source.isProjectile())
			return false;
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
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 425);
		builder = builder.add(Attributes.ARMOR, 40);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 15);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.3);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
		return builder;
	}

	public void setPhase2(boolean set){
		this.Phase2 = set;
	}

	public boolean isPhase2(){
		return this.Phase2;
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Phase2"))
			Phase2 = tag.getBoolean("Phase2");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Phase2",Phase2);
	}

    @Override
    public void baseTick() {
        super.baseTick();
        updateSwimmingMovement();
        Exp009IAProcedure.execute(this.level, this.getX(), this.getY(), this.getZ(), this);
		SetSpeed(this);
        CrawSystem(this.getTarget());
    }

	public void SetSpeed(KetExperiment009Entity entity) {
		AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.325, AttributeModifier.Operation.MULTIPLY_BASE);
		if (entity.getPose() == Pose.SWIMMING) {
			if (!((entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttibuteChange)))) {
				entity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttibuteChange);
			}
		} else {
			entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttibuteChange);
		}
	}

	public void CrawSystem(LivingEntity target) {
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
        double targetEyeY = target.getEyeY();
        double entityEyeY = this.getEyeY();

        if (target.getPose() == Pose.SWIMMING && this.getPose() == Pose.SWIMMING) {
            double deltaX = target.getX() - this.getX();
            double deltaY = target.getY() - this.getY();
            double deltaZ = target.getZ() - this.getZ();
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            if (distance > 1.0) {
                double speed = 0.05;
                double motionX = deltaX / distance * speed;
                double motionY = deltaY / distance * speed;
                double motionZ = deltaZ / distance * speed;
                this.setDeltaMovement(this.getDeltaMovement().add(motionX, motionY, motionZ));
            }
        }
    }

    public void updateSwimmingMovement() {
        if (this.isInWater()) {
            if (this.getTarget() != null) {
                LivingEntity target = this.getTarget();
                double deltaX = target.getX() - this.getX();
                double deltaY = target.getY() - this.getY();
                double deltaZ = target.getZ() - this.getZ();
                double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                if (distance > 0) {
                    double speed = 0.07;
                    double motionX = deltaX / distance * speed;
                    double motionY = deltaY / distance * speed;
                    double motionZ = deltaZ / distance * speed;
                    this.setDeltaMovement(this.getDeltaMovement().add(motionX, motionY, motionZ));
                }
            }

            if (this.isEyeInFluid(FluidTags.WATER)) {
                this.setPose(Pose.SWIMMING);
                this.setSwimming(true);
            } else if (this.getPose() == Pose.SWIMMING && !this.isEyeInFluid(FluidTags.WATER)) {
                this.setPose(Pose.STANDING);
                this.setSwimming(false);
            }
        } else if (this.getPose() == Pose.SWIMMING && !this.isInWater() && this.level.getBlockState(new BlockPos(this.getX(), this.getEyeY(), this.getZ()).above()).isAir()) {
            this.setPose(Pose.STANDING);
        }
    }
}
