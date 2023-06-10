package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class GrabeffectOnEffectActiveTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!((entityiterator.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).equals("")) {
					{
						Entity _ent = entity;
						_ent.teleportTo((entityiterator.getX() + entityiterator.getLookAngle().x), (entityiterator.getY()), (entityiterator.getZ() + entityiterator.getLookAngle().z));
						if (_ent instanceof ServerPlayer _serverPlayer)
							_serverPlayer.connection.teleport((entityiterator.getX() + entityiterator.getLookAngle().x), (entityiterator.getY()), (entityiterator.getZ() + entityiterator.getLookAngle().z), _ent.getYRot(), _ent.getXRot());
					}
				}
			}
		}
	}
}
