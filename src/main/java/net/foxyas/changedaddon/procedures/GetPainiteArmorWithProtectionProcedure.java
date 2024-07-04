package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import net.foxyas.changedaddon.init.ChangedAddonModItems;

import java.util.Iterator;

public class GetPainiteArmorWithProtectionProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack i1 = ItemStack.EMPTY;
		ItemStack i2 = ItemStack.EMPTY;
		ItemStack i3 = ItemStack.EMPTY;
		ItemStack i4 = ItemStack.EMPTY;
		i1 = new ItemStack(ChangedAddonModItems.PAINITE_ARMOR_HELMET.get());
		i2 = new ItemStack(ChangedAddonModItems.PAINITE_ARMOR_CHESTPLATE.get());
		i3 = new ItemStack(ChangedAddonModItems.PAINITE_ARMOR_LEGGINGS.get());
		i4 = new ItemStack(ChangedAddonModItems.PAINITE_ARMOR_BOOTS.get());
		(i1).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
		(i2).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
		(i3).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
		(i4).enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
		if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getItem() == i4.getItem()
				&& (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getItem() == i3.getItem()
				&& (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getItem() == i2.getItem()
				&& (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == i1.getItem()
				&& EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)) >= 4
				&& EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)) >= 4
				&& EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) >= 4
				&& EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) >= 4) {
			if (entity instanceof ServerPlayer _player) {
				Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:deleted_mod_element"));
				AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
				if (!_ap.isDone()) {
					Iterator _iterator = _ap.getRemainingCriteria().iterator();
					while (_iterator.hasNext())
						_player.getAdvancements().award(_adv, (String) _iterator.next());
				}
			}
		}
	}
}
