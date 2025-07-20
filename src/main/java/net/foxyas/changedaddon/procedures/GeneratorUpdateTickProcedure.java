package net.foxyas.changedaddon.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneratorUpdateTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z) {
        double distance = 0;
        if ((new Object() {
            public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null)
                    return blockEntity.getTileData().getBoolean(tag);
                return false;
            }
        }.getValue(world, new BlockPos(x, y, z), "turn_on"))) {
            {
                BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                int _amount = 10;
                if (_ent != null)
                    _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
            }
            if (new Object() {
                public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                    AtomicInteger _retval = new AtomicInteger(0);
                    BlockEntity _ent = level.getBlockEntity(pos);
                    if (_ent != null)
                        _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                    return _retval.get();
                }
            }.getEnergyStored(world, new BlockPos(x, y, z)) > 0) {
                if (new Object() {
                    public boolean canExtractEnergy(LevelAccessor level, BlockPos pos) {
                        AtomicBoolean _retval = new AtomicBoolean(false);
                        BlockEntity _ent = level.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.canExtract()));
                        return _retval.get();
                    }
                }.canExtractEnergy(world, new BlockPos(x + 1, y, z))) {
                    if (new Object() {
                        public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                            AtomicInteger _retval = new AtomicInteger(0);
                            BlockEntity _ent = level.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                            return _retval.get();
                        }
                    }.getEnergyStored(world, new BlockPos(x + 1, y, z)) >= 100) {
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x - 1, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
                        }
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.extractEnergy(_amount, false));
                        }
                    }
                }
                if (new Object() {
                    public boolean canExtractEnergy(LevelAccessor level, BlockPos pos) {
                        AtomicBoolean _retval = new AtomicBoolean(false);
                        BlockEntity _ent = level.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.canExtract()));
                        return _retval.get();
                    }
                }.canExtractEnergy(world, new BlockPos(x - 1, y, z))) {
                    if (new Object() {
                        public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                            AtomicInteger _retval = new AtomicInteger(0);
                            BlockEntity _ent = level.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                            return _retval.get();
                        }
                    }.getEnergyStored(world, new BlockPos(x - 1, y, z)) >= 100) {
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x + 1, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
                        }
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.extractEnergy(_amount, false));
                        }
                    }
                }
                if (new Object() {
                    public boolean canExtractEnergy(LevelAccessor level, BlockPos pos) {
                        AtomicBoolean _retval = new AtomicBoolean(false);
                        BlockEntity _ent = level.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.canExtract()));
                        return _retval.get();
                    }
                }.canExtractEnergy(world, new BlockPos(x, y + 1, z))) {
                    if (new Object() {
                        public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                            AtomicInteger _retval = new AtomicInteger(0);
                            BlockEntity _ent = level.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                            return _retval.get();
                        }
                    }.getEnergyStored(world, new BlockPos(x, y + 1, z)) >= 100) {
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y + 1, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
                        }
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.extractEnergy(_amount, false));
                        }
                    }
                }
                if (new Object() {
                    public boolean canExtractEnergy(LevelAccessor level, BlockPos pos) {
                        AtomicBoolean _retval = new AtomicBoolean(false);
                        BlockEntity _ent = level.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.canExtract()));
                        return _retval.get();
                    }
                }.canExtractEnergy(world, new BlockPos(x, y - 1, z))) {
                    if (new Object() {
                        public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                            AtomicInteger _retval = new AtomicInteger(0);
                            BlockEntity _ent = level.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                            return _retval.get();
                        }
                    }.getEnergyStored(world, new BlockPos(x, y - 1, z)) >= 100) {
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y - 1, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
                        }
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.extractEnergy(_amount, false));
                        }
                    }
                }
                if (new Object() {
                    public boolean canExtractEnergy(LevelAccessor level, BlockPos pos) {
                        AtomicBoolean _retval = new AtomicBoolean(false);
                        BlockEntity _ent = level.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.canExtract()));
                        return _retval.get();
                    }
                }.canExtractEnergy(world, new BlockPos(x, y, z - 1))) {
                    if (new Object() {
                        public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                            AtomicInteger _retval = new AtomicInteger(0);
                            BlockEntity _ent = level.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                            return _retval.get();
                        }
                    }.getEnergyStored(world, new BlockPos(x, y, z - 1)) >= 100) {
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z - 1));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
                        }
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.extractEnergy(_amount, false));
                        }
                    }
                }
                if (new Object() {
                    public boolean canExtractEnergy(LevelAccessor level, BlockPos pos) {
                        AtomicBoolean _retval = new AtomicBoolean(false);
                        BlockEntity _ent = level.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.canExtract()));
                        return _retval.get();
                    }
                }.canExtractEnergy(world, new BlockPos(x, y, z + 1))) {
                    if (new Object() {
                        public int getEnergyStored(LevelAccessor level, BlockPos pos) {
                            AtomicInteger _retval = new AtomicInteger(0);
                            BlockEntity _ent = level.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> _retval.set(capability.getEnergyStored()));
                            return _retval.get();
                        }
                    }.getEnergyStored(world, new BlockPos(x, y, z + 1)) >= 100) {
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z + 1));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.receiveEnergy(_amount, false));
                        }
                        {
                            BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                            int _amount = 100;
                            if (_ent != null)
                                _ent.getCapability(CapabilityEnergy.ENERGY, null).ifPresent(capability -> capability.extractEnergy(_amount, false));
                        }
                    }
                }
            }
        }
    }
}
