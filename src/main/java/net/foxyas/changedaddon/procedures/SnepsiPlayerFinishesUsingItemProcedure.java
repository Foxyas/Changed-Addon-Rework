package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModItems;
import net.ltxprogrammer.changed.process.ProcessTransfur;

import net.minecraft.stats.Stats;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import java.util.Iterator;

public class SnepsiPlayerFinishesUsingItemProcedure {
    public static void execute(Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        Entity target = null;
        if (entity instanceof ServerPlayer serverPlayer) {
            StatsCounter stats = serverPlayer.getStats();
            // Distância percorrida no ar
            if (ProcessTransfur.getPlayerTransfurVariant(serverPlayer) == null) {
                if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_latex_snow_leopard_partial")) {
                    AddTransfurProgressProcedure.SnepsiTransfur(entity, 1);
                } else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp2/male")) {
                    AddTransfurProgressProcedure.SnepsiTransfur(entity, 2);
                } else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp2/female")) {
                    AddTransfurProgressProcedure.SnepsiTransfur(entity, 3);
                } else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp6")) {
                    AddTransfurProgressProcedure.SnepsiTransfur(entity, 4);
                } else if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_latex_snep")) {
                    AddTransfurProgressProcedure.SnepsiTransfur(entity, 5);
                } else {
                    AddTransfurProgressProcedure.SnepsiTransfur(entity, serverPlayer.level().random.nextFloat() <= 0.001f ? 6 : 1);
                }
            }
            /*int Snepsi_Drink_Amount = stats.getValue(Stats.ITEM_USED.get(ChangedAddonModItems.SNEPSI.get()));
            if (Snepsi_Drink_Amount >= 100) {
                Advancement _adv = serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:snepsi_adctive"));
                assert _adv != null;
                AdvancementProgress _ap = serverPlayer.getAdvancements().getOrStartProgress(_adv);
                if (!_ap.isDone()) {
                    for (String string : _ap.getRemainingCriteria()) serverPlayer.getAdvancements().award(_adv, string);
                }
            }*/
            //serverPlayer.displayClientMessage(new TextComponent("Drink this = " + Snepsi_Drink_Amount),false);
        }

    }
}
