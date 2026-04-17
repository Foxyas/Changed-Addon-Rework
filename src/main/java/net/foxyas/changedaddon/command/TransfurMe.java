package net.foxyas.changedaddon.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.command.CommandTransfur;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.ai.ImmediateTransfurDecision;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.extension.ChangedCompatibility;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

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
                                transfurPlayer(context.getSource(), ResourceLocationArgument.getId(context, "form"), TransfurCause.GRAB_REPLICATE.getSerializedName(), null))
                        .then(Commands.argument("cause", StringArgumentType.string())
                                .suggests(CommandTransfur.SUGGEST_TRANSFUR_CAUSE)
                                .executes(context ->
                                        transfurPlayer(context.getSource(), ResourceLocationArgument.getId(context, "form"), StringArgumentType.getString(context, "cause"), null))
                                .then(Commands.argument("nbt", CompoundTagArgument.compoundTag())
                                        .executes(context ->
                                                transfurPlayer(context.getSource(), ResourceLocationArgument.getId(context, "form"), StringArgumentType.getString(context, "cause"), CompoundTagArgument.getCompoundTag(context, "nbt")))
                                )
                        )
                )
        );

        dispatcher.register(Commands.literal("tfme")
                .requires(stack -> stack.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .redirect(transfurNode)
        );
    }

    private static int transfurPlayer(CommandSourceStack source, ResourceLocation form, String causeStr, @Nullable CompoundTag tag) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();

        if (ChangedCompatibility.isPlayerUsedByOtherMod(player)) throw USED_BY_OTHER_MOD.create();

        TransfurCause cause = TransfurCause.fromSerial(causeStr).result().orElse(null);
        if (cause == null) throw NOT_CAUSE.create();

        if (form.equals(RANDOM_VARIANT)) {
            form = Util.getRandom(TransfurVariant.getPublicTransfurVariants().collect(Collectors.toList()), player.getRandom()).getFormId();
        }

        ResourceLocation finalFormId;
        if (TransfurVariant.getPublicTransfurVariants().map(TransfurVariant::getFormId).anyMatch(form::equals)) {
            finalFormId = form;
        } else if (form.equals(TransfurVariant.SPECIAL_LATEX)) {
            finalFormId = Changed.modResource("special/form_" + player.getUUID());
            if (!ChangedRegistry.TRANSFUR_VARIANT.get().containsKey(finalFormId)) throw NO_SPECIAL_FORM.create();
        } else {
            throw NOT_LATEX_FORM.create();
        }

        TransfurVariant<?> variant = ChangedRegistry.TRANSFUR_VARIANT.get().getValue(finalFormId);
        if (ProcessTransfur.isPlayerTransfurred(player)) {
            PlayerUtil.unTransfurPlayer(player);
        }

        ProcessTransfur.transfur(player, ImmediateTransfurDecision.safe(variant, cause, newEntity -> {
            if (tag != null) {
                newEntity.getChangedEntity().readPlayerVariantData(tag);
            }
        }));

        source.sendSuccess(() -> Component.translatable("command.changed.success.transfurred.one", player.getScoreboardName(), variant.getFormId().toString()), false);

        return Command.SINGLE_SUCCESS;
    }
}