
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class ReynEntity extends ChangedEntity {
	public ReynEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.REYN.get(), world);
	}

	public ReynEntity(EntityType<ReynEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 5;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
	}

	protected void setAttributes(AttributeMap attributes) {
		attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()).setBaseValue((0));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.05f);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(0.95f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	@Override
	public Color3 getHairColor(int i) {
		return Color3.getColor("##4c4c4c");
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
		HairStyle Hair = BALD.get();
		if(level.random.nextInt(10) > 5){ Hair = HairStyle.SHORT_MESSY.get();
		} else {
			Hair = BALD.get();
		}
		return Hair;
	}

	@Override
	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.MALE.getStyles();
	}

	@Override
	public Color3 getDripColor() {
		Color3 color = Color3.getColor("#ffffff");
		if(level.random.nextInt(10) > 5){ color = Color3.getColor("#4c4c4c");
		} else {
			color = Color3.getColor("#5c5c5c");
		}
		return color;
	}
	
	public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#4c4c4c");
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
