package net.foxyas.changedaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.command.CommandTransfur;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.extension.ChangedCompatibility;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransfurMe {

    private static final SimpleCommandExceptionType NOT_LATEX_FORM = new SimpleCommandExceptionType(Component.translatable("command.changed.error.not_latex_form"));
    private static final SimpleCommandExceptionType NOT_CAUSE = new SimpleCommandExceptionType(Component.translatable("command.changed.error.not_cause"));
    private static final SimpleCommandExceptionType USED_BY_OTHER_MOD = new SimpleCommandExceptionType(Component.translatable("command.changed.error.used_by_other_mod"));
    private static final SimpleCommandExceptionType NO_SPECIAL_FORM = new SimpleCommandExceptionType(Component.translatable("command.changed.error.no_special_form"));
    private static final ResourceLocation RANDOM_VARIANT = Changed.modResource("random");

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> transfurNode = dispatcher.register(Commands.literal("transfurme")
                .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("form", ResourceLocationArgument.id())
                        .suggests(CommandTransfur.SUGGEST_TRANSFUR_VARIANT)
                        .executes(context ->
                                transfurPlayer(context.getSource(), ResourceLocationArgument.getId(context, "form"), TransfurCause.GRAB_REPLICATE.getSerializedName()))
                        .then(Commands.argument("cause", StringArgumentType.string())
                                .suggests(CommandTransfur.SUGGEST_TRANSFUR_CAUSE)
                                .executes(context ->
                                        transfurPlayer(context.getSource(), ResourceLocationArgument.getId(context, "form"), StringArgumentType.getString(context, "cause")))
                        )
                )
        );

        dispatcher.register(Commands.literal("tfme")
                .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .redirect(transfurNode)
        );
    }

    private static int transfurPlayer(CommandSourceStack source, ResourceLocation form, String cause) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (ChangedCompatibility.isPlayerUsedByOtherMod(player)) throw USED_BY_OTHER_MOD.create();

        TransfurCause transfurCause = TransfurCause.fromSerial(cause).result().orElse(null);
        if (transfurCause == null) throw NOT_CAUSE.create();

        if (form.equals(RANDOM_VARIANT)) {
            form = Util.<TransfurVariant<?>>getRandom(TransfurVariant.getPublicTransfurVariants().collect(Collectors.toList()), player.getRandom()).getFormId();
        }

        Stream<ResourceLocation> stream = TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getFormId);
        if (stream.anyMatch(form::equals)) {
            doTransfur(player, source, form, transfurCause);
        } else {
            if (!form.equals(TransfurVariant.SPECIAL_LATEX)) throw NOT_LATEX_FORM.create();

            ResourceLocation key = Changed.modResource("special/form_" + player.getUUID());
            if (!ChangedRegistry.TRANSFUR_VARIANT.get().containsKey(key)) throw NO_SPECIAL_FORM.create();

            doTransfur(player, source, key, transfurCause);
        }

        final ResourceLocation shutUpCompilerForm = form;

        source.sendSuccess(() -> Component.translatable("command.changed.success.transfurred.one", player.getScoreboardName(), shutUpCompilerForm.toString()), false);

        return Command.SINGLE_SUCCESS;
    }

    private static void doTransfur(ServerPlayer player, CommandSourceStack stack, ResourceLocation tf, TransfurCause cause) {
        if (ProcessTransfur.isPlayerTransfurred(player)) {
            ProcessTransfur.setPlayerTransfurVariant(player, ChangedRegistry.TRANSFUR_VARIANT.get().getValue(tf), cause);
        } else
            ProcessTransfur.transfur(player, stack.getLevel(), ChangedRegistry.TRANSFUR_VARIANT.get().getValue(tf), true, TransfurContext.hazard(cause));
    }
}
