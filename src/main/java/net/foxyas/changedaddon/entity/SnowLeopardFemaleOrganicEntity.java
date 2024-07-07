
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.HairStyle;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class SnowLeopardFemaleOrganicEntity extends AbstractCanTameLatexEntity {
	public SnowLeopardFemaleOrganicEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.SNOW_LEOPARD_FEMALE_ORGANIC.get(), world);
	}

	public SnowLeopardFemaleOrganicEntity(EntityType<SnowLeopardFemaleOrganicEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		setNoAi(false);
		setPersistenceRequired();
	}

	public InteractionResult SnowLeopard(Player player, InteractionHand hand,Player Host) {
		ItemStack itemstack = player.getItemInHand(hand);
		if(Host != null){
			return super.mobInteract(player, hand);
		}
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
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		return SnowLeopard(player,hand,this.getUnderlyingPlayer());
	}

	@Override
	public TransfurMode getTransfurMode() {
		if (level.random.nextInt() > 5) {return TransfurMode.ABSORPTION;}
		return TransfurMode.REPLICATION;
	}

	@Override
	public Gender getGender() {
		return Gender.FEMALE;
	}

	@Override
	public HairStyle getDefaultHairStyle() {
		HairStyle Hair = HairStyle.LONG_KEPT.get();
		if(level.random.nextInt(10) > 5){ Hair = HairStyle.LONG_MESSY.get();
		} else {
			Hair = HairStyle.LONG_KEPT.get();
		}
		return Hair;
	}

	@Override
	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.FEMALE.getStyles();
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