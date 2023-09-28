
package net.foxyas.changedaddon.command;

import org.checkerframework.checker.units.qual.s;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.common.util.FakePlayerFactory;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Direction;
import net.minecraft.commands.Commands;

import net.foxyas.changedaddon.procedures.SetmaxTransfurToleranceProcedure;
import net.foxyas.changedaddon.procedures.SetTransfurProgressCommandProcedure;
import net.foxyas.changedaddon.procedures.SetDefaultValueProcedure;
import net.foxyas.changedaddon.procedures.GetmaxTransfurToleranceProcedure;
import net.foxyas.changedaddon.procedures.AddTransfurProgressCommandProcedure;

import com.mojang.brigadier.arguments.DoubleArgumentType;

@Mod.EventBusSubscriber
public class ChangedAddonAdminCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("changed_addon_admin").requires(s -> s.hasPermission(2))
				.then(Commands.literal("SetTransfurProgress").then(Commands.argument("Number", DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					AddTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("set").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				})))).then(Commands.literal("SetMaxTransfurTolerance").then(Commands.argument("MaxNumber", DoubleArgumentType.doubleArg(0.1)).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetmaxTransfurToleranceProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("Default").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					SetDefaultValueProcedure.execute(entity);
					return 0;
				}))).then(Commands.literal("GetMaxTransfurTolerance").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					double x = arguments.getSource().getPosition().x();
					double y = arguments.getSource().getPosition().y();
					double z = arguments.getSource().getPosition().z();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);
					Direction direction = entity.getDirection();

					GetmaxTransfurToleranceProcedure.execute(entity);
					return 0;
				})));
	}
}
