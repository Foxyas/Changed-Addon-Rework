
package net.foxyas.changedaddon.entity;

import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;

import javax.annotation.Nullable;
import java.util.List;

public class PuroKindFemaleEntity extends AbstractDarkLatexWolf {
	public PuroKindFemaleEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.PURO_KIND_FEMALE.get(), world);
	}

	public PuroKindFemaleEntity(EntityType<PuroKindFemaleEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = AbstractDarkLatexWolf.XP_REWARD_MEDIUM;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(25.0F);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.08f);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	@Override
	public TransfurMode getTransfurMode() {
		return TransfurMode.REPLICATION;
	}

	@Override
	public HairStyle getDefaultHairStyle() {
		return HairStyle.BALD.get();
	}

	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.getAll();
	}

	/*@Override
	public LatexType getLatexType() {
		return LatexType.DARK_LATEX;
	}*/

	@Override
	public Color3 getHairColor(int layer) {
		return Color3.DARK;
	}

	@Override
	public Gender getGender() {
		return Gender.FEMALE;
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
	public Color3 getDripColor() {
		Color3 color = Color3.getColor("#000000");
		if(level.random.nextInt(10) > 5){ color = Color3.getColor("#393939");
		} else {
			color = Color3.getColor("#303030");
		}
		return color;
	}


	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public double getMyRidingOffset() {
		return -0.35D;
	}

	public double getTorsoYOffset(LatexEntity self) {
		float ageAdjusted = (float)self.tickCount * 0.33333334F * 0.25F * 0.15F;
		float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
		float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
		float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
		return (double)(Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
	}

	@Override
	public double getPassengersRidingOffset() {
		return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
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
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}