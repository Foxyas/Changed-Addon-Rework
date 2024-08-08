package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.variants.AddonLatexVariant;
import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.beast.AbstractLatexWolf;
import net.ltxprogrammer.changed.entity.beast.AquaticEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class CreatureDietsHandleProcedure {
	@SubscribeEvent
	public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
		LivingEntity livingEntity = event.getEntityLiving();
		ItemStack item = event.getItem();

		if (event == null) {
			return;
		}



		if (livingEntity != null) {
			if(livingEntity instanceof Player player){
				LatexVariantInstance<?> LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
				if(LatexInstace == null){
					return;
				}
				Level World = player.getLevel();
				if (!World.getGameRules().getBoolean(ChangedAddonModGameRules.CHANGED_ADDON_CREATURE_DIETS)){
					return;
				}
				LatexVariant<?> Variant = LatexInstace.getLatexEntity().getSelfVariant();
				LatexEntity latexEntity = LatexInstace.getLatexEntity();
				List<Item> CatDiet = List.of(Items.COD,Items.COOKED_COD,Items.SALMON,Items.COOKED_SALMON,Items.PUFFERFISH,Items.TROPICAL_FISH);
				List<Item> DragonDiet = List.of(Items.COD,Items.COOKED_COD,Items.SALMON,Items.COOKED_SALMON,Items.PUFFERFISH,Items.TROPICAL_FISH,Items.RABBIT,Items.COOKED_RABBIT,Items.BEEF,Items.COOKED_BEEF,Items.CHICKEN,Items.COOKED_CHICKEN,Items.PORKCHOP,Items.COOKED_PORKCHOP,Items.MUTTON,Items.COOKED_MUTTON);
				List<Item> WolfDiet = List.of(Items.RABBIT,Items.COOKED_RABBIT,Items.BEEF,Items.COOKED_BEEF,Items.CHICKEN,Items.COOKED_CHICKEN,Items.PORKCHOP,Items.COOKED_PORKCHOP,Items.MUTTON,Items.COOKED_MUTTON);
				List<Item> FoxDiet = List.of(Items.SWEET_BERRIES,Items.GLOW_BERRIES,Items.RABBIT,Items.COOKED_RABBIT,Items.BEEF,Items.COOKED_BEEF,Items.CHICKEN,Items.COOKED_CHICKEN,Items.PORKCHOP,Items.COOKED_PORKCHOP,Items.MUTTON,Items.COOKED_MUTTON);
				List<Item> AquaticDiet = List.of(Items.DRIED_KELP,Items.COD,Items.COOKED_COD,Items.SALMON,Items.COOKED_SALMON,Items.PUFFERFISH,Items.TROPICAL_FISH);
				List<Item> SpecialDiet = List.of(ChangedItems.ORANGE.get(),ChangedAddonModItems.FOXTA.get());

				if(item.isEdible()) {
					int foodleveladd = Objects.requireNonNull(item.getItem().getFoodProperties()).getNutrition() + 4;
					float saturationadd = item.getItem().getFoodProperties().getSaturationModifier() + 3;
					int foodlevelplayer = player.getFoodData().getFoodLevel();
					float saturationplayer = player.getFoodData().getSaturationLevel();
					boolean isWarnsOn = player.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables()).showwarns;

					if (Objects.requireNonNull(latexEntity.getType().getRegistryName()).toString().contains("cat") || Variant.is(ChangedTags.LatexVariants.CAT_LIKE) || Variant.is(ChangedTags.LatexVariants.LEOPARD_LIKE)){
						if(CatDiet.contains(item.getItem())){
							player.getFoodData().setFoodLevel(foodlevelplayer + 4);
							player.getFoodData().setSaturation(saturationplayer + 3);
							if(isWarnsOn){
								player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"),true);
							}
						}
					} else if (latexEntity.getType().getRegistryName().toString().contains("dog") || latexEntity.getType().getRegistryName().toString().contains("wolf") || latexEntity instanceof AbstractLatexWolf || Variant.is(ChangedTags.LatexVariants.WOLF_LIKE)) {
							if(WolfDiet.contains(item.getItem())){
							player.getFoodData().setFoodLevel(foodlevelplayer + 4);
							player.getFoodData().setSaturation(saturationplayer + 3);
								if(isWarnsOn){
									player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"),true);
								}
							}
					} else if (Variant == AddonLatexVariant.WOLFY || Variant.is(AddonLatexVariant.ADDON_PURO_KIND.male()) || Variant.is(AddonLatexVariant.ADDON_PURO_KIND.female())){
							if(SpecialDiet.contains(item.getItem())){
								player.getFoodData().setFoodLevel(foodlevelplayer + 4);
								player.getFoodData().setSaturation(saturationplayer + 3);
								if(isWarnsOn){
									player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"),true);
								}
							}
					} else if (latexEntity.getType().getRegistryName().toString().contains("fox") || Variant.is(AddonLatexVariant.EXP1.male()) || Variant.is(AddonLatexVariant.EXP1.female())) {
							if(FoxDiet.contains(item.getItem())){
								player.getFoodData().setFoodLevel(foodlevelplayer + 4);
								player.getFoodData().setSaturation(saturationplayer + 3);
								if(isWarnsOn){
									player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"),true);
								}
							}
						} else if (latexEntity instanceof AquaticEntity || Variant.is(ChangedTags.LatexVariants.SHARK_LIKE) || Variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(), new ResourceLocation("changed:aquatic_like")))){
							if(AquaticDiet.contains(item.getItem())){
								player.getFoodData().setFoodLevel(foodlevelplayer + 4);
								player.getFoodData().setSaturation(saturationplayer + 3);
								if(isWarnsOn){
									player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"),true);
								}
							}
						} else if (Objects.requireNonNull(latexEntity.getType().getRegistryName()).toString().contains("dragon") || Variant.is(TagKey.create(ChangedRegistry.LATEX_VARIANT.get().getRegistryKey(), new ResourceLocation("changed:dragon_like")))){
							if(DragonDiet.contains(item.getItem())){
							player.getFoodData().setFoodLevel(foodlevelplayer + 4);
							player.getFoodData().setSaturation(saturationplayer + 3);
								if(isWarnsOn){
								player.displayClientMessage(new TranslatableComponent("changedaddon.diets.good_food"),true);
								}
							}
						}
					}
				}
			}
		}
	}
