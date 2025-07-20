package net.foxyas.changedaddon.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorEmittedRedstonePowerProcedure {
    public static boolean execute(LevelAccessor world, double x, double y, double z) {
        return new Object() {
            public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                AtomicInteger _retval = new AtomicInteger(0);
                BlockEntity _ent = level.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                return _retval.get();
            }
        }.getEnergyStored(world, new BlockPos(x, y, z)) > 0;
    }
}
