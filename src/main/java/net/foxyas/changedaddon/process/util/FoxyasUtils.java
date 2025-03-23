package net.foxyas.changedaddon.process.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FoxyasUtils {

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
