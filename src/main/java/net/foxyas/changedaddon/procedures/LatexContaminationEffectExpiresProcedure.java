package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class LatexContaminationEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "attribute @s changed_addon:latexinfection modifier remove 0-0-0-0-1");
		}
	}
}
