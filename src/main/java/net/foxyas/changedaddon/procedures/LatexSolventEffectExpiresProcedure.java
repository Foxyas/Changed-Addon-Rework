package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;

public class LatexSolventEffectExpiresProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity == entity) {
			if (new Object() {
				public double getValue() {
					CompoundTag dataIndex1 = new CompoundTag();
					entity.saveWithoutId(dataIndex1);
					return dataIndex1.getDouble("TransfurProgress");
				}
			}.getValue() < 0) {
				CompoundTag dataIndex1 = new CompoundTag();
				entity.saveWithoutId(dataIndex1);
				dataIndex1.putFloat("TransfurProgress", 0);
				entity.load(dataIndex1);
			}
		}
	}
}
