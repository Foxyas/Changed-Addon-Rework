package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class Experiment009phase2EntityDiesProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		ItemStack disc_1 = ItemStack.EMPTY;
		ItemStack disc_2 = ItemStack.EMPTY;
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "stopsound @a * changed_addon:experiment009_theme_phase2");
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
						"summon minecraft:item ~ ~ ~ {Item:{id:\"changed_addon:experiment_009_phase_2_record\",Count:1},Glowing:1b,Age:-32768s}");
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "summon minecraft:item ~ ~ ~ {Item:{id:\"changed_addon:experiment_009_record\",Count:1},Glowing:1b,Age:-32768s}");
		}
		{
			Entity _ent = entity;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), "summon minecraft:item ~ ~ ~ {Item:{id:\"changed_addon:transfur_totem\",Count:1},Glowing:1b,Age:-32768s}");
		}
	}
}
