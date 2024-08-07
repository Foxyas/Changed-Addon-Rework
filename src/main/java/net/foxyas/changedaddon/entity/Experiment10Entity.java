
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.level.GameType;
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

public class Experiment10Entity extends LatexEntity implements GenderedEntity {
	private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);
	private float TpCooldown;
	private boolean Phase2;

	public Experiment10Entity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.EXPERIMENT_10.get(), world);
	}

	public Experiment10Entity(EntityType<Experiment10Entity> type, Level world) {
		super(type, world);
		this.setAttributes(getAttributes());
		maxUpStep = 0.6f;
		xpReward = 3000;
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((325));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.085);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(12);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(20);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
		attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.8);
	}
	@Override
	public Color3 getHairColor(int i) {
		return Color3.getColor("#1f1f1f");
	}

	@Override
	public int getTicksRequiredToFreeze() { return 1000; }

	protected boolean targetSelectorTest(LivingEntity livingEntity) {
		return livingEntity instanceof Player || livingEntity instanceof ServerPlayer || livingEntity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:humanoids")));
	}
	
    @Override
    public void checkDespawn() {
        if(true){
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


	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(10, new FloatGoal(this){
			@Override
			public boolean canUse() {
				return super.canUse() && false;
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

	public void setPhase2(boolean set){
		this.Phase2 = set;
	}

	public boolean isPhase2(){
		return this.Phase2;
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Tp_Cooldown"))
			TpCooldown = tag.getFloat("Tp_Cooldown");
		if (tag.contains("Phase2")){
			Phase2 = tag.getBoolean("Phase2");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("Tp_Cooldown",TpCooldown);
		tag.putBoolean("Phase2",Phase2);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		SwimVoid(this);
		SetDefense(this);
		SetAttack(this);
		TpEntity(this);
	}

	public void SwimVoid(Experiment10Entity entity){
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
	public void SetDefense(Experiment10Entity entity){
		AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "ArmorChange", 20, AttributeModifier.Operation.ADDITION);
		AttributeModifier AttibuteDefenseChange = new AttributeModifier(UUID.fromString("10-10-0-0-0"), "ArmorChange", 0.7, AttributeModifier.Operation.MULTIPLY_BASE);
		if (entity.isPhase2()){
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

	public void SetAttack(Experiment10Entity entity){
		AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Attack", 0.6667, AttributeModifier.Operation.MULTIPLY_BASE);
		if (entity.isPhase2()){
			if (!((entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(AttibuteChange)))) {
				entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(AttibuteChange);
			}
		} else {
			entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(AttibuteChange);
		}
	}
	
	public void TpEntity(Experiment10Entity entity){
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
			if(distance > 3) {
				if (entity.getLastHurtByMob() == Target) {
					entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
					this.level.playLocalSound(entity.getX(),entity.getY(),entity.getZ(), ChangedSounds.BOW2, SoundSource.HOSTILE,10,1,true);
					TpCooldown = 40;
				} else {
					if(Targets != null && !(Targets instanceof ServerPlayer))
					{
					entity.setTarget(Targets);
					} else if (Targets != null && Targets instanceof ServerPlayer serverPlayer) {
						if (serverPlayer.gameMode.getGameModeForPlayer() != GameType.CREATIVE && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR){
							entity.setTarget(Targets);
						}
					}// Check if the entity in not null and is instance of server player if is will check if the gametype and if is not Creative and Spectator return true
					entity.teleportTo(Target.getX(), Target.getY(), Target.getZ());
					this.level.playLocalSound(entity.getX(),entity.getY(),entity.getZ(), ChangedSounds.BOW2, SoundSource.HOSTILE,10,1,true);
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
}
