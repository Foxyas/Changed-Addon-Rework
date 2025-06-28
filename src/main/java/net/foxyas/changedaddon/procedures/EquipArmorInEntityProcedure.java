package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.AbstractCanTameSnepChangedEntity;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.foxyas.changedaddon.item.HazmatSuitItem;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TamableLatexEntity;
import net.ltxprogrammer.changed.entity.beast.DarkLatexWolfPup;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EquipArmorInEntityProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getPlayer().isShiftKeyDown() && event.getTarget() instanceof ChangedEntity changedEntity) {
			ItemStack itemStack = event.getItemStack();
			if (changedEntity instanceof DarkLatexWolfPup
					|| changedEntity instanceof LatexSnepEntity
					|| (changedEntity instanceof AbstractCanTameSnepChangedEntity snepChanged && !snepChanged.isBiped())) {
				return;
			}

			if (changedEntity instanceof TamableLatexEntity tamableLatexEntity){
				if (tamableLatexEntity.getOwner() != event.getPlayer()){
					return;
				}
				if (!event.getPlayer().isShiftKeyDown()){
					return;
				}
				event.setCancellationResult(InteractionResult.FAIL);
				// Check if the item is an armor piece
				if (itemStack.getItem() instanceof ArmorItem armorItem) {
					if (armorItem instanceof HazmatSuitItem) {
						return;
					}
					// Proceed only on the server side
					if (!event.getWorld().isClientSide()){
						equipOrSwapArmor(changedEntity, itemStack, armorItem, event.getPlayer(), event.getHand());
					}
				} else if (itemStack.isEmpty() && event.getHand() == InteractionHand.MAIN_HAND) {
					// Proceed only on the server side
					if (!event.getWorld().isClientSide()){
						checkClickLocationAndUnequipArmor(changedEntity, event.getPlayer(), event.getHand(), getLocationFromHit(event.getPlayer()));
					}
				}
				event.setCanceled(true);
			}
		}
	}

	public static void equipOrSwapArmor(ChangedEntity entity, ItemStack itemStack, ArmorItem armorItem, Player player, InteractionHand hand) {
		// Check if the entity is tamable and has an owner
		if (entity instanceof TamableLatexEntity ChangedEntity && ChangedEntity.getOwner() != null && ChangedEntity.getOwner() == player) {
			EquipmentSlot armorSlot = armorItem.getSlot(); // Determine the correct armor slot
			ItemStack currentArmor = entity.getItemBySlot(armorSlot); // Get the current armor in the slot
			// If the slot is already occupied and the item in the player's hand is a different armor piece
			if (!currentArmor.isEmpty() && !currentArmor.getItem().equals(armorItem)) {
				swapArmor(entity, player, hand, armorSlot, currentArmor, itemStack, armorItem);
			} else if (entity.getItemBySlot(armorSlot).isEmpty() && itemStack.canEquip(armorSlot, entity)) {
				// If the slot is empty or the armor is different from the current one, equip the new armor
				equipArmor(entity, player, hand, itemStack, armorSlot, armorItem);
			}
		}
	}

	private static void swapArmor(ChangedEntity entity, Player player, InteractionHand hand, EquipmentSlot armorSlot, ItemStack currentArmor, ItemStack newArmor, ArmorItem armorItemStack) {
		// Remove the current armor from the slot
		entity.setItemSlot(armorSlot, ItemStack.EMPTY); // Remove the old armor from the slot
		// Equip the new armor
		equipArmor(entity, player, hand, newArmor, armorSlot, armorItemStack);
		// Add the removed armor to the player's inventory or to the hand if empty
		if (player.getItemInHand(hand).isEmpty()) {
			player.setItemInHand(hand, currentArmor);
		} else {
			// Add the removed armor to the player's inventory (or to the ground if necessary)
			if (!player.getInventory().add(currentArmor)) {
				player.drop(currentArmor, false); // Drop on the ground if inventory is full
			}
		}
	}

	private static void equipArmor(ChangedEntity entity, Player player, InteractionHand hand, ItemStack itemStack, EquipmentSlot armorSlot, ArmorItem armorItemStack) {
		// Remove one unit of the new armor and equip it in the entity's slot
		entity.setItemSlot(armorSlot, itemStack.split(1)); // Equip one unit of the new armor
		if (armorItemStack.getEquipSound() != null) {
			entity.level().playSound(null, entity.blockPosition(), armorItemStack.getEquipSound(), SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound
		} else {
			entity.level().playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound
		}
		// Remove the item from the player's hand
		if (!player.level().isClientSide()) {
			// Check if the hand being used is the main or offhand
			if (hand == InteractionHand.MAIN_HAND) {
				player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY); // Clear the main hand item
			} else {
				player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY); // Clear the offhand item
			}
		}
	}

	private static void checkClickLocationAndUnequipArmor(ChangedEntity entity, Player player, InteractionHand hand, Vec3 clickPos) {
		Vec3 eyePosition = player.getEyePosition(1.0F);
		Vec3 playerLook = player.getViewVector(1.0F);
		Vec3 entityPosition = new Vec3(entity.getX(), entity.getY(), entity.getZ());
		// Compute the vector from player eye position to entity position
		Vec3 toEntity = entityPosition.subtract(eyePosition);
		// Normalize the player look vector and the toEntity vector to compute the projection
		Vec3 normalizedPlayerLook = playerLook.normalize();
		double dotProduct = toEntity.normalize().dot(normalizedPlayerLook);
		// Calculate the height relative based on the dot product and distance
		double distance = toEntity.length(); // Get the distance to the entity
		//toEntity.normalize().y - playerLook.normalize().y;
		double yRelative = clickPos.y;
		// Display the yRelative value for debugging
		// player.displayClientMessage(Component.literal("Y Relative: " + yRelative), false);
		// Determine which armor slot to unequip based on the yRelative
		EquipmentSlot clickedSlot = determineArmorSlot(yRelative, entity, player);
		if (clickedSlot != null) {
			ItemStack currentArmor = entity.getItemBySlot(clickedSlot);
			if (!currentArmor.isEmpty()) {
				// Remove the armor from the slot
				entity.setItemSlot(clickedSlot, ItemStack.EMPTY);
				entity.level().playSound(null, entity.blockPosition(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F); // Armor equip sound
				// Add the removed armor to the player's inventory or to the hand if empty
				if (player.getItemInHand(hand).isEmpty()) {
					player.setItemInHand(hand, currentArmor);
				} else {
					// Add the removed armor to the player's inventory (or to the ground if necessary)
					if (!player.getInventory().add(currentArmor)) {
						player.drop(currentArmor, false); // Drop on the ground if inventory is full
					}
				}
			}
		}
	}

	private static Vec3 getLocationFromHit(Player player){
		// Perform Ray Tracing to detect the entity hit
		HitResult hitResult = player.pick(5.0D, 1.0F, false); // Check for hits within 5 blocks
		Vec3 hitPosition = hitResult.getLocation();
		if (hitResult instanceof EntityHitResult entityHitResult) {
			// Entity hit, detect location
			hitPosition = entityHitResult.getLocation();
		}
		return hitPosition;
	}

	private static EquipmentSlot determineArmorSlot(double yClick, Entity entity,Player player) {
		double entityHeight = entity.getY();
		Vec3 EntityPos = new Vec3(0,entityHeight,0);
		Vec3 ClickPos = new Vec3(0,yClick,0);
		double distance = EntityPos.distanceTo(ClickPos);
		double relativeHeight = yClick / entityHeight;
		//@DEBUG player.displayClientMessage(Component.literal("Relative is " + relativeHeight),false);
		//@DEBUG player.displayClientMessage(Component.literal("Distance is " + distance),false);
		//@DEBUG player.displayClientMessage(Component.literal("Distance2 is " + (yClick - entity.getY())),false);

		if (distance >= 2.1) {
			return EquipmentSlot.HEAD;
		} else if (distance > 1) {
			return EquipmentSlot.CHEST;
		} else if (distance > 0) {
			return EquipmentSlot.LEGS;
		} else {
			return EquipmentSlot.FEET;
		}
	}
}
