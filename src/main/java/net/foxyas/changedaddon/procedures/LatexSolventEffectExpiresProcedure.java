package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;

public class LatexSolventEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "attribute @s changed_addon:latexresistance modifier remove f9ff3894-a234-4994-ac8f-d84f45d1827c");
		}
	}
}
