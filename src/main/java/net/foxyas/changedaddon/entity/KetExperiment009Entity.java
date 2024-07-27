
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.procedures.Exp009IAProcedure;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.beast.AquaticEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class KetExperiment009Entity extends LatexEntity implements AquaticEntity {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_6);

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
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.115);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue((1.1));
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(15);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(20);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
		attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.85);
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

	public boolean isPhase2(){
		return this.getHealth() <= 0.75 * this.getMaxHealth() ? true : false;
	}

	public void SwimVoid(KetExperiment009Entity entity){
		double motionZ = 0;
		double deltaZ = 0;
		double distance = 0;
		double deltaX = 0;
		double motionY = 0;
		double deltaY = 0;
		double motionX = 0;
		double maxSpeed = 0;
		double speed = 0;
		if (entity.isInWater()) {
			if (!(entity.getTarget() == (null))) {
				deltaX = entity.getTarget().getX() - entity.getX();
				deltaY = entity.getTarget().getY() - entity.getY();
				deltaZ = entity.getTarget().getZ() - entity.getZ();
				distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
			}
			if (distance > 0) {
				speed = 0.07;
				motionX = deltaX / distance * speed;
				motionY = deltaY / distance * speed;
				motionZ = deltaZ / distance * speed;
				maxSpeed = 0.2;
				{
					Entity _ent = entity;
					if (!_ent.level.isClientSide() && _ent.getServer() != null)
						_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
								("execute as " + entity.getStringUUID() + " at @s run tp @s ~ ~ ~ facing entity " + entity.getTarget().getStringUUID()));
				}
				entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
				if (entity.isEyeInFluid(FluidTags.WATER)){
					entity.setPose(Pose.SWIMMING);
			 } else if (entity.getPose() == Pose.SWIMMING && !entity.isEyeInFluid(FluidTags.WATER)){
					entity.setPose(Pose.STANDING);
				}
			}
		} else if (entity.getPose() == Pose.SWIMMING && !entity.isInWater()){
			entity.setPose(Pose.STANDING);
		}
	}

	@Override
	public void baseTick() {
		super.baseTick();
		SwimVoid(this);
		Exp009IAProcedure.execute(this.level, this.getX(), this.getY(), this.getZ(), this);
	}
}
