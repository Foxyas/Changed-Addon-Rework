package net.foxyas.changedaddon.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

public class PlayerUtilProcedure {
	public static Entity getEntityPlayerLookingAt(Player player, double range) {
		Level world = player.level;
		Vec3 startVec = player.getEyePosition(1.0F); // Posição dos olhos do jogador
		Vec3 lookVec = player.getLookAngle(); // Direção do olhar do jogador
		Vec3 endVec = startVec.add(lookVec.scale(range)); // Ponto final da linha de visão

		Entity closestEntity = null;
		double closestDistance = range;

		// Itera sobre todas as entidades dentro do alcance
		for (Entity entity : world.getEntities(player, player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0D))) {
			// Ignora entidades no modo espectador
			if (entity.isSpectator()) {
				continue;
			}

			AABB entityBoundingBox = entity.getBoundingBox().inflate(entity.getPickRadius());

			// Verifica se a linha de visão intercepta a caixa delimitadora da entidade
			if (entityBoundingBox.contains(startVec) || entityBoundingBox.clip(startVec, endVec).isPresent()) {
				double distanceToEntity = startVec.distanceTo(entity.position());

				if (distanceToEntity < closestDistance) {
					closestEntity = entity;
					closestDistance = distanceToEntity;
				}
			}
		}

		return closestEntity; // Retorna a entidade mais próxima que o jogador está olhando
	}

	public static Entity getEntityPlayerLookingAtType2(Entity entity,Entity player, double entityReach) {
			double distance = entityReach * entityReach;
			Vec3 eyePos = player.getEyePosition(1.0f);
			HitResult hitResult = entity.pick(entityReach, 1.0f, false);
			if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
				distance = hitResult.getLocation().distanceToSqr(eyePos);
				double blockReach = 5;
				if (distance > blockReach * blockReach) {
					Vec3 pos = hitResult.getLocation();
					hitResult = BlockHitResult.miss(pos, Direction.getNearest(eyePos.x, eyePos.y, eyePos.z), new BlockPos(pos));
				}
			}
			Vec3 viewVec = player.getViewVector(1.0F);
			Vec3 toVec = eyePos.add(viewVec.x * entityReach, viewVec.y * entityReach, viewVec.z * entityReach);
			AABB aabb = entity.getBoundingBox().expandTowards(viewVec.scale(entityReach)).inflate(1.0D, 1.0D, 1.0D);
			EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(player, eyePos, toVec, aabb, (p_234237_) -> {
				return !p_234237_.isSpectator();
			}, distance);
			if (entityhitresult != null) {
				Entity entity1 = entityhitresult.getEntity();
				Vec3 targetPos = entityhitresult.getLocation();
				double distanceToTarget = eyePos.distanceToSqr(targetPos);
				if (distanceToTarget > distance || distanceToTarget > entityReach * entityReach) {
					hitResult = BlockHitResult.miss(targetPos, Direction.getNearest(viewVec.x, viewVec.y, viewVec.z), new BlockPos(targetPos));
				} else if (distanceToTarget < distance) {
					hitResult = entityhitresult;
				}
			}
			if (hitResult.getType() == HitResult.Type.ENTITY) {
				return ((EntityHitResult) hitResult).getEntity();
			}
			return null;
		}

}
