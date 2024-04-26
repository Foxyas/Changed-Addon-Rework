
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.HairStyle;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class PuroKindEntity extends AbstractDarkLatexWolf {
	public PuroKindEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.PURO_KIND.get(), world);
	}

	public PuroKindEntity(EntityType<PuroKindEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = AbstractDarkLatexWolf.XP_REWARD_MEDIUM;
		setNoAi(false);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
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
		return Gender.MALE;
	}


	@Override
	protected void registerGoals() {
		super.registerGoals();
		/*this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new FloatGoal(this));*/

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