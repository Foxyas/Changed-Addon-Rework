package net.foxyas.changedaddon.qte;

import net.foxyas.changedaddon.client.gui.ftkc.CircleHoverMinigameScreen;
import net.foxyas.changedaddon.client.gui.ftkc.KeyPressMinigameScreen;
import net.foxyas.changedaddon.client.gui.ftkc.MouseCirclePullMinigameScreen;
import net.foxyas.changedaddon.client.gui.ftkc.MousePullMinigameScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class FightToKeepConsciousnessClient {

    static Supplier<Screen> MOUSE_PULL() {
        return MousePullMinigameScreen::new;
    }

    static Supplier<Screen> MOUSE_CIRCLE_PULL() {
        return MouseCirclePullMinigameScreen::new;
    }

    static Supplier<Screen> KEY_PRESS() {
        return KeyPressMinigameScreen::new;
    }

    static Supplier<Screen> CIRCLE_HOVER() {
        return CircleHoverMinigameScreen::new;
    }
}
