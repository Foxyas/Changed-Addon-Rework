
package net.foxyas.changedaddon.entity;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.player.AbstractClientPlayer;

import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.PowderSnowWalkable;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.LatexEntity;

import net.foxyas.changedaddon.ChangedAddonEntitys;

public class SnowLeopardPartialEntity extends LatexEntity implements PowderSnowWalkable {
	public SnowLeopardPartialEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonEntitys.SNOW_LEOPARD_PARTIAL.get(), world);
	}

	public SnowLeopardPartialEntity(EntityType<SnowLeopardPartialEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		this.setAttributes(getAttributes());
		setNoAi(false);
		setPersistenceRequired();
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 25);
		return builder;
	}

	@Override
	public TransfurMode getTransfurMode() {
		return TransfurMode.NONE;
	}

	public boolean causeFallDamage(float p_148859_, float p_148860_, DamageSource p_148861_) {
		return false;
	}

	public int getTicksRequiredToFreeze() {
		return 420;
	}

	public Color3 getDripColor() {
		return this.level.random.nextInt(10) > 3 ? Color3.GRAY : Color3.WHITE;
	}

	public Color3 getHairColor(int layer) {
		return Color3.WHITE;
	}

	public LatexType getLatexType() {
		return LatexType.NEUTRAL;
	}

	@Override
	protected boolean targetSelectorTest(LivingEntity livingEntity) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getSkinTextureLocation() {
		if (getUnderlyingPlayer() instanceof AbstractClientPlayer clientPlayer)
			return clientPlayer.getSkinTextureLocation();
		return DefaultPlayerSkin.getDefaultSkin(this.getUUID());
	}

	@OnlyIn(Dist.CLIENT)
	public String getModelName() {
		if (getUnderlyingPlayer() instanceof AbstractClientPlayer clientPlayer)
			return clientPlayer.getModelName();
		return DefaultPlayerSkin.getSkinModelName(this.getUUID());
	}
}
