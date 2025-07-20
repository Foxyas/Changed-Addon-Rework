package net.foxyas.changedaddon.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

import net.foxyas.changedaddon.init.ChangedAddonTabs;

public class GoldenOrange extends Item {
	public GoldenOrange() {
		super(new Item.Properties()
			.tab(ChangedAddonTabs.TAB_CHANGED_ADDON)
			.rarity(Rarity.RARE)
			.food(new FoodProperties.Builder()
					.nutrition(4).saturationMod(1.2F)
					.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0), 1.0F)
					.alwaysEat()
					.build()
			)
		);
	}
}
