package net.foxyas.changedaddon.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;

public class TitleUtils {

    /**
     * Sends a full title to the player with custom animations.
     * * @param player   The target server player.
     * @param title    The main title text (can be null).
     * @param subtitle The subtitle text (can be null).
     * @param fadeIn   Ticks for the fade-in animation.
     * @param stay     Ticks for the text to remain visible.
     * @param fadeOut  Ticks for the fade-out animation.
     */
    public static void sendTitle(ServerPlayer player, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        // Reset previous titles and timings to avoid overlapping or ghosting
        resetTitle(player);

        // Set the animation timings
        player.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));

        // Send subtitle first (Minecraft logic: subtitle is bound to the next title packet)
        if (subtitle != null) {
            player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
        }

        // Send the main title
        if (title != null) {
            player.connection.send(new ClientboundSetTitleTextPacket(title));
        }
    }

    public static void sendTitleAndReset(ServerPlayer player, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        sendTitle(player, title, subtitle, fadeIn, stay, fadeOut);
        resetTitle(player);
    }

    /**
     * Clears the current title and resets timings to default values.
     * * @param player The target server player.
     */
    public static void resetTitle(ServerPlayer player) {
        // Passing 'true' clears the text and resets the times to default (10, 70, 20)
        player.connection.send(new ClientboundClearTitlesPacket(true));
    }

    /**
     * Instantly removes the title from the player's screen without resetting timings.
     */
    public static void clearTitle(ServerPlayer player) {
        player.connection.send(new ClientboundClearTitlesPacket(false));
    }
}