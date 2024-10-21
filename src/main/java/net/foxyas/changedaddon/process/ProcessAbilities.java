package net.foxyas.changedaddon.process;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;

import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.ability.DodgeAbility;
import net.foxyas.changedaddon.ability.ChangedAddonAbilitys;

@Mod.EventBusSubscriber
public class ProcessAbilities {
	//TODO Place new Ability Handle here
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		TransfurVariantInstance<?> TransfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
		if (TransfurVariantInstance == null) {
			return;
		}
		DodgeAbilityInstance DodgeAbility = (DodgeAbilityInstance) TransfurVariantInstance.abilityInstances.get(ChangedAddonAbilitys.DODGE.get());
		if (DodgeAbility == null) {
			return;
		}
		int DodgeAmount = DodgeAbility.getDodgeAmount();
		int MaxDodgeAmount = DodgeAbility.getMaxDodgeAmount();
		boolean IsDodgeActivate = DodgeAbility.isDodgeActivate();
		player.displayClientMessage(new TextComponent("TEXT12"), false);
		if (!IsDodgeActivate) {
			if (DodgeAmount < MaxDodgeAmount) {
				if (DodgeRegenCooldown < 0) {
					DodgeAmount++;
					DodgeRegenCooldown = 5;
					if (entity.getEntity() instanceof Player player) {
						player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", +DodgeAmount), true);
					}
				} else {
					DodgeRegenCooldown--;
				}
			}
		}
	}
}
