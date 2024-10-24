package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.UUID;

public class TransfurSicknessOnEffectActiveTickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur) {
			if (GetDefault.execute((Player) entity)) {
				setPlayerTransfurMode.execute((Player) entity, 3);
			}
			if (entity instanceof LivingEntity livingEntity) {
				if (livingEntity.getAttribute(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("changed:transfur_tolerance"))) != null) {
					livingEntity.getAttribute(ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation("changed:transfur_tolerance")))
							.addTransientModifier((new AttributeModifier(UUID.fromString("0-0-0-0-0"), "Transfur_Sickness_debuff", ((-0.1) * amplifier), AttributeModifier.Operation.MULTIPLY_TOTAL)));
				}
			}
		}
	}
}
