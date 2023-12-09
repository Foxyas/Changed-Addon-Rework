package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
	@Mod.EventBusSubscriber
public class AdvancementTriggersProcedure {
	@SubscribeEvent
	public static void OrganicTrigger(ProcessTransfur.EntityVariantAssigned.ChangedVariant event) {

		if (event.newVariant.getEntityType() != null && event.newVariant.getEntityType().is(ChangedTags.EntityTypes.ORGANIC_LATEX)) {
			if (event.livingEntity instanceof Player player) {
				OrganicAdvancementGive(player);
			}
		}
	};

	public static void OrganicAdvancementGive(Player player) {
		if (!player.level.isClientSide() && player.getServer() != null)
			player.getServer().getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "advancement grant @s only changed_addon:organic_transfur_advancement");

	}
}
