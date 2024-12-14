
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.damagesource.DamageSource;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.Packet;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;

public class LatexSnepEntity extends AbstractCanTameSnepChangedEntity {

	public boolean WantLoaf = false; //Lol the cat don't WANT LOAF!

	public LatexSnepEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.LATEX_SNEP.get(), world);
	}

	public LatexSnepEntity(EntityType<LatexSnepEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		setNoAi(false);
		this.setAttributes(this.getAttributes());
	}

	protected void setAttributes(AttributeMap attributes) {
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((10));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(16.0f);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15F);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	@Override
	public TransfurVariant<?> getSelfVariant() {
		return ChangedAddonTransfurVariants.LATEX_SNEP.get();
	}

	public boolean WantToLoaf() {
		return WantLoaf;
	}

	@Override
	protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return false;
    }

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("wantLoaf")){
			this.WantLoaf = tag.getBoolean("wantLoaf");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("wantLoaf",WantLoaf);
	}

	@Override
	public LatexType getLatexType() {
		return LatexType.NEUTRAL;
	}

	@Override
	public TransfurMode getTransfurMode() {
		return TransfurMode.NONE;
	}

	public Gender gender = Gender.MALE;

	@Override
	public Gender getGender() {
		return gender;
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
	public double getMyRidingOffset() {
		return -0.35D;
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return SoundEvents.OCELOT_HURT; //ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return SoundEvents.OCELOT_DEATH; //ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source == DamageSource.FALL)
			return false;
		if (source == DamageSource.FREEZE)
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public boolean isItemAllowedInSlot(ItemStack stack, EquipmentSlot slot) {
		if (slot.getType() == EquipmentSlot.Type.ARMOR) {
			return false;
		} /*else if (slot == EquipmentSlot.OFFHAND) {
			return false;
		}*/
		return super.isItemAllowedInSlot(stack, slot);
	}

	@Override
	public boolean canBeLeashed(Player p_21418_) {
		return !this.isLeashed();
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 3);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 10);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}


}
