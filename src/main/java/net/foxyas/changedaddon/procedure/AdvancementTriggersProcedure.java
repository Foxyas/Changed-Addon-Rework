package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AdvancementTriggersProcedure {

    private static final ResourceLocation ADV = ChangedAddonMod.resourceLoc("organic_transfur_advancement");

    @SubscribeEvent
    public static void OrganicTrigger(ProcessTransfur.EntityVariantAssigned.ChangedVariant event) {
        // If the interacted entity is a player
        if (!(event.livingEntity instanceof ServerPlayer player)) return;
        // Check if the new variant is not null and does not belong to the LATEX type
        if (event.newVariant == null || event.newVariant.getEntityType().is(ChangedTags.EntityTypes.LATEX)) return;

        // Locate the specific advancement using its ResourceLocation
        var organicAdvancement = player.getServer().getAdvancements().getAdvancement(ADV);

        // Grant the advancement if it exists
        if (organicAdvancement != null) {
            var advancementProgress = player.getAdvancements().getOrStartProgress(organicAdvancement);
            if (advancementProgress.isDone()) return;

            for (String criterion : advancementProgress.getRemainingCriteria()) {
                player.getAdvancements().award(organicAdvancement, criterion);
            }
        }
    }
}
