package net.foxyas.changedaddon.procedures;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class IridiumoreBlockDestroyedByExplosionProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        if (world instanceof Level _level && !_level.isClientSide())
            _level.addFreshEntity(new ExperienceOrb(_level, x, y, z, 15));
    }
}
