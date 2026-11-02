package net.foxyas.changedaddon.process.variantsExtraStats.diets;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class TransfurVariantDietsHandler {

    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event == null) return;

        LivingEntity livingEntity = event.getEntity();
        ItemStack item = event.getItem();
        if (!item.isEdible()) return;

        if (!(livingEntity instanceof Player player)) {
            return;
        }

        TransfurVariantInstance<?> variantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variantInstance == null) return;

        Level level = player.level();
        if (level.isClientSide) return;

        if (!level.getGameRules().getBoolean(ChangedAddonGameRules.CHANGED_ADDON_CREATURE_DIETS)) return;

        // Retrieve all active diets matching the player's variant
        List<TransfurVariantDiet> dietsForVariant = TransfurVariantDietManager.getDietsForVariant(variantInstance);
        if (dietsForVariant.isEmpty()) return;

        // Collect matching diet entries across all active diets
        List<MatchedDietPair> matchedPairs = new ArrayList<>();
        boolean foundAnyGoodFoodMatch = false;

        for (TransfurVariantDiet diet : dietsForVariant) {
            List<FoodDietEntry> matchingEntries = diet.foods().stream()
                    .filter(entry -> entry.ingredients().stream().anyMatch(ing -> ing.test(item)))
                    .toList();

            for (FoodDietEntry entry : matchingEntries) {
                if (entry.shouldApplyEffects(player, item)) {
                    matchedPairs.add(new MatchedDietPair(diet, entry));

                    if (!entry.isSickFor(player)) {
                        foundAnyGoodFoodMatch = true;
                    }
                }
            }
        }

        // Fire the FormDietEvent for each matching diet entry
        if (!matchedPairs.isEmpty()) {
            for (MatchedDietPair pair : matchedPairs) {
                TransfurVariantDietEvent dietEvent = new TransfurVariantDietEvent(
                        variantInstance,
                        pair.diet(),
                        player,
                        foundAnyGoodFoodMatch,
                        item
                );

                // If event is canceled, skip applying this diet entry
                if (ChangedAddonMod.postEvent(dietEvent)) {
                    continue;
                }

                TransfurVariantDiet finalDiet = dietEvent.getDiet();
                if (finalDiet.offDietEffects() == null) continue;

                // Apply effects using event.getDiet() in case a listener swapped the diet
                pair.entry().applyEffectsAfterEat(player, variantInstance, finalDiet, item, foundAnyGoodFoodMatch);
            }
        } else {
            // If no matching food entries were found, evaluate off-diet / sickness effects
            for (TransfurVariantDiet diet : dietsForVariant) {
                TransfurVariantDietEvent dietEvent = new TransfurVariantDietEvent(
                        variantInstance,
                        diet,
                        player,
                        false, // No good diet match was found
                        item
                );

                if (ChangedAddonMod.postEvent(dietEvent)) {
                    continue;
                }

                TransfurVariantDiet finalDiet = dietEvent.getDiet();
                if (finalDiet.offDietEffects() == null || finalDiet.offDietEffects().isEmpty()) continue;

                for (MobEffectHolder effectHolder : finalDiet.offDietEffects()) {
                    if (effectHolder.shouldApplyEffect(player, null, true) && shouldApplySickEffects(player, variantInstance, item)) {
                        effectHolder.addMobEffects(player, variantInstance, finalDiet, foundAnyGoodFoodMatch, item, null, true);
                    }
                }
            }
        }
    }

    public static boolean shouldApplySickEffects(Player player, TransfurVariantInstance<?> latexInstance, ItemStack item) {
        if (player == null || item.isEmpty()) {
            return false;
        }

        if (latexInstance == null) {
            return false;
        }

        if (item.is(ChangedAddonTags.Items.UNIVERSAL_FOODS)) {
            return false;
        }

        if (!ChangedAddonServerConfiguration.DEBUFFS.get()) {
            return false;
        }

        return latexInstance.ageAsVariant < ChangedAddonServerConfiguration.AGE_NEED.get();
    }

    private record MatchedDietPair(TransfurVariantDiet diet, FoodDietEntry entry) {
    }
}