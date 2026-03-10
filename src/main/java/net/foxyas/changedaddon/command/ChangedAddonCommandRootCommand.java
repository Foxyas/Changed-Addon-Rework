package net.foxyas.changedaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.advanced.TimedKeypadBlock;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.IDynamicCoatColors;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.BasicPlayerInfo;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.server.command.EnumArgument;

public class ChangedAddonCommandRootCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("changed-addon")
                .requires(stack -> stack.getEntity() instanceof Player)
                .then(Commands.literal("toggle_reset_transfur_advancement")
                        .then(Commands.literal("info")
                                .executes(arguments -> {
                                    Player player = arguments.getSource().getPlayerOrException();

                                    ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
                                    if (vars == null) return 0;

                                    player.displayClientMessage(Component.literal(("reset transfur progress is " + vars.resetTransfurAdvancements)), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.argument("turn", BoolArgumentType.bool())
                                .executes(arguments -> {
                                    Player player = arguments.getSource().getPlayerOrException();

                                    ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
                                    if (vars == null) return 0;

                                    boolean newVal = BoolArgumentType.getBool(arguments, "turn");

                                    if (newVal == vars.resetTransfurAdvancements) {
                                        player.displayClientMessage(Component.literal("§cNothing changed, it already had that value"), false);
                                        return Command.SINGLE_SUCCESS;
                                    }

                                    player.displayClientMessage(Component.literal("You " + (newVal ? "Activated" : "Disabled") + " the Transfur Reset Achievements"), false);

                                    vars.resetTransfurAdvancements = newVal;
                                    vars.syncPlayerVariables(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("toggle_addon_warns")
                        .requires(stack -> stack.getEntity() instanceof Player)
                        .then(Commands.argument("warns", BoolArgumentType.bool())
                                .executes(arguments -> {
                                    Player player = arguments.getSource().getPlayerOrException();

                                    ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
                                    if (vars == null) return 0;

                                    vars.showWarns = BoolArgumentType.getBool(arguments, "warns");
                                    vars.syncPlayerVariables(player);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(Commands.literal("info")
                                .executes(arguments -> {
                                    Player player = arguments.getSource().getPlayerOrException();

                                    ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
                                    if (vars == null) return 0;

                                    player.displayClientMessage(Component.literal("Warns is §4" + vars.showWarns), true);
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
                .then(Commands.literal("Size_Manipulator")
                        .then(Commands.argument("size", FloatArgumentType.floatArg())
                                .executes(arguments -> {
                                    Player player = (Player) arguments.getSource().getEntityOrException();

                                    float newSize = getSize(player, FloatArgumentType.getFloat(arguments, "size"), true);
                                    Changed.config.client.basicPlayerInfo.setSize(newSize); // Change Size
                                    ChangedAddonMod.LOGGER.info("Size changed to: {} for player: {}", newSize, player.getName().getString()); // Command Classic Log
                                    player.displayClientMessage(Component.literal("Size changed to: " + newSize), false); // Chat log for the player

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                )
        );

        dispatcher.register(Commands.literal("setTimerInKeypad")
                .then(Commands.argument("timer", IntegerArgumentType.integer(0, 9999))
                        .requires(cs -> cs.hasPermission(Commands.LEVEL_ALL)) // Minimum permission
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            if (!(source.getEntity() instanceof ServerPlayer player)) {
                                source.sendFailure(Component.literal("This command can only be used by players."));
                                return 0;
                            }

                            ItemStack heldItem = player.getMainHandItem();
                            if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
                                source.sendFailure(Component.literal("You must be holding a block item."));
                                return 0;
                            }

                            Block block = blockItem.getBlock();
                            if (!(block instanceof TimedKeypadBlock)) {
                                source.sendFailure(Component.literal("The block must be a TimedKeypad."));
                                return 0;
                            }

                            int timerValue = IntegerArgumentType.getInteger(context, "timer");
                            CompoundTag tag = heldItem.getOrCreateTag();
                            tag.putInt("TimerValue", timerValue);

                            source.sendSuccess(() -> Component.literal("Timer set to " + timerValue + "."), false);
                            return 1;
                        })
                )
        );

        LiteralCommandNode<CommandSourceStack> TransfurColorsCommandNode = dispatcher.register(
                Commands.literal("changed-addon")
                        .requires(stack -> stack.getEntity() instanceof Player)
                        .then(Commands.literal("TransfurColors")
                                .then(Commands.literal("setColor")
                                        .then(Commands.argument("colorOrHex", StringArgumentType.string())
                                                .then(Commands.argument("layer", IntegerArgumentType.integer(0, 2))
                                                        .executes(context -> {
                                                            Player player = context.getSource().getPlayerOrException();
                                                            if (!IDynamicCoatColors.playerHasTransfurWithExtraColors(player)) {
                                                                throw new CommandRuntimeException(Component.literal("You don't have any extra colors!"));
                                                            }
                                                            Color3 color3;
                                                            String StringColor = StringArgumentType.getString(context, "colorOrHex");
                                                            color3 = Color3.parseHex(StringColor);

                                                            if (color3 == null) {
                                                                try {
                                                                    color3 = Color3.fromInt(Integer.parseInt(StringColor));
                                                                } catch (NumberFormatException ignored) {
                                                                }

                                                                if (color3 == null) {
                                                                    context.getSource().sendFailure(Component.literal("Failed to parse color. Are you sure you are using the correct code?"));
                                                                    return 0;
                                                                }
                                                            }
                                                            //String layerString = StringArgumentType.getString(context, "layer");
                                                            int layer = IntegerArgumentType.getInteger(context, "layer");

                                                            if (IDynamicCoatColors.playerHasTransfurWithExtraColors(player)) {
                                                                TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
                                                                if (transfur != null && transfur.getChangedEntity() instanceof AvaliEntity avaliEntity) {
                                                                    avaliEntity.setColor(layer, color3);
                                                                    context.getSource().sendSuccess(() -> Component.literal("Set color for layer " + layer), false);
                                                                    return 1;
                                                                } else if (transfur != null && transfur.getChangedEntity() instanceof IDynamicCoatColors dynamicColors) {
                                                                    dynamicColors.setColor(layer, color3);
                                                                    context.getSource().sendSuccess(() -> Component.literal("Set color for layer " + layer), false);
                                                                    return 1;
                                                                }
                                                            }

                                                            context.getSource().sendFailure(Component.literal("Failed to set color."));
                                                            return 0;
                                                        }))))
                                .then(Commands.literal("setStyle")
                                        .then(Commands.argument("style", StringArgumentType.word()).suggests(((commandContext, suggestionsBuilder) -> {
                                                    if (commandContext.getSource().getEntity() instanceof Player player) {
                                                        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
                                                        if (transfurVariant != null && transfurVariant.getChangedEntity() instanceof AvaliEntity avaliEntity) {
                                                            avaliEntity.StyleTypes.forEach(suggestionsBuilder::suggest);
                                                        }
                                                    }
                                                    return suggestionsBuilder.buildFuture();
                                                }))
                                                .executes(context -> {
                                                    Player player = context.getSource().getPlayerOrException();
                                                    String style = StringArgumentType.getString(context, "style");

                                                    if (IDynamicCoatColors.playerHasTransfurWithExtraColors(player)) {
                                                        TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
                                                        if (transfur != null && transfur.getChangedEntity() instanceof AvaliEntity avaliEntity) {
                                                            AvaliEntity.StyleType styleType = AvaliEntity.StyleType.valueOf(style.toUpperCase());
                                                            avaliEntity.setStyleOfColor(styleType);
                                                            context.getSource().sendSuccess(() -> Component.literal("Set style to " + style), false);
                                                            return 1;
                                                        } else if (transfur != null && transfur.getChangedEntity() instanceof IDynamicCoatColors dynamicColor) {
                                                            dynamicColor.setStyleOfColor(style);
                                                            context.getSource().sendSuccess(() -> Component.literal("Set style to " + style), false);
                                                            return 1;
                                                        }
                                                    }

                                                    context.getSource().sendFailure(Component.literal("Failed to set style."));
                                                    return 0;
                                                })))
                        ));

        dispatcher.register(Commands.literal("setTransfurColor").redirect(TransfurColorsCommandNode.getChild("TransfurColors")));
    }

    private static float getSize(Player player, float size, boolean overrideSize) {
        float MINIMUM_SIZE_TOLERANCE = BasicPlayerInfo.getSizeMinimum(player);
        float MAX_SIZE_TOLERANCE = BasicPlayerInfo.getSizeMaximum(player);
        if (size < MINIMUM_SIZE_TOLERANCE) {
            ChangedAddonMod.LOGGER.atWarn().log("Size value is too low: {}, The Size Value will probably to be auto set to 0.95", size); // Too Low Warn
        } else if (size > MAX_SIZE_TOLERANCE) {
            ChangedAddonMod.LOGGER.atWarn().log("Size value is too high: {}, The Size Value will probably to be auto set to 1.05", size); // Too High Warn
        }
        return overrideSize ? Mth.clamp(size, MINIMUM_SIZE_TOLERANCE, MAX_SIZE_TOLERANCE) : size;
    }
}
