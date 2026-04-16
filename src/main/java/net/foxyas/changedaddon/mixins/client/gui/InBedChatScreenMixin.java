package net.foxyas.changedaddon.mixins.client.gui;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.network.packet.ServerboundSwitchCuddlePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.InBedChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InBedChatScreen.class)
public abstract class InBedChatScreenMixin extends Screen {

    @Unique
    private Button cuddleButton;

    @Unique
    private boolean closeSilently;

    protected InBedChatScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void onInit(CallbackInfo ci) {
        cuddleButton = Button.builder(Component.translatable("text.changed_addon.cuddle_button"), (self) -> {
            ChangedAddonMod.PACKET_HANDLER.sendToServer(ServerboundSwitchCuddlePacket.INSTANCE);
            closeSilently = true;
            minecraft.setScreen(null);
        }).bounds(this.width / 2 - 100, 20, 200, 20).build();
        addRenderableWidget(cuddleButton);
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void hideButtonAndCloseIfCuddling(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        if (!ChangedAddonVariables.ofOrDefault(minecraft.player).isCuddling) return;

        ci.cancel();
        closeSilently = true;
        minecraft.setScreen(null);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"),
            method = "render")
    private void drawCuddleButtonNoChat(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        cuddleButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Inject(at = @At("HEAD"), method = "sendWakeUp", cancellable = true)
    private void closeSilentlyWhenCuddling(CallbackInfo ci) {
        if (!closeSilently && !ChangedAddonVariables.ofOrDefault(minecraft.player).isCuddling) return;

        ci.cancel();
        Minecraft.getInstance().setScreen(null);
    }
}
