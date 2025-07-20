
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.HairStyle;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.beast.AbstractAquaticEntity;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LatexSquidTigerSharkEntity extends AbstractAquaticEntity {
	public LatexSquidTigerSharkEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonEntities.LATEX_SQUID_TIGER_SHARK.get(), world);
	}

	public LatexSquidTigerSharkEntity(EntityType<LatexSquidTigerSharkEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		setNoAi(false);
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void setAttributes(AttributeMap attributes) {
		super.setAttributes(attributes);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(0.975);
		attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.32);
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue(32.0);
	}

	@Override
	public HairStyle getDefaultHairStyle() {
		return HairStyle.SHORT_MESSY.get();
	}

	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.MALE.getStyles();
	}

	@Override
	public Color3 getHairColor(int layer) {
		return Color3.WHITE;
	}

	public Color3 getTransfurColor(TransfurCause cause) {
		return Color3.getColor("#969696");
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 3);
		builder.add((Attribute) ForgeMod.SWIM_SPEED.get(),1.0);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.975);
		builder = builder.add(Attributes.MAX_HEALTH, 32);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 6);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}
