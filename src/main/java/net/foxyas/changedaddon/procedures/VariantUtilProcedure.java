package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.util.Cacheable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.UUID;

public class VariantUtilProcedure {

	private static final Cacheable<AttributeMap> BASE_ATTRIBUTES = Cacheable.of(() -> {
		return new AttributeMap(Player.createAttributes().build());
	});

	public static float GetLandSpeed(String stringvariant,Player player) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				ChangedEntity InstanceEntity = variant.getEntityType().create(player.level);
                assert InstanceEntity != null;
                InstanceEntity.setUnderlyingPlayer(player);
				TransfurVariantInstance<?> Instance = new TransfurVariantInstance<>(variant,player);
				return variant == null ? 0f : (float) (InstanceEntity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) - BASE_ATTRIBUTES.get().getBaseValue(Attributes.MOVEMENT_SPEED));
			} else {
				return 0f;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetLandSpeed: " + e.getMessage());
			return 0f;
		}
	}

	public static float GetSwimSpeed(String stringvariant,Player player) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				ChangedEntity InstanceEntity = variant.getEntityType().create(player.level);
				assert InstanceEntity != null;
				InstanceEntity.setUnderlyingPlayer(player);
				TransfurVariantInstance<?> Instance = new TransfurVariantInstance<>(variant,player);
				return variant == null ? 0f : (float) (InstanceEntity.getAttributeBaseValue(ForgeMod.SWIM_SPEED.get()) - BASE_ATTRIBUTES.get().getBaseValue(ForgeMod.SWIM_SPEED.get()));
			} else {
				return 0f;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetSwimSpeed: " + e.getMessage());
			return 0f;
		}
	}

	public static float GetExtraHp(String stringvariant,Player player) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				ChangedEntity Instance = variant.getEntityType().create(player.level);
				assert Instance != null;
				Instance.setUnderlyingPlayer(player);
				return variant == null ? 0 : (Instance.getMaxHealth() - Player.MAX_HEALTH);
			} else {
				return 0;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetExtraHp: " + e.getMessage());
			return 0;
		}
	}

	public static int GetLegs(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				return variant == null ? 0 : variant.legCount;
			} else {
				return 0;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetLegs: " + e.getMessage());
			return 0;
		}
	}

	public static boolean CanGlideandFly(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				return variant != null && variant.canGlide;
			} else {
				return false;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing CanGlideandFly: " + e.getMessage());
			return false;
		}
	}

	public static boolean CanClimb(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				return variant != null && variant.canClimb;
			} else {
				return false;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing CanClimb: " + e.getMessage());
			return false;
		}
	}

	public static float GetJumpStrength(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				return variant == null ? 0f : variant.jumpStrength;
			} else {
				return 0f;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetJumpStrength: " + e.getMessage());
			return 0f;
		}
	}

	public static EntityType<?> GetEntity(String stringvariant, Level world) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getRegistryName).anyMatch(form::equals)) {
				TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(form);
				return variant == null ? ChangedTransfurVariants.WHITE_LATEX_WOLF_MALE.get().getEntityType() : variant.getEntityType();
			} else {
				return ChangedTransfurVariants.WHITE_LATEX_WOLF_MALE.get().getEntityType();
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetEntity: " + e.getMessage());
			return ChangedTransfurVariants.WHITE_LATEX_WOLF_MALE.get().getEntityType();
		}
	}

	public static void SummonVariantEntity(EntityType<ChangedEntity> entityType, Level world){
		Objects.requireNonNull(entityType.create(world)).setUUID(UUID.randomUUID());
	}
}
