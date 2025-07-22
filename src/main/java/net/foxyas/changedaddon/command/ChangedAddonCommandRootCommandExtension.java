package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.foxyas.changedaddon.block.advanced.TimedKeypad;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.variants.IDynamicCoatColors;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonCommandRootCommandExtension {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        LiteralCommandNode<CommandSourceStack> TransfurColorsCommandNode = event.getDispatcher().register(
                Commands.literal("changed-addon")
                        .requires(stack -> stack.getEntity() instanceof Player)
                        .then(Commands.literal("TransfurColors")
                                .then(Commands.literal("setColor")
                                        .then(Commands.argument("colorOrHex", StringArgumentType.string())
                                                .then(Commands.argument("layer", IntegerArgumentType.integer(0, 2)) // Supondo máx 3 layers
                                                        .executes(context -> {
                                                            Player player = context.getSource().getPlayerOrException();
                                                            if (!IDynamicCoatColors.playerHasTransfurWithExtraColors(player)) {
                                                                throw new CommandRuntimeException(new TextComponent("You don't have any extra colors!"));
                                                            }
                                                            Color3 color3;
                                                            String StringColor = StringArgumentType.getString(context, "colorOrHex");
                                                            color3 = Color3.parseHex(StringColor);

                                                            if (color3 == null) {
                                                                try {
                                                                    color3 = Color3.fromInt(Integer.parseInt(StringColor));
                                                                } catch (NumberFormatException ignored) {}

                                                                if (color3 == null) {
                                                                    context.getSource().sendFailure(new TextComponent("Failed to parse color. Are you sure you are using the correct code?"));
                                                                    return 0;
                                                                }
                                                            }

                                                            int layer = IntegerArgumentType.getInteger(context, "layer");

                                                            if (IDynamicCoatColors.playerHasTransfurWithExtraColors(player)) {
                                                                TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
                                                                if (transfur != null && transfur.getChangedEntity() instanceof AvaliEntity avaliEntity) {
                                                                    avaliEntity.setColor(layer, color3);
                                                                    context.getSource().sendSuccess(new TextComponent("Set color for layer " + layer), true);
                                                                    return 1;
                                                                } else if (transfur != null && transfur.getChangedEntity() instanceof IDynamicCoatColors dynamicColors) {
                                                                    dynamicColors.setColor(layer, color3);
                                                                    context.getSource().sendSuccess(new TextComponent("Set color for layer " + layer), true);
                                                                    return 1;
                                                                }
                                                            }

                                                            context.getSource().sendFailure(new TextComponent("Failed to set color."));
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
                                                            avaliEntity.setStyleOfColor(style);
                                                            context.getSource().sendSuccess(new TextComponent("Set style to " + style), true);
                                                            return 1;
                                                        } else if (transfur != null && transfur.getChangedEntity() instanceof IDynamicCoatColors dynamicColor) {
                                                            dynamicColor.setStyleOfColor(style);
                                                            context.getSource().sendSuccess(new TextComponent("Set style to " + style), true);
                                                            return 1;
                                                        }
                                                    }

                                                    context.getSource().sendFailure(new TextComponent("Failed to set style."));
                                                    return 0;
                                                })))
                        ));

        event.getDispatcher().register(Commands.literal("setTransfurColor").redirect(TransfurColorsCommandNode.getChild("TransfurColors")));

        event.getDispatcher().register(Commands.literal("setTimerInKeypad")
                .then(Commands.argument("timer", IntegerArgumentType.integer(0, 9999))
                        .requires(cs -> cs.hasPermission(Commands.LEVEL_ALL)) // Minimum permission
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            if (!(source.getEntity() instanceof ServerPlayer player)) {
                                source.sendFailure(new TextComponent("This command can only be used by players."));
                                return 0;
                            }

                            ItemStack heldItem = player.getMainHandItem();
                            if (!(heldItem.getItem() instanceof BlockItem blockItem)) {
                                source.sendFailure(new TextComponent("You must be holding a block item."));
                                return 0;
                            }

                            Block block = blockItem.getBlock();
                            if (!(block instanceof TimedKeypad)) {
                                source.sendFailure(new TextComponent("The block must be a TimedKeypad."));
                                return 0;
                            }

                            int timerValue = IntegerArgumentType.getInteger(context, "timer");
                            CompoundTag tag = heldItem.getOrCreateTag();
                            tag.putInt("TimerValue", timerValue);

                            source.sendSuccess(new TextComponent("Timer set to " + timerValue + "."), true);
                            return 1;
                        })
                )
        );
    }
}
