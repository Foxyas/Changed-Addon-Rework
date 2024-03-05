package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.LatexEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.LatexVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;


public class SummonEntityProcedure {
	public static void execute(Level world,Player player) {
		if (world instanceof ServerLevel _level) {
			double x = player.getX();
			double y = player.getY();
			double z = player.getZ();
			LatexVariantInstance instance = ProcessTransfur.getPlayerLatexVariant(player);
			LatexEntity fakeEntity = instance.getLatexEntity();
			Entity entityToSpawn = fakeEntity;
			entityToSpawn.moveTo(x, y, z, 0, 0);
			entityToSpawn.setYBodyRot(0);
			entityToSpawn.setYHeadRot(0);
			Player _ent = player;
			if (!_ent.level.isClientSide() && _ent.getServer() != null)
				_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4), ("summon " + ForgeRegistries.ENTITIES.getKey(entityToSpawn.getType()).toString() + " ~ ~ ~"));

		}

	}
}

