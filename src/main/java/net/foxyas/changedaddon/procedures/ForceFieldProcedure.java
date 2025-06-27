package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class ForceFieldProcedure {
	public static void execute(LevelAccessor world, Entity entity, boolean pulse) {
		if (entity == null)
			return;
		if (!pulse) {
			{
				final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(10 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof FallingBlockEntity || entityiterator.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minecraft:impact_projectiles")))) {
						entityiterator.setDeltaMovement(new Vec3((entityiterator.getDeltaMovement().x() + -(entityiterator.getDeltaMovement().x())), (entityiterator.getDeltaMovement().y() + -(entityiterator.getDeltaMovement().y())),
								(entityiterator.getDeltaMovement().z() + -(entityiterator.getDeltaMovement().z()))));
					}
				}
			}
		} else {
			{
				final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof FallingBlockEntity || entityiterator.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("minecraft:impact_projectiles")))) {
						entityiterator.setDeltaMovement(new Vec3((-(entityiterator.getDeltaMovement().x())), (-(entityiterator.getDeltaMovement().y())), (-(entityiterator.getDeltaMovement().z()))));
					}
				}
			}
		}
	}
}
