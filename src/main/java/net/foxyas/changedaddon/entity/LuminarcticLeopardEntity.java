
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.AbstractLuminarCrystal;
import net.foxyas.changedaddon.entity.CustomHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.CameraUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import net.minecraft.network.chat.TextComponent;

public class LuminarcticLeopardEntity extends AbstractLuminarcticLeopard {

	public LuminarcticLeopardEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.LUMINARCTIC_LEOPARD.get(), world);
	}

	public LuminarcticLeopardEntity(EntityType<LuminarcticLeopardEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = XP_REWARD_HUGE;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		//Attack stats
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6));
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(6.0f);
		attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(
				AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK) + 1.5f
		);

		//Armor Stats
		attributes.getInstance(Attributes.ARMOR).setBaseValue(8);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(2);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);

		//Health Stats
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((60));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(128.0F);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.25f);
		attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.05f);
	}

	@Override
	public TransfurMode getTransfurMode() {
		if (this.getTarget() != null && (this.getTarget().getHealth() / this.getTarget().getMaxHealth() * 100) <= 15) {
			return TransfurMode.ABSORPTION;
		}
		return TransfurMode.NONE;
	}

	@Override
	protected boolean targetSelectorTest(LivingEntity livingEntity) {
		return false;
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
		return super.getMyRidingOffset();
	}

	public double getTorsoYOffset(ChangedEntity self) {
		float ageAdjusted = (float)self.tickCount * 0.33333334F * 0.25F * 0.15F;
		float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
		float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
		float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
		return (double)(Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
	}

	public double getTorsoYOffsetForFallFly(ChangedEntity self){
		float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
		return 0.375 + bpiSize;
	}

	@Override
	public double getPassengersRidingOffset() {
		if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
			return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
		}
		return getTorsoYOffsetForFallFly(this);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 6);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 1.25f);
		builder = builder.add(Attributes.MAX_HEALTH, 60F);
		builder = builder.add(Attributes.ARMOR, 8F);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}

	@Override
	public Gender getGender() {
		return Gender.MALE;
	}

}
