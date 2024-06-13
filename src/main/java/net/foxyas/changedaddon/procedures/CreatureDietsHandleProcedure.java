package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.entity.AbstractCanTameLatexEntity;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
				LatexVariantInstance LatexInstace = ProcessTransfur.getPlayerLatexVariant(player);
				if(LatexInstace == null){
					return;
				}
				LatexVariant Variant = LatexInstace.getLatexEntity().getSelfVariant();
				if(item.isEdible()) {
					int foodleveladd = item.getItem().getFoodProperties().getNutrition() + 4;
					float saturationadd = item.getItem().getFoodProperties().getSaturationModifier() + 3;
					int foodlevelplayer = player.getFoodData().getFoodLevel();
					float saturationplayer = player.getFoodData().getSaturationLevel();

					if(Variant.is(ChangedTags.LatexVariants.CAT_LIKE) || Variant.is(ChangedTags.LatexVariants.LEOPARD_LIKE)){
						if(item.is(Items.COD) || item.is(Items.COOKED_COD) || item.is(Items.SALMON) || item.is(Items.COOKED_SALMON) || item.is(Items.PUFFERFISH) || item.is(Items.TROPICAL_FISH)){
							player.getFoodData().setFoodLevel(foodlevelplayer + 4);
							player.getFoodData().setSaturation(saturationplayer + 3);
						}
					} else if (Variant.is(ChangedTags.LatexVariants.WOLF_LIKE)) {
						if(item.is(Items.RABBIT) || item.is(Items.COOKED_RABBIT) || item.is(Items.BEEF) || item.is(Items.COOKED_BEEF) || item.is(Items.CHICKEN) || item.is(Items.COOKED_CHICKEN) || item.is(Items.PORKCHOP) || item.is(Items.COOKED_PORKCHOP)  || item.is(Items.MUTTON) || item.is(Items.COOKED_MUTTON)){
							player.getFoodData().setFoodLevel(foodlevelplayer + 4);
							player.getFoodData().setSaturation(saturationplayer + 3);
						}
					}
				}
			}
		}
	}


}
