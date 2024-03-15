package net.foxyas.changedaddon.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;

import java.util.UUID;

public class Experiment009OnInitialEntitySpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		AttributeModifier HardModeBuff = null;
		HardModeBuff = new AttributeModifier(UUID.fromString("d6aa1721-3594-4b7b-ad84-977d7b14a94d"), "gameruleBuff", ((world.getLevelData().getGameRules().getInt(ChangedAddonModGameRules.CHANGED_ADDON_HARD_MODE_BOSSES)) / 100),
				AttributeModifier.Operation.MULTIPLY_BASE);
		if (!(((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).hasModifier(HardModeBuff)))
			((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).addTransientModifier(HardModeBuff);
		if (!(((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).hasModifier(HardModeBuff)))
			((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).addTransientModifier(HardModeBuff);
		if (entity instanceof LivingEntity _entity)
			_entity.setHealth(entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "stopsound @a * changed_addon:experiment009_theme");
		}
	}
}
