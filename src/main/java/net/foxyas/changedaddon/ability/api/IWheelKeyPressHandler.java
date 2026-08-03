package net.foxyas.changedaddon.ability.api;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.packet.AbilityWheelKeyPressPacket;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.minecraft.world.entity.player.Player;

public interface IWheelKeyPressHandler {

    /**
     * Called on the client side when the bound key is pressed.
     *
     * @param player     The player who pressed the key. It's always the client player
     * @param isMouse    tells if such key ID is from mouse.
     * @param keyPressed The key pressed on the moment.
     * @param action     The action of the key input
     * @param modifiers  The modifiers (shift, control, etc.)
     * @return returns if the key send the packet to the server.
     */
    default boolean onClientWheelKeyPressed(Player player, boolean isMouse, int keyPressed, int action, int modifiers) {
        if (!(this instanceof AbstractAbilityInstance self))
            return false;

        if (isWheelKeyPressedValid(player, isMouse, keyPressed, action, modifiers)) {
            ChangedAddonMod.PACKET_HANDLER.sendToServer(new AbilityWheelKeyPressPacket(keyPressed, action, modifiers, isMouse, self.ability));
            return true;
        }
        return false;
    }

    default boolean detectsScroll() {
        return false;
    }

    /**
     * Called on the client side when the bound key is pressed.
     *
     * @param player     The player who pressed the key. It's always the client/server player
     * @param isMouse    tells if such key ID is from mouse.
     * @param keyPressed The key pressed on the moment.
     * @param action     The action of the key input
     * @param modifiers  The modifiers (shift, control, etc.)
     */
    void onServerProcessWheelKeyPressed(Player player, boolean isMouse, int keyPressed, int action, int modifiers);

    /**
     * Bidirecional function it can be called on server and client sides with no problems
     *
     * @param player     The player who pressed the key. It's always the client/server player
     * @param isMouse    tells if such key ID is from mouse.
     * @param keyPressed The key to be validated.
     * @param action     The action of the key input
     * @param modifiers  The modifiers (shift, control, etc.)
     */
    boolean isWheelKeyPressedValid(Player player, boolean isMouse, int keyPressed, int action, int modifiers);
}
