package net.foxyas.changedaddon.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.TextComponent;

public class Experiment009phase2EntityDiesProcedure {
	public static void execute(Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		ItemStack disc_1 = ItemStack.EMPTY;
		ItemStack disc_2 = ItemStack.EMPTY;
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
		if (sourceentity instanceof Player _player && !_player.level.isClientSide())
			_player.displayClientMessage(new TextComponent("\u00A7o\u00A7n\u00A7l\u00A73 Sorry Ria....And Sorry Andy....."), false);
	}
}
