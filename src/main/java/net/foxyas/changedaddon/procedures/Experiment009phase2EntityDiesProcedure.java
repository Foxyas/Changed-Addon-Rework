package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class Experiment009phase2EntityDiesProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "stopsound @a * changed_addon:experiment009_theme_phase2");
		}
	}
}
