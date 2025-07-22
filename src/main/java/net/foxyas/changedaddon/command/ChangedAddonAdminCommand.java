
package net.foxyas.changedaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonAdminCommand {

	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("changed-addon-admin")
				.requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.literal("allow_boss_transfur")
						.then(Commands.literal("Ket_Exp_009")
								.then(Commands.literal("get")
										.then(Commands.argument("player", EntityArgument.player())
												.executes(arguments -> {
													Player target = EntityArgument.getPlayer(arguments, "player");

													ChangedAddonModVariables.PlayerVariables vars = target.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
													if(vars == null) return 0;

													arguments.getSource().sendSuccess(new TextComponent(target.getDisplayName().getString() + (vars.Exp009TransfurAllowed ? " has Exp009Transfur permission" : " has no Exp009Transfur permission")), false);
													return Command.SINGLE_SUCCESS;
												})
										)
								)
								.then(Commands.literal("set")
										.then(Commands.argument("target", EntityArgument.player())
												.then(Commands.argument("set", BoolArgumentType.bool())
														.executes(arguments -> {
																Player target = EntityArgument.getPlayer(arguments, "target");
																boolean val = BoolArgumentType.getBool(arguments, "set");

																arguments.getSource().sendSuccess(new TextComponent(("The Exp009Transfur Perm of the " + target.getDisplayName().getString() + " was set to " + val)), true);

																target.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
																	capability.Exp009TransfurAllowed = val;
																	capability.syncPlayerVariables(target);
																});

																return Command.SINGLE_SUCCESS;
														})
												)
										)
								)
						)
						.then(Commands.literal("Exp_10")
								.then(Commands.literal("get")
										.then(Commands.argument("player", EntityArgument.player())
												.executes(arguments -> {
														Player target = EntityArgument.getPlayer(arguments, "player");

														ChangedAddonModVariables.PlayerVariables vars = target.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
														if(vars == null) return 0;

														arguments.getSource().sendSuccess(new TextComponent(target.getDisplayName().getString() + (vars.Exp10TransfurAllowed ? " has Exp10Transfur permission" : " has no Exp10Transfur permission")), false);
														return Command.SINGLE_SUCCESS;
												})
										)
								)
								.then(Commands.literal("set")
										.then(Commands.argument("target", EntityArgument.player())
												.then(Commands.argument("set", BoolArgumentType.bool())
														.executes(arguments -> {
																Player target = EntityArgument.getPlayer(arguments, "target");
																boolean val = BoolArgumentType.getBool(arguments, "set");

																arguments.getSource().sendSuccess(new TextComponent(("The Exp10Transfur Perm of the " + target.getDisplayName().getString() + " was set to " + val)), true);

																target.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
																	capability.Exp10TransfurAllowed = val;
																	capability.syncPlayerVariables(target);
																});

																return Command.SINGLE_SUCCESS;
														})
												)
										)
								)
						)
				)
				.then(Commands.literal("SetTransfurProgress")//Add/set progress self
						.requires(stack -> stack.getEntity() instanceof Player)
						.then(Commands.argument("Number", DoubleArgumentType.doubleArg())
								.then(Commands.literal("add")
										.executes(arguments ->
												setTFProgress(arguments.getSource().getPlayerOrException(), FloatArgumentType.getFloat(arguments, "Number"), true))
								)
								.then(Commands.literal("set")
										.executes(arguments ->
												setTFProgress(arguments.getSource().getPlayerOrException(), FloatArgumentType.getFloat(arguments, "Number"), false))
								)
						)
				)
				.then(Commands.literal("SetPlayerTransfurProgress")//Add/set progress other
						.then(Commands.argument("Target", EntityArgument.player())
								.then(Commands.argument("Number", FloatArgumentType.floatArg())
										.then(Commands.literal("add")
												.executes(arguments ->
														setTFProgress(EntityArgument.getPlayer(arguments, "Target"), FloatArgumentType.getFloat(arguments, "Number"), true))
										)
										.then(Commands.literal("set")
												.executes(arguments ->
														setTFProgress(EntityArgument.getPlayer(arguments, "Target"), FloatArgumentType.getFloat(arguments, "Number"), false))
										)
								)
						)
				)
				.then(Commands.literal("SetMaxTransfurTolerance")//Set tf tolerance other
						.then(Commands.argument("target", EntityArgument.player())
								.then(Commands.argument("Number", FloatArgumentType.floatArg(.1f))
										.executes(arguments ->
												setTFTolerance(EntityArgument.getPlayer(arguments, "target"), FloatArgumentType.getFloat(arguments, "Number")))
								)
								.then(Commands.literal("Default")
										.executes(arguments ->
												setTFTolerance(EntityArgument.getPlayer(arguments, "target"), 0))
								)
						)
				)
				.then(Commands.literal("GetMaxTransfurTolerance")//Get tf tolerance self
						.requires(stack -> stack.getEntity() instanceof Player)
						.executes(arguments -> {
								ServerPlayer player = arguments.getSource().getPlayerOrException();
																									// !!! this method includes modifiers like armor, items etc
								player.displayClientMessage(new TextComponent("The maximum Transfur Tolerance is §6" + ProcessTransfur.getEntityTransfurTolerance(player)), false);
								return Command.SINGLE_SUCCESS;
						})
				)
				.then(Commands.literal("setBlocksInfectionType")
						.then(Commands.argument("minPos", BlockPosArgument.blockPos())
								.then(Commands.argument("maxPos", BlockPosArgument.blockPos())
										.then(Commands.literal("white_latex")
												.executes(ctx -> setBlockInfection(ctx, LatexType.WHITE_LATEX))
										).then(Commands.literal("dark_latex")
												.executes(ctx -> setBlockInfection(ctx, LatexType.DARK_LATEX))
										).then(Commands.literal("neutral")
												.executes(ctx -> setBlockInfection(ctx, LatexType.NEUTRAL))
										)
								)
						)
				)
		);
	}

	private static int setTFProgress(Player player, float amount, boolean add){
		if(add) amount += ProcessTransfur.getPlayerTransfurProgress(player);

		ProcessTransfur.setPlayerTransfurProgress(player, amount);

		return Command.SINGLE_SUCCESS;
	}

	private static int setTFTolerance(Player player, float amount){
		if(amount == 0) amount = (float) ChangedAttributes.TRANSFUR_TOLERANCE.get().getDefaultValue();

		player.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get()).setBaseValue(amount);
		player.displayClientMessage(new TextComponent("Transfur Tolerance has been set to §6<" + amount + ">"), false);
        ChangedAddonMod.LOGGER.info("Transfur Tolerance of {} has been set to {}", player.getDisplayName().getString(), amount);

		return Command.SINGLE_SUCCESS;
	}

	private static int setBlockInfection(CommandContext<CommandSourceStack> ctx, LatexType enumValue){
		CommandSourceStack source = ctx.getSource();
		ServerLevel world = source.getLevel();

		BlockPos minPos;
		BlockPos maxPos;
		try {
			minPos = BlockPosArgument.getLoadedBlockPos(ctx, "minPos");
			maxPos = BlockPosArgument.getLoadedBlockPos(ctx, "maxPos");
		} catch (CommandSyntaxException e) {
			source.sendFailure(new TextComponent("One or both of the selected position are not loaded!"));
			return 0;
		}

		long value = BlockPos.betweenClosedStream(minPos, maxPos).count();

		if (value > Short.MAX_VALUE) {
			source.sendFailure(new TextComponent("Too many blocks selected: " + value + " > " + Short.MAX_VALUE));
			return 0;
		}

		for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
			BlockState state = world.getBlockState(pos);
			if (state.hasProperty(AbstractLatexBlock.COVERED)) {
				BlockState newState = state.setValue(AbstractLatexBlock.COVERED, enumValue);
				world.setBlock(pos, newState, 3);
			}
		}

		source.sendSuccess(new TextComponent("Set Infection of " + value + " blocks to " + enumValue.toString().toLowerCase().replace("_", " ")), true);
		return 1;
	}
}
