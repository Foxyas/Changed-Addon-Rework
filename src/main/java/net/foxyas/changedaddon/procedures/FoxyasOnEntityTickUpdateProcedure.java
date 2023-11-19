package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class FoxyasOnEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double motionZ = 0;
		double deltaZ = 0;
		double distance = 0;
		double deltaX = 0;
		double motionY = 0;
		double deltaY = 0;
		double motionX = 0;
		double maxSpeed = 0;
		double speed = 0;
		{
			final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (entityiterator.getPersistentData().getBoolean("FoxyasGui_open") == true) {
					if (entity instanceof Mob _entity)
						_entity.getNavigation().moveTo((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 0.8);
				}
			}
		}
		if (entity.isInWater()) {
			if (!((entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null) == (null))) {
				deltaX = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getX() - entity.getX();
				deltaY = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getY() - entity.getY();
				deltaZ = (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getZ() - entity.getZ();
				distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
			}
			if (distance > 0) {
				speed = 0.04;
				motionX = deltaX / distance * speed;
				motionY = deltaY / distance * speed;
				motionZ = deltaZ / distance * speed;
				maxSpeed = 0.2;
				{
					Entity _ent = entity;
					if (!_ent.level.isClientSide() && _ent.getServer() != null)
						_ent.getServer().getCommands().performCommand(_ent.createCommandSourceStack().withSuppressedOutput().withPermission(4),
								("execute as " + entity.getStringUUID() + " at @s run tp @s ~ ~ ~ facing entity " + (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null).getStringUUID()));
				}
				entity.setDeltaMovement(entity.getDeltaMovement().add(motionX, motionY, motionZ));
			}
			if (motionX > maxSpeed) {
				motionX = maxSpeed;
			}
			if (motionY > maxSpeed) {
				motionY = maxSpeed;
			}
			if (motionZ > maxSpeed) {
				motionZ = maxSpeed;
			}
		}
	}
}
