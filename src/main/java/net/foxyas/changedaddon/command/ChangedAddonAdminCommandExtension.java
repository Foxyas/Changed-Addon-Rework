package net.foxyas.changedaddon.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonAdminCommandExtension {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("changed-addon-admin")
                        .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.literal("setBlocksInfectionType")
                                .then(Commands.argument("minPos", BlockPosArgument.blockPos())
                                        .then(Commands.argument("maxPos", BlockPosArgument.blockPos())
                                                .then(Commands.literal("white_latex")
                                                        .executes(ctx -> execute(ctx, LatexType.WHITE_LATEX))
                                                ).then(Commands.literal("dark_latex")
                                                        .executes(ctx -> execute(ctx, LatexType.DARK_LATEX))
                                                ).then(Commands.literal("neutral")
                                                        .executes(ctx -> execute(ctx, LatexType.NEUTRAL))
                                                )
                                        )
                                )
                        )
        );
    }

    private static int execute(CommandContext<CommandSourceStack> ctx, LatexType enumValue){
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
