
package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.foxyas.changedaddon.procedures.*;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonAdminCommand {

	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("changed-addon-admin").requires(s -> s.hasPermission(2))
				.then(Commands.literal("allow_boss_transfur").then(Commands.literal("Ket_Exp_009").then(Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					GetBossPermProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("set").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("set", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SetBossPermProcedure.execute(arguments, entity);
					return 0;
				}))))).then(Commands.literal("Exp_10").then(Commands.literal("get").then(Commands.argument("player", EntityArgument.player()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					GetExp10BossPermProcedure.execute(arguments, entity);
					return 0;
				}))).then(Commands.literal("set").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("set", BoolArgumentType.bool()).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SetExp10BossPermProcedure.execute(arguments, entity);
					return 0;
				})))))).then(Commands.literal("SetTransfurProgress").then(Commands.argument("Number", DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					AddTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("set").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SetTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				})))).then(Commands.literal("SetPlayerTransfurProgress").then(Commands.argument("Target", EntityArgument.player()).then(Commands.argument("Number", DoubleArgumentType.doubleArg()).then(Commands.literal("add").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					AddPlayerTransfurProgressProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("set").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SetPlayerTransfurProgressCommandProcedure.execute(arguments, entity);
					return 0;
				}))))).then(Commands.literal("SetMaxTransfurTolerance").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("MaxNumber", DoubleArgumentType.doubleArg(0.1)).executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SetmaxTransfurToleranceProcedure.execute(arguments, entity);
					return 0;
				})).then(Commands.literal("Default").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					SetDefaultValueProcedure.execute(arguments, entity);
					return 0;
				})))).then(Commands.literal("GetMaxTransfurTolerance").executes(arguments -> {
					ServerLevel world = arguments.getSource().getLevel();
					Entity entity = arguments.getSource().getEntity();
					if (entity == null)
						entity = FakePlayerFactory.getMinecraft(world);

					GetmaxTransfurToleranceProcedure.execute(entity);
					return 0;
				})));
	}


}
