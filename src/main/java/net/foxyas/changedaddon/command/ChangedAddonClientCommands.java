package net.foxyas.changedaddon.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.foxyas.changedaddon.client.gui.ComplexBestiaryScreen;
import net.foxyas.changedaddon.client.gui.SimplerBestiaryScreen;
import net.foxyas.changedaddon.client.gui.ftkc.CircleHoverMinigameScreen;
import net.foxyas.changedaddon.client.gui.ftkc.CircleMinigameScreen;
import net.foxyas.changedaddon.client.gui.ftkc.MouseCirclePullMinigameScreen;
import net.foxyas.changedaddon.client.gui.ftkc.MousePullMinigameScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class ChangedAddonClientCommands {

    public static void registerClientCommand(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("changed-addon")
                .then(Commands.literal("openBestiary")
                        .executes(context -> {
                            // Abre a tela do Bestiário no próximo tick do cliente
                            Minecraft.getInstance().tell(() -> {
                                Minecraft.getInstance().setScreen(new ComplexBestiaryScreen());
                            });
                            return 1;
                        })
                        .then(Commands.argument("type", StringArgumentType.word())
                                .executes(context -> {
                                    String type = StringArgumentType.getString(context, "type");
                                    Screen pGuiScreen = switch (type) {
                                        case "complex" -> new ComplexBestiaryScreen();
                                        case "simpler" -> new SimplerBestiaryScreen();
                                        default -> throw new CommandRuntimeException(Component.translatable("commands.changed_addon.openBestiary.fail.notValidType", type));
                                    };
                                    // Abre a tela do Bestiário no próximo tick do cliente
                                    Minecraft.getInstance().tell(() -> {
                                        Minecraft.getInstance().setScreen(pGuiScreen);
                                    });
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("openMinigame")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                                        Arrays.stream(MinigameScreens.values()).map(Enum::name), builder))
                                .executes(context -> {
                                    String type = StringArgumentType.getString(context, "type");
                                    MinigameScreens screenEnum;

                                    try {
                                        screenEnum = MinigameScreens.valueOf(type.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        return 0; // Falha se o enum não existir
                                    }

                                    // Define a screen baseada no Enum
                                    Minecraft.getInstance().tell(() -> {
                                        Minecraft.getInstance().setScreen(screenEnum.createScreen());
                                    });

                                    return 1;
                                })
                        )
                )
        );
    }

    public enum MinigameScreens {
        MOUSE_PULL(() -> new MousePullMinigameScreen() {
            @Override
            protected void increaseStruggle() {
            }
        }),
        MOUSE_CIRCLE_PULL(() -> new MouseCirclePullMinigameScreen() {
            @Override
            protected void increaseStruggle() {
            }
        }),
        CIRCLE_HOVER(() -> new CircleHoverMinigameScreen() {
            @Override
            protected void increaseStruggle() {
            }
        });

        private final Supplier<CircleMinigameScreen> screenSupplier;

        MinigameScreens(Supplier<CircleMinigameScreen> screenSupplier) {
            this.screenSupplier = screenSupplier;
        }

        public Screen createScreen() {
            CircleMinigameScreen screen = screenSupplier.get();
            screen.shouldCloseOnInvalidState = false;
            return screen;
        }
    }
}
