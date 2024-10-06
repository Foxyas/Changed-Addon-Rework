package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.Comparator;

public class IflatexentityProcedure {
	public static boolean execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return false;
		Entity Entitya = null;
		Entity target = null;
		Entitya = (Entity) world.getEntitiesOfClass(LivingEntity.class, AABB.ofSize(new Vec3(x, y, z), 64, 64, 64), e -> true).stream().sorted(new Object() {
			Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
				return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
			}
		}.compareDistOf(x, y, z)).findFirst().orElse(null);
		target = entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null;
		if (Entitya == null) {
			return false;
		} else if (target == null) {
			return false;
		}
		if ((entity instanceof net.ltxprogrammer.changed.entity.ChangedEntity || target instanceof Monster) && target.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed:latexes")))) {
			return true;
		} else if (target instanceof Player && (entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur
				&& !(entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).organic_transfur) {
			return true;
		}
		return false;
	}
}
