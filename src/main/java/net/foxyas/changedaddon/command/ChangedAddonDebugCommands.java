package net.foxyas.changedaddon.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.foxyas.changedaddon.mixins.mods.changed.FacilitySinglePieceAccessor;
import net.foxyas.changedaddon.mixins.mods.changed.FacilitySinglePieceInstanceAccessor;
import net.foxyas.changedaddon.util.StructureUtil;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.world.features.structures.FacilityPieces;
import net.ltxprogrammer.changed.world.features.structures.facility.ConfiguredFacilityPiece;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilityPieceCollection;
import net.ltxprogrammer.changed.world.features.structures.facility.FacilitySinglePiece;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;
import java.util.Objects;

public class ChangedAddonDebugCommands {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_FACILITY_PIECES_IDS =
            (context, builder) -> SharedSuggestionProvider.suggest(
                    FacilityPieceSuggestions.ALL_IDS,
                    builder
            );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("changed-addon-admin")
                        .then(Commands.literal("debug")
                                .requires(src -> src.hasPermission(Commands.LEVEL_ADMINS)) // admin only
                                .then(
                                        Commands.literal("facility")
                                                .then(
                                                        Commands.literal("hasPiece")
                                                                .then(
                                                                        Commands.argument("template", ResourceLocationArgument.id()).suggests(SUGGEST_FACILITY_PIECES_IDS)
                                                                                .executes(ChangedAddonDebugCommands::hasFacilityPiece)
                                                                )
                                                )
                                )
                        )
        );
    }

    private static int hasFacilityPiece(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player;

        try {
            player = source.getPlayerOrException();
        } catch (CommandSyntaxException e) {
            source.sendFailure(Component.literal("This command must be executed by a player."));
            return 0;
        }

        ResourceLocation resourceId = ResourceLocationArgument.getId(ctx, "template");

        StructureStart structureAt = StructureUtil.getFacilityAt(
                player.serverLevel(),
                player.getOnPos()
        );

        if (structureAt == null || !structureAt.isValid()) {
            source.sendFailure(Component.literal("No facility found at your position."));
            return 0;
        }

        for (StructurePiece piece : structureAt.getPieces()) {
            if (piece instanceof FacilitySinglePiece.StructureInstance facilityPieceInstance) {

                FacilitySinglePieceInstanceAccessor accessor =
                        (FacilitySinglePieceInstanceAccessor) facilityPieceInstance;

                ResourceLocation templateName = accessor.getTemplateName();

                if (templateName.equals(resourceId)) {
                    BlockPos genPos = accessor.getGenerationPosition();
                    BlockPos center = facilityPieceInstance.getBoundingBox().getCenter();

                    MutableComponent lineBreak = Component.literal("\n");

                    MutableComponent pMessage = Component.literal("Facility HAS piece: ").withStyle(style -> style
                            .withColor((TextColor) null)
                    ).append(Component.literal(resourceId.toString()).withStyle(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/changed-addon-admin debug facility hasPiece %s", resourceId)))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy the facility piece id")))
                                    .withColor(ChatFormatting.GREEN)
                            )
                    );
                    MutableComponent generationPos = Component.literal("Generation Pos: ").withStyle(style -> style
                            .withColor((TextColor) null)
                    ).append(Component.literal(genPos.toString())
                            .withStyle(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tp %d %d %d", genPos.getX(), genPos.getY(), genPos.getZ())))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy the teleport command")))
                                    .withColor(ChatFormatting.GREEN)
                            )
                    );
                    MutableComponent generationCenterPos = Component.literal("Center: ").append(Component.literal(center.toString())
                            .withStyle(style -> style
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tp %d %d %d", center.getX(), center.getY(), center.getZ())))
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy the teleport command")))
                                    .withColor(ChatFormatting.GREEN)
                            )
                    );

                    source.sendSuccess(
                            () -> pMessage.append(lineBreak).append(generationPos).append(lineBreak).append(generationCenterPos),
                            false
                    );
                    return 1;
                }
            }
        }

        source.sendFailure(
                Component.literal("Facility does NOT contain piece: " + resourceId)
        );
        return 0;
    }

    public static final class FacilityPieceSuggestions {

        public static final List<String> ALL_IDS = build();

        private static List<String> build() {
            return ChangedRegistry.FACILITY_PIECE_TYPES.get().getValues().stream()
                    .map(FacilityPieces::getPiecesOfType)
                    .filter(Objects::nonNull)
                    .flatMap(FacilityPieceCollection::stream)
                    .map(ConfiguredFacilityPiece::getFacilityPiece)
                    .filter(piece -> piece instanceof FacilitySinglePieceAccessor)
                    .map(piece -> ((FacilitySinglePieceAccessor) piece).getTemplateName())
                    .filter(Objects::nonNull)
                    .map(ResourceLocation::toString)
                    .distinct()
                    .sorted()
                    .toList();
        }
    }
}
