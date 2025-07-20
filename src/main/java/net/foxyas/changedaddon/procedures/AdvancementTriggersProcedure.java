package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class AdvancementTriggersProcedure {
    @SubscribeEvent
    public static void OrganicTrigger(ProcessTransfur.EntityVariantAssigned.ChangedVariant event) {
        // Check if the new variant is not null and does not belong to the LATEX type
        if (event.newVariant != null && !event.newVariant.getEntityType().is(ChangedTags.EntityTypes.LATEX)) {
            // If the interacted entity is a player
            if (event.livingEntity instanceof Player player) {
                // Grant the advancement
                OrganicAdvancementGive(player);
            }
        }
    }

    // Method to grant the advancement
    public static void OrganicAdvancementGive(Player player) {
        // Ensure we are on the server side
        if (!player.level.isClientSide()) {
            // Get the player's server
            var server = player.getServer();
            if (server != null) {
                // Get the server's advancement manager
                var advancementManager = server.getAdvancements();

                // Locate the specific advancement using its ResourceLocation
                var organicAdvancement = advancementManager.getAdvancement(new ResourceLocation("changed_addon", "organic_transfur_advancement"));

                // Grant the advancement if it exists
                if (organicAdvancement != null) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        var advancementProgress = serverPlayer.getAdvancements().getOrStartProgress(organicAdvancement);
                        if (!advancementProgress.isDone()) {
                            for (String criterion : advancementProgress.getRemainingCriteria()) {
                                serverPlayer.getAdvancements().award(organicAdvancement, criterion);
                            }
                        }
                    }
                }
            }
        }
    }
}
