package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipes.RecipesHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CatalyzerUpdateTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
        ItemStack output;
        ItemStack Slot0;
        BlockState block;
        if (new Object() {
            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null)
                    return blockEntity.getTileData().getDouble(tag);
                return -1;
            }
        }.getValue(world, new BlockPos(x, y, z), "nitrogen_power") < 200) {
            if (!world.isClientSide()) {
                BlockPos _bp = new BlockPos(x, y, z);
                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                BlockState _bs = world.getBlockState(_bp);
                if (_blockEntity != null)
                    _blockEntity.getTileData().putDouble("nitrogen_power", (new Object() {
                        public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getDouble(tag);
                            return -1;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "nitrogen_power") + 1));
                if (world instanceof Level _level)
                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
            }
        }
        if ((new Object() {
            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                return _retval.get();
            }
        }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == Blocks.AIR.asItem()) {
            if (!world.isClientSide()) {
                BlockPos _bp = new BlockPos(x, y, z);
                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                BlockState _bs = world.getBlockState(_bp);
                if (_blockEntity != null)
                    _blockEntity.getTileData().putBoolean("recipe_on", false);
                if (world instanceof Level _level)
                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
            }
            if (new Object() {
                public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity != null)
                        return blockEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos(x, y, z), "recipe_progress") > 0) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos(x, y, z);
                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_blockEntity != null)
                        _blockEntity.getTileData().putDouble("recipe_progress", ((new Object() {
                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "recipe_progress")) - 5));
                    if (world instanceof Level _level)
                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
            if (new Object() {
                public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity != null)
                        return blockEntity.getTileData().getDouble(tag);
                    return -1;
                }
            }.getValue(world, new BlockPos(x, y, z), "recipe_progress") < 0) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos(x, y, z);
                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_blockEntity != null)
                        _blockEntity.getTileData().putDouble("recipe_progress", 0);
                    if (world instanceof Level _level)
                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                }
            }
        }
        if (new Object() {
            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicInteger _retval = new AtomicInteger(0);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                return _retval.get();
            }
        }.getAmount(world, new BlockPos(x, y, z), 1) < ((new Object() {
            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                return _retval.get();
            }
        }.getItemStack(world, new BlockPos(x, y, z), 1))).getMaxStackSize()) {
            if (!world.isClientSide()) {
                BlockPos _bp = new BlockPos(x, y, z);
                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                BlockState _bs = world.getBlockState(_bp);
                if (_blockEntity != null)
                    _blockEntity.getTileData().putBoolean("Full", false);
                if (world instanceof Level _level)
                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
            }
        } else {
            if (!world.isClientSide()) {
                BlockPos _bp = new BlockPos(x, y, z);
                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                BlockState _bs = world.getBlockState(_bp);
                if (_blockEntity != null)
                    _blockEntity.getTileData().putBoolean("Full", true);
                if (world instanceof Level _level)
                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
            }
        }
        if ((new Object() {
            public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity != null)
                    return blockEntity.getTileData().getBoolean(tag);
                return false;
            }
        }.getValue(world, new BlockPos(x, y, z), "start_recipe"))) {
            if (ChangedAddonServerConfiguration.CUSTOMRECIPES.get()) {
                block = blockstate;
                Slot0 = (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0));
                output = RecipesHandle.getCatalyzerRecipeOutputOrDefault(world, Slot0);
                if (!(output.getItem() == Blocks.AIR.asItem())) {
                    if (!(output.getItem() == Blocks.AIR.asItem())) {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", true);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", false);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if ((new Object() {
                        public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getBoolean(tag);
                            return false;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "recipe_on"))) {
                        if (new Object() {
                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "recipe_progress") < 100) {
                            if (!world.isClientSide()) {
                                BlockPos _bp = new BlockPos(x, y, z);
                                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                BlockState _bs = world.getBlockState(_bp);
                                if (_blockEntity != null)
                                    _blockEntity.getTileData().putDouble("recipe_progress", (new Object() {
                                        public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                            BlockEntity blockEntity = world.getBlockEntity(pos);
                                            if (blockEntity != null)
                                                return blockEntity.getTileData().getDouble(tag);
                                            return -1;
                                        }
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + RecipesHandle.getCatalyzerRecipeProgressSpeed(world, block, Slot0)));
                                if (world instanceof Level _level)
                                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                            }
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putDouble("recipe_progress", 0);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if (new Object() {
                        public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getDouble(tag);
                            return -1;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") >= 100) {
                        if (!(new Object() {
                            public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getBoolean(tag);
                                return false;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "Full"))) {
                            if ((new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == output.getItem() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Blocks.AIR.asItem()) {
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 0;
                                        final int _amount = 1;
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable) {
                                                ItemStack _stk = capability.getStackInSlot(_slotid).copy();
                                                _stk.shrink(_amount);
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _stk);
                                            }
                                        });
                                    }
                                }
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 1;
                                        final ItemStack _setstack = output;
                                        _setstack.setCount((output).getCount() + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 1));
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable)
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
                                        });
                                    }
                                }
                                if (!world.isClientSide()) {
                                    BlockPos _bp = new BlockPos(x, y, z);
                                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                    BlockState _bs = world.getBlockState(_bp);
                                    if (_blockEntity != null)
                                        _blockEntity.getTileData().putDouble("recipe_progress", 0);
                                    if (world instanceof Level _level)
                                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                                }
                                if (!world.isClientSide()) {
                                    BlockPos _bp = new BlockPos(x, y, z);
                                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                    BlockState _bs = world.getBlockState(_bp);
                                    if (_blockEntity != null)
                                        _blockEntity.getTileData().putDouble("nitrogen_power", ((new Object() {
                                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                                if (blockEntity != null)
                                                    return blockEntity.getTileData().getDouble(tag);
                                                return -1;
                                            }
                                        }.getValue(world, new BlockPos(x, y, z), "nitrogen_power")) - RecipesHandle.getCatalyzerRecipeNitrogenUsage(world, Slot0)));
                                    if (world instanceof Level _level)
                                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                                }
                            }
                        }
                    }
                }
            } else {
                if ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.AMMONIA_COMPRESSED.get()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.AMMONIA_COMPRESSED.get()) {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", true);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", false);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if ((new Object() {
                        public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getBoolean(tag);
                            return false;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "recipe_on"))) {
                        if (new Object() {
                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "recipe_progress") < 100) {
                            if (!world.isClientSide()) {
                                BlockPos _bp = new BlockPos(x, y, z);
                                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                BlockState _bs = world.getBlockState(_bp);
                                if (_blockEntity != null)
                                    _blockEntity.getTileData().putDouble("recipe_progress", (new Object() {
                                        public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                            BlockEntity blockEntity = world.getBlockEntity(pos);
                                            if (blockEntity != null)
                                                return blockEntity.getTileData().getDouble(tag);
                                            return -1;
                                        }
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 0.5));
                                if (world instanceof Level _level)
                                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                            }
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putDouble("recipe_progress", 0);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if (new Object() {
                        public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getDouble(tag);
                            return -1;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") >= 100) {
                        if ((new Object() {
                            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                BlockEntity _ent = world.getBlockEntity(pos);
                                if (_ent != null)
                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                return _retval.get();
                            }
                        }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.AMMONIA.get() || (new Object() {
                            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                BlockEntity _ent = world.getBlockEntity(pos);
                                if (_ent != null)
                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                return _retval.get();
                            }
                        }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Blocks.AIR.asItem()) {
                            if (!(new Object() {
                                public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                                    BlockEntity blockEntity = world.getBlockEntity(pos);
                                    if (blockEntity != null)
                                        return blockEntity.getTileData().getBoolean(tag);
                                    return false;
                                }
                            }.getValue(world, new BlockPos(x, y, z), "Full"))) {
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 0;
                                        final int _amount = 1;
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable) {
                                                ItemStack _stk = capability.getStackInSlot(_slotid).copy();
                                                _stk.shrink(_amount);
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _stk);
                                            }
                                        });
                                    }
                                }
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 1;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.AMMONIA.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 1));
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable)
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
                                        });
                                    }
                                }
                                if (!world.isClientSide()) {
                                    BlockPos _bp = new BlockPos(x, y, z);
                                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                    BlockState _bs = world.getBlockState(_bp);
                                    if (_blockEntity != null)
                                        _blockEntity.getTileData().putDouble("recipe_progress", 0);
                                    if (world instanceof Level _level)
                                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                                }
                            }
                        }
                    }
                }
                if ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.IMPURE_AMMONIA.get()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.IMPURE_AMMONIA.get()) {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", true);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", false);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if ((new Object() {
                        public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getBoolean(tag);
                            return false;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "recipe_on"))) {
                        if (new Object() {
                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "recipe_progress") < 100) {
                            if (!world.isClientSide()) {
                                BlockPos _bp = new BlockPos(x, y, z);
                                BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                BlockState _bs = world.getBlockState(_bp);
                                if (_blockEntity != null)
                                    _blockEntity.getTileData().putDouble("recipe_progress", (new Object() {
                                        public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                            BlockEntity blockEntity = world.getBlockEntity(pos);
                                            if (blockEntity != null)
                                                return blockEntity.getTileData().getDouble(tag);
                                            return -1;
                                        }
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 2.5));
                                if (world instanceof Level _level)
                                    _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                            }
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putDouble("recipe_progress", 0);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.AMMONIA_PARTICLE.get() || (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Blocks.AIR.asItem()) {
                        if (new Object() {
                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "recipe_progress") >= 100) {
                            if (!(new Object() {
                                public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                                    BlockEntity blockEntity = world.getBlockEntity(pos);
                                    if (blockEntity != null)
                                        return blockEntity.getTileData().getBoolean(tag);
                                    return false;
                                }
                            }.getValue(world, new BlockPos(x, y, z), "Full"))) {
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 0;
                                        final int _amount = 1;
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable) {
                                                ItemStack _stk = capability.getStackInSlot(_slotid).copy();
                                                _stk.shrink(_amount);
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _stk);
                                            }
                                        });
                                    }
                                }
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 1;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.AMMONIA_PARTICLE.get());
                                        _setstack.setCount(4 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 1));
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable)
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
                                        });
                                    }
                                }
                                if (!world.isClientSide()) {
                                    BlockPos _bp = new BlockPos(x, y, z);
                                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                    BlockState _bs = world.getBlockState(_bp);
                                    if (_blockEntity != null)
                                        _blockEntity.getTileData().putDouble("recipe_progress", 0);
                                    if (world instanceof Level _level)
                                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                                }
                            }
                        }
                    }
                }
                if ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")) || (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe"))) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")) || (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe"))) {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", true);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putBoolean("recipe_on", false);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if ((new Object() {
                        public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity != null)
                                return blockEntity.getTileData().getBoolean(tag);
                            return false;
                        }
                    }.getValue(world, new BlockPos(x, y, z), "recipe_on"))) {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putDouble("recipe_progress", (new Object() {
                                    public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                        BlockEntity blockEntity = world.getBlockEntity(pos);
                                        if (blockEntity != null)
                                            return blockEntity.getTileData().getDouble(tag);
                                        return -1;
                                    }
                                }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 2.5));
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    } else {
                        if (!world.isClientSide()) {
                            BlockPos _bp = new BlockPos(x, y, z);
                            BlockEntity _blockEntity = world.getBlockEntity(_bp);
                            BlockState _bs = world.getBlockState(_bp);
                            if (_blockEntity != null)
                                _blockEntity.getTileData().putDouble("recipe_progress", 0);
                            if (world instanceof Level _level)
                                _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                        }
                    }
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.CATALYZEDDNA.get() || (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Blocks.AIR.asItem()) {
                        if (new Object() {
                            public double getValue(LevelAccessor world, BlockPos pos, String tag) {
                                BlockEntity blockEntity = world.getBlockEntity(pos);
                                if (blockEntity != null)
                                    return blockEntity.getTileData().getDouble(tag);
                                return -1;
                            }
                        }.getValue(world, new BlockPos(x, y, z), "recipe_progress") >= 100) {
                            if (!(new Object() {
                                public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                                    BlockEntity blockEntity = world.getBlockEntity(pos);
                                    if (blockEntity != null)
                                        return blockEntity.getTileData().getBoolean(tag);
                                    return false;
                                }
                            }.getValue(world, new BlockPos(x, y, z), "Full"))) {
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 0;
                                        final int _amount = 1;
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable) {
                                                ItemStack _stk = capability.getStackInSlot(_slotid).copy();
                                                _stk.shrink(_amount);
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _stk);
                                            }
                                        });
                                    }
                                }
                                {
                                    BlockEntity _ent = world.getBlockEntity(new BlockPos(x, y, z));
                                    if (_ent != null) {
                                        final int _slotid = 1;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.CATALYZEDDNA.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 1));
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                                            if (capability instanceof IItemHandlerModifiable)
                                                ((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
                                        });
                                    }
                                }
                                if (!world.isClientSide()) {
                                    BlockPos _bp = new BlockPos(x, y, z);
                                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                                    BlockState _bs = world.getBlockState(_bp);
                                    if (_blockEntity != null)
                                        _blockEntity.getTileData().putDouble("recipe_progress", 0);
                                    if (world instanceof Level _level)
                                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
