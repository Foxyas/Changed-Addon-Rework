
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.registers.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;

import java.util.Objects;

public class SnowLeopardPartialEntity extends AbstractSnowLeopard implements PowderSnowWalkable,ComplexRenderer {
	public SnowLeopardPartialEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonEntities.SNOW_LEOPARD_PARTIAL.get(), world);
	}

	public SnowLeopardPartialEntity(EntityType<SnowLeopardPartialEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		this.setAttributes(getAttributes());
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((2));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0F);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.10f);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(4);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
		builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder.add(Attributes.MAX_HEALTH, 24);
		builder.add(Attributes.ARMOR, 4);
		builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder.add(Attributes.FOLLOW_RANGE, 40);
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

	@Override
	public Gender getGender() {
		return Gender.MALE;
	}
}
