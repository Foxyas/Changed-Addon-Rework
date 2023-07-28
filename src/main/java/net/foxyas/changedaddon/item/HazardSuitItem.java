
package net.foxyas.changedaddon.item;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.init.ChangedAddonModTabs;

public abstract class HazardSuitItem extends ArmorItem {
	public HazardSuitItem(EquipmentSlot slot, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForSlot(EquipmentSlot slot) {
				return new int[]{13, 15, 16, 11}[slot.getIndex()] * 25;
			}

			@Override
			public int getDefenseForSlot(EquipmentSlot slot) {
				return new int[]{1, 4, 5, 1}[slot.getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 9;
			}

			@Override
			public SoundEvent getEquipSound() {
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:armor_equip"));
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.LEATHER), new ItemStack(Items.IRON_INGOT));
			}

			@Override
			public String getName() {
				return "hazard_suit";
			}

			@Override
			public float getToughness() {
				return 0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, slot, properties);
	}

	public static class Helmet extends HazardSuitItem {
		public Helmet() {
			super(EquipmentSlot.HEAD, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/hazard_armor__layer_1.png";
		}
	}

	public static class Chestplate extends HazardSuitItem {
		public Chestplate() {
			super(EquipmentSlot.CHEST, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/hazard_armor__layer_1.png";
		}
	}

	public static class Leggings extends HazardSuitItem {
		public Leggings() {
			super(EquipmentSlot.LEGS, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/hazard_armor__layer_2.png";
		}
	}

	public static class Boots extends HazardSuitItem {
		public Boots() {
			super(EquipmentSlot.FEET, new Item.Properties().tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "changed_addon:textures/models/armor/hazard_armor__layer_1.png";
		}
	}
}
