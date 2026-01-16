package net.foxyas.changedaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.ComponentUtil;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.data.AccessorySlots;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.*;
import java.util.stream.Collectors;

public class ChangedAddonAdminCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> mainCommand = dispatcher.register(Commands.literal("changed-addon-admin")
                .requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("alphaGene")
                        .then(Commands.literal("setEntityAlphaGene")
                                .then(Commands.argument("targets", EntityArgument.entities())
                                        .then(Commands.argument("value", BoolArgumentType.bool())
                                                .executes(ChangedAddonAdminCommand::setEntityAlphaGene)
                                        )
                                )
                        )
                        .then(Commands.literal("getEntityAlphaGene")
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
                                    ArrayList<ResourceLocation> list = TransfurVariant.getPublicTransfurVariants().map(ForgeRegistryEntry::getRegistryName).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new));
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
                .then(Commands.literal("allow_boss_transfur")
                        .then(Commands.literal("Exp9")
                                .then(Commands.literal("get")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(arguments -> {
                                                    Player target = EntityArgument.getPlayer(arguments, "player");

                                                    ChangedAddonVariables.PlayerVariables vars = target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
                                                    if (vars == null) return 0;

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
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(arguments -> {
                                                    Player target = EntityArgument.getPlayer(arguments, "player");

                                                    ChangedAddonVariables.PlayerVariables vars = target.getCapability(ChangedAddonVariables.PLAYER_VARIABLES_CAPABILITY).resolve().orElse(null);
                                                    if (vars == null) return 0;

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
                ).then(Commands.literal("latexLanguage")
                        .executes(ChangedAddonAdminCommand::getLatexLanguage)
                        .then(Commands.argument("set", BoolArgumentType.bool())
                                .executes(ChangedAddonAdminCommand::setLatexLanguage)
                        )
                )
        );

        dispatcher.register(Commands.literal("alphaGeneHandle")
                .requires(s -> s.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .redirect(mainCommand.getChild("alphaGene")));
    }

    private static int setTFProgress(Player player, float amount, boolean add) {
        if (add) amount += ProcessTransfur.getPlayerTransfurProgress(player);

        ProcessTransfur.setPlayerTransfurProgress(player, amount);

        return Command.SINGLE_SUCCESS;
    }

    private static int getLatexLanguage(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        Boolean value = ChangedAddonServerConfiguration.TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.get();
        source.sendSuccess(ComponentUtil.literal(String.format("The latex Language is set to %b", value)), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int setLatexLanguage(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        boolean value = BoolArgumentType.getBool(context, "set");

        ChangedAddonServerConfiguration.TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.set(value);
        //ChangedAddonServerConfiguration.TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE.save();
        source.sendSuccess(ComponentUtil.literal(String.format("The latex Language was been set to %b", value)), true);
        return Command.SINGLE_SUCCESS;

    }

    private static int setTFTolerance(Player player, float amount) {
        if (amount == 0) amount = (float) ChangedAttributes.TRANSFUR_TOLERANCE.get().getDefaultValue();

        player.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get()).setBaseValue(amount);
        player.displayClientMessage(new TextComponent("Transfur Tolerance has been set to §6<" + amount + ">"), false);
        ChangedAddonMod.LOGGER.info("Transfur Tolerance of {} has been set to {}", player.getDisplayName().getString(), amount);

        return Command.SINGLE_SUCCESS;
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
            context.getSource().sendSuccess(
                    new TranslatableComponent(
                            "commands.changed_addon.alpha.set.success",
                            value,
                            affected
                    ),
                    true
            );
            return affected;
        }

        context.getSource().sendFailure(
                new TranslatableComponent("commands.changed_addon.alpha.set.fail")
        );
        return 0;
    }


    private static int getEntityAlphaGene(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "target");
        entity = resolveChangedEntity(entity);

        if (entity instanceof IAlphaAbleEntity alpha) {
            boolean value = alpha.isAlpha();

            context.getSource().sendSuccess(
                    new TranslatableComponent(
                            "commands.changed_addon.alpha.get.success",
                            value
                    ),
                    false
            );

            return value ? 1 : 0;
        }

        context.getSource().sendFailure(
                new TranslatableComponent("commands.changed_addon.alpha.get.fail")
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
            context.getSource().sendSuccess(
                    new TranslatableComponent(
                            "commands.changed_addon.alpha_scale.set.success",
                            value,
                            affected
                    ),
                    true
            );
            return affected;
        }

        context.getSource().sendFailure(
                new TranslatableComponent("commands.changed_addon.alpha.set.fail")
        );
        return 0;
    }


    private static int getEntityAlphaGeneScale(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "target");
        entity = resolveChangedEntity(entity);

        if (entity instanceof IAlphaAbleEntity alpha) {
            float value = alpha.alphaAdditionalScale();

            context.getSource().sendSuccess(
                    new TranslatableComponent(
                            "commands.changed_addon.alpha_scale.get.success",
                            value
                    ),
                    false
            );

            return (int) value;
        }

        context.getSource().sendFailure(
                new TranslatableComponent("commands.changed_addon.alpha.get.fail")
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

    private static int setBlockInfection(CommandContext<CommandSourceStack> ctx, LatexType enumValue) {
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
                    successMessages.add(new TranslatableComponent("changed_addon.command.ultra_instinct.enabled", entity.getName()));
                } else {
                    successMessages.add(new TranslatableComponent("changed_addon.command.ultra_instinct.disabled", entity.getName()));
                }
            } else {
                failureMessages.add(new TranslatableComponent("changed_addon.command.ultra_instinct.fail", entity.getName()));
            }
        }

        sendCondensedMessage(source, successMessages, true);
        sendCondensedMessage(source, failureMessages, false);

        return 1;
    }

    private static final int MAX_OUTPUT = 20; // max lines to show

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
                source.sendSuccess(ComponentUtil.literal("⚠ Data too large for chat, showing only " + MAX_OUTPUT + " entries.").withStyle(ChatFormatting.YELLOW), false);
                break;
            }

            EntityType<?> entityType = entry.getKey();
            TransfurVariant<?> variant = entry.getValue();

            Entity tempEntity = entityType.create(level);
            if (!(tempEntity instanceof LivingEntity livingEntity)) continue;

            Optional<AccessorySlots> optionalSlots = AccessorySlots.getForEntity(livingEntity);

            Component header = ComponentUtil.literal("EntityType: ")
                    .append(ComponentUtil.literal(entityType.toShortString()).withStyle(ChatFormatting.AQUA))
                    .append(ComponentUtil.literal("\nTransfurVariant: "))
                    .append(ComponentUtil.literal(variant.getFormId().toString()).withStyle(ChatFormatting.GOLD));

            if (optionalSlots.isPresent()) {
                AccessorySlots slots = optionalSlots.get();
                List<String> slotNames = slots.getSlotTypes()
                        .filter(s -> s.getRegistryName() != null)
                        .map(s -> s.getRegistryName().toString())
                        .toList();

                if (!slotNames.isEmpty() && (filter.contains("with_slots") || filter.isBlank())) {
                    Component slotText = ComponentUtil.literal("\nSlots: ")
                            .append(ComponentUtil.literal(String.join(", ", slotNames)).withStyle(ChatFormatting.GREEN))
                            .append("\n");
                    source.sendSuccess(header.copy().append(slotText), false);
                } else if (slotNames.isEmpty() && (filter.contains("none_slots") || filter.isBlank())) {
                    Component noSlots = ComponentUtil.literal("\nSlots: None").withStyle(ChatFormatting.DARK_GRAY).append("\n");
                    source.sendSuccess(header.copy().append(noSlots), false);
                }
            } else if (filter.contains("no_slots") || filter.isBlank()) {
                Component noSlotInfo = ComponentUtil.literal("\nSlots: [No AccessorySlots]").withStyle(ChatFormatting.RED).append("\n---");
                source.sendSuccess(header.copy().append(noSlotInfo), false);
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
            Component full = ComponentUtils.formatList(messages, new TextComponent("\n"));
            if (success)
                source.sendSuccess(full, true);
            else
                source.sendFailure(full);
        } else {
            List<Component> trimmed = messages.subList(0, maxLines);
            int remaining = messages.size() - maxLines;
            trimmed.add(new TranslatableComponent("changed_addon.command.ultra_instinct.more", remaining));
            Component full = ComponentUtils.formatList(trimmed, new TextComponent("\n"));
            if (success)
                source.sendSuccess(full, true);
            else
                source.sendFailure(full);
        }
    }
}
