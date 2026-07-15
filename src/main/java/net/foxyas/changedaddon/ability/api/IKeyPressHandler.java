package net.foxyas.changedaddon.ability.api;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.packet.AbilityKeyPressPacket;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.minecraft.world.entity.player.Player;

public interface IKeyPressHandler {

    /**
     * Called on the client side when the bound key is pressed.
     *
     * @param player     The player who pressed the key. It's always the client player
     * @param keyPressed The key pressed on the moment.
     * @param action     The action of the key input
     * @param modifiers  The modifiers (shift, control, etc.)
     */
    default void onClientKeyPressed(Player player, int keyPressed, int action, int modifiers) {
        if (!(this instanceof AbstractAbilityInstance self)) return;

        if (isKeyPressedValid(player, keyPressed, action, modifiers)) {
            ChangedAddonMod.PACKET_HANDLER.sendToServer(new AbilityKeyPressPacket(keyPressed, action, modifiers, self.ability));
        }
    }

    /**
     * Called on the client side when the bound key is pressed.
     *
     * @param player     The player who pressed the key. It's always the client/server player
     * @param keyPressed The key pressed on the moment.
     * @param action     The action of the key input
     * @param modifiers  The modifiers (shift, control, etc.)
     */
    void onServerProcessKeyPressed(Player player, int keyPressed, int action, int modifiers);

    /**
     * Bidirecional function it can be called on server and client sides with no problems
     *
     * @param player     The player who pressed the key. It's always the client/server player
     * @param keyPressed The key to be validated.
     * @param action     The action of the key input
     * @param modifiers  The modifiers (shift, control, etc.)
     */
    boolean isKeyPressedValid(Player player, int keyPressed, int action, int modifiers);
}
