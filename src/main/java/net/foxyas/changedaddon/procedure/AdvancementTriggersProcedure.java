package net.foxyas.changedaddon.procedure;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AdvancementTriggersProcedure {//TODO delete?

//    private static final ResourceLocation ADV = ChangedAddonMod.resourceLoc("organic_transfur");
//
//    @SubscribeEvent
//    public static void OrganicTrigger(ProcessTransfur.EntityVariantAssigned.ChangedVariant event) {
//        // If the interacted entity is a player
//        if (!(event.livingEntity instanceof ServerPlayer player)) return;
//        // Check if the new variant is not null and does not belong to the LATEX type
//        if (event.newVariant == null || event.newVariant.getEntityType().is(ChangedTags.EntityTypes.LATEX)) return;
//
//        // Locate the specific advancement using its ResourceLocation
//        var organicAdvancement = player.getServer().getAdvancements().getAdvancement(ADV);
//
//        // Grant the advancement if it exists
//        if (organicAdvancement != null) {
//            var advancementProgress = player.getAdvancements().getOrStartProgress(organicAdvancement);
//            if (advancementProgress.isDone()) return;
//
//            for (String criterion : advancementProgress.getRemainingCriteria()) {
//                player.getAdvancements().award(organicAdvancement, criterion);
//            }
//        }
//    }
}
