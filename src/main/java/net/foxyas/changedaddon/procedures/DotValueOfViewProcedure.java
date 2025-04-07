package net.foxyas.changedaddon.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;

public class DotValueOfViewProcedure {
	public static double execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return 0;

		Vec3 lookView = entity.getLookAngle(); // vetor de direção (normalizado)
		Vec3 signalPos = new Vec3(
			itemstack.getOrCreateTag().getDouble("x"),
			itemstack.getOrCreateTag().getDouble("y"),
			itemstack.getOrCreateTag().getDouble("z")
		);

		// Cria o vetor do jogador até o sinal
		Vec3 toSignal = signalPos.subtract(entity.getEyePosition(1.0f)).normalize();

		// Produto escalar entre os vetores normalizados
		return lookView.dot(toSignal);
	}
}