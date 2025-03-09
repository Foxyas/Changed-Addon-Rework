package net.foxyas.changedaddon.ability;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.ChangedAddonMod;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

public class ClawsAbility extends SimpleAbility {
	public boolean isActive = false;

	public ClawsAbility() {
		super();
	}

	@Override
	public void saveData(CompoundTag tag, IAbstractChangedEntity entity) {
		super.saveData(tag, entity);
		tag.putBoolean("isActive", isActive);
	}

	@Override
	public void readData(CompoundTag tag, IAbstractChangedEntity entity) {
		super.readData(tag, entity);
		if (tag.contains("isActive")) {
			this.isActive = tag.getBoolean("isActive");
		}
	}

	@Override
	public ResourceLocation getTexture(IAbstractChangedEntity entity) {
		return new ResourceLocation("changed_addon:textures/screens/normal_paw.png");
	}

	@Override
	public Component getAbilityName(IAbstractChangedEntity entity) {
		return new TranslatableComponent("changed_addon.ability.claws");
	}

	@Override
	public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
		Collection<Component> list = new ArrayList<>();
		list.add(new TranslatableComponent("changed_addon.ability.claws.desc"));
		return list;
	}

	@Override
	public UseType getUseType(IAbstractChangedEntity entity) {
		return UseType.INSTANT;
	}

	@Override
	public int getCoolDown(IAbstractChangedEntity entity) {
		return 2;
	}

	@Override
	public boolean canUse(IAbstractChangedEntity entity) {
		if (entity.getTransfurVariantInstance() == null) {
			return false;
		}
		return entity.getTransfurVariantInstance().getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAT_LIKE) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.LEOPARD_LIKE);
	}

	@Override
	public boolean canKeepUsing(IAbstractChangedEntity entity) {
		if (entity.getTransfurVariantInstance() == null) {
			return false;
		}
		return entity.getTransfurVariantInstance().getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAT_LIKE) || entity.getTransfurVariantInstance().getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.LEOPARD_LIKE);
	}

	@Override
	public void startUsing(IAbstractChangedEntity entity) {
		if (!entity.getLevel().isClientSide()) {
			TurnOnClaws();
			this.setDirty(entity);
		}
	}

	public void TurnOnClaws() {
		this.isActive = !this.isActive;
	}
}
