package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.TransfurVariantDietManager;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

        TransfurVariantInstance<?> latexInstance = ProcessTransfur.getPlayerTransfurVariant(player);
        if (latexInstance == null) return;

        Level level = player.level();

        if (level.isClientSide) return;

        if (!level.getGameRules().getBoolean(ChangedAddonGameRules.CHANGED_ADDON_CREATURE_DIETS)) return;

        ChangedEntity changedEntity = latexInstance.getChangedEntity();
        TransfurVariant<?> variant = changedEntity.getSelfVariant();

        // Retrieve matching diet entries for the current variant and eaten item
        List<FoodDietEntry> matchingEntries = TransfurVariantDietManager.getDietItemsFor(variant, item);
        if (matchingEntries.isEmpty()) return;

        // Apply effects for all matching diet objects
        for (FoodDietEntry entry : matchingEntries) {
            if (entry.shouldApplyEffects(player, item)) {
                entry.applyEffectsAfterEat(player, item);
            }
        }
    }
}