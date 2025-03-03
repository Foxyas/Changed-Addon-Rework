package net.foxyas.changedaddon.ability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.Registry;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexCentaur;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;

import java.util.Optional;

public class CarryAbility extends SimpleAbility {
	public CarryAbility() {
		super();
	}

	@Override
	public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
		return new TranslatableComponent("changed_addon.ability.carry");
	}

	@Override
	public ResourceLocation getTexture(IAbstractChangedEntity entity) {
		return new ResourceLocation("changed_addon:textures/screens/carry_ability.png");
	}

	@Override
	public UseType getUseType(IAbstractChangedEntity entity) {
		return UseType.INSTANT;
	}

	@Override
	public int getCoolDown(IAbstractChangedEntity entity) {
		return 5;
	}

	@Override
	public boolean canUse(IAbstractChangedEntity entity) {
		if (Spectator(entity.getEntity()))
			return false;
		Optional<TransfurVariant<?>> variant = Optional.ofNullable(entity.getTransfurVariantInstance()).map(TransfurVariantInstance::getParent);
		return variant.filter(
				v -> v.is(ChangedAddonTransfurVariants.Gendered.EXP2.getFemaleVariant()) || v.is(ChangedAddonTransfurVariants.Gendered.EXP2.getMaleVariant()) || v.is(ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getFemaleVariant())
						|| v.is(ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getMaleVariant()) || v.is(ChangedAddonTransfurVariants.Gendered.ADDON_PURO_KIND.getFemaleVariant())
						|| v.is(ChangedAddonTransfurVariants.Gendered.ADDON_PURO_KIND.getMaleVariant()) || v.is(TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), new ResourceLocation("changed_addon:able_to_carry"))))
				.isPresent();
	}

	@Override
	public void startUsing(IAbstractChangedEntity entity) {
		super.startUsing(entity);
		Run(entity.getEntity());
	}

	@Override
	public void onRemove(IAbstractChangedEntity entity) {
		super.onRemove(entity);
		SafeRemove(entity.getEntity());
	}

	public static boolean Spectator(Entity entity) {
		return entity instanceof Player player && player.isSpectator();
	}

	public Entity CarryTarget(Player player) {
		return PlayerUtilProcedure.getEntityPlayerLookingAt(player, 4);
	}

	private void Run(Entity mainEntity) {
		if (!(mainEntity instanceof Player player))
			return;
		Entity carriedEntity = player.getFirstPassenger();
		if (carriedEntity != null) {
			boolean isShifting = player.isShiftKeyDown();
			carriedEntity.stopRiding();
			syncMount(player);
			if (carriedEntity instanceof Player) {
				if (isShifting) {
					syncMountNoMotion((Player) carriedEntity);
				} else {
					syncMount((Player) carriedEntity, player);
				}
			}
			if (!isShifting) {
				carriedEntity.setDeltaMovement(player.getLookAngle().scale(1.05));
				carriedEntity.hasImpulse = true;
				if (!player.level.isClientSide()) {
					((ServerLevel) player.level).getChunkSource().broadcast(carriedEntity, new ClientboundSetEntityMotionPacket(carriedEntity));
				}
			}
			return;
		}
		if (player.isSpectator())
			return;
		Entity carryTarget = this.CarryTarget(player);
		if (carryTarget == null)
			return;
		if (carryTarget instanceof WhiteLatexCentaur || (carryTarget instanceof Player p && ProcessTransfur.getPlayerTransfurVariant(p) != null && ProcessTransfur.getPlayerTransfurVariant(p).is(ChangedTransfurVariants.WHITE_LATEX_CENTAUR.get()))) {
			player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry", carryTarget.getDisplayName()), true);
			return;
		}
		if (carryTarget instanceof Player carryPlayer && carryPlayer.isCreative() && !player.isCreative())
			return;
		if (carryTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) || carryTarget.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:can_carry")))) {
			if (carryTarget.startRiding(player, true)) {
				syncMount(player);
				if (carryTarget instanceof Player)
					syncMount((Player) carryTarget);
			}
		}
	}

	public static void SoundPlay(Player player) {
		player.level.playSound(null, player.blockPosition(), ChangedSounds.BOW2, SoundSource.PLAYERS, 2.5f, 1.0f);
	}

	private static void syncMount(Player player) {
		if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new ClientboundSetPassengersPacket(player));
			serverPlayer.setDeltaMovement(serverPlayer.getLookAngle().scale(1.05));
			SoundPlay(serverPlayer);
		}
	}

	private static void syncMount(Player player, Player player2) {
		if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new ClientboundSetPassengersPacket(player));
			serverPlayer.setDeltaMovement(player2.getLookAngle().scale(1.05));
			SoundPlay(serverPlayer);
		}
	}


	private static void syncMountNoMotion(Player player) {
		if (!player.level.isClientSide && player instanceof ServerPlayer serverPlayer) {
			serverPlayer.connection.send(new ClientboundSetPassengersPacket(player));
			SoundPlay(serverPlayer);
		}
	}

	public static void SafeRemove(Entity mainEntity) {
		if (mainEntity.getFirstPassenger() != null) {
			mainEntity.getFirstPassenger().stopRiding();
		}
	}
}
