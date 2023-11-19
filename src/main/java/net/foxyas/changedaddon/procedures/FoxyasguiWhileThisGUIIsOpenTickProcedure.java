package net.foxyas.changedaddon.procedures;

import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.client.gui.components.EditBox;

import net.foxyas.changedaddon.world.inventory.FoxyasGui2Menu;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.HashMap;

import io.netty.buffer.Unpooled;

public class FoxyasguiWhileThisGUIIsOpenTickProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, HashMap guistate) {
		if (entity == null || guistate == null)
			return;
		if ((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).transfur == false) {
			if (world.isClientSide()) {
				if ((guistate.containsKey("text:Deals") ? ((EditBox) guistate.get("text:Deals")).getValue() : "").equals("i want be transfured by you")) {
					{
						if (entity instanceof ServerPlayer _ent) {
							BlockPos _bpos = new BlockPos(x, y, z);
							NetworkHooks.openGui((ServerPlayer) _ent, new MenuProvider() {
								@Override
								public Component getDisplayName() {
									return new TextComponent("FoxyasGui2");
								}

								@Override
								public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
									return new FoxyasGui2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
								}
							}, _bpos);
						}
					}
				}
			}
		}
	}
}
