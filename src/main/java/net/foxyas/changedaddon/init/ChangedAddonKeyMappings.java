package net.foxyas.changedaddon.init;

import com.mojang.blaze3d.platform.InputConstants;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.client.gui.TransfurSoundsGuiScreen;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.PatKeyPacket;
import net.foxyas.changedaddon.network.packet.TurnOffTransfurPacket;
import net.foxyas.changedaddon.network.packet.VariantSecondAbilityActivate;
import net.foxyas.changedaddon.variant.TransfurVariantInstanceExtensor;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.tutorial.ChangedTutorial;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ChangedAddonKeyMappings {

    public static final KeyMapping OPEN_EXTRA_DETAILS = new KeyMapping("key.changed_addon.open_extra_details", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                Minecraft minecraft = Minecraft.getInstance();
                Player player = minecraft.player;
                if (player != null && !player.isDeadOrDying() && !player.isSpectator() && minecraft.screen == null) {

                    if (ProcessTransfur.isPlayerTransfurred(player)) {
                        minecraft.setScreen(new TransfurSoundsGuiScreen());
                    } else {
                        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
                        if (vars != null && vars.showWarns) {
                            player.displayClientMessage(Component.translatable("changed_addon.when_not.transfur"), true);
                        }
                    }
                }
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping TURN_OFF_TRANSFUR = new KeyMapping("key.changed_addon.turn_off_transfur", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                ChangedAddonMod.PACKET_HANDLER.sendToServer(new TurnOffTransfurPacket(0, 0));
                TurnOffTransfurPacket.pressAction(Minecraft.getInstance().player, 0);
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping PAT_KEY = new KeyMapping("key.changed_addon.pat_key", GLFW.GLFW_KEY_UNKNOWN, "key.categories.changed_addon") {
        //private boolean isDownOld = false;
        // Foxyas here.. i'm going to allow the player to hold the key to Spam Pats, the packet is too small to cause any harm
        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDown) {
                Player player = Minecraft.getInstance().player;
                if (player == null || player.isDeadOrDying()) return;

                ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.nonNullOf(Minecraft.getInstance().player);
                if (vars.patCooldown) return;

                ChangedAddonMod.PACKET_HANDLER.sendToServer(new PatKeyPacket(0, 0));
                PatKeyPacket.pressAction(Minecraft.getInstance().player, 0);
            }
            //isDownOld = isDown;
        }
    };

    public static final KeyMapping USE_SECOND_ABILITY = new KeyMapping(
            "key.changed_addon.use_second_ability",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.movement") {

        @Override
        public void setDown(boolean newState) {
            super.setDown(newState);
            LocalPlayer local = Minecraft.getInstance().player;
            ProcessTransfur.ifPlayerTransfurred(local, (variant) -> {
                if (!ChangedAddonServerConfiguration.ALLOW_SECOND_ABILITY_USE.get()) return;
                assert local != null;
                if (variant.isTemporaryFromSuit() || !(variant instanceof TransfurVariantInstanceExtensor transfurVariantInstanceExtensor))
                    return;

                // KeyStateTracker will check if the state has changed
                if (transfurVariantInstanceExtensor.getSecondAbilityKey().queueKeyState(newState)) {
                    ChangedTutorial.triggerOnUseAbility(transfurVariantInstanceExtensor.getSecondSelectedAbilityInstance());
                    ChangedAddonMod.PACKET_HANDLER.sendToServer(new VariantSecondAbilityActivate(local, newState, transfurVariantInstanceExtensor.getSecondSelectedAbility()));
                }
            });
        }
    };

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_EXTRA_DETAILS);
        event.register(TURN_OFF_TRANSFUR);
        event.register(PAT_KEY);
        event.register(USE_SECOND_ABILITY);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (Minecraft.getInstance().screen != null) return;

            OPEN_EXTRA_DETAILS.consumeClick();
            TURN_OFF_TRANSFUR.consumeClick();
            PAT_KEY.consumeClick();
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
        }
    }
}
