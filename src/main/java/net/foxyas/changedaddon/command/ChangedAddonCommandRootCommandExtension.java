package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.foxyas.changedaddon.block.advanced.TimedKeypad;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variants.ExtraVariantStats;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
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
		event.getDispatcher().register(
				Commands.literal("changed-addon").then(Commands.literal("TransfurColors").requires(cs -> cs.getEntity() instanceof Player player
								&& ExtraVariantStats.PlayerHasTransfurWithExtraColors(player)
								&& cs.hasPermission(0))
								.then(Commands.literal("setColor")
										.then(Commands.argument("color", IntegerArgumentType.integer())
												.then(Commands.argument("layer", IntegerArgumentType.integer(0, 2)) // Supondo máx 3 layers
														.executes(context -> {
															Player player = context.getSource().getPlayerOrException();
															int color = IntegerArgumentType.getInteger(context, "color");
															int layer = IntegerArgumentType.getInteger(context, "layer");

															if (ExtraVariantStats.PlayerHasTransfurWithExtraColors(player)) {
																TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
																if (transfur != null && transfur.getChangedEntity() instanceof AvaliEntity avaliEntity) {
																	avaliEntity.setColor(layer, Color3.fromInt(color));
																	context.getSource().sendSuccess(new TextComponent("Set color for layer " + layer), true);
																	return 1;
																}
															}

															context.getSource().sendFailure(new TextComponent("Failed to set color."));
															return 0;
														}))))
								.then(Commands.literal("setStyle")
										.then(Commands.argument("style", StringArgumentType.word()).suggests(((commandContext, suggestionsBuilder) -> {
											if (commandContext.getSource().getEntity() instanceof Player player){
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

													if (ExtraVariantStats.PlayerHasTransfurWithExtraColors(player)) {
														TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
														if (transfur != null && transfur.getChangedEntity() instanceof AvaliEntity avaliEntity) {
															avaliEntity.setStyleOfColor(style);
															context.getSource().sendSuccess(new TextComponent("Set style to " + style), true);
															return 1;
														}
													}

													context.getSource().sendFailure(new TextComponent("Failed to set style."));
													return 0;
												})))
						)
		);

		event.getDispatcher().register(Commands.literal("setTimerInKeypad")
                .then(Commands.argument("timer", IntegerArgumentType.integer(0, 9999))
                        .requires(cs -> cs.hasPermission(0)) // Minimum permission
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
