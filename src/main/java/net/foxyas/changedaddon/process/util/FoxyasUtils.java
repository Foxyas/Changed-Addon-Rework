package net.foxyas.changedaddon.process.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class FoxyasUtils {

	public static BlockHitResult manualRaycastIgnoringBlocks(Level level, Entity entity, double maxDistance, Set<Block> ignoredBlocks) {
		Vec3 start = entity.getEyePosition(1.0F);
		Vec3 lookVec = entity.getViewVector(1.0F);
		Vec3 end = start.add(lookVec.scale(maxDistance));

		double stepSize = 0.1; // tamanho do passo
		Vec3 currentPos = start;
		double steps = maxDistance / stepSize;

		for (int i = 0; i < steps; i++) {
			BlockPos blockPos = new BlockPos(currentPos);
			BlockState state = level.getBlockState(blockPos);

			if (!ignoredBlocks.contains(state.getBlock()) && state.isSolidRender(level, blockPos)) {
				// Retorna o hit manual quando encontra um bloco não ignorado
				return new BlockHitResult(currentPos, Direction.getNearest(lookVec.x, lookVec.y, lookVec.z), blockPos, true);
			}

			currentPos = currentPos.add(lookVec.scale(stepSize));
		}

		// Nada atingido
		return BlockHitResult.miss(end, Direction.getNearest(lookVec.x, lookVec.y, lookVec.z), new BlockPos(end));
	}

	public static Vec3 getRelativePositionEyes(Entity entity, float deltaX, float deltaY, float deltaZ) {
		// Obtém os vetores locais da entidade
		Vec3 forward = entity.getViewVector(1.0f); // Direção que a entidade está olhando (Surge)
		Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
		Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

		// Combina os deslocamentos locais
		Vec3 offset = right.scale(-deltaX) // Sway (esquerda/direita)
				.add(up.scale(deltaY)) // Heave (cima/baixo)
				.add(forward.scale(deltaZ)); // Surge (frente/trás)

		// Retorna a nova posição baseada no deslocamento local
		return entity.getEyePosition().add(offset);
	}

	public static Vec3 getRelativePositionCommandStyle(Entity entity, double deltaX, double deltaY, double deltaZ) {
        Vec2 rotation = entity.getRotationVector(); // Obtém a rotação (Yaw e Pitch)
        Vec3 position = entity.position(); // Posição atual da entidade

        // Cálculo dos vetores locais
        float yawRad = (rotation.y + 90.0F) * ((float) Math.PI / 180F);
        float pitchRad = -rotation.x * ((float) Math.PI / 180F);
        float pitchRad90 = (-rotation.x + 90.0F) * ((float) Math.PI / 180F);

        float cosYaw = Mth.cos(yawRad);
        float sinYaw = Mth.sin(yawRad);
        float cosPitch = Mth.cos(pitchRad);
        float sinPitch = Mth.sin(pitchRad);
        float cosPitch90 = Mth.cos(pitchRad90);
        float sinPitch90 = Mth.sin(pitchRad90);

        Vec3 forward = new Vec3(cosYaw * cosPitch, sinPitch, sinYaw * cosPitch); // Vetor para frente (Surge)
        Vec3 up = new Vec3(cosYaw * cosPitch90, sinPitch90, sinYaw * cosPitch90); // Vetor para cima (Heave)
        Vec3 right = forward.cross(up).scale(-1.0D); // Vetor para direita (Sway)

        // Calcula nova posição baseada nos deslocamentos locais
        double newX = forward.x * deltaZ + up.x * deltaY + right.x * deltaX;
        double newY = forward.y * deltaZ + up.y * deltaY + right.y * deltaX;
        double newZ = forward.z * deltaZ + up.z * deltaY + right.z * deltaX;

        return new Vec3(position.x + newX, position.y + newY, position.z + newZ);
    }

	public static Vec3 getRelativePosition(Entity entity, double deltaX, double deltaY, double deltaZ, boolean onlyOffset) {
		if (entity == null) {
			return Vec3.ZERO;
		}
		// Obtém os vetores locais da entidade
		Vec3 forward = entity.getViewVector(1.0F); // Direção que a entidade está olhando (Surge)
		Vec3 up = entity.getUpVector(1.0F); // Vetor "para cima" da entidade (Heave)
		Vec3 right = forward.cross(up).normalize(); // Calcula o vetor para a direita (Sway)

		// Combina os deslocamentos locais
		Vec3 offset = right.scale(deltaX) // Sway (esquerda/direita)
				.add(up.scale(deltaY)) // Heave (cima/baixo)
				.add(forward.scale(deltaZ)); // Surge (frente/trás)

		if (onlyOffset) {
			return offset;
		}
		// Retorna a nova posição baseada no deslocamento local
		return entity.position().add(offset);
	}

}
