package net.foxyas.changedaddon.ability.handle;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.api.IKeyPressHandler;
import net.foxyas.changedaddon.ability.api.IWheelKeyPressHandler;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.client.gui.AbilityRadialScreen;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class AbilityInstanceKeyHandler {

    // --- KEYBOARD EVENTS ---
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.Key event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.screen != null) return;

        int button = event.getKey();
        int action = event.getAction();
        int modifiers = event.getModifiers();

        ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent(variantInstance -> {
            for (AbstractAbilityInstance abilityInstance : variantInstance.abilityInstances.values()) {
                if (abilityInstance instanceof IKeyPressHandler iKeyPressHandler) {
                    iKeyPressHandler.onClientKeyPressed(player, false, button, action, modifiers);
                }
            }
        });
    }

    // --- IN-GAME MOUSE INPUT (No Screen Open) ---
    @SubscribeEvent
    public static void onInGameMouseInput(InputEvent.MouseButton event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.screen != null) return;

        int button = event.getButton();
        int action = event.getAction();
        int modifiers = event.getModifiers();

        ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent(variantInstance -> {
            for (AbstractAbilityInstance abilityInstance : variantInstance.abilityInstances.values()) {
                if (abilityInstance instanceof IKeyPressHandler iKeyPressHandler) {
                    iKeyPressHandler.onClientKeyPressed(player, true, button, action, modifiers);
                }
            }
        });
    }

    // --- SCREEN EVENTS (When AbilityRadialScreen is Open) ---

    @SubscribeEvent
    public static void onScreenMousePressed(ScreenEvent.MouseButtonPressed.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        Screen screen = event.getScreen();
        if (screen instanceof AbilityRadialScreen abilityRadialScreen) {
            int mouseX = (int) event.getMouseX();
            int mouseY = (int) event.getMouseY();
            int button = event.getButton();
            int action = GLFW.GLFW_PRESS; // 1
            int modifiers = getCurrentModifiers();

            handleRadialWheelClick(event, player, abilityRadialScreen, mouseX, mouseY, button, action, modifiers);
        }
    }

    @SubscribeEvent
    public static void onScreenMouseReleased(ScreenEvent.MouseButtonReleased.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        Screen screen = event.getScreen();
        if (screen instanceof AbilityRadialScreen abilityRadialScreen) {
            int mouseX = (int) event.getMouseX();
            int mouseY = (int) event.getMouseY();
            int button = event.getButton();
            int action = GLFW.GLFW_RELEASE; // 0
            int modifiers = getCurrentModifiers();

            handleRadialWheelClick(event, player, abilityRadialScreen, mouseX, mouseY, button, action, modifiers);
        }
    }

    @SubscribeEvent
    public static void onScreenMouseDragged(ScreenEvent.MouseDragged.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        Screen screen = event.getScreen();
        if (screen instanceof AbilityRadialScreen abilityRadialScreen) {
            int mouseX = (int) event.getMouseX();
            int mouseY = (int) event.getMouseY();
            int button = event.getMouseButton();
            int action = GLFW.GLFW_REPEAT; // 2
            int modifiers = getCurrentModifiers();

            handleRadialWheelClick(event, player, abilityRadialScreen, mouseX, mouseY, button, action, modifiers);
        }
    }

    @SubscribeEvent
    public static void onScreenMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        Screen screen = event.getScreen();
        if (screen instanceof AbilityRadialScreen abilityRadialScreen) {
            int mouseX = (int) event.getMouseX();
            int mouseY = (int) event.getMouseY();
            // Using scroll direction: positive button = scroll up, negative = scroll down
            int button = event.getScrollDelta() > 0 ? 0 : 1;
            int action = GLFW.GLFW_PRESS;
            int modifiers = getCurrentModifiers();

            handleRadialWheelScroll(event, player, abilityRadialScreen, mouseX, mouseY, button, action, modifiers);
        }
    }

    // --- HELPER METHODS ---

    /**
     * Finds the hovered radial section and triggers IWheelKeyPressHandler for the corresponding ability.
     */
    private static <E extends ScreenEvent> void handleRadialWheelScroll(E event, LocalPlayer player, AbilityRadialScreen abilityRadialScreen,
                                                                        int mouseX, int mouseY, int button, int action, int modifiers) {
        Optional<Integer> sectionAt = abilityRadialScreen.getSectionAt(mouseX, mouseY);
        if (sectionAt.isEmpty()) return;

        AbstractAbility<?> ability = abilityRadialScreen.abilities.get(sectionAt.get());

        ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent(variantInstance -> {
            AbstractAbilityInstance abilityInstance = variantInstance.getAbilityInstance(ability);
            if (abilityInstance instanceof IWheelKeyPressHandler iKeyPressHandler) {
                if (iKeyPressHandler.detectsScroll()) {
                    if (iKeyPressHandler.onClientWheelKeyPressed(player, true, button, action, modifiers)) {
                        event.setCanceled(true);
                    }
                }
            }
        });
    }

    /**
     * Finds the hovered radial section and triggers IWheelKeyPressHandler for the corresponding ability.
     */
    private static <E extends ScreenEvent> void handleRadialWheelClick(E event, LocalPlayer player, AbilityRadialScreen abilityRadialScreen,
                                                                       int mouseX, int mouseY, int button, int action, int modifiers) {
        Optional<Integer> sectionAt = abilityRadialScreen.getSectionAt(mouseX, mouseY);
        if (sectionAt.isEmpty()) return;

        AbstractAbility<?> ability = abilityRadialScreen.abilities.get(sectionAt.get());

        ProcessTransfur.getPlayerTransfurVariantSafe(player).ifPresent(variantInstance -> {
            AbstractAbilityInstance abilityInstance = variantInstance.getAbilityInstance(ability);
            if (abilityInstance instanceof IWheelKeyPressHandler iKeyPressHandler) {
                if (iKeyPressHandler.onClientWheelKeyPressed(player, true, button, action, modifiers)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    event.setCanceled(true);
                }
            }
        });
    }

    /**
     * Checks GLFW key states to construct the bitfield modifier mask for ScreenEvents.
     */
    private static int getCurrentModifiers() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        int modifiers = 0;

        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS) {
            modifiers |= GLFW.GLFW_MOD_SHIFT;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_CONTROL) == GLFW.GLFW_PRESS) {
            modifiers |= GLFW.GLFW_MOD_CONTROL;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS) {
            modifiers |= GLFW.GLFW_MOD_ALT;
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_SUPER) == GLFW.GLFW_PRESS ||
                GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SUPER) == GLFW.GLFW_PRESS) {
            modifiers |= GLFW.GLFW_MOD_SUPER;
        }

        return modifiers;
    }
}