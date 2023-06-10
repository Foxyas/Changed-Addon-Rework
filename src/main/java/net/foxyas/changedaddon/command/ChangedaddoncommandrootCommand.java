
package net.foxyas.changedaddon.command;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

import net.foxyas.changedaddon.procedures.TooglewarnsProcedure;
import net.foxyas.changedaddon.procedures.ToggleresettransfuradvancementsProcedure;
import net.foxyas.changedaddon.procedures.TogglehumanaddonguiProcedure;
import net.foxyas.changedaddon.procedures.TogglealladdonguiProcedure;
import net.foxyas.changedaddon.procedures.ToggleaddonguiprocedureProcedure;
import net.foxyas.changedaddon.procedures.ToggleOrganicOverlayProcedure;
import net.foxyas.changedaddon.procedures.InforesettransfuradvancementProcedure;
import net.foxyas.changedaddon.procedures.InfoonlytransfuraddonguiProcedure;
import net.foxyas.changedaddon.procedures.InfoonlyhumanaddonguiProcedure;
import net.foxyas.changedaddon.procedures.InfoaddonwarnsProcedure;
import net.foxyas.changedaddon.procedures.InfoaddonguiProcedure;

import com.mojang.brigadier.arguments.BoolArgumentType;

@Mod.EventBusSubscriber
public class ChangedaddoncommandrootCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("changed_addon")

				.then(Commands.literal("toggle_reset_transfur_advancement").then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					InforesettransfuradvancementProcedure.execute(entity);
					return 0;
				})).then(Commands.argument("turn", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					ToggleresettransfuradvancementsProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("toggle_addon_warns").then(Commands.argument("warns", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					TooglewarnsProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					InfoaddonwarnsProcedure.execute(entity);
					return 0;
				}))).then(Commands.literal("toggle_addon_gui").then(Commands.literal("transfur_gui").then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					InfoonlytransfuraddonguiProcedure.execute(entity);
					return 0;
				})).then(Commands.literal("organic_overlay").then(Commands.argument("turn", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					ToggleOrganicOverlayProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.argument("turn", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					ToggleaddonguiprocedureProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("human_gui").then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					InfoonlyhumanaddonguiProcedure.execute(entity);
					return 0;
				})).then(Commands.argument("turn", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					TogglehumanaddonguiProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("all_gui").then(Commands.literal("info").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					InfoaddonguiProcedure.execute(entity);
					return 0;
				})).then(Commands.argument("turn", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					TogglealladdonguiProcedure.execute(arguments, entity);
					return 0;
				})))));
	}
}
