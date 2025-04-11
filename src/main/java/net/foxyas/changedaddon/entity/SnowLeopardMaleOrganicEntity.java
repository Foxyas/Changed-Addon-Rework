
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class SnowLeopardMaleOrganicEntity extends AbstractCanTameSnepChangedEntity {
	public SnowLeopardMaleOrganicEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.SNOW_LEOPARD_MALE_ORGANIC.get(), world);
	}

	public SnowLeopardMaleOrganicEntity(EntityType<SnowLeopardMaleOrganicEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((0));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0F);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17f);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}
	public InteractionResult SnowLeopard(Player player, InteractionHand hand,Player Host) {
		ItemStack itemstack = player.getItemInHand(hand);
		/*if(Host != null){
			return super.mobInteract(player, hand);
		}*/
		if (this.level.isClientSide) {
			boolean flag = this.isOwnedBy(player) || this.isTame() || this.isTameItem(itemstack) && !this.isTame();
			return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
		} else {
			if (!this.isTame() && this.isTameItem(itemstack)) {
				if (!player.getAbilities().instabuild) {
					itemstack.shrink(1);
				}
				boolean istransfur = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables()).transfur;

					if (!istransfur && this.random.nextInt(3) == 0) { // One in 3 chance
						this.tame(player);
						this.navigation.stop();
						this.setTarget(null);
						this.level.broadcastEntityEvent(this, (byte)7);
					} else if(istransfur && this.random.nextInt(6) == 0) {
						this.tame(player);
						this.navigation.stop();
						this.setTarget(null);
						this.level.broadcastEntityEvent(this, (byte)7);
					} else {
						this.level.broadcastEntityEvent(this, (byte)6);
					}

				return InteractionResult.SUCCESS;
			}

			return super.mobInteract(player, hand);
		}
	}

	@Override
	protected @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
		return SnowLeopard(player,hand,this.getUnderlyingPlayer());
	}

	@Override
	public TransfurMode getTransfurMode() {
		if (level.random.nextInt() > 5) {return TransfurMode.ABSORPTION;}
		return TransfurMode.REPLICATION;
	}

	@Override
	public Gender getGender() {
		return Gender.MALE;
	}

	public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.GRAY;
    }

	@Override
	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.MALE.getStyles();
	}

	@Override
	public HairStyle getDefaultHairStyle() {
		HairStyle Hair = BALD.get();
		if(level.random.nextInt(10) > 5){
			return HairStyle.SHORT_MESSY.get();
		}
		return BALD.get();
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected boolean targetSelectorTest(LivingEntity livingEntity) {
		return false;
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		//this.goalSelector.addGoal(5, new SleepingWithOwnerGoal.BipedSleepGoal(this));
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
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}


}