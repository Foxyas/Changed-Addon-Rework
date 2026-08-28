package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.MobEffectHolder;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDiet;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDietManager;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class CreatureDietsHandleProcedure {

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

        boolean foundAnyGoodFoodMatch = false;

        // Iterate through all diets for this variant
        for (TransfurVariantDiet diet : dietsForVariant) {
            // Find matching entries inside this diet for the eaten food item
            List<FoodDietEntry> matchingEntries = diet.foods().stream()
                    .filter(entry -> entry.ingredients().stream().anyMatch(ing -> ing.test(item)))
                    .toList();

            for (FoodDietEntry entry : matchingEntries) {
                if (entry.shouldApplyEffects(player, item)) {
                    entry.applyEffectsAfterEat(player, item);

                    // Track if this was beneficial (non-sick) food
                    if (!entry.isSickFor(player)) {
                        foundAnyGoodFoodMatch = true;
                    }
                }
            }
        }

        // If NONE of the matching diets contained a non-sick entry for this item
        if (!foundAnyGoodFoodMatch) {
            for (TransfurVariantDiet diet : dietsForVariant) {
                if (diet.offDietEffects() == null || diet.offDietEffects().isEmpty()) continue;

                for (MobEffectHolder effectHolder : diet.offDietEffects()) {
                    // Evaluated using a dummy/empty FoodDietEntry for status checks
                    if (effectHolder.shouldApplyEffect(player, null) && shouldApplySickEffects(player, variantInstance, item)) {
                        player.addEffect(new MobEffectInstance(effectHolder.mobEffectInstance()));
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

        // Do not process non-food items tagged as exempt
        if (item.is(ChangedAddonTags.Items.NOT_FOOD)) {
            return false;
        }

        // If debuffs are disabled in configuration, do not apply sickness effects
        if (!ChangedAddonServerConfiguration.DEBUFFS.get()) {
            return false;
        }

        // If the player has surpassed the adaptation age threshold, they are immune to diet sickness
        return latexInstance.ageAsVariant < ChangedAddonServerConfiguration.AGE_NEED.get();
    }
}