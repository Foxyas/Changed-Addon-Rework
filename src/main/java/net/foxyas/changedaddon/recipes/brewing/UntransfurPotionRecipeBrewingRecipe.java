
package net.foxyas.changedaddon.recipes.brewing;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ChangedAddonModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import net.foxyas.changedaddon.init.ChangedAddonModPotions;
import net.foxyas.changedaddon.init.ChangedAddonModItems;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UntransfurPotionRecipeBrewingRecipe implements IBrewingRecipe {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		if(!ChangedAddonMod.config.common.PotionSynthesis.get()) {
			event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new UntransfurPotionRecipeBrewingRecipe()));
		}
	}

	@Override
	public boolean isInput(ItemStack input) {
		Item inputItem = input.getItem();
		return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) && PotionUtils.getPotion(input) == Potions.AWKWARD;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return Ingredient.of(new ItemStack(ChangedAddonModItems.LITIX_CAMONIA.get())).test(ingredient);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			return PotionUtils.setPotion(new ItemStack(input.getItem()), ChangedAddonModPotions.LITIXCAMONIAEFFECT.get());
		}
		return ItemStack.EMPTY;
	}
}
