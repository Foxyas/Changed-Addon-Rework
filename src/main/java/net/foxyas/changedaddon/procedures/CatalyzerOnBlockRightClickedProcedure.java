package net.foxyas.changedaddon.procedures;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.world.inventory.CatalyzerGuiMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class CatalyzerOnBlockRightClickedProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, BlockState blockstate, Entity entity) {
        if (entity == null)
            return;
        if (entity.isShiftKeyDown()) {
            if ((new Object() {
                public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
                    BlockEntity blockEntity = world.getBlockEntity(pos);
                    if (blockEntity != null)
                        return blockEntity.getTileData().getBoolean(tag);
                    return false;
                }
            }.getValue(world, new BlockPos(x, y, z), "start_recipe"))) {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos(x, y, z);
                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_blockEntity != null)
                        _blockEntity.getTileData().putBoolean("start_recipe", false);
                    if (world instanceof Level _level)
                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(new TextComponent(("you stop the " + new TranslatableComponent(("block." + (ForgeRegistries.BLOCKS.getKey(blockstate.getBlock()).toString()).replace(":", "."))).getString())), true);
            } else {
                if (!world.isClientSide()) {
                    BlockPos _bp = new BlockPos(x, y, z);
                    BlockEntity _blockEntity = world.getBlockEntity(_bp);
                    BlockState _bs = world.getBlockState(_bp);
                    if (_blockEntity != null)
                        _blockEntity.getTileData().putBoolean("start_recipe", true);
                    if (world instanceof Level _level)
                        _level.sendBlockUpdated(_bp, _bs, _bs, 3);
                }
                if (entity instanceof Player _player && !_player.level.isClientSide())
                    _player.displayClientMessage(new TextComponent(("you start the " + new TranslatableComponent(("block." + (ForgeRegistries.BLOCKS.getKey(blockstate.getBlock()).toString()).replace(":", "."))).getString())), true);
            }
        } else {
            {
                if (entity instanceof ServerPlayer _ent) {
                    BlockPos _bpos = new BlockPos(x, y, z);
                    NetworkHooks.openGui(_ent, new MenuProvider() {
                        @Override
                        public @NotNull Component getDisplayName() {
                            return new TextComponent("CatalyzerGui");
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
                            return new CatalyzerGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                        }
                    }, _bpos);
                }
            }
        }
    }
}
