package net.foxyas.changedaddon.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;

public abstract class AbstractBestiaryScreen extends Screen implements MouseMoveListener {

    protected AbstractBestiaryScreen(Component pTitle) {
        super(pTitle);
    }
}
