package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.recipes.RecipesHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

public class UnifuserUpdateTickProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
        ItemStack a = ItemStack.EMPTY;
        ItemStack b = ItemStack.EMPTY;
        ItemStack Slot0 = ItemStack.EMPTY;
        ItemStack Slot1 = ItemStack.EMPTY;
        ItemStack Slot2 = ItemStack.EMPTY;
        ItemStack output = ItemStack.EMPTY;
        BlockState block = Blocks.AIR.defaultBlockState();
        if ((new Object() {
            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                return _retval.get();
            }
        }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == Blocks.AIR.asItem() && (new Object() {
            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                return _retval.get();
            }
        }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Blocks.AIR.asItem() && (new Object() {
            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                return _retval.get();
            }
        }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == Blocks.AIR.asItem()) {
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
        }.getAmount(world, new BlockPos(x, y, z), 3) < ((new Object() {
            public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                BlockEntity _ent = world.getBlockEntity(pos);
                if (_ent != null)
                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                return _retval.get();
            }
        }.getItemStack(world, new BlockPos(x, y, z), 3))).getMaxStackSize()) {
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
                Slot1 = (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 1));
                Slot2 = (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2));
                output = RecipesHandle.getUnifuserRecipeOutputOrDefault(world, Slot0, Slot1, Slot2);
                if ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Items.DIAMOND && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == Blocks.TINTED_GLASS.asItem()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Items.DIAMOND && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == Blocks.TINTED_GLASS.asItem()) {
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
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 1));
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.LUNAR_ROSE_HELMET.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.LUNAR_ROSE_HELMET.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + RecipesHandle.getUnifuserRecipeProgressSpeed(world, block, Slot0, Slot1, Slot2)));
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == output.getItem() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 2;
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = output;
                                        _setstack.setCount((output).getCount() + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
            } else {
                if ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.LAETHIN.get() && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == Blocks.AIR.asItem()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.LAETHIN.get() && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == Blocks.AIR.asItem()) {
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get());
                                        _setstack.setCount(3 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.AMMONIA.get() && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.ANTI_LATEX_BASE.get()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.AMMONIA.get() && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.ANTI_LATEX_BASE.get()) {
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.LITIX_CAMONIA.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.LAETHIN.get() && ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe")) || (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")))) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == ChangedAddonItems.LAETHIN.get() && ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe")) || (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")))) {
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
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 0.25));
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 2;
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.SYRINGE_WITH_LITIX_CAMMONIA.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
                }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Items.POTION && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ChangedAddonItems.CATALYZEDDNA.get()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Items.POTION && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LITIX_CAMONIA.get() && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ChangedAddonItems.CATALYZEDDNA.get()) {
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
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 0.3));
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.POTWITHCAMONIA.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 2;
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.POTWITHCAMONIA.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
                }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Items.DIAMOND && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == Blocks.TINTED_GLASS.asItem()) {
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).getItem() == Items.DIAMOND && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == Blocks.TINTED_GLASS.asItem()) {
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
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 1));
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.LUNAR_ROSE_HELMET.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = new ItemStack(ChangedAddonItems.LUNAR_ROSE_HELMET.get());
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
                }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LAETHIN.get() && (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 1)).is(ItemTags.create(new ResourceLocation("changed_addon:gooey"))) && ((new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe")) || (new Object() {
                    public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                        AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                        BlockEntity _ent = world.getBlockEntity(pos);
                        if (_ent != null)
                            _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                        return _retval.get();
                    }
                }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")))) {
                    a = (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0));
                    if ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 0)).getItem() == ChangedAddonItems.LAETHIN.get() && (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 1)).is(ItemTags.create(new ResourceLocation("changed_addon:gooey"))) && ((new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:blood_syringe")) || (new Object() {
                        public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                            AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                            BlockEntity _ent = world.getBlockEntity(pos);
                            if (_ent != null)
                                _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                            return _retval.get();
                        }
                    }.getItemStack(world, new BlockPos(x, y, z), 2)).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("changed:latex_syringe")))) {
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
                                    }.getValue(world, new BlockPos(x, y, z), "recipe_progress") + 0.25));
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
                        b = new ItemStack(ChangedAddonItems.LAETHIN_SYRINGE.get());
                        {
                            CompoundTag _nbtTag = a.getTag();
                            if (_nbtTag != null)
                                b.setTag(_nbtTag.copy());
                        }
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
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == ChangedAddonItems.LAETHIN_SYRINGE.get() || (new Object() {
                                public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
                                    AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
                                    BlockEntity _ent = world.getBlockEntity(pos);
                                    if (_ent != null)
                                        _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
                                    return _retval.get();
                                }
                            }.getItemStack(world, new BlockPos(x, y, z), 3)).getItem() == Blocks.AIR.asItem()) {
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
                                        final int _slotid = 2;
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
                                        final int _slotid = 3;
                                        final ItemStack _setstack = b;
                                        _setstack.setCount(1 + new Object() {
                                            public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
                                                AtomicInteger _retval = new AtomicInteger(0);
                                                BlockEntity _ent = world.getBlockEntity(pos);
                                                if (_ent != null)
                                                    _ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
                                                return _retval.get();
                                            }
                                        }.getAmount(world, new BlockPos(x, y, z), 3));
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
