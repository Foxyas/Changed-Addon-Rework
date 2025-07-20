package net.foxyas.changedaddon.procedures;

import io.netty.buffer.Unpooled;
import net.foxyas.changedaddon.world.inventory.FoxyasGuiMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class FoxyasRightClickedOnEntityProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
        if (entity == null || sourceentity == null)
            return;
        {
            if (sourceentity instanceof ServerPlayer _ent) {
                BlockPos _bpos = new BlockPos(x, y, z);
                NetworkHooks.openGui(_ent, new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return new TextComponent("Foxyasgui");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
                        return new FoxyasGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                    }
                }, _bpos);
            }
        }
        if (entity instanceof Mob _entity)
            _entity.getNavigation().moveTo((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), 1);
    }
}
