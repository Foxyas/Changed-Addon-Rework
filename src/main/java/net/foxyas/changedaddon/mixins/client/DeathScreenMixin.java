package net.foxyas.changedaddon.mixins.client;

import net.foxyas.changedaddon.client.gui.RespawnAsTransfurScreen;
import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
public abstract class DeathScreenMixin extends Screen {

    @Shadow
    @Final
    private List<Button> exitButtons;

    @Shadow
    @Final
    private boolean hardcore;

    protected DeathScreenMixin(Component pTitle) {
        super(pTitle);
    }

    @Inject(at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER), method = "init")
    private void addTFButton(CallbackInfo ci) {
        assert minecraft != null;
        LocalPlayer player = this.minecraft.player;
        assert player != null;
        if (!ChangedAddonServerConfiguration.ALLOW_RESPAWN_AS_TRANSFUR.get()) return;
        if (ProcessTransfur.isPlayerTransfurred(player) && !ChangedAddonServerConfiguration.ALLOW_TRANSFURED_PLAYERS_TO_RESPAWN_WAS_TRANSFUR.get())
            return;

        exitButtons.add(this.addRenderableWidget(
                Button.builder(hardcore ? Component.translatable("deathScreen.select_tf.hardcore") : Component.translatable("deathScreen.select_tf"), (button) -> {
                            RespawnAsTransfurScreen respawnAsTransfurScreen = new RespawnAsTransfurScreen((DeathScreen) (Object) this);
                            if (player.hasPermissions(2) || ChangedAddonServerConfiguration.ALLOW_PLAYERS_TO_SELECT_RESPAWN_TRANSFUR.get()) {
                                this.minecraft.setScreen(respawnAsTransfurScreen);
                            } else {
                                ConfirmScreen confirmscreen = new ConfirmScreen(respawnAsTransfurScreen::handleRespawnAsTransfur,
                                        Component.translatable("deathScreen.select_tf.confirm"),
                                        ChangedAddonServerConfiguration.APPLY_UNTRANSFUR_IMMUNITY_AFTER_RESPAWN_AS_TRANSFUR.get() ?
                                                Component.translatable("deathScreen.select_tf.confirm.info") :
                                                Component.literal(""),
                                        Component.translatable("deathScreen.select_tf.spawn_as_infected"),
                                        Component.translatable("deathScreen.select_tf.cancel_spawn_as_infected")
                                );
                                confirmscreen.setDelay(20);
                                this.minecraft.setScreen(confirmscreen);
                            }
                        }
                ).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build()
        ));
    }
}