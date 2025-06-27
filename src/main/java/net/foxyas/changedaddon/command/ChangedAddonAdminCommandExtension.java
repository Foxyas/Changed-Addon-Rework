package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.ltxprogrammer.changed.block.AbstractLatexBlock;
import net.ltxprogrammer.changed.entity.LatexType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChangedAddonAdminCommandExtension {
    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("changed-addon-admin")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("setBlocksInfectionType")
                                .then(Commands.argument("minPos", BlockPosArgument.blockPos())
                                        .then(Commands.argument("maxPos", BlockPosArgument.blockPos())
                                                .then(Commands.literal("white_latex")
                                                        .executes(ctx -> {
                                                            ServerLevel world = ctx.getSource().level();
                                                            BlockPos minPos = BlockPosArgument.getLoadedBlockPos(ctx, "minPos");
                                                            BlockPos maxPos = BlockPosArgument.getLoadedBlockPos(ctx, "maxPos");

                                                            long value = BlockPos.betweenClosedStream(minPos, maxPos).count();
                                                            int SAFE_LIMIT = 32768;

                                                            if (value > SAFE_LIMIT) {
                                                                ctx.getSource().sendFailure(new TextComponent("Too many blocks selected: " + value + " > " + SAFE_LIMIT));
                                                                return 0;
                                                            }

                                                            AffectBlocksProcedure.execute(world, minPos, maxPos, LatexType.WHITE_LATEX);
                                                            ctx.getSource().sendSuccess(new TextComponent("Set Infection of " + value + " blocks to white_latex"), true);
                                                            return 1;

                                                        })
                                                ).then(Commands.literal("dark_latex")
                                                        .executes(ctx -> {
                                                            ServerLevel world = ctx.getSource().level();
                                                            BlockPos minPos = BlockPosArgument.getLoadedBlockPos(ctx, "minPos");
                                                            BlockPos maxPos = BlockPosArgument.getLoadedBlockPos(ctx, "maxPos");

                                                            long value = BlockPos.betweenClosedStream(minPos, maxPos).count();
                                                            int SAFE_LIMIT = 32768;

                                                            if (value > SAFE_LIMIT) {
                                                                ctx.getSource().sendFailure(new TextComponent("Too many blocks selected: " + value + " > " + SAFE_LIMIT));
                                                                return 0;
                                                            }

                                                            AffectBlocksProcedure.execute(world, minPos, maxPos, LatexType.DARK_LATEX);
                                                            ctx.getSource().sendSuccess(new TextComponent("Set Infection of " + value + " blocks to dark_latex"), true);
                                                            return 1;

                                                        })
                                                ).then(Commands.literal("neutral")
                                                        .executes(ctx -> {
                                                            ServerLevel world = ctx.getSource().level();
                                                            BlockPos minPos = BlockPosArgument.getLoadedBlockPos(ctx, "minPos");
                                                            BlockPos maxPos = BlockPosArgument.getLoadedBlockPos(ctx, "maxPos");

                                                            long value = BlockPos.betweenClosedStream(minPos, maxPos).count();
                                                            int SAFE_LIMIT = 32768;

                                                            if (value > SAFE_LIMIT) {
                                                                ctx.getSource().sendFailure(new TextComponent("Too many blocks selected: " + value + " > " + SAFE_LIMIT));
                                                                return 0;
                                                            }

                                                            AffectBlocksProcedure.execute(world, minPos, maxPos, LatexType.NEUTRAL);
                                                            ctx.getSource().sendSuccess(new TextComponent("Set Infection of " + value + " blocks to neutral"), true);
                                                            return 1;

                                                        })
                                                )
                                        )
                                )
                        )
        );
    }


    public static class AffectBlocksProcedure {
        public static void execute(LevelAccessor world, BlockPos minPos, BlockPos maxPos, LatexType enumValue) {
            for (BlockPos pos : BlockPos.betweenClosed(minPos, maxPos)) {
                BlockState state = world.getBlockState(pos);
                if (state.hasProperty(AbstractLatexBlock.COVERED)) {
                    BlockState newState = state.setValue(AbstractLatexBlock.COVERED, enumValue);
                    world.setBlock(pos, newState, 3);
                }
            }
        }
    }
}
