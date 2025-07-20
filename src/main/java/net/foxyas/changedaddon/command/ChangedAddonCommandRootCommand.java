
package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.foxyas.changedaddon.procedures.*;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonCommandRootCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("changed-addon")

				.then(Commands.literal("toggle_reset_transfur_advancement").then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					InforesettransfuradvancementProcedure.execute(entity);
					return 0;
				})).then(Commands.argument("turn", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					ToggleresettransfuradvancementsProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("toggle_addon_warns").then(Commands.argument("warns", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					TooglewarnsProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					InfoaddonwarnsProcedure.execute(entity);
					return 0;
				}))).then(Commands.literal("Size_Manipulator").then(Commands.argument("size", DoubleArgumentType.doubleArg()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SizeManipulatorProcedure.execute(arguments, entity);
					return 0;
				}))));
	}
}
