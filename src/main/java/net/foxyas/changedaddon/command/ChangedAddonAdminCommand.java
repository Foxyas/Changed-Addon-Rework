package net.foxyas.changedaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.event.UntransfurEvent;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.qte.FightToKeepConsciousness.MinigameType;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.command.CommandTransfur;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.latex.SpreadingLatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.world.LatexCoverState;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.server.command.EnumArgument;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ChangedAddonAdminCommand {

    private static final int MAX_OUTPUT = 20; // max lines to show

    public static final LiteralArgumentBuilder<CommandSourceStack> COMMAND_ROOT = Commands.literal("changed-addon-admin")
            .requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        ArgumentBuilder<CommandSourceStack, ?> untfImmunity = Commands.argument("target", EntityArgument.players())
                .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(context -> untfImmunity(context.getSource(), EntityArgument.getPlayers(context, "target"), BoolArgumentType.getBool(context, "value"), UntransfurEvent.UntransfurType.SURVIVAL))
                        .then(Commands.argument("type", EnumArgument.enumArgument(UntransfurEvent.UntransfurType.class))
                                .executes(context -> untfImmunity(context.getSource(), EntityArgument.getPlayers(context, "target"), BoolArgumentType.getBool(context, "value"), context.getArgument("type", UntransfurEvent.UntransfurType.class)))
                        )
                );


        ArgumentBuilder<CommandSourceStack, ?> getFightToKeepConscience = Commands.argument("target", EntityArgument.player())
                .executes(context -> {
                    ServerPlayer target = EntityArgument.getPlayer(context, "target");
                    ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.of(target);
                    if (!ProcessTransfur.isPlayerTransfurred(target) || playerVariables == null || playerVariables.FTKCminigameType == null) {
                        context.getSource().sendFailure(Component.translatable("commands.changed_addon.ftkMinigame.get.hasnt"));
                        return 0;
                    }
                    MinigameType ftkCminigameType = playerVariables.FTKCminigameType;
                    context.getSource().sendSuccess(() -> Component.translatable("commands.changed_addon.ftkMinigame.get.has", ftkCminigameType), false);
                    return 1;
                });

        ArgumentBuilder<CommandSourceStack, ?> setFightToKeepConscience = Commands.argument("targets", EntityArgument.players())
                .then(Commands.argument("type", EnumArgument.enumArgument(MinigameType.class))
                        .executes(context -> {
                            Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                            MinigameType minigameType = context.getArgument("type", MinigameType.class);
                            boolean single = targets.size() < 2;

                            int resultInt = 0;

                            for (ServerPlayer target : targets) {
                                if (!ProcessTransfur.isPlayerTransfurred(target)) {
                                    continue;
                                }

                                ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.of(target);
                                if (playerVariables == null) {
                                    continue;
                                }

                                playerVariables.FTKCminigameType = minigameType;
                                playerVariables.syncPlayerVariables(target);
                                resultInt++;
                            }

                            if (resultInt > 0) {
                                int finalResultInt = resultInt;
                                context.getSource().sendSuccess(() -> Component.translatable("commands.changed_addon.ftkMinigame.set.success", finalResultInt), true);
                            } else {
                                context.getSource().sendFailure(Component.translatable("commands.changed_addon.ftkMinigame.set.fail"));
                            }

                            return resultInt;
                        })
                )
                .then(Commands.literal("reset").executes(context -> {
                    Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, "targets");
                    boolean single = targets.size() < 2;
                    int resultInt = 0;
                    for (ServerPlayer target : targets) {
                        if (!ProcessTransfur.isPlayerTransfurred(target)) {
                            continue;
                        }

                        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.of(target);
                        if (playerVariables == null) {
                            continue;
                        }

                        playerVariables.FTKCminigameType = null;
                        playerVariables.syncPlayerVariables(target);
                        resultInt++;
                    }

                    if (resultInt > 0) {
                        int finalResultInt = resultInt;
                        context.getSource().sendSuccess(() -> Component.translatable("commands.changed_addon.ftkMinigame.reset.success", finalResultInt), true);
                    } else {
                        context.getSource().sendFailure(Component.translatable("commands.changed_addon.ftkMinigame.reset.success"));
                    }

                    return resultInt;
                }));
        LiteralCommandNode<CommandSourceStack> mainCommand = dispatcher.register(COMMAND_ROOT
                .then(Commands.literal("alphaGene")
                        .then(Commands.literal("setEntityAlphaGene")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(ChangedAddonAdminCommand::setEntityAlphaGene)
                                        )
                                )
                        )
                        .then(Commands.literal("getEntityAlphaGene")
                                .executes(ctx -> getEntityAlphaGene(ctx.getSource().isPlayer() ? ctx.getSource().getPlayerOrException() : null, ctx))
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(ChangedAddonAdminCommand::getEntityAlphaGene)
                                )
                        )
                        .then(Commands.literal("setEntityAlphaGeneScale")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("scale", FloatArgumentType.floatArg(-1, 30))
                                                .executes(ChangedAddonAdminCommand::setEntityAlphaGeneScale)
                                        )
                                )
                        )
                        .then(Commands.literal("getEntityAlphaGeneScale")
                                .executes(ctx -> getEntityAlphaGeneScale(ctx.getSource().isPlayer() ? ctx.getSource().getPlayerOrException() : null, ctx))
                                .then(Commands.argument("target", EntityArgument.entity())
                                        .executes(ChangedAddonAdminCommand::getEntityAlphaGeneScale)
                                )
                        )
                ).then(Commands.literal("setUltraInstinctDodge")
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ChangedAddonAdminCommand::setUltraInstinctDodge)
                                )
                        )
                )
                .then(Commands.literal("showTransfursSlots")
                        .then(Commands.argument("NamespaceFormId", StringArgumentType.greedyString())
                                .suggests((context, builder) -> {
                                    String namespaceFormId = StringArgumentType.getString(context, "NamespaceFormId").toLowerCase();
                                    ArrayList<ResourceLocation> list = TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getFormId).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
                                    list.add(TransfurVariant.SPECIAL_LATEX);
                                    TreeSet<String> set = list.stream().map(ResourceLocation::getNamespace).collect(Collectors.toCollection(TreeSet::new));
                                    if (namespaceFormId.isBlank()) {
                                        list.stream().map(ResourceLocation::toString).toList().forEach(builder::suggest);
                                    } else if (namespaceFormId.startsWith("$")) {
                                        set.forEach(builder::suggest);
                                    } else {
                                        list.stream().map(ResourceLocation::toString).toList().forEach(builder::suggest);
                                    }

                                    return builder.buildFuture();
                                })
                                .then(Commands.argument("FilterWithSlots", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            List<String> suggestions = List.of("\"\"", "no_slots", "none_slots", "with_slots");
                                            suggestions.forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ChangedAddonAdminCommand::showTransfursSlots)
                                )
                                .executes(ChangedAddonAdminCommand::showTransfursSlots)
                        )
                )
                .then(Commands.literal("allowPlayerBossTransfurVariant")
                        .then(Commands.literal("Exp9")
                                .then(Commands.literal("get")
                                        .executes(ctx -> {
                                            if (!ctx.getSource().isPlayer()) {
                                                return 0;
                                            }
                                            ServerPlayer target = ctx.getSource().getPlayerOrException();
                                            ChangedAddonVariables.PlayerVariables vars = target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
                                            if (vars == null) return 0;

                                            ctx.getSource().sendSuccess(() -> Component.literal(target.getDisplayName().getString() + (vars.Exp009TransfurAllowed ? " has Exp009Transfur permission" : " has no Exp009Transfur permission")), false);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(arguments -> {
                                                    Player target = EntityArgument.getPlayer(arguments, "player");

                                                    ChangedAddonVariables.PlayerVariables vars = target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
                                                    if (vars == null) return 0;

                                                    arguments.getSource().sendSuccess(() -> Component.literal(target.getDisplayName().getString() + (vars.Exp009TransfurAllowed ? " has Exp009Transfur permission" : " has no Exp009Transfur permission")), false);
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

                                                            arguments.getSource().sendSuccess(() -> Component.literal(("The Exp009Transfur Perm of the " + target.getDisplayName().getString() + " was set to " + val)), true);

                                                            target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
                                                                capability.Exp009TransfurAllowed = val;
                                                                capability.syncPlayerVariables(target);
                                                            });

                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("Exp10")
                                .then(Commands.literal("get")
                                        .executes(arguments -> {
                                            if (!arguments.getSource().isPlayer()) {
                                                return 0;
                                            }
                                            ServerPlayer target = arguments.getSource().getPlayerOrException();
                                            ChangedAddonVariables.PlayerVariables vars = target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
                                            if (vars == null) return 0;

                                            arguments.getSource().sendSuccess(() -> Component.literal(target.getDisplayName().getString() + (vars.Exp10TransfurAllowed ? " has Exp10Transfur permission" : " has no Exp10Transfur permission")), false);
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(arguments -> {
                                                    Player target = EntityArgument.getPlayer(arguments, "player");

                                                    ChangedAddonVariables.PlayerVariables vars = target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
                                                    if (vars == null) return 0;

                                                    arguments.getSource().sendSuccess(() -> Component.literal(target.getDisplayName().getString() + (vars.Exp10TransfurAllowed ? " has Exp10Transfur permission" : " has no Exp10Transfur permission")), false);
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

                                                            arguments.getSource().sendSuccess(() -> Component.literal(("The Exp10Transfur Perm of the " + target.getDisplayName().getString() + " was set to " + val)), true);

                                                            target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
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
                .then(Commands.literal("setTransfurProgress")//Add/set progress self
                        .requires(stack -> stack.getEntity() instanceof Player)
                        .then(Commands.argument("value", FloatArgumentType.floatArg())
                                .then(Commands.literal("add")
                                        .executes(arguments ->
                                                setTFProgress(arguments.getSource().getPlayerOrException(), FloatArgumentType.getFloat(arguments, "value"), true))
                                )
                                .then(Commands.literal("set")
                                        .executes(arguments ->
                                                setTFProgress(arguments.getSource().getPlayerOrException(), FloatArgumentType.getFloat(arguments, "value"), false))
                                )
                        )
                )
                .then(Commands.literal("setPlayerTransfurProgress")//Add/set progress other
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("value", FloatArgumentType.floatArg())
                                        .then(Commands.literal("add")
                                                .executes(arguments ->
                                                        setTFProgress(EntityArgument.getPlayer(arguments, "target"), FloatArgumentType.getFloat(arguments, "value"), true))
                                        )
                                        .then(Commands.literal("set")
                                                .executes(arguments ->
                                                        setTFProgress(EntityArgument.getPlayer(arguments, "target"), FloatArgumentType.getFloat(arguments, "value"), false))
                                        )
                                )
                        )
                )
                .then(Commands.literal("setPlayerMaxTransfurTolerance")//Set tf tolerance other
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("value", FloatArgumentType.floatArg(.1f))
                                        .executes(arguments ->
                                                setTFTolerance(EntityArgument.getPlayer(arguments, "target"), FloatArgumentType.getFloat(arguments, "value")))
                                )
                                .then(Commands.literal("Default")
                                        .executes(arguments ->
                                                setTFTolerance(EntityArgument.getPlayer(arguments, "target"), 0))
                                )
                        )
                )
                .then(Commands.literal("getMaxTransfurTolerance")//Get tf tolerance self
                        .requires(stack -> stack.getEntity() instanceof Player)
                        .executes(arguments -> {
                            ServerPlayer player = arguments.getSource().getPlayerOrException();
                            // !!! this method includes modifiers like armor, items etc
                            player.displayClientMessage(Component.literal("The maximum Transfur Tolerance is §6" + ProcessTransfur.getEntityTransfurTolerance(player)), false);
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .then(Commands.literal("setBlocksInfectionType")
                        .then(Commands.argument("minPos", BlockPosArgument.blockPos())
                                .then(Commands.argument("maxPos", BlockPosArgument.blockPos())
                                        .then(Commands.literal("white_latex")
                                                .executes(ctx -> setBlockInfection(ctx, ChangedLatexTypes.WHITE_LATEX.get()))
                                        ).then(Commands.literal("dark_latex")
                                                .executes(ctx -> setBlockInfection(ctx, ChangedLatexTypes.DARK_LATEX.get()))
                                        ).then(Commands.literal("neutral")
                                                .executes(ctx -> setBlockInfection(ctx, ChangedLatexTypes.NONE.get()))
                                        )
                                )
                        )
                ).then(Commands.literal("latexLanguage")
                        .executes(ChangedAddonAdminCommand::getLatexLanguage)
                        .then(Commands.argument("set", BoolArgumentType.bool())
                                .executes(ChangedAddonAdminCommand::setLatexLanguage)
                        )
                )
                .then(Commands.literal("untransfurImmunity")
                        .then(untfImmunity)
                )
                .then(Commands.literal("untfImmunity")
                        .then(untfImmunity)
                )
                .then(Commands.literal("FtkMinigame")
                        .then(Commands.literal("set")
                                .then(setFightToKeepConscience)
                        )
                        .then(Commands.literal("get")
                                .then(getFightToKeepConscience)
                        )
                )
                .then(Commands.literal("setPlayerLatexInfection")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("active", BoolArgumentType.bool())
                                        .executes(context -> {
                                            Player player = EntityArgument.getPlayer(context, "target");
                                            boolean isActive = BoolArgumentType.getBool(context, "active");
                                            return setPlayerTransfurInfection(context, player, isActive, TransfurMe.RANDOM_VARIANT, false);
                                        })
                                        .then(Commands.argument("form", ResourceLocationArgument.id())
                                                .executes(context -> {
                                                    Player player = EntityArgument.getPlayer(context, "target");
                                                    boolean isActive = BoolArgumentType.getBool(context, "active");
                                                    ResourceLocation form = ResourceLocationArgument.getId(context, "form");
                                                    return setPlayerTransfurInfection(context, player, isActive, form, false);
                                                })
                                                .suggests(CommandTransfur.SUGGEST_TRANSFUR_VARIANT)
                                                .then(Commands.argument("shouldStallTransfurProgress", BoolArgumentType.bool())
                                                        .executes(context -> {
                                                            Player player = EntityArgument.getPlayer(context, "target");
                                                            boolean shouldStallTransfurProgress = BoolArgumentType.getBool(context, "shouldStallTransfurProgress");
                                                            boolean isActive = BoolArgumentType.getBool(context, "active");
                                                            ResourceLocation form = ResourceLocationArgument.getId(context, "form");
                                                            return setPlayerTransfurInfection(context, player, isActive, form, shouldStallTransfurProgress);
                                                        })
                                                )
                                        )
                                )
                        )
                )
                .then(Commands.literal("clearPlayerLatexInfection")
                        .requires(CommandSourceStack::isPlayer)
                        .executes(context -> clearPlayerLatexInfection(context.getSource().getPlayerOrException()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> {
                                    int returnValue = clearPlayerLatexInfection(EntityArgument.getPlayer(context, "target"));
                                    if (returnValue >= 1) {
                                        context.getSource().sendSuccess(() -> Component.translatable("commands.changed_addon.setPlayerLatexInfection.clear.success"), false);
                                    }
                                    return returnValue;
                                })
                        )
                )
        );

        dispatcher.register(Commands.literal("alphaGeneHandle")
                .requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .redirect(mainCommand.getChild("alphaGene")));

        dispatcher.register(Commands.literal("FtkMinigame")
                .requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .redirect(mainCommand.getChild("FtkMinigame"))
        );
    }

    private static int setPlayerTransfurInfection(CommandContext<CommandSourceStack> context, Player player, boolean isActive, ResourceLocation form, boolean shouldStallTransfurProgress) throws CommandSyntaxException {
        var vars = ChangedAddonVariables.ofOrDefault(player);
        if (form.equals(TransfurMe.RANDOM_VARIANT)) {
            form = Util.getRandom(TransfurVariant.getPublicTransfurVariants().collect(Collectors.toList()), player.getRandom()).getFormId();
        }

        ResourceLocation finalFormId;
        if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getFormId).anyMatch(form::equals)) {
            finalFormId = form;
        } else if (form.equals(TransfurVariant.SPECIAL_LATEX)) {
            finalFormId = Changed.modResource("special/form_" + player.getUUID());
            if (!ChangedRegistry.TRANSFUR_VARIANT.get().containsKey(finalFormId))
                throw TransfurMe.NO_SPECIAL_FORM.create();
        } else {
            throw TransfurMe.NOT_LATEX_FORM.create();
        }

        vars.latexInfection.setActive(isActive);
        vars.latexInfection.setInfectionVariant(ChangedRegistry.TRANSFUR_VARIANT.get().getValue(finalFormId));
        vars.latexInfection.setShouldStallTransfurProgress(shouldStallTransfurProgress);

        vars.syncPlayerVariables(player);
        context.getSource().sendSuccess(() -> Component.translatable("commands.changed_addon.setPlayerLatexInfection.set.success", isActive, finalFormId, shouldStallTransfurProgress), false);
        return 1;
    }

    private static int clearPlayerLatexInfection(Player player) {
        var vars = ChangedAddonVariables.ofOrDefault(player);
        vars.latexInfection.restoreDefault();
        vars.syncPlayerVariables(player);
        return 1;
    }

    private static int untfImmunity(CommandSourceStack stack, Collection<ServerPlayer> targets, boolean value, UntransfurEvent.UntransfurType type) {
        for (ServerPlayer player : targets) {
            TransfurVariantInstance<?> inst = ProcessTransfur.getPlayerTransfurVariant(player);
            if (inst instanceof TransfurVariantInstanceExtensor ext) {
                ext.setUntransfurImmunity(type, value);
            }
        }
        if (!targets.isEmpty())
            stack.sendSuccess(() -> Component.literal(String.format("Untransfur immunity of selected players is set to %s, type %s", value, type)), true);
        return 1;
    }

    private static int setTFProgress(Player player, float amount, boolean add) {
        if (add) amount += ProcessTransfur.getPlayerTransfurProgress(player);

        ProcessTransfur.setPlayerTransfurProgress(player, amount);

        return Command.SINGLE_SUCCESS;
    }

    private static int getLatexLanguage(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Boolean value = ChangedAddonServerConfiguration.TRANSFURRED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.get();
        source.sendSuccess(() -> Component.literal(String.format("The latex Language is set to %b", value)), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setLatexLanguage(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        boolean value = BoolArgumentType.getBool(context, "set");

        ChangedAddonServerConfiguration.TRANSFURRED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.set(value);
        //ChangedAddonServerConfiguration.TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.save();
        return Command.SINGLE_SUCCESS;

    }

    private static int setTFTolerance(Player player, float amount) {
        if (amount == 0) amount = (float) ChangedAttributes.TRANSFUR_TOLERANCE.get().getDefaultValue();

        player.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get()).setBaseValue(amount);
        player.displayClientMessage(Component.literal("Transfur Tolerance has been set to §6<" + amount + ">"), false);
        ChangedAddonMod.LOGGER.info("Transfur Tolerance of {} has been set to {}", player.getDisplayName().getString(), amount);

        return Command.SINGLE_SUCCESS;
    }

    private static int setBlockInfection(CommandContext<CommandSourceStack> ctx, LatexType latexType) {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();

        BlockPos minPos;
        BlockPos maxPos;
        try {
            minPos = BlockPosArgument.getLoadedBlockPos(ctx, "minPos");
            maxPos = BlockPosArgument.getLoadedBlockPos(ctx, "maxPos");
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.literal(
                    "One or both of the selected positions are not loaded!"
            ));
            return 0;
        }

        long count = BlockPos.betweenClosedStream(minPos, maxPos).count();
        if (count > Short.MAX_VALUE) {
            source.sendFailure(Component.literal(
                    "Too many blocks selected: " + count + " > " + Short.MAX_VALUE
            ));
            return 0;
        }

        final var spreadingType = latexType instanceof SpreadingLatexType st
                ? st
                : null;

        int affected = 0;

        for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
            BlockState blockState = level.getBlockState(pos);

            if (blockState.is(ChangedTags.Blocks.DENY_LATEX_COVER))
                continue;
            if (!blockState.isAir())
                continue;

            LatexCoverState originalCover = LatexCoverState.getAt(level, pos);

            if (latexType == ChangedLatexTypes.NONE.get()) {
                if (!originalCover.is(ChangedLatexTypes.NONE.get())) {
                    LatexCoverState.setAtAndUpdate(
                            level,
                            pos,
                            ChangedLatexTypes.NONE.get().defaultCoverState()
                    );
                    affected++;
                }
                continue;
            }

            if (spreadingType == null) {
                LatexCoverState.setAtAndUpdate(
                        level,
                        pos,
                        latexType.defaultCoverState()
                );
                affected++;
                continue;
            }

            var plannedCover = spreadingType.spreadState(
                    level,
                    pos,
                    spreadingType.sourceCoverState()
            );

            var event = new SpreadingLatexType.CoveringBlockEvent(
                    spreadingType,
                    blockState,
                    blockState,
                    plannedCover,
                    pos,
                    level
            );

            spreadingType.defaultCoverBehavior(event);

            if (Changed.postModEvent(event))
                continue;

            if (event.originalState == event.getPlannedState()
                    && event.plannedCoverState == originalCover)
                continue;

            level.setBlockAndUpdate(pos, event.getPlannedState());
            LatexCoverState.setAtAndUpdate(level, pos, event.plannedCoverState);
            affected++;
        }

        int finalAffected = affected;
        source.sendSuccess(
                () -> Component.literal(
                        "Set infection of " + finalAffected + " blocks to "
                                + latexType.toString().toLowerCase().replace("_", " ")
                ),
                true
        );

        return affected > 0 ? 1 : 0;
    }

    private static int setEntityAlphaGene(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");
        boolean value = BoolArgumentType.getBool(context, "value");

        int affected = 0;

        for (Entity entity : entities) {
            entity = resolveChangedEntity(entity);
            if (entity instanceof IAlphaAbleEntity alpha) {
                alpha.setAlpha(value);
                affected++;
            }
        }

        if (affected > 0) {
            int finalAffected = affected;
            context.getSource().sendSuccess(
                    () -> Component.translatable(
                            "commands.changed_addon.alpha.set.success",
                            value,
                            finalAffected
                    ),
                    true
            );
            return affected;
        }

        context.getSource().sendFailure(
                Component.translatable("commands.changed_addon.alpha.set.fail")
        );
        return 0;
    }

    private static int getEntityAlphaGene(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return getEntityAlphaGene(null, context);
    }

    private static int getEntityAlphaGene(@Nullable Entity target, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = target == null ? EntityArgument.getEntity(context, "target") : target;
        entity = resolveChangedEntity(entity);

        if (entity instanceof IAlphaAbleEntity alpha) {
            boolean value = alpha.isAlpha();

            context.getSource().sendSuccess(
                    () -> Component.translatable(
                            "commands.changed_addon.alpha.get.success",
                            value
                    ),
                    false
            );

            return value ? 1 : 0;
        }

        context.getSource().sendFailure(
                Component.translatable("commands.changed_addon.alpha.get.fail")
        );
        return 0;
    }

    private static int setEntityAlphaGeneScale(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");
        float value = FloatArgumentType.getFloat(context, "scale");

        int affected = 0;

        for (Entity entity : entities) {
            entity = resolveChangedEntity(entity);
            if (entity instanceof IAlphaAbleEntity alpha) {
                alpha.setAlphaScale(value);
                affected++;
            }
        }

        if (affected > 0) {
            int finalAffected = affected;
            context.getSource().sendSuccess(
                    () -> Component.translatable(
                            "commands.changed_addon.alpha_scale.set.success",
                            value,
                            finalAffected
                    ),
                    true
            );
            return affected;
        }

        context.getSource().sendFailure(
                Component.translatable("commands.changed_addon.alpha.set.fail")
        );
        return 0;
    }

    private static int getEntityAlphaGeneScale(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return getEntityAlphaGeneScale(null, context);
    }

    private static int getEntityAlphaGeneScale(@Nullable Entity target, CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = target == null ? EntityArgument.getEntity(context, "target") : target;
        entity = resolveChangedEntity(entity);

        if (entity instanceof IAlphaAbleEntity alpha) {
            float value = alpha.alphaAdditionalScale();

            context.getSource().sendSuccess(
                    () -> Component.translatable(
                            "commands.changed_addon.alpha_scale.get.success",
                            value
                    ),
                    false
            );

            return (int) value;
        }

        context.getSource().sendFailure(
                Component.translatable("commands.changed_addon.alpha.get.fail")
        );
        return 0;
    }

    private static Entity resolveChangedEntity(Entity entity) {
        if (entity instanceof Player player) {
            TransfurVariantInstance<?> transfur = ProcessTransfur.getPlayerTransfurVariant(player);
            if (transfur != null) {
                return transfur.getChangedEntity();
            }
        }
        return entity;
    }

    private static int setUltraInstinctDodge(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Collection<? extends Entity> entitiesList = EntityArgument.getEntities(context, "targets");
        boolean value = BoolArgumentType.getBool(context, "value");

        List<Component> successMessages = new ArrayList<>();
        List<Component> failureMessages = new ArrayList<>();

        for (Entity entity : entitiesList) {
            DodgeAbilityInstance dodgeAbilityInstance = null;

            if (entity instanceof ChangedEntity changedEntity) {
                dodgeAbilityInstance = changedEntity.getAbilityInstance(ChangedAddonAbilities.DODGE.get());
            } else if (entity instanceof Player player) {
                TransfurVariantInstance<?> transfurVariantInstance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (transfurVariantInstance != null) {
                    dodgeAbilityInstance = transfurVariantInstance.getAbilityInstance(ChangedAddonAbilities.DODGE.get());
                }
            }

            if (dodgeAbilityInstance != null) {
                dodgeAbilityInstance.setUltraInstinct(value);
                if (value) {
                    successMessages.add(Component.translatable("commands.changed_addon.ultra_instinct.enabled", entity.getName()));
                } else {
                    successMessages.add(Component.translatable("commands.changed_addon.ultra_instinct.disabled", entity.getName()));
                }
            } else {
                failureMessages.add(Component.translatable("commands.changed_addon.ultra_instinct.fail", entity.getName()));
            }
        }

        sendCondensedMessage(source, successMessages, true);
        sendCondensedMessage(source, failureMessages, false);

        return 1;
    }

    private static int showTransfursSlots(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String namespaceFormId = StringArgumentType.getString(context, "NamespaceFormId").toLowerCase().replace("$", "");
        String filter = "";
        try {
            filter = StringArgumentType.getString(context, "FilterWithSlots").toLowerCase();
        } catch (IllegalArgumentException ignored) {
        }

        List<String> validFilters = List.of("\"\"", "no_slots", "none_slots", "with_slots");
        if (!validFilters.contains(filter)) filter = "";

        String namespace = "";
        String formIdFilter = "";

        String[] parts = namespaceFormId.split(":", 2);
        if (parts.length > 0) namespace = parts[0];
        if (parts.length > 1) formIdFilter = parts[1];

        final String finalNamespace = namespace;
        final String finalFormIdFilter = formIdFilter;

        ServerLevel level = source.getLevel();

        Map<EntityType<?>, TransfurVariant<?>> variantMap = ChangedRegistry.TRANSFUR_VARIANT.get()
                .getValues().stream()
                .filter(variant -> finalNamespace.isEmpty() || variant.getFormId().getNamespace().equalsIgnoreCase(finalNamespace))
                .filter(variant -> finalFormIdFilter.isEmpty() || variant.getFormId().getPath().toLowerCase().contains(finalFormIdFilter))
                .sorted(Comparator.comparing(variant -> variant.getFormId().toString()))
                .collect(Collectors.toMap(
                        TransfurVariant::getEntityType,
                        variant -> variant,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        int count = 0;
        for (Map.Entry<EntityType<?>, TransfurVariant<?>> entry : variantMap.entrySet()) {
            if (count >= MAX_OUTPUT) {
                source.sendSuccess(() -> Component.literal("⚠ Data too large for chat, showing only " + MAX_OUTPUT + " entries.").withStyle(ChatFormatting.YELLOW), false);
                break;
            }

            EntityType<?> entityType = entry.getKey();
            TransfurVariant<?> variant = entry.getValue();

            Entity tempEntity = entityType.create(level);
            if (!(tempEntity instanceof LivingEntity livingEntity)) continue;

            Optional<AccessorySlots> optionalSlots = AccessorySlots.getForEntity(livingEntity);

            Component header = Component.literal("EntityType: ")
                    .append(Component.literal(entityType.toShortString()).withStyle(ChatFormatting.AQUA))
                    .append(Component.literal("\nTransfurVariant: "))
                    .append(Component.literal(variant.getFormId().toString()).withStyle(ChatFormatting.GOLD));

            if (optionalSlots.isPresent()) {
                AccessorySlots slots = optionalSlots.get();
                List<String> slotNames = slots.getSlotTypes()
                        .filter(s -> ChangedRegistry.ACCESSORY_SLOTS.get().getKey(s) != null)
                        .map(s -> ChangedRegistry.ACCESSORY_SLOTS.get().getKey(s).toString())
                        .toList();

                if (!slotNames.isEmpty() && (filter.contains("with_slots") || filter.isBlank())) {
                    Component slotText = Component.literal("\nSlots: ")
                            .append(Component.literal(String.join(", ", slotNames)).withStyle(ChatFormatting.GREEN))
                            .append("\n");
                    source.sendSuccess(() -> header.copy().append(slotText), false);
                } else if (slotNames.isEmpty() && (filter.contains("none_slots") || filter.isBlank())) {
                    Component noSlots = Component.literal("\nSlots: None").withStyle(ChatFormatting.DARK_GRAY).append("\n");
                    source.sendSuccess(() -> header.copy().append(noSlots), false);
                }
            } else if (filter.contains("no_slots") || filter.isBlank()) {
                Component noSlotInfo = Component.literal("\nSlots: [No AccessorySlots]").withStyle(ChatFormatting.RED).append("\n---");
                source.sendSuccess(() -> header.copy().append(noSlotInfo), false);
            }

            count++;
        }

        variantMap.clear();
        return 1;
    }

    private static void sendCondensedMessage(CommandSourceStack source, List<Component> messages, boolean success) {
        int maxLines = 6;

        if (messages.isEmpty())
            return;

        if (messages.size() <= maxLines) {
            Component full = ComponentUtils.formatList(messages, Component.literal("\n"));
            if (success)
                source.sendSuccess(() -> full, true);
            else
                source.sendFailure(full);
        } else {
            List<Component> trimmed = messages.subList(0, maxLines);
            int remaining = messages.size() - maxLines;
            trimmed.add(Component.translatable("commands.changed_addon.ultra_instinct.more", remaining));
            Component full = ComponentUtils.formatList(trimmed, Component.literal("\n"));
            if (success)
                source.sendSuccess(() -> full, true);
            else
                source.sendFailure(full);
        }
    }
}
