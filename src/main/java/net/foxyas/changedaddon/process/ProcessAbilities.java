package net.foxyas.changedaddon.process;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.TranslatableComponent;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;

import net.foxyas.changedaddon.abilities.DodgeAbilityInstance;
import net.foxyas.changedaddon.abilities.ChangedAddonAbilities;

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
		DodgeAbilityInstance DodgeAbility = (DodgeAbilityInstance) TransfurVariantInstance.abilityInstances.get(ChangedAddonAbilities.DODGE.get());
		if (DodgeAbility == null) {
			return;
		}
		int DodgeAmount = DodgeAbility.getDodgeAmount();
		int MaxDodgeAmount = DodgeAbility.getMaxDodgeAmount();
		boolean IsDodgeActivate = DodgeAbility.isDodgeActivate();
		boolean nonIframe = player.invulnerableTime <= 0;
		boolean nonHurtFrame = player.hurtTime <= 5;
		if (!IsDodgeActivate && nonHurtFrame) {
			if (DodgeAmount < MaxDodgeAmount) {
				if (DodgeAbility.DodgeRegenCooldown <= 0) {
					DodgeAbility.addDodgeAmount();
					DodgeAbility.DodgeRegenCooldown = DodgeAbility.DefaultDodgeRegenCooldown;
					player.displayClientMessage(new TranslatableComponent("changed_addon.ability.dodge.dodge_amount", +DodgeAmount), true);
				} else {
					DodgeAbility.DodgeRegenCooldown--;
				}
			}
		}
	}
}
