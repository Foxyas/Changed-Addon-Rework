
package net.foxyas.changedaddon.entity;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.LatexSnowLeopardMale;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;

public class SnepsiLeopardEntity extends LatexSnowLeopardMale {
	public SnepsiLeopardEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.SNEPSI_LEOPARD.get(), world);
	}

	public SnepsiLeopardEntity(EntityType<SnepsiLeopardEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = XP_REWARD_MEDIUM;
		this.setAttributes(getAttributes());
		setPersistenceRequired();
		setNoAi(false);
	}

	@Override
	public Color3 getDripColor() {
		Color3 color = Color3.getColor("#ffffff");
		if(level.random.nextInt(10) > 5){ color = Color3.getColor("#95D161");
		} else {
			color = Color3.getColor("#B5DF90");
		}
		return color;
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

	public static void init() {
	}

	@Override
	public Color3 getTransfurColor(TransfurCause cause) {
		return Color3.GREEN;
	}

	@Override
	public Color3 getHairColor(int layer) {
		return Color3.GREEN;
	}

	@Override
	public TransfurMode getTransfurMode() {
		return TransfurMode.NONE;
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 22);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}
